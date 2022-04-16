import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;
import state.BookingState;
import state.EventState;
import state.UserState;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEventState {
    EntertainmentProvider entertainmentProvider1;
    EntertainmentProvider entertainmentProvider2;
    EntertainmentProvider entertainmentProvider3;
    TicketedEvent ticketedEvent1;
    TicketedEvent ticketedEvent2;
    TicketedEvent ticketedEvent3;
    NonTicketedEvent nonTicketedEvent1;
    NonTicketedEvent nonTicketedEvent2;
    NonTicketedEvent nonTicketedEvent3;

    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @BeforeEach
    void setup() throws Exception {
        entertainmentProvider1 = new EntertainmentProvider(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
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
                "No org",
                "Leith Walk",
                "a hat on the ground",
                "the best musicican ever",
                "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!",
                Collections.emptyList(),
                Collections.emptyList()
        );

        ticketedEvent1 = new TicketedEvent(
                1,
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        ticketedEvent2 = new TicketedEvent(
                2,
                entertainmentProvider1,
                "Winter Olympics",
                EventType.Sports,
                40000,
                400
        );

        nonTicketedEvent1 = new NonTicketedEvent(
                1,
                entertainmentProvider3,
                "Music for everyone!",
                EventType.Music
        );

        nonTicketedEvent2 = new NonTicketedEvent(
                2,
                entertainmentProvider2,
                "The Shining at the Meadows (Free Screening) (Live Action)",
                EventType.Sports
        );
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    // createTicketedEventPerformance() unit tests
    @Test
    void createCorrectTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
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
       assertNotNull(eventPerformance);
    }

    @Test
    void createNullTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                ticketedEvent1,
                null,
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(8),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                3000
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createNullStartTimeEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                ticketedEvent1,
                "Wimbledon",
                null,
                LocalDateTime.now().plusMonths(1).plusHours(8),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                3000
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createNullEndTimeEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                ticketedEvent1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1),
                null,
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                3000
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createNullPerformerNamesTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                ticketedEvent1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(8),
                null,
                false,
                true,
                true,
                3000,
                3000
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createZeroCapacityLimitTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                ticketedEvent1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(8),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                0,
                3000
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createZeroVenueSizeTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                ticketedEvent1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(8),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                0
        );
        assertNotNull(eventPerformance);
    }

    // createNonTicketedEvent()
    @Test
    void createNormalNonTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                nonTicketedEvent1,
                "Wimbledon",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createNullEventExceptionNonTicketedEventPerformance() {
        EventState eventState = new EventState();

        try{
            EventPerformance eventPerformance = eventState.createEventPerformance(
                null,
                "Wimbledon",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
            );
            fail("Didn't throw exception when I expected it to");
        } catch (Exception exception) {
            assertEquals("java.lang.NullPointerException", exception.toString());
        }
    }

    @Test
    void createNullVenueAddressNonTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                nonTicketedEvent1,
                null,
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createNullStartTimeNonTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                nonTicketedEvent1,
                "Wimbledon",
                null,
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createNullEndTimeNonTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                nonTicketedEvent1,
                "Wimbledon",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                null,
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createNullPerformerNamesNonTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                nonTicketedEvent1,
                "Wimbledon",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                null,
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createZeroCapacityLimitNonTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                nonTicketedEvent1,
                "Wimbledon",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("The same musician"),
                true,
                true,
                true,
                0,
                Integer.MAX_VALUE
        );
        assertNotNull(eventPerformance);
    }

    @Test
    void createZeroVenueSizeNonTicketedEventPerformance() {
        EventState eventState = new EventState();

        EventPerformance eventPerformance = eventState.createEventPerformance(
                nonTicketedEvent1,
                "Wimbledon",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                0
        );
        assertNotNull(eventPerformance);
    }

    // createNonTicketedEvent() unit tests
    @Test
    void createNormalNonTicketedEvent() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                entertainmentProvider3,
                "Music for everyone!",
                EventType.Music
        );
        assertNotNull(nonTicketedEvent);
    }

    @Test
    void createNullOrganiserNonTicketedEvent() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                null,
                "Music for everyone!",
                EventType.Music
        );
        assertNotNull(nonTicketedEvent);
    }

    @Test
    void createNullTitleNonTicketedEvent() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                entertainmentProvider3,
                null,
                EventType.Music
        );
        assertNotNull(nonTicketedEvent);
    }

    @Test
    void createNullTypeNonTicketedEvent() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                entertainmentProvider3,
                "Music for everyone!",
                null
        );
        assertNotNull(nonTicketedEvent);
    }

    @Test
    void createAllNullNonTicketedEvent() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                null,
                null,
                null
        );
        assertNotNull(nonTicketedEvent);
    }

    // creatTicketedEvent() unit tests
    @Test
    void createNormalTicketedEvent() {
        EventState eventState = new EventState();

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );
        assertNotNull(ticketedEvent);
    }

    @Test
    void createNullOrganiserTicketedEvent() {
        EventState eventState = new EventState();

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                null,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );
        assertNotNull(ticketedEvent);
    }

    @Test
    void createNullTitleTicketedEvent() {
        EventState eventState = new EventState();

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                null,
                EventType.Sports,
                123456,
                25
        );
        assertNotNull(ticketedEvent);
    }

    @Test
    void createNullTypeTicketedEvent() {
        EventState eventState = new EventState();

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                null,
                123456,
                25
        );
        assertNotNull(ticketedEvent);
    }

    @Test
    void createNegativePriceTicketedEvent() {
        EventState eventState = new EventState();

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                -123456,
                25
        );
        assertNotNull(ticketedEvent);
    }

    @Test
    void createNegativeNumberTicketsTicketedEvent() {
        EventState eventState = new EventState();

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                -25
        );
        assertNotNull(ticketedEvent);
    }

    // getAllEvents() unit tests
    @Test
    void getAllEvents_EmptyList() {
        EventState eventState = new EventState();

        assertEquals(0,eventState.getAllEvents().size());

    }

    @Test
    void getAllEvents_OneNonTicketedEvent() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                entertainmentProvider3,
                "Music for everyone!",
                EventType.Music
        );

        assertEquals(1,eventState.getAllEvents().size());

    }

    @Test
    void getAllEvents_MultipleNonTicketedEvents() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent1 = eventState.createNonTicketedEvent(
                entertainmentProvider3,
                "Music for everyone!",
                EventType.Music
        );

        NonTicketedEvent nonTicketedEvent2 = eventState.createNonTicketedEvent(
                entertainmentProvider2,
                "The Shining at the Meadows (Free Screening) (Live Action)",
                EventType.Sports
        );

        assertEquals(2,eventState.getAllEvents().size());

    }

    @Test
    void getAllEvents_TonsOfNonTicketedEvents() {
        EventState eventState = new EventState();

        for (int i = 0; i < 999999; i++) {
            eventState.createNonTicketedEvent(
                    entertainmentProvider3,
                    "Music for everyone!",
                    EventType.Music
            );
        }

        assertEquals(999999,eventState.getAllEvents().size());
    }

    @Test
    void getAllEvents_OneTicketedEvent() {
        EventState eventState = new EventState();

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        assertEquals(1,eventState.getAllEvents().size());

    }

    @Test
    void getAllEvents_TwoTicketedEvent() {
        EventState eventState = new EventState();

        TicketedEvent ticketedEvent1 = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        TicketedEvent ticketedEvent2 = eventState.createTicketedEvent(
                entertainmentProvider1,
                "Winter Olympics",
                EventType.Sports,
                40000,
                400
        );

        assertEquals(2,eventState.getAllEvents().size());

    }

    @Test
    void getAllEvents_TonsOfTicketedEvent() {
        EventState eventState = new EventState();

        for (int i = 0; i < 999999; i++) {
            eventState.createTicketedEvent(
                    entertainmentProvider1,
                    "Winter Olympics",
                    EventType.Sports,
                    40000,
                    400
            );
        }

        assertEquals(999999,eventState.getAllEvents().size());
    }

    @Test
    void getAllEvents_MixedTicketedEvents() {
        EventState eventState = new EventState();

        for (int i = 0; i < 999999; i++) {
            eventState.createTicketedEvent(
                    entertainmentProvider1,
                    "London Summer Olympics",
                    EventType.Sports,
                    123456,
                    25
            );
            eventState.createTicketedEvent(
                    entertainmentProvider1,
                    "Winter Olympics",
                    EventType.Sports,
                    40000,
                    400
            );
            eventState.createNonTicketedEvent(
                    entertainmentProvider2,
                    "The Shining at the Meadows (Free Screening) (Live Action)",
                    EventType.Sports
            );
            eventState.createNonTicketedEvent(
                    entertainmentProvider3,
                    "Music for everyone!",
                    EventType.Music
            );

        }

        assertEquals(4*999999,eventState.getAllEvents().size());
    }


    // findEventByNumber() unit tests
    @Test
    void findEventByExistedNumber() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                entertainmentProvider3,
                "Music for everyone!",
                EventType.Music
        );

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        Event event = eventState.findEventByNumber(2);
        assertEquals("London Summer Olympics", event.getTitle());
    }

    @Test
    void findEventByLongNumber() {
        EventState eventState = new EventState();

        for (int i = 0; i < 999999; i++) {
            eventState.createNonTicketedEvent(null,null,null);
        }
        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        Event event = eventState.findEventByNumber(999999 + 1);
        assertEquals("London Summer Olympics", event.getTitle());
    }

    @Test
    void findEventByNonExistedNumber() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                entertainmentProvider3,
                "Music for everyone!",
                EventType.Music
        );

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        Event event = eventState.findEventByNumber(3);
        assertNull(event);
    }

    @Test
    void findEventByZeroNumber() {
        EventState eventState = new EventState();

        NonTicketedEvent nonTicketedEvent = eventState.createNonTicketedEvent(
                entertainmentProvider3,
                "Music for everyone!",
                EventType.Music
        );

        TicketedEvent ticketedEvent = eventState.createTicketedEvent(
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        Event event = eventState.findEventByNumber(0);
        assertNull(event);
    }

}
