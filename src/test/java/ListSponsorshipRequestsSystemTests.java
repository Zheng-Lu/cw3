import command.ListSponsorshipRequestsCommand;
import controller.Controller;
import logging.Logger;
import model.SponsorshipRequest;
import org.junit.jupiter.api.*;

import java.util.List;

public class ListSponsorshipRequestsSystemTests {
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
    @DisplayName("List Sponsorship Requests should work")
    void testListSponsorshipRequests() {
        Controller controller = new Controller();

        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();
        System.out.println(requests);
    }
}
