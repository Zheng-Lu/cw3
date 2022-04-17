import command.CreateTicketedEventCommand;
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

public class CreateTicketedEventSystemTests {
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

    private static void loginOlympicsProvider(Controller controller) {
        controller.runCommand(new LoginCommand("anonymous@gmail.com", "anonymous"));
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
    @DisplayName("Creat Ticketed Event should work")
    void canCreateTicketedEvent() {
        Controller controller = new Controller();

        registerOlympicsProvider(controller);
        controller.runCommand(new LogoutCommand());
        loginOlympicsProvider(controller);
        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25,
                true
        );
        controller.runCommand(eventCmd1);
        long numEvents1 = eventCmd1.getResult();
        CreateTicketedEventCommand eventCmd2 = new CreateTicketedEventCommand(
                "Winter Olympics",
                EventType.Sports,
                40000,
                400,
                true
        );
        controller.runCommand(eventCmd2);
        long numEvents2 = eventCmd2.getResult();
        controller.runCommand(new LogoutCommand());


        registerCinemaProvider(controller);
        controller.runCommand(new LogoutCommand());
        loginCinemaProvider(controller);
        CreateTicketedEventCommand eventCmd3 = new CreateTicketedEventCommand(
                "The LEGO Movie",
                EventType.Movie,
                50,
                15.75,
                false
        );
        controller.runCommand(eventCmd3);
        long numEvents3 = eventCmd3.getResult();
        controller.runCommand(new LogoutCommand());

        assertEquals(1, numEvents1,
                "Number of events should match the expectation");
        assertEquals(2, numEvents2,
                "Number of events should match the expectation");
        assertEquals(3, numEvents3,
                "Number of events should match the expectation");

    }
}
