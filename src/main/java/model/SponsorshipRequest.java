package model;

public class SponsorshipRequest {
    private long requestNumber;
    private TicketedEvent event;
    private SponsorshipStatus status;
    private Integer sponsoredPricePercent = 0;
    private TicketedEvent attribute;
    private String sponsorAccuntEmail;

    public SponsorshipRequest(long requestNumber, TicketedEvent event){
        this.requestNumber = requestNumber;
        this.event = event;
        this.status = SponsorshipStatus.PENDING;
    }

    public long getRequestNumber(){
        return this.requestNumber;
    }

    public TicketedEvent getEvent(){
        return this.event;
    }

    public SponsorshipStatus getStatus(){
        return this.status;
    }

    public Integer getSponsoredPricePercent(){
        return this.sponsoredPricePercent;
    }

    public String getSponsorAccuntEmail(){
        return this.sponsorAccuntEmail;
    }

    public void accept(int percent, String sponsorAccuntEmail){
        this.status = SponsorshipStatus.ACCEPTED;
        this.sponsorAccuntEmail = sponsorAccuntEmail;
        this.sponsoredPricePercent = percent;
    }

    public void reject(){
        this.status = SponsorshipStatus.REJECTED;
    }
}
