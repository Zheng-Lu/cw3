package model;

public class TicketedEvent extends Event{

    private double ticketPrice;
    private int numTickets;
    private SponsorshipRequest sponsorshipRequest;

    public TicketedEvent(long eventNumber, EntertainmentProvider organiser, String title, EventType type,
                         double ticketPrice, int numTickets){
        super(eventNumber,organiser,title,type);
        this.ticketPrice = ticketPrice;
        this.numTickets = numTickets;
    }

    public double getOriginalTicketPrice(){
        return this.ticketPrice;
    }

    public double getDiscountedTicketPrice(){
        if (this.isSponsored()){
            return ticketPrice - ticketPrice*sponsorshipRequest.getSponsoredPricePercent()/100;
        }else{
            return ticketPrice;
        }
    }

    public int getNumTickets(){
        return numTickets;
    }

    public String getSponsorAccountEmail(){
        return sponsorshipRequest.getSponsorAccuntEmail();
    }

    public boolean isSponsored(){
        return sponsorshipRequest.getStatus().equals(SponsorshipStatus.ACCEPTED);
    }

    public void setSponsorshipRequest(SponsorshipRequest sponsorshipRequest){
        this.sponsorshipRequest = sponsorshipRequest;
    }

    @Override
    public String toString(){
        return "";
    }
}
