package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventPerformance extends Object {
    private long perfermanceNumber;
    private Event event;
    private String venueAddress;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<String> performerNames = new ArrayList<>();
    private boolean hasSocialDistancing;
    private boolean hasAirFiltretion;
    private boolean isOutdoors;
    private int capacityLimit;
    private int venueSize;

    public EventPerformance(long performanceNumber, Event event, String venueAddress, LocalDateTime startDateTime,
                            LocalDateTime endDateTime, List<String> performerNames, boolean hasSocialDistancing,
                            boolean hasAirFiltration,
                            boolean isOutdoors, int capacityLimit, int venueSize){
        this.perfermanceNumber = performanceNumber;
        this.event = event;
        this.venueAddress = venueAddress;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.hasSocialDistancing = hasSocialDistancing;
        this.hasAirFiltretion = hasAirFiltration;
        this.isOutdoors = isOutdoors;
        this.capacityLimit = capacityLimit;
        this.venueAddress = venueAddress;
        this.venueSize = venueSize;
    }

    public long getPerformanceNumber(){
        return this.perfermanceNumber;
    }

    public Event getEvent(){
        return this.event;
    }

    public LocalDateTime getStartDateTime(){
        return this.startDateTime;
    }

    public LocalDateTime getEndDateTime(){
        return this.endDateTime;
    }

    public boolean hasSocialDistancing(){
        return this.hasSocialDistancing;
    }

    public boolean hasAirFiltretionas(){
        return this.hasAirFiltretion;
    }

    public boolean isOutdoors(){
        return this.isOutdoors;
    }

    public int getCapacityLimit(){
        return this.capacityLimit;
    }

    public int getVenueSize(){
        return this.venueSize;
    }

    @Override
    public String toString(){
        return "";
    }
}
