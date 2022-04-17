package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.Event;
import model.EventPerformance;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEventPerformanceCommand implements ICommand {
    private final long eventNumber;
    private final String venueAddress;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final List<String> performerNames;
    private final boolean hasSocialDistancing;
    private final boolean hasAirFiltration;
    private final boolean isOutdoors;
    private final int capacityLimit;
    private final int venueSize;
    private EventPerformance eventPerformanceResult;
    private LogStatus logStatus;

    public AddEventPerformanceCommand(long eventNumber,
                                      String venueAddress,
                                      LocalDateTime startDateTime,
                                      LocalDateTime endDateTime,
                                      List<String> performerNames,
                                      boolean hasSocialDistancing,
                                      boolean hasAirFiltration,
                                      boolean isOutdoors,
                                      int capacityLimit,
                                      int venueSize) {
        this.eventNumber = eventNumber;
        this.venueAddress = venueAddress;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.hasSocialDistancing = hasSocialDistancing;
        this.hasAirFiltration = hasAirFiltration;
        this.isOutdoors = isOutdoors;
        this.capacityLimit = capacityLimit;
        this.venueSize = venueSize;

    }

    @Override
    public void execute(Context context) {

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if (context.getUserState().getCurrentUser() == null) {
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_LOGGED_IN;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                    getResult(), info);
            return;
        }

        if (this.startDateTime.isAfter(this.endDateTime)) {
            logStatus = LogStatus.ADD_PERFORMANCE_START_AFTER_END;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                    getResult(), info);
            return;
        }

        if (this.capacityLimit < 1) {
            logStatus = LogStatus.ADD_PERFORMANCE_CAPACITY_LESS_THAN_1;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                    getResult(), info);
            return;
        }

        if (this.venueSize < 1) {
            logStatus = LogStatus.ADD_PERFORMANCE_VENUE_SIZE_LESS_THAN_1;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                    getResult(), info);
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != EntertainmentProvider.class) {
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_ENTERTAINMENT_PROVIDER;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                    getResult(), info);
            return;
        }

        List<Event> events = context.getEventState().getAllEvents();
        boolean event_found = false;
        for (Event event : events) {
            if (context.getEventState().findEventByNumber(this.eventNumber) == event) {
                event_found = true;
                break;
            }
        }
        if (!event_found) {
            logStatus = LogStatus.ADD_PERFORMANCE_EVENT_NOT_FOUND;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                    getResult(), info);
            return;
        }

        if (context.getEventState().findEventByNumber(this.eventNumber).getOrganiser() != context.getUserState().getCurrentUser()) {
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                    getResult(), info);
            return;
        }
        Event eventToAdd = context.getEventState().findEventByNumber(this.eventNumber);
        for (Event event : events) {
            if (event.getTitle().equals(eventToAdd.getTitle())) {
                Collection<EventPerformance> performances = event.getPerformances();
                for (EventPerformance performance : performances) {
                    if (performance.getStartDateTime() == startDateTime && performance.getEndDateTime() == endDateTime) {
                        logStatus = LogStatus.ADD_PERFORMANCE_EVENTS_WITH_SAME_TITLE_CLASH;
                        info.put("STATUS:", this.logStatus);
                        Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                                getResult(), info);
                        return;
                    }
                }
            }
        }

        logStatus = LogStatus.ADD_PERFORMANCE_SUCCESS;
        eventPerformanceResult = context.getEventState().createEventPerformance(context.getEventState().findEventByNumber(this.eventNumber),
                venueAddress, startDateTime, endDateTime, performerNames, hasSocialDistancing, hasAirFiltration, isOutdoors, capacityLimit, venueSize);

        context.getEventState().findEventByNumber(eventNumber).getOrganiser().getProviderSystem().recordNewPerformance(eventNumber, eventPerformanceResult.getPerformanceNumber(), startDateTime, endDateTime);

        info.put("STATUS:", this.logStatus);
        Logger.getInstance().logAction("AddEventPerformanceCommand.execute()",
                getResult(), info);
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.ADD_PERFORMANCE_SUCCESS) {
            return eventPerformanceResult;
        }
        return null;
    }

    private enum LogStatus {
        ADD_PERFORMANCE_SUCCESS,
        ADD_PERFORMANCE_START_AFTER_END,
        ADD_PERFORMANCE_CAPACITY_LESS_THAN_1,
        ADD_PERFORMANCE_VENUE_SIZE_LESS_THAN_1,
        ADD_PERFORMANCE_EVENTS_WITH_SAME_TITLE_CLASH,
        ADD_PERFORMANCE_USER_NOT_LOGGED_IN,
        ADD_PERFORMANCE_USER_NOT_ENTERTAINMENT_PROVIDER,
        ADD_PERFORMANCE_EVENT_NOT_FOUND,
        ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER
    }

}
