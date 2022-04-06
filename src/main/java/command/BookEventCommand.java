package command;

import controller.Context;
import model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class BookEventCommand extends Object implements ICommand{

    // TODO remember to update the provider's system and relative states in the context
    // use event.getOrganiser().getProviderSystem() to get the provider's system
    // DO NOT USE event.getNumTickets() to get the current number of tickets left for a SINGLE PERFORMANCE
    // instead asks teh number from the provider's system
    // USE event.getOrganiser().getProviderSystem().getNumTicketsLeft()

    private int numTicketsRequested;
    private long bookingNumberResult, eventNumber, performanceNumber;

    private LogStatus logStatus;
    private Object Consumer;
    private Object TicketedEvent;

    private enum LogStatus{
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


    BookEventCommand(long eventNumber, long performanceNumber, int numTicketsRequested){
        this.numTicketsRequested = numTicketsRequested;
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
    }

    @Override
    public void execute(Context context) {

        Boolean event_found = false;
        Boolean performance_found = false;

        if (context.getUserState().getCurrentUser().getClass() != Consumer.getClass()){
            logStatus = LogStatus.BOOK_EVENT_USER_NOT_CONSUMER;
            return;
        }

        List<Event> events = context.getEventState().getAllEvents();
        for (Event e:events){
            if (e.getEventNumber()==this.eventNumber){
                event_found = true;
                break;
            }
        }
        if (!event_found){
            logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_FOUND;
            return;
        }

        Event event = context.getEventState().findEventByNumber(eventNumber);

        if(event.getStatus() != EventStatus.ACTIVE){
            logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_ACTIVE;
            return;
        }

        if(event.getClass() != TicketedEvent.getClass()){
            logStatus = LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT;
            return;
        }

        if (this.numTicketsRequested < 1){
            logStatus = LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS;
            return;
        }

        Collection<EventPerformance> performances = event.getPerformances();
        for (EventPerformance performance: performances) {
            if (performance.getPerformanceNumber() == this.performanceNumber){
                performance_found = true;
                break;
            }
        }
        if (!performance_found){
            logStatus = LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND;
            return;
        }

        if (LocalDateTime.now().isAfter(event.getPerformanceByNumber(performanceNumber).getEndDateTime())){
            logStatus = LogStatus.BOOK_EVENT_ALREADY_OVER;
            return;
        }

        if (this.numTicketsRequested > event.getOrganiser().getProviderSystem().getNumTicketsLeft(this.eventNumber,this.performanceNumber))  {
            logStatus = LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT;
            return;
        }


        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(this.eventNumber);
        String sellerAccountEmail = event.getOrganiser().getEmail();

        for (Booking booking: bookings) {
            String buyerAccountEmail = booking.getBooker().getEmail();
            double transactionAmount = booking.getAmountPaid();
            if (!context.getPaymentSystem().processPayment(buyerAccountEmail,sellerAccountEmail,transactionAmount)) {
                logStatus = LogStatus.BOOK_EVENT_PAYMENT_FAILED;
                return;
            }
        }

        logStatus = LogStatus.BOOK_EVENT_SUCCESS;
        this.bookingNumberResult = bookings.size();

    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.BOOK_EVENT_SUCCESS){
            return this.bookingNumberResult;
        }

        return null;
    }


}
