package command;

import controller.Context;
import logging.Logger;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GovernmentReport2Command extends Object implements ICommand{

    private String orgName;
    private List<Consumer> consumerListResult = new ArrayList<>();

    private LogStatus logStatus;
    private enum LogStatus{
        GOVERNMENT_REPORT2_NOT_LOGGED_IN,
        GOVERNMENT_REPORT2_USER_NOT_GOVERNMENT_REPRESENTATIVE,
        GOVERNMENT_REPORT2_NO_SUCH_ORGANISATION,
        GOVERNMENT_REPORT2_SUCCESS
    }

    public GovernmentReport2Command(String orgName){
        this.orgName = orgName;
    }

    @Override
    public void execute(Context context) {

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if(context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.GOVERNMENT_REPORT2_NOT_LOGGED_IN;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("GovernmentReport2Command.execute()",
                    getResult(),info);
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != GovernmentRepresentative.class){
            logStatus = LogStatus.GOVERNMENT_REPORT2_USER_NOT_GOVERNMENT_REPRESENTATIVE;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("GovernmentReport2Command.execute()",
                    getResult(),info);
            return;
        }

        List<Event> events = context.getEventState().getAllEvents();
        boolean organisation_found = false;
        for (Event event: events) {
            if (event.getOrganiser().getOrgName().equals(this.orgName)){
                organisation_found = true;
                break;
            }
        }
        if (!organisation_found){
            logStatus = LogStatus.GOVERNMENT_REPORT2_NO_SUCH_ORGANISATION;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("GovernmentReport2Command.execute()",
                    getResult(),info);
            return;
        }

        logStatus = LogStatus.GOVERNMENT_REPORT2_SUCCESS;

        List<Booking> bookings = context.getBookingState().getBookings();
        for (Booking b : bookings){
            if (b.getEventPerformance().getEvent().getClass() == TicketedEvent.class &&
                    b.getEventPerformance().getEvent().getOrganiser().getOrgName().equals(this.orgName) &&
                    b.getEventPerformance().getEvent().getStatus() == EventStatus.ACTIVE){
                if( !this.consumerListResult.contains(b.getBooker()) ){
                    this.consumerListResult.add(b.getBooker());
                }
            }
        }

        info.put("STATUS:",this.logStatus);
        Logger.getInstance().logAction("GovernmentReport2Command.execute()",
                getResult(),info);
    }

    @Override
    public List<Consumer> getResult() {
        if(logStatus == LogStatus.GOVERNMENT_REPORT2_SUCCESS){
            return consumerListResult;
        }
        return null;
    }
}
