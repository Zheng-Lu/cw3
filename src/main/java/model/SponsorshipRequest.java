package model;

public class SponsorshipRequest {
    private long requestNumber;
    private TicketedEvent event;
    private SponsorshipStatus status;

    public SponsorshipRequest(long requestNumber, TicketedEvent event){
        this.requestNumber = requestNumber;
        this.event = event;
        this.status = SponsorshipStatus.PENDING;
    }
}
