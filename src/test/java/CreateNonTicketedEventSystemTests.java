import command.CreateNonTicketedEventCommand;
import command.LoginCommand;
import command.LogoutCommand;
import command.RegisterEntertainmentProviderCommand;
import controller.Controller;
import logging.Logger;
import model.EventType;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateNonTicketedEventSystemTests {
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
    }

    private static void loginCinemaProvider(Controller controller) {
        controller.runCommand(new LoginCommand("odeon@cineworld.com", "F!ghT th3 R@Pture"));
    }

    private static void loginBuskingProvider(Controller controller) {
        controller.runCommand(new LoginCommand("busk@every.day", "When they say 'you can't do this': Ding Dong! You are wrong!"));
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
    @DisplayName("Create 1 Non Ticketed Event should work")
    void testCreate1NonTicketedEvent() {
        Controller controller = new Controller();

        registerBuskingProvider(controller);
        controller.runCommand(new LogoutCommand());
        loginBuskingProvider(controller);

        CreateNonTicketedEventCommand eventCmd = new CreateNonTicketedEventCommand(
                "Music for everyone!",
                EventType.Music
        );
        controller.runCommand(eventCmd);
        long eventNumber = eventCmd.getResult();
        assertEquals(1, eventNumber,
                "Number of events should match the expectation");
    }

    @Test
    @DisplayName("Create 2 Non Ticketed Events should work")
    void testCreate2NonTicketedEvents() {
        Controller controller = new Controller();

        // Example 1
        registerBuskingProvider(controller);
        controller.runCommand(new LogoutCommand());
        loginBuskingProvider(controller);

        CreateNonTicketedEventCommand eventCmd1 = new CreateNonTicketedEventCommand(
                "Music for everyone!",
                EventType.Music
        );
        controller.runCommand(eventCmd1);
        long eventNumber1 = eventCmd1.getResult();

        controller.runCommand(new LogoutCommand());

        // Example 2
        registerCinemaProvider(controller);
        controller.runCommand(new LogoutCommand());
        loginCinemaProvider(controller);

        CreateNonTicketedEventCommand eventCmd2 = new CreateNonTicketedEventCommand(
                "The Shining at the Meadows (Free Screening) (Live Action)",
                EventType.Sports
        );
        controller.runCommand(eventCmd2);
        long eventNumber2 = eventCmd2.getResult();

        controller.runCommand(new LogoutCommand());


        assertEquals(1, eventNumber1,
                "Number of events should match the expectation");
        assertEquals(2, eventNumber2,
                "Number of events should match the expectation");
    }
}
