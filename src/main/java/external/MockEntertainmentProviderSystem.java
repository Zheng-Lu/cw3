package external;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MockEntertainmentProviderSystem implements EntertainmentProviderSystem{

    // Only usage is to record how many tickets are left for a event(referenced by event number)
    // And when asked, return the number of tickets left
    Map<Long, Integer> eventTickets = new HashMap<>(); // store <eventNumber, eventTickets>
    Map<Long, Long> bookingsEvents = new HashMap<>(); // store <bookingNumber, eventNumber>
    Map<Long, Integer> bookings = new HashMap<>();  // store <bookingNumber, bookedTickets>

    public void recordNewEvent(long eventNumber,String title, int numTickets){
        eventTickets.put(eventNumber,numTickets);
    }

    public void cancelEvent(long eventNumber, String message){
        // dummy
    }

    public void recordNewPerformance(long eventNumber, long performanceNumber,
                                     LocalDateTime startDateTime, LocalDateTime endDateTime){
    }

    public int getNumTicketsLeft(long eventNumber, long performanceNumber){
        return eventTickets.get(eventNumber);
    }

    public void recordNewBooking(long eventNumber, long performanceNumber, long bookingNumber,
                                 String consumerName, String consumerEmail, int bookedTickets){
        bookings.put(bookingNumber,bookedTickets);
        bookingsEvents.put(bookingNumber,eventNumber);
        int i = eventTickets.get(eventNumber);
        eventTickets.put(eventNumber,i-bookedTickets);
    }

    public void cancelBooking(long bookingNumber){
        int i = bookings.get(bookingNumber);
        long e = bookingsEvents.get(bookingNumber);
        int n = eventTickets.get(e);
        eventTickets.put(e,n-i);
    }

    public void recordSponsorshipAcceptance(long eventNumber, int sponsoredPricePercent) {
        // dummy
    }

    public void recordSponsorshipRejection(long eventNumber){
        // dummy
    }
}
