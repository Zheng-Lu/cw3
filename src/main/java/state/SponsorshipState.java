package state;

import model.SponsorshipRequest;
import model.SponsorshipStatus;
import model.TicketedEvent;

import java.util.ArrayList;
import java.util.List;

public class SponsorshipState implements ISponsorshipState {

    private long nextRequestNumber;
    private final List<SponsorshipRequest> sponsorshipRequests;

    public SponsorshipState() {
        this.nextRequestNumber = 1;
        this.sponsorshipRequests = new ArrayList<>();
    }

    public SponsorshipState(ISponsorshipState other) {
        this.nextRequestNumber = other.getNextRequestNumber();
        this.sponsorshipRequests = other.getAllSponsorshipRequest();
    }

    @Override
    public SponsorshipRequest addSponsorshipRequest(TicketedEvent event) {
        SponsorshipRequest request = new SponsorshipRequest(this.nextRequestNumber, event);
        this.nextRequestNumber = this.nextRequestNumber + 1;
        this.sponsorshipRequests.add(request);
        return request;
    }

    @Override
    public List<SponsorshipRequest> getAllSponsorshipRequest() {
        return sponsorshipRequests;
    }

    @Override
    public List<SponsorshipRequest> getPendingSponsorshipRequest() {
        List<SponsorshipRequest> pendingRequests = new ArrayList<>();
        for (SponsorshipRequest r : this.sponsorshipRequests) {
            if (r.getStatus() == SponsorshipStatus.PENDING) {
                pendingRequests.add(r);
            }
        }
        return pendingRequests;
    }

    @Override
    public SponsorshipRequest findRequestByNumber(long requestNumber) {
        for (SponsorshipRequest r : this.sponsorshipRequests) {
            if (r.getRequestNumber() == requestNumber) {
                return r;
            }
        }
        return null;
    }

    @Override
    public long getNextRequestNumber() {
        return this.nextRequestNumber;
    }
}
