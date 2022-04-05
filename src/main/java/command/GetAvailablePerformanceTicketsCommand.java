package command;

import controller.Context;
import logging.Logger;
import model.Event;
import model.EventPerformance;
import model.NonTicketedEvent;
import model.TicketedEvent;

import java.util.HashMap;
import java.util.Map;

public class GetAvailablePerformanceTicketsCommand implements ICommand{
    private long eventNumber;
    private long performanceNumber;
    private int numTicketsResult;
    private LogStatus logStatus;

    private enum LogStatus{
        GET_AVAILABLE_PERFORMANCE_TICKETS_EVENT_NOT_TICKETED,
        GET_AVAILABLE_PERFORMANCE_TICKETS_SUCCESS,
        GET_AVAILABLE_PERFORMANCE_TICKETS_EVENT_NOT_FOUND,
        GET_AVAILABLE_PERFORMANCE_TICKETS_PERFORMANCE_NOT_FOUND
    }

    public GetAvailablePerformanceTicketsCommand(long eventNumber, long performanceNumber){
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
    }

    @Override
    public void execute(Context context) {
        Event event = context.getEventState().findEventByNumber(this.eventNumber);
        if (event == null){
            this.logStatus = LogStatus.GET_AVAILABLE_PERFORMANCE_TICKETS_EVENT_NOT_FOUND;
        }
        else if (event.getClass() == NonTicketedEvent.class){
            this.logStatus = LogStatus.GET_AVAILABLE_PERFORMANCE_TICKETS_EVENT_NOT_TICKETED;
        }
        else {
            EventPerformance eventPerformance = event.getPerformanceByNumber(this.performanceNumber);
            if (eventPerformance == null) {
                this.logStatus = LogStatus.GET_AVAILABLE_PERFORMANCE_TICKETS_PERFORMANCE_NOT_FOUND;
            }
            else {
                this.numTicketsResult = event.getOrganiser().getProviderSystem().getNumTicketsLeft(this.eventNumber,
                        this.performanceNumber);
                this.logStatus = LogStatus.GET_AVAILABLE_PERFORMANCE_TICKETS_SUCCESS;
            }
        }

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();
        info.put("STATUS:",this.logStatus);
        Logger.getInstance().logAction("GetAvailablePerformanceTicketsCommand.execute()",
                getResult(),info);
    }

    @Override
    public Object getResult() {
        if (this.logStatus == LogStatus.GET_AVAILABLE_PERFORMANCE_TICKETS_SUCCESS){
            return this.numTicketsResult;
        }
        return null;
    }
}
