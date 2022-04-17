import command.ListSponsorshipRequestsCommand;
import command.RespondSponsorshipCommand;
import controller.Controller;
import logging.Logger;
import model.SponsorshipRequest;
import org.junit.jupiter.api.*;

import java.util.List;


public class RespondSponsorshipSystemTests {
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
    @DisplayName("Respond Sponsorship should work")
    void testRespondSponsorship() {
        Controller controller = new Controller();

        governmentAcceptAllSponsorships(controller);
    }
}
