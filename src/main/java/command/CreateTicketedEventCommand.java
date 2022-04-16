package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.EventType;
import model.SponsorshipRequest;
import model.TicketedEvent;

import java.util.HashMap;
import java.util.Map;

public class CreateTicketedEventCommand extends CreateEventCommand {
    private final int numTickets;
    private final double ticketPrice;
    private final boolean requestSponsorship;
    private LogStatus logStatus;

    public CreateTicketedEventCommand(String title, EventType type, int numTickets,
                                      double ticketPrice, boolean requestSponsorship) {
        super(title, type);
        this.numTickets = numTickets;
        this.ticketPrice = ticketPrice;
        this.requestSponsorship = requestSponsorship;
    }

    @Override
    public void execute(Context context) {
        if (isUserAllowedToCreateEvent(context)) {
            TicketedEvent newEvent = context.getEventState().createTicketedEvent((EntertainmentProvider)
                            context.getUserState().getCurrentUser(), this.title, this.type, this.ticketPrice,
                    this.numTickets);

            if (requestSponsorship) {
                SponsorshipRequest sponsorshipRequest = context.getSponsorshipState().addSponsorshipRequest(newEvent);
                newEvent.setSponsorshipRequest(sponsorshipRequest);
                // inform MockEntertainmentProvider's System about the creation of the event
                ((EntertainmentProvider) (context.getUserState().getCurrentUser())).getProviderSystem().recordNewEvent(
                        newEvent.getEventNumber(), this.title, this.numTickets);
                this.logStatus = LogStatus.CREATE_EVENT_REQUESTED_SPONSORSHIP;
            } else {
                this.logStatus = LogStatus.CREATE_TICKETED_EVENT_SUCCESS;
            }
            // update the provider's system
            context.getEventState().findEventByNumber(newEvent.getEventNumber()).getOrganiser().getProviderSystem().
                    recordNewEvent(newEvent.getEventNumber(), this.title, this.numTickets);
            this.eventNumberResult = newEvent.getEventNumber();
        }

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();
        if (this.logStatus != null) {
            info.put("STATUS:", this.logStatus);
        }
        if (super.logStatus != null) {
            info.put("USER_STATUS:", super.logStatus);
        }
        Logger.getInstance().logAction("CreateTicketedEventCommand.execute()",
                getResult(), info);
    }

    private enum LogStatus {
        CREATE_TICKETED_EVENT_SUCCESS,
        CREATE_EVENT_REQUESTED_SPONSORSHIP
    }
}
