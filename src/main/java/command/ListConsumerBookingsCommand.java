package command;

import controller.Context;
import model.Booking;
import model.Consumer;

import java.util.List;

public class ListConsumerBookingsCommand extends Object implements ICommand{

    private List<Booking> bookingListResult;
    private LogStatus logStatus;
    private Object Consumer;

    private enum LogStatus{
        LIST_CONSUMER_BOOKINGS_NOT_LOGGED_IN,
        LIST_CONSUMER_BOOKINGS_USER_NOT_CONSUMER,
        LIST_CONSUMER_BOOKINGS_SUCCESS
    }

    public ListConsumerBookingsCommand(){

    }

    @Override
    public void execute(Context context) {
        if (context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.LIST_CONSUMER_BOOKINGS_NOT_LOGGED_IN;
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != Consumer.getClass()){
            logStatus = LogStatus.LIST_CONSUMER_BOOKINGS_USER_NOT_CONSUMER;
            return;
        }

        logStatus = LogStatus.LIST_CONSUMER_BOOKINGS_SUCCESS;
        this.bookingListResult = context.getBookingState().getBookings();
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.LIST_CONSUMER_BOOKINGS_SUCCESS){
            return bookingListResult;
        }
        return null;
    }
}
