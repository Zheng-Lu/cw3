package command;

import controller.Context;
import model.*;

import java.util.Collection;
import java.util.List;

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
            for (Event e : eventList){
                if(e.getStatus() != EventStatus.ACTIVE){
                    eventList.remove(e);
                }
            }
        }
        if (this.userEventsOnly && user == null){
            this.logStatus = LogStatus.LIST_USER_EVENTS_NOT_LOGGED_IN;
            return;
        }
        else if (this.userEventsOnly){
            if (user.getClass() == EntertainmentProvider.class){
                for (Event e : eventList){
                    if (e.getOrganiser() != context.getUserState().getCurrentUser()){
                        eventList.remove(e);
                    }
                }
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
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.LIST_USER_EVENTS_SUCCESS){
            return this.eventListResult;
        }
        return null;
    }
}
