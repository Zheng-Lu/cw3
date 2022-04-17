package state;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventState implements IEventState {

    protected List<Event> events;
    private long nextEventNumber;
    private long nextPerformanceNumber;

    public EventState() {
        this.events = new ArrayList<>();
        this.nextEventNumber = 1;
        this.nextPerformanceNumber = 1;
    }

    public EventState(IEventState other) {
        this.events = other.getAllEvents();
        this.nextEventNumber = other.getNextEventNumber();
        this.nextPerformanceNumber = other.getNextPerformanceNumber();
    }

    @Override
    public List<Event> getAllEvents() {
        return this.events;
    }

    @Override
    public Event findEventByNumber(long eventNumber) {
        for (Event e : events) {
            if (e.getEventNumber() == eventNumber) {
                return e;
            }
        }
        return null;
    }

    @Override
    public NonTicketedEvent createNonTicketedEvent(EntertainmentProvider organiser,
                                                   String title, EventType type) {
        NonTicketedEvent newEvent = new NonTicketedEvent(this.nextEventNumber, organiser, title, type);
        organiser.addEvent(newEvent);
        this.events.add(newEvent);
        this.nextEventNumber = this.nextEventNumber + 1; // increment next event number by 1
        return newEvent;
    }

    @Override
    public TicketedEvent createTicketedEvent(EntertainmentProvider organiser, String title,
                                             EventType type, double ticketPrice, int numTickets) {
        TicketedEvent newEvent = new TicketedEvent(this.nextEventNumber, organiser, title, type,
                ticketPrice, numTickets);
        organiser.addEvent(newEvent);
        this.events.add(newEvent);
        this.nextEventNumber = this.nextEventNumber + 1;
        return newEvent;
    }

    @Override
    public EventPerformance createEventPerformance(Event event, String venueAddress,
                                                   LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                   List<String> performerNames, boolean hasSocialDistancing,
                                                   boolean hasAirFiltration, boolean isOutdoors,
                                                   int capacityLimit, int venueSize) {
        EventPerformance newPerformance = new EventPerformance(this.nextPerformanceNumber, event, venueAddress,
                startDateTime, endDateTime, performerNames, hasSocialDistancing, hasAirFiltration, isOutdoors,
                capacityLimit, venueSize);
        event.addPerformance(newPerformance);
        this.nextPerformanceNumber = this.nextPerformanceNumber + 1;
        return newPerformance;
    }

    @Override
    public long getNextEventNumber() {
        return this.nextEventNumber;
    }

    @Override
    public long getNextPerformanceNumber() {
        return this.nextPerformanceNumber;
    }
}
