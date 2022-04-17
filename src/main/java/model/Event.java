package model;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Event {

    private final long eventNumber;
    private final EntertainmentProvider organiser;
    private final String title;
    private final EventType type;
    private final Collection<EventPerformance> performances = new ArrayList<>();
    private EventStatus status;


    protected Event(long eventNumber, EntertainmentProvider organiser, String title, EventType type) {
        this.eventNumber = eventNumber;
        this.organiser = organiser;
        this.title = title;
        this.type = type;
        this.status = EventStatus.ACTIVE;
    }

    public long getEventNumber() {
        return this.eventNumber;
    }

    public EntertainmentProvider getOrganiser() {
        return this.organiser;
    }

    public String getTitle() {
        return this.title;
    }

    public EventType getType() {
        return this.type;
    }

    public EventStatus getStatus() {
        return this.status;
    }

    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }

    public void addPerformance(EventPerformance performance) {
        this.performances.add(performance);
    }

    public EventPerformance getPerformanceByNumber(long performanceNumber) {
        for (EventPerformance p : performances) {
            if (p.getPerformanceNumber() == performanceNumber) {
                return p;
            }
        }
        return null;
    }

    public Collection<EventPerformance> getPerformances() {
        return performances;
    }
}
