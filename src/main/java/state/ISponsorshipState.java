package state;

import model.SponsorshipRequest;
import model.TicketedEvent;

import java.util.List;

public interface ISponsorshipState {
    SponsorshipRequest addSponsorshipRequest(TicketedEvent event);

    List<SponsorshipRequest> getAllSponsorshipRequest();

    List<SponsorshipRequest> getPendingSponsorshipRequest();

    SponsorshipRequest findRequestByNumber(long requestNumber);

    // use for copying SponsorshipState
    long getNextRequestNumber();
}
