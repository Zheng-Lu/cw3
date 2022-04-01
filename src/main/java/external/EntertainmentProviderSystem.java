package external;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public interface EntertainmentProviderSystem {

    void recordNewEvent(long eventNumber,String title, int numTickets);

    void cancelEvent(long eventNumber, String message);

    void recordNewPerformance(long eventNumber, long performanceNumber,
                                     LocalDateTime startDateTime, LocalDateTime endDateTime);


    int getNumTicktesLeft(long eventNumber, long performanceNumber);

    void recordNewBooking(long eventNumber, long performanceNumber, long bookingNumber,
                                 String consumerName, String consumerEmail, int bookedTickets);

    void cancelBooking(long bookingNumber);

    void recordSponsorshipAcceptance(long eventNumber, int sponsoredPricePercent) ;

    void recordSponsorshipRejection(long eventNumber);

}
