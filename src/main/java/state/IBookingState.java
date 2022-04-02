package state;

import model.Booking;
import model.Consumer;
import model.EventPerformance;

import java.util.List;

public interface IBookingState {

    Booking findBookingByNumber(long bookingNumber);

    List<Booking> findBookingsByEventNumber(long eventNumber);

    Booking createBooking(Consumer booker, EventPerformance performance, int numTickets,
                         double amountPaid);

    long getNextBookingNumber(); // use for copying BookingState

    List<Booking> getBookings(); // use for copying BookingState
}
