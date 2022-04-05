package command;

import controller.Context;
import logging.Logger;
import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEventsCommand implements ICommand{
    private boolean userEventsOnly;
    private boolean activeEventsOnly;
    private List<Event> eventListResult;
    private LogStatus logStatus;

    private enum LogStatus{
        LIST_USER_EVENTS_SUCCESS,
        LIST_USER_EVENTS_NOT_LOGGED_IN
    }

    public ListEventsCommand(boolean userEventsOnly, boolean activeEventsOnly){
        this.userEventsOnly = userEventsOnly;
        this.activeEventsOnly = activeEventsOnly;
    }

    @Override
    public void execute(Context context) {
        User user = context.getUserState().getCurrentUser();
        List<Event> eventList = context.getEventState().getAllEvents();
        if (this.activeEventsOnly){
            eventList.removeIf(e -> e.getStatus() != EventStatus.ACTIVE);
        }
        if (this.userEventsOnly && user == null){
            this.logStatus = LogStatus.LIST_USER_EVENTS_NOT_LOGGED_IN;
        }
        else if (this.userEventsOnly){
            if (user.getClass() == EntertainmentProvider.class){
                eventList.removeIf(e -> e.getOrganiser() != user);
            }
            if(user.getClass() == Consumer.class){
                for (Event e : eventList){
                    Collection<EventPerformance> ePerformances = e.getPerformances();
                    boolean testResult = false;
                    // at least one performance of an event needs to satisfy consumer's preference
                    for (EventPerformance ep : ePerformances){
                        if (((Consumer) user).getPreferences().satisfyPreferences(ep)){
                            testResult = true;
                            break;
                        }
                    }
                    // if no performance satisfies consumer's preference
                    if (!testResult){
                        eventList.remove(e);
                    }
                }

            }
        }
        this.eventListResult = eventList;
        this.logStatus = LogStatus.LIST_USER_EVENTS_SUCCESS;

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();
        info.put("STATUS:",this.logStatus);
        Logger.getInstance().logAction("ListEventsCommand.execute()",
                getResult(),info);
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.LIST_USER_EVENTS_SUCCESS){
            return this.eventListResult;
        }
        return null;
    }
}
