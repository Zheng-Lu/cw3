package external;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MockEntertainmentProviderSystem implements EntertainmentProviderSystem{
    Map<Long, Integer> eventTickets = new HashMap<>();
    Map<Long, Long> bookingsEvents = new HashMap<>(); // store <bookingNumber, eventNumber>
    Map<Long, Integer> bookings = new HashMap<>();  // store <bookingNumber, bookedTickets>

    public void recordNewEvent(long eventNumber,String title, int numTickets){
        eventTickets.put(eventNumber,numTickets);
    }

    public void cancelEvent(long eventNumber, String message){

    }

    public void recordNewPerformance(long eventNumber, long performanceNumber,
                                     LocalDateTime startDateTime, LocalDateTime endDateTime){
    }

    public int getNumTicktesLeft(long eventNumber, long performanceNumber){
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

    }

    public void recordSponsorshipRejection(long eventNumber){

    }
}
