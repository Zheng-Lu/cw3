package command;

import controller.Context;
import model.EntertainmentProvider;
import model.Event;
import model.EventPerformance;
import org.w3c.dom.events.EventException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class AddEventPerformanceCommand extends Object implements  ICommand{
    // TODO remember to update the provider's system and relative states in the context
    // use event.getOrganiser().getProviderSystem() to get the provider's system
    // DO NOT USE event.getNumTickets() to get the current number of tickets left for a SINGLE PERFORMANCE
    // instead asks teh number from the provider's system
    // USE event.getOrganiser().getProviderSystem().getNumTicketsLeft()

    private long eventNumber;
    private String venueAddress;
    private LocalDateTime startDateTime, endDateTime;
    private List<String> performerNames;
    private boolean hasSocialDistancing, hasAirFiltration, isOutdoors;
    private int capacityLimit, venueSize;
    private EventPerformance eventPerformanceResult;
    private LogStatus logStatus;
    private Object EntertainmentProvider;

    private enum LogStatus{
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

    public AddEventPerformanceCommand(long eventNumber,
                                       String venueAddress,
                                       LocalDateTime startDateTime,
                                       LocalDateTime endDateTime,
                                       List<String> performerNames,
                                       boolean hasSocialDistancing,
                                       boolean hasAirFiltration,
                                       boolean isOutdoors,
                                       int capacityLimit,
                                       int venueSize){
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
        if (context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_LOGGED_IN;
            return;
        }

        if(this.startDateTime.isAfter(this.endDateTime)){
            logStatus = LogStatus.ADD_PERFORMANCE_START_AFTER_END;
            return;
        }

        if (this.capacityLimit<1){
            logStatus = LogStatus.ADD_PERFORMANCE_CAPACITY_LESS_THAN_1;
            return;
        }

        if (this.venueSize<1){
            logStatus = LogStatus.ADD_PERFORMANCE_VENUE_SIZE_LESS_THAN_1;
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != EntertainmentProvider.getClass()){
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_ENTERTAINMENT_PROVIDER;
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
        if (!event_found){
            logStatus = LogStatus.ADD_PERFORMANCE_EVENT_NOT_FOUND;
            return;
        }

        if (context.getEventState().findEventByNumber(this.eventNumber).getOrganiser() == context.getUserState().getCurrentUser()){
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER;
            return;
        }
        Event eventToAdd = context.getEventState().findEventByNumber(this.eventNumber);
        for (Event event: events){
            if (event.getTitle() == eventToAdd.getTitle()){
                Collection<EventPerformance> performances = event.getPerformances();
                for (EventPerformance performance: performances){
                    if(performance.getStartDateTime() == startDateTime && performance.getEndDateTime() == endDateTime){
                        logStatus = LogStatus.ADD_PERFORMANCE_EVENTS_WITH_SAME_TITLE_CLASH;
                        return;
                    }
                }
            }
        }

        logStatus = LogStatus.ADD_PERFORMANCE_SUCCESS;
        eventPerformanceResult = context.getEventState().createEventPerformance(eventToAdd,venueAddress,startDateTime,endDateTime,performerNames,hasSocialDistancing,hasAirFiltration,isOutdoors,capacityLimit,venueSize);
    }

    @Override
    public Object getResult() {
        if(logStatus == LogStatus.ADD_PERFORMANCE_SUCCESS){
            return eventPerformanceResult;
        }
        return null;
    }

}
