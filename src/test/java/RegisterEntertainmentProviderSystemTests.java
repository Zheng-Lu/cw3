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

import static org.junit.jupiter.api.Assertions.*;

public class RegisterEntertainmentProviderSystemTests {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    String getLog() {
        List<LogEntry> entries = Logger.getInstance().getLog();
        String logStatus = entries.get(entries.size()-1).getAdditionalInfo().toString();
        System.out.println(logStatus);
        return logStatus;
    }

    @Test
    @DisplayName("Entertainment Provider Registrations should work")
    void successfulEntertainmentProviderRegistered(){
        Controller controller = new Controller();

        RegisterEntertainmentProviderCommand cmd = new RegisterEntertainmentProviderCommand(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        );
        controller.runCommand(cmd);
        String logStatus = getLog();
        assertNotNull(logStatus);
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",logStatus);
    }

    @Test
    @DisplayName("Entertainment Provider Registrations with existed organisation should not work")
    void failedEntertainmentProviderRegisteredWithExistedOrg(){
        Controller controller = new Controller();

        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        );
        controller.runCommand(cmd1);
        String logStatus1 = getLog();
        assertNotNull(logStatus1);
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",logStatus1);

        controller.runCommand(new LogoutCommand());

        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                "Olympics Committee",
                "Mr. Meeseek",
                "nnnnnnnnnnoreply@gmail.com",
                "Morty Smith",
                "qc29m734yrc2q8ytrbn@gmail.com",
                "c9pn8wqrwayansuh",
                List.of("Unknown Spy", "Actor"),
                List.of("Actor@gmail.com", "Unknown&Spy@gmail.com")
        );

        controller.runCommand(cmd2);
        String logStatus2 = getLog();
        assertNotNull(logStatus2);
        assertEquals("{STATUS:=USER_REGISTER_ORG_ALREADY_REGISTERED}",logStatus2);
    }

    @Test
    @DisplayName("Entertainment Provider Registrations with existed email should not work")
    void failedEntertainmentProviderRegisteredWithExistedEmail(){
        Controller controller = new Controller();

        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        );
        controller.runCommand(cmd1);
        String logStatus1 = getLog();
        assertNotNull(logStatus1);
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",logStatus1);

        controller.runCommand(new LogoutCommand());

        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                "International Olympics Committee",
                "Mr. Meeseek",
                "nnnnnnnnnnoreply@gmail.com",
                "Morty Smith",
                "anonymous@gmail.com",
                "c9pn8wqrwayansuh",
                List.of("Unknown Spy", "Actor"),
                List.of("Actor@gmail.com", "Unknown&Spy@gmail.com")
        );

        controller.runCommand(cmd2);
        String logStatus2 = getLog();
        assertNotNull(logStatus2);
        assertEquals("{STATUS:=USER_REGISTER_EMAIL_ALREADY_REGISTERED}",logStatus2);
    }

}
