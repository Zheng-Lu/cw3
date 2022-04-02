package model;

import java.time.LocalDateTime;

public class Booking extends Object{
    private long bookingNumber;
    private Consumer booker;
    private EventPerformance performance;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;

    public Booking(long bookingNumber, Consumer booker, EventPerformance performance, int numTickets,
                   double amountPaid, LocalDateTime bookingDateTime ){

        this.bookingNumber = bookingNumber;
        this.booker = booker;
        this.performance = performance;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid;
        this.bookingDateTime = bookingDateTime;
        this.status = BookingStatus.Active;
    }

    public long getBookingNumber(){
        return this.bookingNumber;
    }

    public BookingStatus getStatus(){
        return this.status;
    }

    public Consumer getBooker(){
        return this.booker;
    }

    public EventPerformance getEventPerformance(){
        return this.performance;
    }

    public double getAmountPaid(){
        return this.amountPaid;
    }

    public void cancelByConsumer(){
        this.status = BookingStatus.CancelledByConsumer;
        return;
    }

    public void cancelPaymentFailed(){
        this.status = BookingStatus.PaymentFailed;
        return;
    }

    public void cancelByProvider(){
        this.status = BookingStatus.CancelledByProvider;
        return;
    }

    public String toString(){
        return "NOT IMPLEMENTED";  // TODO implement toString
    }
}
