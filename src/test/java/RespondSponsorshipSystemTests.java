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



public class RespondSponsorshipSystemTests {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    private static void governmentAcceptAllSponsorships(Controller controller) {
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests) {
            controller.runCommand(new RespondSponsorshipCommand(
                    request.getRequestNumber(), 25
            ));
        }
    }

    @Test
    @DisplayName("Respond Sponsorship should work")
    void testRespondSponsorship() {
        Controller controller = new Controller();
        
        governmentAcceptAllSponsorships(controller);
    }
}
