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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateEventSystemTests {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    private static void registerCinemaProvider(Controller controller) {
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
        controller.runCommand(new LogoutCommand());
    }

    private static void registerOlympicsProvider(Controller controller) {
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
        controller.runCommand(new LogoutCommand());
    }

    private static void registerBuskingProvider(Controller controller) {
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
        controller.runCommand(new LogoutCommand());
    }

    private static void loginOlympicsProvider(Controller controller) {
        controller.runCommand(new LoginCommand("anonymous@gmail.com", "anonymous"));
    }

    private static void loginCinemaProvider(Controller controller) {
        controller.runCommand(new LoginCommand("odeon@cineworld.com", "F!ghT th3 R@Pture"));
    }

    private static void loginBuskingProvider(Controller controller) {
        controller.runCommand(new LoginCommand("busk@every.day", "When they say 'you can't do this': Ding Dong! You are wrong!"));
    }

    private String getLog() {
        List<LogEntry> entries = Logger.getInstance().getLog();
        String logStatus = entries.get(entries.size()-1).getAdditionalInfo().toString();
        System.out.println(logStatus);
        return logStatus;
    }

    @Test
    @DisplayName("Creat Ticketed Events should work >")
    void canCreateTicketedEvent() {
        Controller controller = new Controller();

        registerOlympicsProvider(controller);
        loginOlympicsProvider(controller);
        System.out.println("Creating Ticketed Event1 with requested Sponsorship:");
        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25,
                true
        );
        controller.runCommand(eventCmd1);
        long numEvents1 = eventCmd1.getResult();
        String tickedEventStatus1 = getLog();
        assertEquals("{STATUS:=CREATE_EVENT_REQUESTED_SPONSORSHIP}", tickedEventStatus1);

        System.out.println("Creating Ticketed Event2 without requested Sponsorship:");
        CreateTicketedEventCommand eventCmd2 = new CreateTicketedEventCommand(
                "Winter Olympics",
                EventType.Sports,
                40000,
                400,
                false
        );
        controller.runCommand(eventCmd2);
        long numEvents2 = eventCmd2.getResult();
        String tickedEventStatus2 = getLog();
        assertEquals("{STATUS:=CREATE_TICKETED_EVENT_SUCCESS}", tickedEventStatus2);
        controller.runCommand(new LogoutCommand());

        registerCinemaProvider(controller);
        loginCinemaProvider(controller);
        System.out.println("Creating Ticketed Event3 without requested Sponsorship:");
        CreateTicketedEventCommand eventCmd3 = new CreateTicketedEventCommand(
                "The LEGO Movie",
                EventType.Movie,
                50,
                15.75,
                false
        );
        controller.runCommand(eventCmd3);
        long numEvents3 = eventCmd3.getResult();
        String tickedEventStatus3 = getLog();
        assertEquals("{STATUS:=CREATE_TICKETED_EVENT_SUCCESS}", tickedEventStatus3);
        controller.runCommand(new LogoutCommand());

        assertEquals(1, numEvents1,
                "Number of events should match the expectation");
        assertEquals(2, numEvents2,
                "Number of events should match the expectation");
        assertEquals(3, numEvents3,
                "Number of events should match the expectation");

    }

    @Test
    @DisplayName("Creat Non-Ticketed Events should work >")
    void canCreateNonTicketedEvent() {
        Controller controller = new Controller();

        registerBuskingProvider(controller);
        loginBuskingProvider(controller);
        System.out.println("Creating Non-Ticketed Event1:");
        CreateNonTicketedEventCommand eventCmd1 = new CreateNonTicketedEventCommand(
                "Music for everyone!",
                EventType.Music
        );
        controller.runCommand(eventCmd1);
        long numEvents1 = eventCmd1.getResult();
        String tickedEventStatus1 = getLog();
        assertEquals("{STATUS:=CREATE_NON_TICKETED_EVENT_SUCCESS}", tickedEventStatus1);
        controller.runCommand(new LogoutCommand());

        registerCinemaProvider(controller);
        loginCinemaProvider(controller);
        System.out.println("Creating Non-Ticketed Event2:");
        CreateNonTicketedEventCommand eventCmd2 = new CreateNonTicketedEventCommand(
                "The Shining at the Meadows (Free Screening) (Live Action)",
                EventType.Sports
        );
        controller.runCommand(eventCmd2);
        long numEvents2 = eventCmd2.getResult();
        String tickedEventStatus2 = getLog();
        assertEquals("{STATUS:=CREATE_NON_TICKETED_EVENT_SUCCESS}", tickedEventStatus2);
        controller.runCommand(new LogoutCommand());

        assertEquals(1, numEvents1,
                "Number of events should match the expectation");
        assertEquals(2, numEvents2,
                "Number of events should match the expectation");
    }

//    @Test
//    @DisplayName("Test Creating Events With Null Information >")
//    void testCreateEventsWithNullInformation() {
//        Controller controller = new Controller();
//
//        registerBuskingProvider(controller);
//        loginBuskingProvider(controller);
//        System.out.println("Creating Non-Ticketed Event1:");
//        CreateNonTicketedEventCommand eventCmd1 = new CreateNonTicketedEventCommand(
//                null,
//                EventType.Music
//        );
//        controller.runCommand(eventCmd1);
//        long numEvents1 = eventCmd1.getResult();
//        controller.runCommand(new LogoutCommand());
//
//        registerCinemaProvider(controller);
//        loginCinemaProvider(controller);
//        System.out.println("Creating Non-Ticketed Event2:");
//        CreateNonTicketedEventCommand eventCmd2 = new CreateNonTicketedEventCommand(
//                "The Shining at the Meadows (Free Screening) (Live Action)",
//                null
//        );
//        controller.runCommand(eventCmd2);
//        long numEvents2 = eventCmd2.getResult();
//        controller.runCommand(new LogoutCommand());
//
//        assertEquals(0, numEvents1,
//                "Number of events should match the expectation");
//        assertEquals(0, numEvents2,
//                "Number of events should match the expectation");
//    }
}
