package command;

import controller.Context;
import logging.Logger;
import model.Booking;
import model.Consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if (context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.LIST_CONSUMER_BOOKINGS_NOT_LOGGED_IN;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("ListConsumerBookingsCommand.execute()",
                    getResult(),info);
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != Consumer.getClass()){
            logStatus = LogStatus.LIST_CONSUMER_BOOKINGS_USER_NOT_CONSUMER;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("ListConsumerBookingsCommand.execute()",
                    getResult(),info);
            return;
        }

        logStatus = LogStatus.LIST_CONSUMER_BOOKINGS_SUCCESS;
        this.bookingListResult = context.getBookingState().getBookings();

        info.put("STATUS:",this.logStatus);
        Logger.getInstance().logAction("ListConsumerBookingsCommand.execute()",
                getResult(),info);
    }

    @Override
    public List<Booking> getResult() {
        if (logStatus == LogStatus.LIST_CONSUMER_BOOKINGS_SUCCESS){
            return bookingListResult;
        }
        return null;
    }
}
