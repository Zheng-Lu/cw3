import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;
import state.BookingState;
import state.UserState;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestBookingState {
    Booking booking1;
    Booking booking2;
    Booking booking3;
    Consumer consumer1;
    Consumer consumer2;
    Consumer consumer3;
    EntertainmentProvider entertainmentProvider1;
    EntertainmentProvider entertainmentProvider2;
    EntertainmentProvider entertainmentProvider3;
    TicketedEvent ticketedEvent1;
    TicketedEvent ticketedEvent2;
    TicketedEvent ticketedEvent3;
    EventPerformance eventPerformance1;

    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @BeforeEach
    void setup() throws Exception {
        consumer1 = new Consumer(
                "Shikai Geng",
                "Shikai_Geng@163.com",
                "07751234567",
                "Shikai_love_Varian",
                "Shikai_Geng@163.com"
        );

        consumer2 = new Consumer(
                "Rays Zhang",
                "Rays_Zhang@gmail.com",
                "07757654321",
                "Shuyuan&Zhang2002",
                "Rays_Zhang@qq.com"
        );

        consumer3 = new Consumer(
                "Lawrence Zhu",
                "LawrenceZYZ@outlook.com",
                "07759876543",
                "ZYZ1234567890",
                "LawrenceZYZ@gmail.com"
        );

        entertainmentProvider1 = new EntertainmentProvider(
                "No org",
                "Leith Walk",
                "a hat on the ground",
                "the best musicican ever",
                "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!",
                Collections.emptyList(),
                Collections.emptyList()
        );

        entertainmentProvider2 = new EntertainmentProvider(
                "Cinema Conglomerate",
                "Global Office, International Space Station",
                "$$$@there'sNoEmailValidation.wahey!",
                "Mrs Representative",
                "odeon@cineworld.com",
                "F!ghT th3 R@Pture",
                List.of("Dr Strangelove"),
                List.of("we_dont_get_involved@cineworld.com")
        );

        entertainmentProvider3 = new EntertainmentProvider(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        );

        ticketedEvent1 = new TicketedEvent(
                1,
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        eventPerformance1 = new EventPerformance(
                1,
                ticketedEvent1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(8),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                3000
        );
    }


    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    // createBooking() unit tests
    @Test
    void testCreateNormalBooking() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(consumer1,eventPerformance1,1,123456);
        assertNull(booking1);
    }

    @Test
    void testCreateNullConsumerBooking() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(null,eventPerformance1,1,123456);
        assertNull(booking1);
    }

    @Test
    void testCreateNullEventPerformanceBooking() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(consumer1,null,1,123456);
        assertNull(booking1);
    }

    @Test
    void testCreateZeroTicketBooking() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(consumer1,eventPerformance1,0,123456);
        assertNull(booking1);
    }

    @Test
    void testCreateIncorrectPaymentBooking() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(consumer1,eventPerformance1,1,0.0);
        assertNull(booking1);
    }

    @Test
    void testCreateExcessBooking() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(consumer1,eventPerformance1,26,123456);
        assertNull(booking1);
    }


    // findBookingByEventNumber() unit tests
    @Test
    void testFindBookingsByCorrectEventNumber() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(consumer1,eventPerformance1,1,123456);

        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        assertEquals(1, bookings.size(),"Don't match");
        assertEquals("Shikai Geng", bookings.get(0).getBooker().getName(),"Don't match");
    }

    @Test
    void testFindBookingsByIncorrectEventNumber() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(consumer1,eventPerformance1,1,123456);

        List<Booking> bookings = bookingState.findBookingsByEventNumber(999);
        assertEquals(0, bookings.size(),"Don't match");
    }


    // findBookingByNumber() unit tests
    @Test
    void testFindBookingsByCorrectNumber() {
        BookingState bookingState = new BookingState();

        booking1 = bookingState.createBooking(consumer1,eventPerformance1,1,123456);

        Booking booking = bookingState.findBookingByNumber(1);
        assertEquals(booking1, booking,"Don't match");
    }

    @Test
    void testFindBookingsByIncorrectNumber() {
        BookingState bookingState = new BookingState();

        Booking booking = bookingState.findBookingByNumber(999);
        assertNull(booking);
    }

}
