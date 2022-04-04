package command;

import controller.Context;
import model.EntertainmentProvider;
import model.EventType;
import model.SponsorshipRequest;
import model.TicketedEvent;

public class CreateTicketedEventCommand extends CreateEventCommand{
    private int numTickets;
    private double ticketPrice;
    private boolean requestSponsorship;
    private LogStatus logStatus;

    private enum LogStatus{
        CREATE_TICKETED_EVENT_SUCCESS,
        CREATE_EVENT_REQUESTED_SPONSORSHIP
    }

    public CreateTicketedEventCommand(String title, EventType type, int numTickets,
                                      double ticketPrice, boolean requestSponsorship) {
        super(title, type);
        this.numTickets = numTickets;
        this.ticketPrice = ticketPrice;
        this.requestSponsorship = requestSponsorship;
    }

    @Override
    public void execute(Context context) {
        if(isUserAllowedToCreateEvent(context)){
            if (requestSponsorship){
                TicketedEvent newEvent = context.getEventState().createTicketedEvent((EntertainmentProvider)
                        context.getUserState().getCurrentUser(),this.title,this.type,this.ticketPrice,
                        this.numTickets);
                SponsorshipRequest sponsorshipRequest = context.getSponsorshipState().addSponsorshipRequest(newEvent);
                newEvent.setSponsorshipRequest(sponsorshipRequest);
                this.logStatus = LogStatus.CREATE_EVENT_REQUESTED_SPONSORSHIP;
                this.eventNumberResult = newEvent.getEventNumber();
            }
            else {
                TicketedEvent newEvent = context.getEventState().createTicketedEvent((EntertainmentProvider)
                                context.getUserState().getCurrentUser(),this.title,this.type,this.ticketPrice,
                        this.numTickets);
                this.logStatus = LogStatus.CREATE_TICKETED_EVENT_SUCCESS;
                this.eventNumberResult = newEvent.getEventNumber();
            }
        }
    }
}
