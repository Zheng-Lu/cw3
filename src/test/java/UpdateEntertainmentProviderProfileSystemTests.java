import command.LoginCommand;
import command.LogoutCommand;
import command.RegisterEntertainmentProviderCommand;
import command.UpdateEntertainmentProviderProfileCommand;
import controller.Controller;
import logging.Logger;
import model.User;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UpdateEntertainmentProviderProfileSystemTests {
    private static void register3EntertainmentProvider(Controller controller) {
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
    @DisplayName("")
    void testUpdateAllInformation() {
        Controller controller = new Controller();

        register3EntertainmentProvider(controller);
        loginOlympicsProvider(controller);
        UpdateEntertainmentProviderProfileCommand cmd = new UpdateEntertainmentProviderProfileCommand(
                "anonymous",
                "International Olympics Committee",
                "Château de Vidy Case postale 356 1001 Lausanne Switzerland",
                "07751234567",
                "Payment@outlook.com",
                "Shu Gu",
                "BouquinGU@outlook.com",
                List.of("Judy Zhu", "Squanchy"),
                List.of("unknown666@gmail.com", "Actor666@gmail.com")
        );
        controller.runCommand(cmd);
        Boolean isUpdateSuccessful = cmd.getResult();
        assertTrue(isUpdateSuccessful, "Updating Profile is failed");
        controller.runCommand(new LogoutCommand());

        LoginCommand login_cmd = new LoginCommand(
                "BouquinGU@outlook.com",
                "07751234567"
        );
        controller.runCommand(login_cmd);
        User consumer1 = login_cmd.getResult();
        assertNotNull(consumer1, "User not found");
        assertEquals("BouquinGU@outlook.com", consumer1.getEmail(), "Incorrect Email");
        assertTrue(consumer1.checkPasswordMatch("07751234567"), "Incorrect password");
    }


}
