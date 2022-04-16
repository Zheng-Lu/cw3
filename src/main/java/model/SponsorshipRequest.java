package model;

public class SponsorshipRequest {
    private final long requestNumber;
    private final TicketedEvent event;
    private SponsorshipStatus status;
    private Integer sponsoredPricePercent = 0;
    private TicketedEvent attribute;
    private String sponsorAccountEmail;

    public SponsorshipRequest(long requestNumber, TicketedEvent event) {
        this.requestNumber = requestNumber;
        this.event = event;
        this.status = SponsorshipStatus.PENDING;
    }

    public long getRequestNumber() {
        return this.requestNumber;
    }

    public TicketedEvent getEvent() {
        return this.event;
    }

    public SponsorshipStatus getStatus() {
        return this.status;
    }

    public Integer getSponsoredPricePercent() {
        return this.sponsoredPricePercent;
    }

    public String getSponsorAccountEmail() {
        if (status.equals(SponsorshipStatus.ACCEPTED)) {
            return this.sponsorAccountEmail;
        } else {
            return null;
        }
    }

    public void accept(int percent, String sponsorAccuntEmail) {
        this.status = SponsorshipStatus.ACCEPTED;
        this.sponsorAccountEmail = sponsorAccuntEmail;
        this.sponsoredPricePercent = percent;
    }

    public void reject() {
        this.status = SponsorshipStatus.REJECTED;
    }
}
