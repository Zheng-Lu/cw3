package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.EventType;
import model.NonTicketedEvent;

import java.util.HashMap;
import java.util.Map;

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
            // no need to inform the provider's system
            this.eventNumberResult = newEvent.getEventNumber();
            this.logStatus = LogStatus.CREATE_NON_TICKETED_EVENT_SUCCESS;
        }

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();
        if (this.logStatus != null){
            info.put("STATUS:",this.logStatus);
        }
        if (super.logStatus != null){
            info.put("USER_STATUS:",super.logStatus);
        }
        Logger.getInstance().logAction("CreateNonTicketedEventCommand.execute()",
                getResult(),info);
    }
}
