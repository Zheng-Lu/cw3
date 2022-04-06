package command;

import controller.Context;
import logging.Logger;
import model.GovernmentRepresentative;
import model.SponsorshipRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSponsorshipRequestsCommand extends Object implements ICommand{

    private boolean pendingRequestsOnly;
    private List<SponsorshipRequest> requestListResult;

    private LogStatus logStatus;
    private Object GovernmentRepresentative;

    private enum LogStatus{
        LIST_SPONSORSHIP_REQUESTS_NOT_LOGGED_IN,
        LIST_SPONSORSHIP_REQUESTS_NOT_GOVERNMENT_REPRESENTATIVE,
        LIST_SPONSORSHIP_REQUESTS_SUCCESS
    }

    public ListSponsorshipRequestsCommand(boolean pendingRequestsOnly){
        this.pendingRequestsOnly = pendingRequestsOnly;
    }

    @Override
    public void execute(Context context) {

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if(context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.LIST_SPONSORSHIP_REQUESTS_NOT_LOGGED_IN;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("ListSponsorshipRequestsCommand.execute()",
                    getResult(),info);
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != GovernmentRepresentative.getClass()){
            logStatus = LogStatus.LIST_SPONSORSHIP_REQUESTS_NOT_GOVERNMENT_REPRESENTATIVE;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("ListSponsorshipRequestsCommand.execute()",
                    getResult(),info);
            return;
        }

        logStatus = LogStatus.LIST_SPONSORSHIP_REQUESTS_SUCCESS;
        this.requestListResult = context.getSponsorshipState().getAllSponsorshipRequest();

        info.put("STATUS:",this.logStatus);
        Logger.getInstance().logAction("ListSponsorshipRequestsCommand.execute()",
                getResult(),info);
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.LIST_SPONSORSHIP_REQUESTS_SUCCESS){
            return requestListResult;
        }
        return null;
    }
}
