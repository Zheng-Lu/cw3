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

    @Test
    @DisplayName("Entertainment Provider Registrations should work")
    void successfulEntertainmentProviderRegistered(){
        Controller controller = new Controller();

//        register3Consumers(controller);
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
        List<LogEntry> logEntries = Logger.getInstance().getLog();
        System.out.println(logEntries.get(logEntries.size()-1).getResult());
//        assertNotNull(logEntries.get(logEntries.size()-1).getResult());
    }
}
