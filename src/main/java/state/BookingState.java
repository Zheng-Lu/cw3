package state;

import model.Booking;
import model.Consumer;
import model.EventPerformance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingState implements IBookingState {
    private long nextBookingNumber;
    private final List<Booking> bookings;

    public BookingState() {
        this.nextBookingNumber = 1;
        this.bookings = new ArrayList<>();
    }

    public BookingState(IBookingState other) {
        this.nextBookingNumber = other.getNextBookingNumber();
        this.bookings = other.getBookings();
    }

    @Override
    public Booking findBookingByNumber(long bookingNumber) {
        for (Booking b : bookings) {
            if (b.getBookingNumber() == bookingNumber) {
                return b;
            }
        }
        return null;
    }

    @Override
    public List<Booking> findBookingsByEventNumber(long eventNumber) {
        List<Booking> eventBookings = new ArrayList<>();
        for (Booking b : this.bookings) {
            if (b.getEventPerformance().getEvent().getEventNumber() == eventNumber) {
                eventBookings.add(b);
            }
        }
        return eventBookings;
    }

    @Override
    public Booking createBooking(Consumer booker, EventPerformance performance,
                                 int numTickets, double amountPaid) {
        Booking newBooking = new Booking(this.nextBookingNumber, booker, performance, numTickets,
                amountPaid, LocalDateTime.now());
        this.bookings.add(newBooking);
        this.nextBookingNumber = this.nextBookingNumber + 1; // increment next booking number by 1
        return newBooking;
    }

    @Override
    public long getNextBookingNumber() {
        return this.nextBookingNumber;
    }

    @Override
    public List<Booking> getBookings() {
        return this.bookings;
    }
}
