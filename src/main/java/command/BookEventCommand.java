package command;

import controller.Context;
import logging.Logger;
import model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookEventCommand implements ICommand {

    private final int numTicketsRequested;
    private final long eventNumber;
    private final long performanceNumber;
    private long bookingNumberResult;
    private LogStatus logStatus;

    public BookEventCommand(long eventNumber, long performanceNumber, int numTicketsRequested) {
        this.numTicketsRequested = numTicketsRequested;
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
    }

    @Override
    public void execute(Context context) {

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        boolean event_found = false;
        boolean performance_found = false;

        if (context.getUserState().getCurrentUser().getClass() != Consumer.class) {
            logStatus = LogStatus.BOOK_EVENT_USER_NOT_CONSUMER;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("BookEventCommand.execute()",
                    getResult(), info);
            return;
        }

        List<Event> events = context.getEventState().getAllEvents();
        for (Event e : events) {
            if (e.getEventNumber() == this.eventNumber) {
                event_found = true;
                break;
            }
        }
        if (!event_found) {
            logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_FOUND;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("BookEventCommand.execute()",
                    getResult(), info);
            return;
        }

        Event event = context.getEventState().findEventByNumber(eventNumber);

        if (event.getStatus() != EventStatus.ACTIVE) {
            logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_ACTIVE;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("BookEventCommand.execute()",
                    getResult(), info);
            return;
        }

        if (event.getClass() != TicketedEvent.class) {
            logStatus = LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("BookEventCommand.execute()",
                    getResult(), info);
            return;
        }

        if (this.numTicketsRequested < 1) {
            logStatus = LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("BookEventCommand.execute()",
                    getResult(), info);
            return;
        }

        Collection<EventPerformance> performances = event.getPerformances();
        for (EventPerformance performance : performances) {
            if (performance.getPerformanceNumber() == this.performanceNumber) {
                performance_found = true;
                break;
            }
        }
        if (!performance_found) {
            logStatus = LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("BookEventCommand.execute()",
                    getResult(), info);
            return;
        }

        if (LocalDateTime.now().isAfter(event.getPerformanceByNumber(performanceNumber).getEndDateTime())) {
            logStatus = LogStatus.BOOK_EVENT_ALREADY_OVER;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("BookEventCommand.execute()",
                    getResult(), info);
            return;
        }

        if (this.numTicketsRequested > event.getOrganiser().getProviderSystem().getNumTicketsLeft(this.eventNumber, this.performanceNumber)) {
            logStatus = LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("BookEventCommand.execute()",
                    getResult(), info);
            return;
        }


        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(this.eventNumber);
        String sellerAccountEmail = event.getOrganiser().getEmail();
        double transactionAmount = 0;

        for (Booking booking : bookings) {
            String buyerAccountEmail = booking.getBooker().getEmail();
            transactionAmount = booking.getAmountPaid();
            if (!context.getPaymentSystem().processPayment(buyerAccountEmail, sellerAccountEmail, transactionAmount)) {
                logStatus = LogStatus.BOOK_EVENT_PAYMENT_FAILED;
                info.put("STATUS:", this.logStatus);
                Logger.getInstance().logAction("BookEventCommand.execute()",
                        getResult(), info);
                return;
            }
        }

        logStatus = LogStatus.BOOK_EVENT_SUCCESS;
        Booking newBooking = context.getBookingState().createBooking((Consumer) context.getUserState().getCurrentUser(),
                event.getPerformanceByNumber(performanceNumber), numTicketsRequested, transactionAmount);
        this.bookingNumberResult = newBooking.getBookingNumber();

        event.getOrganiser().getProviderSystem().recordNewBooking(eventNumber, performanceNumber, bookingNumberResult,
                ((Consumer) context.getUserState().getCurrentUser()).getName(), context.getUserState().getCurrentUser().getEmail(), numTicketsRequested);

        info.put("STATUS:", this.logStatus);
        Logger.getInstance().logAction("BookEventCommand.execute()",
                getResult(), info);
    }

    @Override
    public Long getResult() {
        if (logStatus == LogStatus.BOOK_EVENT_SUCCESS) {
            return this.bookingNumberResult;
        }

        return null;
    }

    private enum LogStatus {
        BOOK_EVENT_SUCCESS,
        BOOK_EVENT_USER_NOT_CONSUMER,
        BOOK_EVENT_NOT_A_TICKETED_EVENT,
        BOOK_EVENT_EVENT_NOT_ACTIVE,
        BOOK_EVENT_ALREADY_OVER,
        BOOK_EVENT_INVALID_NUM_TICKETS,
        BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT,
        BOOK_EVENT_PAYMENT_FAILED,
        BOOK_EVENT_EVENT_NOT_FOUND,
        BOOK_EVENT_PERFORMANCE_NOT_FOUND,
    }


}
