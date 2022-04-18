import command.*;
import controller.Controller;
import logging.LogEntry;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookEventSystemTests {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    private static void register3Consumers(Controller controller) {
        controller.runCommand(new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        ));
        controller.runCommand(new LogoutCommand());

        controller.runCommand(new RegisterConsumerCommand(
                "Jane Giantsdottir",
                "jane@inf.ed.ac.uk",
                "04462187232",
                "giantsRverycool",
                "jane@aol.com"
        ));
        controller.runCommand(new LogoutCommand());

        controller.runCommand(new RegisterConsumerCommand(
                "Wednesday Kebede",
                "i-will-kick-your@gmail.com",
                "-",
                "it is wednesday my dudes",
                "i-will-kick-your@gmail.com"
        ));
        controller.runCommand(new LogoutCommand());
    }

    private static void loginConsumer1(Controller controller) {
        controller.runCommand(new LoginCommand("jbiggson1@hotmail.co.uk", "jbiggson2"));
    }

    private static void loginConsumer2(Controller controller) {
        controller.runCommand(new LoginCommand("jane@inf.ed.ac.uk", "giantsRverycool"));
    }

    private static void loginConsumer3(Controller controller) {
        controller.runCommand(new LoginCommand("i-will-kick-your@gmail.com", "it is wednesday my dudes"));
    }

    private static void createBuskingProviderWith1Event(Controller controller) {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "No org",
                "Leith Walk",
                "a hat on the ground",
                "the best musicican ever",
                "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!",
                Collections.emptyList(),
                Collections.emptyList()
        ));

        CreateNonTicketedEventCommand eventCmd = new CreateNonTicketedEventCommand(
                "Music for everyone!",
                EventType.Music
        );
        controller.runCommand(eventCmd);
        long eventNumber = eventCmd.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber,
                "Leith as usual",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber,
                "You know it",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        ));

        controller.runCommand(new LogoutCommand());
    }

    private static void createCinemaProviderWith3Events(Controller controller) {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "Cinema Conglomerate",
                "Global Office, International Space Station",
                "$$$@there'sNoEmailValidation.wahey!",
                "Mrs Representative",
                "odeon@cineworld.com",
                "F!ghT th3 R@Pture",
                List.of("Dr Strangelove"),
                List.of("we_dont_get_involved@cineworld.com")
        ));

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "The LEGO Movie",
                EventType.Movie,
                50,
                15.75,
                false
        );
        controller.runCommand(eventCmd1);
        long eventNumber1 = eventCmd1.getResult();
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "You know how much it hurts when you step on a Lego piece?!?!",
                LocalDateTime.now().minusWeeks(1),
                LocalDateTime.now().minusWeeks(1).plusHours(2),
                Collections.emptyList(),
                false,
                true,
                false,
                50,
                50
        ));

        CreateTicketedEventCommand eventCmd2 = new CreateTicketedEventCommand(
                "Frozen Ballet",
                EventType.Dance,
                50,
                35,
                true
        );
        controller.runCommand(eventCmd2);
        long eventNumber2 = eventCmd2.getResult();
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber2,
                "Odeon cinema",
                LocalDateTime.now().plusWeeks(1).plusDays(2).plusHours(3),
                LocalDateTime.now().plusWeeks(1).plusDays(2).plusHours(6),
                Collections.emptyList(),
                false,
                true,
                false,
                50,
                50
        ));

        CreateNonTicketedEventCommand eventCmd3 = new CreateNonTicketedEventCommand(
                "The Shining at the Meadows (Free Screening) (Live Action)",
                EventType.Sports
        );
        controller.runCommand(eventCmd3);
        long eventNumber3 = eventCmd3.getResult();
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber3,
                "The Meadows, Edinburgh",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                List.of("You"),
                false,
                false,
                true,
                1000,
                9999
        ));

        controller.runCommand(new LogoutCommand());
    }

    private static void createOlympicsProviderWith2Events(Controller controller) {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        ));

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25,
                true
        );
        controller.runCommand(eventCmd1);
        long eventNumber1 = eventCmd1.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(8),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                3000
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Swimming arena",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusHours(8),
                List.of("Everyone in swimming"),
                true,
                true,
                false,
                200,
                300
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1).plusDays(1),
                LocalDateTime.now().plusMonths(1).plusDays(1).plusHours(6),
                List.of("Everyone in javelin throw and long jump"),
                false,
                true,
                true,
                3000,
                3000
        ));

        CreateTicketedEventCommand eventCmd2 = new CreateTicketedEventCommand(
                "Winter Olympics",
                EventType.Sports,
                40000,
                400,
                true
        );
        controller.runCommand(eventCmd2);
        long eventNumber2 = eventCmd2.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber2,
                "The Alps",
                LocalDateTime.now().plusYears(2).plusMonths(7),
                LocalDateTime.now().plusYears(2).plusMonths(7).plusDays(3),
                List.of("Everyone in slalom skiing"),
                true,
                true,
                true,
                4000,
                10000
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber2,
                "Somewhere else",
                LocalDateTime.now().plusYears(2).plusMonths(7).plusDays(2),
                LocalDateTime.now().plusYears(2).plusMonths(7).plusDays(4),
                List.of("Everyone in ski jump"),
                true,
                true,
                true,
                4000,
                10000
        ));

        controller.runCommand(new LogoutCommand());
    }

    private static void consumerBookXticketsNthTicketedEvent(Controller controller, int x, int n, boolean userOnly, boolean active) {
        ListEventsCommand cmd = new ListEventsCommand(userOnly, active);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();
        System.out.println(events.get(events.size()-1));

        if (n <= events.size()) {
            for (Event event : events) {
                if (event instanceof TicketedEvent) {
                    n--;
                }

                if (n <= 0) {
                    Collection<EventPerformance> performances = event.getPerformances();
                    controller.runCommand(new BookEventCommand(
                            event.getEventNumber(),
                            performances.iterator().next().getPerformanceNumber(),
                            x
                    ));
                    return;
                }
            }
        } else {
            Collection<EventPerformance> performances = null;
            controller.runCommand(new BookEventCommand(
                    n,
                    0,
                    x
            ));
            return;
        }
    }

    private static String getLog() {
        List<LogEntry> entries = Logger.getInstance().getLog();
        String logStatus = entries.get(entries.size()-1).getAdditionalInfo().toString();
        System.out.println(logStatus);
        return logStatus;
    }

    @Test
    @DisplayName("Book Event Should Work >")
    void testCanBookEvent() {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer1(controller);
        System.out.println("Consumer 1 books 1 ticket for 1st ticketed event:");
        consumerBookXticketsNthTicketedEvent(controller, 1,1,false,true);
        assertEquals("{STATUS:=BOOK_EVENT_SUCCESS}", getLog());
        controller.runCommand(new LogoutCommand());

        loginConsumer2(controller);
        System.out.println("Consumer 2 books 5 tickets for 2nd ticketed event:");
        consumerBookXticketsNthTicketedEvent(controller, 5,2,false,true);
        assertEquals("{STATUS:=BOOK_EVENT_SUCCESS}", getLog());
        controller.runCommand(new LogoutCommand());

        loginConsumer3(controller);
        System.out.println("Consumer 3 books 15 tickets for 3rd ticketed event:");
        consumerBookXticketsNthTicketedEvent(controller, 15,4,false,true);
        assertEquals("{STATUS:=BOOK_EVENT_SUCCESS}", getLog());
        controller.runCommand(new LogoutCommand());
    }

    @Test
    @DisplayName("Book Event with excess or invalid tickets >")
    void testBookEventWithExcessOrInvalidNumberTickets() {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer1(controller);
        System.out.println("Consumer 1 books 9999999 ticket for 1st ticketed event:");
        consumerBookXticketsNthTicketedEvent(controller, 9999999,1,false,true);
        assertEquals("{STATUS:=BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT}", getLog());
        controller.runCommand(new LogoutCommand());

        loginConsumer2(controller);
        System.out.println("Consumer 2 books 0 ticket for 4th ticketed event:");
        consumerBookXticketsNthTicketedEvent(controller, 0,4,false,true);
        assertEquals("{STATUS:=BOOK_EVENT_INVALID_NUM_TICKETS}", getLog());
        controller.runCommand(new LogoutCommand());
    }

    @Test
    @DisplayName("Book Invalid Events Testing >")
    void testBookInvalidEvent() {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer1(controller);
        System.out.println("Consumer 1 books 1 ticket for a event already over:");
        consumerBookXticketsNthTicketedEvent(controller, 1,3,false,true);
        assertEquals("{STATUS:=BOOK_EVENT_ALREADY_OVER}", getLog());
        controller.runCommand(new LogoutCommand());

        loginConsumer2(controller);
        System.out.println("Consumer 2 books 1 ticket for 999th ticketed event:");
        consumerBookXticketsNthTicketedEvent(controller, 1,999,false,true);
        assertEquals("{STATUS:=BOOK_EVENT_EVENT_NOT_FOUND}", getLog());
        controller.runCommand(new LogoutCommand());
    }

//    @Test
//    @DisplayName("Book Ticketed Events Testing >")
//    void testBookTicketedEvent() {
//        Controller controller = new Controller();
//
//        createOlympicsProviderWith2Events(controller);
//        createCinemaProviderWith3Events(controller);
//        createBuskingProviderWith1Event(controller);
//        register3Consumers(controller);
//
//        loginConsumer3(controller);
//        System.out.println("Consumer 3 books 1 ticket for a ticketed event:");
//        consumerBookXticketsNthTicketedEvent(controller, 1,6,false,true);
//        assertEquals("{STATUS:=BOOK_EVENT_NOT_A_TICKETED_EVENT}", getLog());
//        controller.runCommand(new LogoutCommand());
//    }
}
