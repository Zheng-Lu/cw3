package command;

import controller.Context;
import model.EntertainmentProvider;
import model.EventType;
import model.NonTicketedEvent;

public class CreateNonTicketedEventCommand extends CreateEventCommand{
    private LogStatus logStatus;

    private enum LogStatus{
        CREATE_NON_TICKETED_EVENT_SUCCESS
    }

    public CreateNonTicketedEventCommand(String title, EventType type) {
        super(title, type);
    }

    @Override
    public void execute(Context context) {
        if(isUserAllowedToCreateEvent(context)){
            NonTicketedEvent newEvent = context.getEventState().createNonTicketedEvent((EntertainmentProvider)
                            context.getUserState().getCurrentUser(), this.title,this.type);
            this.eventNumberResult = newEvent.getEventNumber();
            this.logStatus = LogStatus.CREATE_NON_TICKETED_EVENT_SUCCESS;
        }
    }
}
