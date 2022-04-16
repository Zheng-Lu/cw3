import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;
import state.SponsorshipState;
import state.UserState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestSponsorshipState {

    EntertainmentProvider entertainmentProvider1;
    EntertainmentProvider entertainmentProvider2;
    EntertainmentProvider entertainmentProvider3;
    TicketedEvent ticketedEvent1;
    TicketedEvent ticketedEvent2;
    TicketedEvent ticketedEvent3;
    EventPerformance eventPerformance1;

    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @BeforeEach
    void setup() throws Exception {
        entertainmentProvider1 = new EntertainmentProvider(
                "No org",
                "Leith Walk",
                "a hat on the ground",
                "the best musicican ever",
                "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!",
                Collections.emptyList(),
                Collections.emptyList()
        );

        entertainmentProvider2 = new EntertainmentProvider(
                "Cinema Conglomerate",
                "Global Office, International Space Station",
                "$$$@there'sNoEmailValidation.wahey!",
                "Mrs Representative",
                "odeon@cineworld.com",
                "F!ghT th3 R@Pture",
                List.of("Dr Strangelove"),
                List.of("we_dont_get_involved@cineworld.com")
        );

        entertainmentProvider3 = new EntertainmentProvider(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        );

        ticketedEvent1 = new TicketedEvent(
                1,
                entertainmentProvider1,
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25
        );

        ticketedEvent2 = new TicketedEvent(
                2,
                entertainmentProvider1,
                "Winter Olympics",
                EventType.Sports,
                40000,
                400
        );
    }


    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    // addSponsorshipRequest() unit tests
    @Test
    void testAddSponsorshipRequest() {
        SponsorshipState sponsorshipState = new SponsorshipState();

        SponsorshipRequest sponsorshipRequest = sponsorshipState.addSponsorshipRequest(ticketedEvent1);
        long requestNumber = sponsorshipRequest.getRequestNumber();
        assertNotNull(sponsorshipRequest);
        assertEquals(1,requestNumber);
    }

    @Test
    void testAddNullSponsorshipRequest() {
        SponsorshipState sponsorshipState = new SponsorshipState();

        SponsorshipRequest sponsorshipRequest = sponsorshipState.addSponsorshipRequest(null);
        assertNotNull(sponsorshipRequest);
    }

    // getAllSponsorshipRequest() unit test
    @Test
    void testGetAllSponsorshipRequest() {
        SponsorshipState sponsorshipState = new SponsorshipState();

        for (int i = 0; i < 999999; i++) {
            sponsorshipState.addSponsorshipRequest(ticketedEvent1);
        }

        List<SponsorshipRequest> sponsorshipRequestList = sponsorshipState.getAllSponsorshipRequest();
        assertEquals(999999, sponsorshipRequestList.size());
    }

    @Test
    void testGetEmptyListSponsorshipRequest() {
        SponsorshipState sponsorshipState = new SponsorshipState();

        List<SponsorshipRequest> sponsorshipRequestList = sponsorshipState.getAllSponsorshipRequest();
        assertEquals(0, sponsorshipRequestList.size());
    }

    // getPendingSponsorshipRequest() unit tests
    @Test
    void testGetPendingSponsorshipRequest() {
        SponsorshipState sponsorshipState = new SponsorshipState();

        SponsorshipRequest sponsorshipRequest = sponsorshipState.addSponsorshipRequest(ticketedEvent1);
        SponsorshipStatus sponsorshipStatus = sponsorshipRequest.getStatus();
        assertEquals(SponsorshipStatus.PENDING, sponsorshipStatus);

        List<SponsorshipRequest> PendingSponsorshipRequestList = sponsorshipState.getPendingSponsorshipRequest();
        assertEquals(1, PendingSponsorshipRequestList.size());
    }

    @Test
    void testGetEmptyListPendingSponsorshipRequest() {
        SponsorshipState sponsorshipState = new SponsorshipState();

        List<SponsorshipRequest> PendingSponsorshipRequestList = sponsorshipState.getPendingSponsorshipRequest();
        assertEquals(0, PendingSponsorshipRequestList.size());
    }
}
