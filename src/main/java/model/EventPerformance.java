package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventPerformance {
    private final long perfermanceNumber;
    private final Event event;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final boolean hasSocialDistancing;
    private final boolean hasAirFiltration;
    private final boolean isOutdoors;
    private final int capacityLimit;
    private final int venueSize;
    private String venueAddress;
    private List<String> performerNames = new ArrayList<>();

    public EventPerformance(long performanceNumber, Event event, String venueAddress, LocalDateTime startDateTime,
                            LocalDateTime endDateTime, List<String> performerNames, boolean hasSocialDistancing,
                            boolean hasAirFiltration,
                            boolean isOutdoors, int capacityLimit, int venueSize) {
        this.perfermanceNumber = performanceNumber;
        this.event = event;
        this.venueAddress = venueAddress;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.hasSocialDistancing = hasSocialDistancing;
        this.hasAirFiltration = hasAirFiltration;
        this.isOutdoors = isOutdoors;
        this.capacityLimit = capacityLimit;
        this.venueAddress = venueAddress;
        this.venueSize = venueSize;
    }

    public long getPerformanceNumber() {
        return this.perfermanceNumber;
    }

    public Event getEvent() {
        return this.event;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    public boolean hasSocialDistancing() {
        return this.hasSocialDistancing;
    }

    public boolean hasAirFiltration() {
        return this.hasAirFiltration;
    }

    public boolean isOutdoors() {
        return this.isOutdoors;
    }

    public int getCapacityLimit() {
        return this.capacityLimit;
    }

    public int getVenueSize() {
        return this.venueSize;
    }

}
