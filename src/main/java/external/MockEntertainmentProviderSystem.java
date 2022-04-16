package external;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockEntertainmentProviderSystem implements EntertainmentProviderSystem {

    // as the only purpose of the system is to record the number of tickets of performances
    // Using map is enough

    private final Map<Long, Integer> eventTickets = new HashMap<>(); // store <eventNumber, eventTickets>

    private final Map<Long, Long> eventPerformances = new HashMap<>(); // store <performanceNumber, eventNumber>
    // multiple performance may point to the same event

    private final Map<Long, Integer> performanceTickets = new HashMap<>(); // store <performanceNumber, performanceTickets>
    // this is the map storing current number of tickets left for a performance

    private final Map<Long, Long> bookings = new HashMap<>(); // store <bookingNumber, performanceNumber>
    private final Map<Long, Integer> bookingTickets = new HashMap<>();  // store <bookingNumber, bookedTickets>

    public void recordNewEvent(long eventNumber, String title, int numTickets) {
        eventTickets.put(eventNumber, numTickets);
    }

    /**
     * Remove an event and all its performances records on the provider's system
     * DOES NOT REMOVE any booking information
     *
     * @param eventNumber the event number of the event to cancel
     * @param message     any message(not used)
     */
    public void cancelEvent(long eventNumber, String message) {
        eventTickets.remove(eventNumber);

        // get all performances of an event
        List<Long> performanceList = new ArrayList<>();
        for (Map.Entry<Long, Long> mapEntry : eventPerformances.entrySet()) {
            if (mapEntry.getValue() == eventNumber) {
                performanceList.add(mapEntry.getKey());
            }
        }
        for (Long pNumber : performanceList) {
            eventPerformances.remove(pNumber);
            performanceTickets.remove(pNumber);
        }
    }

    public void recordNewPerformance(long eventNumber, long performanceNumber,
                                     LocalDateTime startDateTime, LocalDateTime endDateTime) {
        performanceTickets.put(performanceNumber, eventTickets.get(eventNumber));
        eventPerformances.put(performanceNumber, eventNumber);
    }

    public int getNumTicketsLeft(long eventNumber, long performanceNumber) {
        return performanceTickets.get(performanceNumber);
    }

    public void recordNewBooking(long eventNumber, long performanceNumber, long bookingNumber,
                                 String consumerName, String consumerEmail, int bookedTickets) {
        bookingTickets.put(bookingNumber, bookedTickets);
        bookings.put(bookingNumber, performanceNumber);
        int i = performanceTickets.get(performanceNumber);
        performanceTickets.put(performanceNumber, i - bookedTickets);
    }

    public void cancelBooking(long bookingNumber) {
        int i = bookingTickets.get(bookingNumber); // the number of tickets booked
        long p = bookings.get(bookingNumber); // the performance number of the booking
        int l = performanceTickets.get(p); // the number of the tickets left for that performance
        performanceTickets.put(p, l + i);
        bookings.remove(bookingNumber); // reverse the booking process
    }

    public void recordSponsorshipAcceptance(long eventNumber, int sponsoredPricePercent) {
        // dummy
    }

    public void recordSponsorshipRejection(long eventNumber) {
        // dummy
    }
}
