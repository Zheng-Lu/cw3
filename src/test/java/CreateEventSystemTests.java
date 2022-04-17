import command.*;
import controller.Controller;
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

    private static void loginCinemaProvider(Controller controller) {
        controller.runCommand(new LoginCommand("odeon@cineworld.com", "F!ghT th3 R@Pture"));
    }

    private static void loginBuskingProvider(Controller controller) {
        controller.runCommand(new LoginCommand("busk@every.day", "When they say 'you can't do this': Ding Dong! You are wrong!"));
    }
}
