package command;

import controller.Context;
import logging.Logger;
import model.*;

import java.time.LocalDateTime;
import java.util.*;

public class CancelEventCommand extends Object implements ICommand{
    // TODO remember to update the provider's system and relative states in the context
    // use event.getOrganiser().getProviderSystem() to get the provider's system
    // DO NOT USE event.getNumTickets() to get the current number of tickets left for a SINGLE PERFORMANCE
    // instead asks teh number from the provider's system
    // USE event.getOrganiser().getProviderSystem().getNumTicketsLeft()

    private long eventNumber;
    private String organiserMessage;
    private Boolean successResult;

    private LogStatus logStatus;


    //shown in uml but not in javadoc, needs better looking
    private enum LogStatus{
        CANCEL_EVENT_SUCCESS,
        CANCEL_EVENT_MESSAGE_MUST_NOT_BE_BLANK,
        CANCEL_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER,
        CANCEL_EVENT_EVENT_NOT_FOUND,
        CANCEL_EVENT_NOT_ACTIVE,
        CANCEL_EVENT_USER_NOT_ORGANISER,
        CANCEL_EVENT_PERFORMANCE_ALREADY_STARTED,
        CANCEL_EVENT_REFUND_SPONSORSHIP_SUCCESS,
        CANCEL_EVENT_REFUND_SPONSORSHIP_FAILED,
        CANCEL_EVENT_REFUND_BOOKING_SUCCESS,
        CANCEL_EVENT_REFUND_BOOKING_ERROR
    }


    public CancelEventCommand(long eventNumber, String organiserMessage){
        this.eventNumber = eventNumber;
        this.organiserMessage = organiserMessage;
    }

    @Override
    public void execute(Context context) {
        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if(Objects.equals(this.organiserMessage, "")){
            logStatus = LogStatus.CANCEL_EVENT_MESSAGE_MUST_NOT_BE_BLANK;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("CancelEventCommand.execute()",
                    getResult(),info);
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != EntertainmentProvider.class){
            logStatus = LogStatus.CANCEL_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("CancelEventCommand.execute()",
                    getResult(),info);
            return;
        }

        List<Event> events = context.getEventState().getAllEvents();
        boolean event_found = false;
        for (Event event : events) {
            if (event.getEventNumber() == this.eventNumber){
                event_found = true;
                break;
            }
        }
        if (!event_found){
            logStatus = LogStatus.CANCEL_EVENT_EVENT_NOT_FOUND;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("CancelEventCommand.execute()",
                    getResult(),info);
            return;
        }

        if (context.getEventState().findEventByNumber(this.eventNumber).getStatus() != EventStatus.ACTIVE){
            logStatus = LogStatus.CANCEL_EVENT_NOT_ACTIVE;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("CancelEventCommand.execute()",
                    getResult(),info);
            return;
        }

        if (context.getUserState().getCurrentUser() != context.getEventState().findEventByNumber(this.eventNumber).getOrganiser()){
            logStatus = LogStatus.CANCEL_EVENT_USER_NOT_ORGANISER;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("CancelEventCommand.execute()",
                    getResult(),info);
            return;
        }

        Collection<EventPerformance> eventPerformances = context.getEventState().findEventByNumber(this.eventNumber).getPerformances();
        LocalDateTime now = LocalDateTime.now();
        for (EventPerformance eventPerformance : eventPerformances) {
            if (now.isAfter(eventPerformance.getStartDateTime()) || now.isAfter(eventPerformance.getEndDateTime())) {
                logStatus = LogStatus.CANCEL_EVENT_PERFORMANCE_ALREADY_STARTED;
                info.put("STATUS:",this.logStatus);
                Logger.getInstance().logAction("CancelEventCommand.execute()",
                        getResult(),info);
                return;
            }
        }

        if (context.getEventState().findEventByNumber(this.eventNumber).getClass() == TicketedEvent.class) {
            TicketedEvent ticketedEvent = (TicketedEvent) context.getEventState().findEventByNumber(this.eventNumber);
            if(ticketedEvent.isSponsored()){
                int ticketNum =  ticketedEvent.getNumTickets();
                Collection<EventPerformance>performances = ticketedEvent.getPerformances();
                //ticketLeft used here
                for (EventPerformance performance: performances){
                    ticketNum = ticketNum - ticketedEvent.getOrganiser().getProviderSystem().getNumTicketsLeft(eventNumber,performance.getPerformanceNumber());
                }

                double refund_amount = ticketNum * (ticketedEvent.getOriginalTicketPrice()-ticketedEvent.getDiscountedTicketPrice());

                if(context.getPaymentSystem().processRefund(ticketedEvent.getSponsorAccountEmail(),context.getEventState().findEventByNumber(this.eventNumber).getOrganiser().getPaymentAccountEmail(),refund_amount)){
                    logStatus = LogStatus.CANCEL_EVENT_REFUND_SPONSORSHIP_SUCCESS;
                    info.put("STATUS:",this.logStatus);
                }else {
                    logStatus = LogStatus.CANCEL_EVENT_REFUND_SPONSORSHIP_FAILED;
                    info.put("STATUS:",this.logStatus);
                    Logger.getInstance().logAction("CancelEventCommand.execute()",
                            getResult(),info);
                    return;
                }
            }

            Collection<Booking> bookings = context.getBookingState().findBookingsByEventNumber(eventNumber);

            for (Booking booking : bookings){
                if (!context.getPaymentSystem().processRefund(booking.getBooker().getPaymentAccountEmail(),context.getEventState().findEventByNumber(this.eventNumber).getOrganiser().getPaymentAccountEmail(),booking.getAmountPaid())){
                    logStatus = LogStatus.CANCEL_EVENT_REFUND_BOOKING_ERROR;
                    info.put("STATUS:",this.logStatus);
                    Logger.getInstance().logAction("CancelEventCommand.execute()",
                            getResult(),info);
                    return;
                }
            }
            logStatus = LogStatus.CANCEL_EVENT_REFUND_BOOKING_SUCCESS;
            info.put("STATUS:",this.logStatus);
        }

        logStatus = LogStatus.CANCEL_EVENT_SUCCESS;
        this.successResult = true;
        context.getEventState().findEventByNumber(this.eventNumber).cancel();

        context.getEventState().findEventByNumber(this.eventNumber).getOrganiser().getProviderSystem().cancelEvent(eventNumber,organiserMessage);

        info.put("STATUS:",this.logStatus);
        Logger.getInstance().logAction("CancelEventCommand.execute()",
                getResult(),info);

    }

    @Override
    public Boolean getResult() {
        if (logStatus == LogStatus.CANCEL_EVENT_SUCCESS || logStatus == LogStatus.CANCEL_EVENT_REFUND_SPONSORSHIP_SUCCESS){
            return successResult;
        }
        return false;
    }

}
