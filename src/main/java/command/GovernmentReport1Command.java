package command;

import controller.Context;
import model.Booking;
import model.GovernmentRepresentative;

import java.time.LocalDateTime;
import java.util.List;

public class GovernmentReport1Command extends Object implements ICommand{

    private LocalDateTime intervalStartInclusive, intervalEndInclusive;
    private List<Booking> bookingListResult;
    private LogStatus logStatus;
    private Object GovernmentRepresentative;

    private enum LogStatus{
        GOVERNMENT_REPORT1_NOT_LOGGED_IN,
        GOVERNMENT_REPORT1_USER_NOT_GOVERNMENT_REPRESENTATIVE,
        GOVERNMENT_REPORT1_SUCCESS
    }

    GovernmentReport1Command(LocalDateTime intervalStartInclusive, LocalDateTime intervalEndInclusive){
        this.intervalStartInclusive = intervalStartInclusive;
        this.intervalEndInclusive = intervalEndInclusive;
    }



    @Override
    public void execute(Context context) {
        if (context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.GOVERNMENT_REPORT1_NOT_LOGGED_IN;
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != GovernmentRepresentative.getClass()){
            logStatus = LogStatus.GOVERNMENT_REPORT1_USER_NOT_GOVERNMENT_REPRESENTATIVE;
            return;
        }
        logStatus = LogStatus.GOVERNMENT_REPORT1_SUCCESS;
        List<Booking> bookingList = context.getBookingState().getBookings();
        for (Booking booking : bookingList){
            if((booking.getEventPerformance().getStartDateTime().equals(intervalStartInclusive) || booking.getEventPerformance().getStartDateTime().isBefore(intervalStartInclusive))
                    && (booking.getEventPerformance().getEndDateTime().equals(intervalEndInclusive) || booking.getEventPerformance().getEndDateTime().isAfter(intervalEndInclusive))){
                this.bookingListResult.add(booking);
            }
        }
    }

    @Override
    public List<Booking> getResult() {
        if (logStatus == LogStatus.GOVERNMENT_REPORT1_SUCCESS){
            return bookingListResult;
        }
        return null;
    }
}
