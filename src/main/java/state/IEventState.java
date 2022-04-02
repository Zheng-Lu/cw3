package state;

import model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface IEventState {
    List<Event> getAllEvents();

    Event findEventByNumber(long eventNumber);

    NonTicketedEvent createNonTicketedEvent(EntertainmentProvider organiser,
                                           String title, EventType type);

    TicketedEvent createTicketedEvent(EntertainmentProvider organiser,
                                      String title, EventType type,
                                      double ticketPrice, int numTickets);

    EventPerformance createEventPerformance(Event event, String venueAddress,
                                            LocalDateTime startDateTime,
                                            LocalDateTime endDateTime,
                                            List<String> performerNames,
                                            boolean hasSocialDistancing,
                                            boolean hasAirFiltration,
                                            boolean isOutdoors,
                                            int capacityLimit,
                                            int venueSize);

    // use for copying EventState
    long getNextEventNumber();
    long getNextPerformanceNumber();
}
