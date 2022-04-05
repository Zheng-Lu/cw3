package command;

import controller.Context;
import model.Booking;
import model.Consumer;
import model.EventStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

public class CancelBookingCommand extends Object implements ICommand{

    private long bookingNumber;
    private LogStatus logStatus;
    private Boolean sucessResult;
    private Object Consumer;

    private enum LogStatus{
        CANCEL_BOOKING_SUCCESS,
        CANCEL_BOOKING_USER_NOT_CONSUMER,
        CANCEL_BOOKING_BOOKING_NOT_FOUND,
        CANCEL_BOOKING_USER_IS_NOT_BOOKER,
        CANCEL_BOOKING_BOOKING_NOT_ACTIVE,
        CANCEL_BOOKING_REFUND_FAILED,
        CANCEL_BOOKING_NO_CANCELLATIONS_WITHIN_24H
    }

    public CancelBookingCommand(long bookingNumber){
        this.bookingNumber = bookingNumber;
    }

    @Override
    public void execute(Context context) {
        if(context.getUserState().getCurrentUser().getClass() != Consumer.getClass()){
            logStatus = LogStatus.CANCEL_BOOKING_USER_NOT_CONSUMER;
            sucessResult = false;
            return;
        }

        List<Booking> bookings = context.getBookingState().getBookings();
        boolean booking_found = false;
        for (Booking booking : bookings) {
            if (booking.getBookingNumber() == this.bookingNumber) {
                booking_found = true;
                break;
            }
        }
        if (!booking_found){
            logStatus = LogStatus.CANCEL_BOOKING_BOOKING_NOT_FOUND;
            sucessResult = false;
            return;
        }

        if (context.getUserState().getCurrentUser() != context.getBookingState().findBookingByNumber(this.bookingNumber).getBooker()){
            logStatus = LogStatus.CANCEL_BOOKING_USER_IS_NOT_BOOKER;
            sucessResult = false;
            return;
        }

        if (context.getBookingState().findBookingByNumber(this.bookingNumber).getEventPerformance().getEvent().getStatus() == EventStatus.CANCELLED){
            logStatus = LogStatus.CANCEL_BOOKING_BOOKING_NOT_ACTIVE;
            sucessResult = false;
            return;
        }

        Duration duration = Duration.between(LocalDateTime.now(),context.getBookingState().findBookingByNumber(this.bookingNumber).getEventPerformance().getStartDateTime());
        if (duration.toHours() < 24){
            logStatus = LogStatus.CANCEL_BOOKING_NO_CANCELLATIONS_WITHIN_24H;
            sucessResult = false;
            return;
        }

        String buyerAccountEmail = context.getBookingState().findBookingByNumber(this.bookingNumber).getBooker().getEmail();
        String sellerAccountEmail = context.getBookingState().findBookingByNumber(this.bookingNumber).getEventPerformance().getEvent().getOrganiser().getEmail();
        double transactionAmount = context.getBookingState().findBookingByNumber(this.bookingNumber).getAmountPaid();
        if (!context.getPaymentSystem().processRefund(buyerAccountEmail,sellerAccountEmail,transactionAmount)){
            logStatus = LogStatus.CANCEL_BOOKING_REFUND_FAILED;
            sucessResult = false;
            return;
        }

        logStatus = LogStatus.CANCEL_BOOKING_SUCCESS;
        sucessResult = true;
    }

    @Override
    public Object getResult() {
        if (this.logStatus == LogStatus.CANCEL_BOOKING_SUCCESS){
            return this.sucessResult;
        }
        return null;
    }
}
