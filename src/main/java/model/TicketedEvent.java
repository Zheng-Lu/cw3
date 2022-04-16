package model;

public class TicketedEvent extends Event {

    private final double ticketPrice;
    private final int numTickets;
    private SponsorshipRequest sponsorshipRequest;

    public TicketedEvent(long eventNumber, EntertainmentProvider organiser, String title, EventType type,
                         double ticketPrice, int numTickets) {
        super(eventNumber, organiser, title, type);
        this.ticketPrice = ticketPrice;
        this.numTickets = numTickets;
    }

    public double getOriginalTicketPrice() {
        return this.ticketPrice;
    }

    public double getDiscountedTicketPrice() {
        if (this.isSponsored()) {
            return ticketPrice - ticketPrice * sponsorshipRequest.getSponsoredPricePercent() / 100;
        } else {
            return ticketPrice;
        }
    }

    /**
     * get the MAXIMUM possible number of the tickets left
     * IF want to get the current number of tickets left, ask event provider's system by using
     * event.getOrganiser().getProviderSystem().getNumTicketsLeft()
     *
     * @return MAXIMUM possible number of the tickets left
     */
    public int getNumTickets() {
        return numTickets;
    }

    public String getSponsorAccountEmail() {
        return sponsorshipRequest.getSponsorAccountEmail();
    }

    public boolean isSponsored() {
        return sponsorshipRequest.getStatus().equals(SponsorshipStatus.ACCEPTED);
    }

    public void setSponsorshipRequest(SponsorshipRequest sponsorshipRequest) {
        this.sponsorshipRequest = sponsorshipRequest;
    }

}
