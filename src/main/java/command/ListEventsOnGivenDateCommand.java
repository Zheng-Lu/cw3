package command;

import controller.Context;
import logging.Logger;
import model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEventsOnGivenDateCommand extends ListEventsCommand {
    private final LocalDateTime searchDateTime;
    private final boolean activeEventsOnly;
    private final boolean userEventsOnly;
    private List<Event> eventListResult;
    private LogStatus logStatus;

    public ListEventsOnGivenDateCommand(boolean userEventsOnly, boolean activeEventsOnly,
                                        LocalDateTime searchDateTime) {
        super(userEventsOnly, activeEventsOnly);
        this.activeEventsOnly = activeEventsOnly;
        this.userEventsOnly = userEventsOnly;
        this.searchDateTime = searchDateTime;
    }

    @Override
    public void execute(Context context) {
        User user = context.getUserState().getCurrentUser();
        List<Event> eventList = context.getEventState().getAllEvents();
        if (this.activeEventsOnly) {
            eventList.removeIf(e -> e.getStatus() != EventStatus.ACTIVE);
        }
        if (this.userEventsOnly && user == null) {
            this.logStatus = LogStatus.LIST_USER_EVENTS_NOT_LOGGED_IN;
        } else if (this.userEventsOnly) {
            if (user.getClass() == EntertainmentProvider.class) {
                eventList.removeIf(e -> e.getOrganiser() != context.getUserState().getCurrentUser());
            }
            if (user.getClass() == Consumer.class) {
                for (Event e : eventList) {
                    Collection<EventPerformance> ePerformances = e.getPerformances();
                    boolean testResult = false;
                    // at least one performance of an event needs to satisfy consumer's preference
                    for (EventPerformance ep : ePerformances) {
                        if (((Consumer) user).getPreferences().satisfyPreferences(ep)) {
                            testResult = true;
                            break;
                        }
                    }
                    // if no performance satisfies consumer's preference
                    if (!testResult) {
                        eventList.remove(e);
                    }
                }

            }
        }
        if (this.logStatus == null) {
            // add date filter
            for (Event e : eventList) {
                Collection<EventPerformance> ePerformances = e.getPerformances();
                boolean testResult = false;
                for (EventPerformance ep : ePerformances) {
                    if (searchDateTime.minusDays(1).isBefore(ep.getStartDateTime()) &&
                            searchDateTime.plusDays(1).isAfter(ep.getEndDateTime())) {
                        testResult = true;
                        break;
                    }
                }
                if (!testResult) {
                    eventList.remove(e);
                }
            }
            this.eventListResult = eventList;
            this.logStatus = LogStatus.LIST_USER_EVENTS_SUCCESS;
        }

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();
        info.put("STATUS:", this.logStatus);
        Logger.getInstance().logAction("ListEventsOnGivenDateCommand.execute()",
                getResult(), info);
    }

    @Override
    public List<Event> getResult() {
        if (logStatus == LogStatus.LIST_USER_EVENTS_SUCCESS) {
            return this.eventListResult;
        }
        return null;
    }

    private enum LogStatus {
        LIST_USER_EVENTS_SUCCESS,
        LIST_USER_EVENTS_NOT_LOGGED_IN
    }
}
