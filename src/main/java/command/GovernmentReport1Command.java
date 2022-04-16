package command;

import controller.Context;
import logging.Logger;
import model.Booking;
import model.GovernmentRepresentative;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GovernmentReport1Command extends Object implements ICommand {

    private final LocalDateTime intervalStartInclusive;
    private final LocalDateTime intervalEndInclusive;
    private List<Booking> bookingListResult;
    private LogStatus logStatus;

    public GovernmentReport1Command(LocalDateTime intervalStartInclusive, LocalDateTime intervalEndInclusive) {
        this.intervalStartInclusive = intervalStartInclusive;
        this.intervalEndInclusive = intervalEndInclusive;
    }

    @Override
    public void execute(Context context) {

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if (context.getUserState().getCurrentUser() == null) {
            logStatus = LogStatus.GOVERNMENT_REPORT1_NOT_LOGGED_IN;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("GovernmentReport1Command.execute()",
                    getResult(), info);
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != GovernmentRepresentative.class) {
            logStatus = LogStatus.GOVERNMENT_REPORT1_USER_NOT_GOVERNMENT_REPRESENTATIVE;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("GovernmentReport1Command.execute()",
                    getResult(), info);
            return;
        }

        logStatus = LogStatus.GOVERNMENT_REPORT1_SUCCESS;
        List<Booking> bookingList = context.getBookingState().getBookings();
        for (Booking booking : bookingList) {
            if ((booking.getEventPerformance().getStartDateTime().equals(intervalStartInclusive) || booking.getEventPerformance().getStartDateTime().isBefore(intervalStartInclusive))
                    && (booking.getEventPerformance().getEndDateTime().equals(intervalEndInclusive) || booking.getEventPerformance().getEndDateTime().isAfter(intervalEndInclusive))) {
                this.bookingListResult.add(booking);
            }
        }

        info.put("STATUS:", this.logStatus);
        Logger.getInstance().logAction("GovernmentReport1Command.execute()",
                getResult(), info);

    }

    @Override
    public List<Booking> getResult() {
        if (logStatus == LogStatus.GOVERNMENT_REPORT1_SUCCESS) {
            return bookingListResult;
        }
        return null;
    }

    private enum LogStatus {
        GOVERNMENT_REPORT1_NOT_LOGGED_IN,
        GOVERNMENT_REPORT1_USER_NOT_GOVERNMENT_REPRESENTATIVE,
        GOVERNMENT_REPORT1_SUCCESS
    }
}
