package model;

import java.time.LocalDateTime;

public class Booking extends Object{
    private long bookingNumber;
    private Consumer booker;
    private EventPerfermance perfermance;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;

    public Booking(long bookingNumber, Consumer booker, EventPerfermance perfermance, int numTickets,
                   double amountPaid, LocalDateTime bookingDateTime ){

        this.bookingNumber = bookingNumber;
        this.booker = booker;
        this.perfermance = perfermance;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid;
        this.bookingDateTime = bookingDateTime;
        this.status = BookingStatus.Active;
    };

    public long getBookingNumber(){
        return this.bookingNumber;
    }

    public BookingStatus getStatus(){
        return this.status;
    }

    public Consumer getBooker(){
        return this.booker;
    }

    public EventPerfermance getEventPerfermance(){
        return this.perfermance;
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
        return "NOT IMPLEMENTED";
    }
}
