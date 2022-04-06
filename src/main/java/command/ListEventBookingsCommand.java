package command;

import controller.Context;
import model.*;

import java.util.List;

public class ListEventBookingsCommand extends Object implements ICommand{

    private long eventNumber;
    private List<Booking> bookingListResult;
    private LogStatus logStatus;
    private Object TicketedEvent;
    private Object GovernmentRepresentative;
    private Object EntertainmentProvider;

    private enum LogStatus{
        LIST_EVENT_BOOKINGS_USER_NOT_LOGGED_IN,
        LIST_EVENT_BOOKINGS_EVENT_NOT_TICKETED,
        LIST_EVENT_BOOKINGS_SUCCESS,
        LIST_EVENT_BOOKINGS_EVENT_NOT_FOUND,
        LIST_EVENT_BOOKINGS_USER_NOT_ORGANISER_NOR_GOV
    }

    public ListEventBookingsCommand(long eventNumber){
        this.eventNumber = eventNumber;
    }

    @Override
    public void execute(Context context) {
        if (context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.LIST_EVENT_BOOKINGS_USER_NOT_LOGGED_IN;
            return;
        }

        List<Event> events = context.getEventState().getAllEvents();
        boolean event_found = false;
        for (Event event : events) {
            if (context.getEventState().findEventByNumber(this.eventNumber) == event) {
                event_found = true;
                break;
            }
        }
        if (!event_found){
            logStatus = LogStatus.LIST_EVENT_BOOKINGS_EVENT_NOT_FOUND;
            return;
        }

        if (context.getEventState().findEventByNumber(this.eventNumber).getClass() != TicketedEvent.getClass()){
            logStatus = LogStatus.LIST_EVENT_BOOKINGS_EVENT_NOT_TICKETED;
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != GovernmentRepresentative.getClass() || context.getUserState().getCurrentUser() != context.getEventState().findEventByNumber(this.eventNumber).getOrganiser()){
            logStatus = LogStatus.LIST_EVENT_BOOKINGS_USER_NOT_ORGANISER_NOR_GOV;
            return;
        }
        logStatus = LogStatus.LIST_EVENT_BOOKINGS_SUCCESS;
        this.bookingListResult = context.getBookingState().findBookingsByEventNumber(this.eventNumber);
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.LIST_EVENT_BOOKINGS_SUCCESS){
            return this.bookingListResult;
        }
        return null;
    }
}
