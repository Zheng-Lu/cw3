package command;

import controller.Context;
import model.Booking;

import java.util.List;

public class ListConsumerBookingsCommand implements ICommand{
    private long eventNumber;
    private List<Booking> bookingListResult;

    public ListConsumerBookingsCommand(){}

    @Override
    public void execute(Context context) {

    }

    @Override
    public Object getResult() {
        return null;
    }
}
