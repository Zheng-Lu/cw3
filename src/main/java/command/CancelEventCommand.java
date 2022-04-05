package command;

import controller.Context;
import model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
    private Object EntertainmentProvider;
    private Object TicketedEvent;

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


    CancelEventCommand(long eventNumber, String organiserMessage){
        this.eventNumber = eventNumber;
        this.organiserMessage = organiserMessage;
    }

    @Override
    public void execute(Context context) {
        if(Objects.equals(this.organiserMessage, "")){
            logStatus = LogStatus.CANCEL_EVENT_MESSAGE_MUST_NOT_BE_BLANK;
            successResult = false;
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != EntertainmentProvider.getClass()){
            logStatus = LogStatus.CANCEL_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER;
            successResult = false;
            return;
        }

        List<Event> events = context.getEventState().getAllEvents();
        Boolean event_found = false;
        for (Event event : events) {
            if (event.getEventNumber() == this.eventNumber){
                event_found = true;
                break;
            }
        }
        if (!event_found){
            logStatus = LogStatus.CANCEL_EVENT_EVENT_NOT_FOUND;
            successResult = false;
            return;
        }

        if (context.getEventState().findEventByNumber(this.eventNumber).getStatus() != EventStatus.ACTIVE){
            logStatus = LogStatus.CANCEL_EVENT_NOT_ACTIVE;
            successResult = false;
            return;
        }

        if (context.getUserState().getCurrentUser() != context.getEventState().findEventByNumber(this.eventNumber).getOrganiser()){
            logStatus = LogStatus.CANCEL_EVENT_USER_NOT_ORGANISER;
            successResult = false;
            return;
        }

        Collection<EventPerformance> eventPerformances = context.getEventState().findEventByNumber(this.eventNumber).getPerformances();
        LocalDateTime now = LocalDateTime.now();
        for (EventPerformance eventPerformance : eventPerformances) {
            if (now.isAfter(eventPerformance.getStartDateTime()) || now.isAfter(eventPerformance.getEndDateTime())) {
                logStatus = LogStatus.CANCEL_EVENT_PERFORMANCE_ALREADY_STARTED;
                successResult = false;
                return;
            }
        }
        // still needs the last part of sponsorship and booking
    }

    @Override
    public Object getResult() {
        return null;
    }

}
