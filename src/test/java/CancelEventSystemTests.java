import command.*;
import controller.Controller;
import logging.Logger;
import model.Event;
import model.EventType;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CancelEventSystemTests {
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
    }

    private static void loginOlympicsProvider(Controller controller) {
        controller.runCommand(new LoginCommand("anonymous@gmail.com", "anonymous"));
    }

    private static long createOlympicsProviderWith2Events(Controller controller) {
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

        return eventNumber2;
    }

    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    @Test
    @DisplayName("Cancel Event should work")
    void canCancelEvent() {
        Controller controller = new Controller();

        // Entertainment Provider Register
        registerOlympicsProvider(controller);
        controller.runCommand(new LogoutCommand());

        // Login and create the event then Logout
        loginOlympicsProvider(controller);
        long numEvents1 = createOlympicsProviderWith2Events(controller);
        controller.runCommand(new LogoutCommand());

        // Login then cancel the event
        loginOlympicsProvider(controller);
        ListEventsCommand list_events_cmd = new ListEventsCommand(true, true);
        controller.runCommand(list_events_cmd);
        List<Event> events = list_events_cmd.getResult();

        CancelEventCommand cmd = new CancelEventCommand(events.get(0).getEventNumber(), "Event Cancelled");
        controller.runCommand(cmd);
        Boolean isCancelSuccessful = cmd.getResult();

        assertTrue(isCancelSuccessful);
    }
}