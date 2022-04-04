package command;

import controller.Context;
import model.Booking;

import java.util.List;

public class ListConsumerBookingsCommand implements ICommand{
    private long eventNumber;
    private List<Booking> bookingListResult;
    private LogStatus logStatus;

    private enum LogStatus{
        LIST_EVENT_BOOKINGS_USER_NOT_LOGGED_IN,
        LIST_EVENT_BOOKINGS_EVENT_NOT_TICKETED,
        LIST_EVENT_BOOKINGS_SUCCESS,
        LIST_EVENT_BOOKINGS_EVENT_NOT_FOUND,
        LIST_EVENT_BOOKINGS_USER_NOT_ORGANISER_NOR_GOV
    }

    public ListConsumerBookingsCommand(){}

    @Override
    public void execute(Context context) {
        if (context.getUserState().getCurrentUser() != null){

        }
    }

    @Override
    public Object getResult() {
        return null;
    }
}
