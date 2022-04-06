package command;

import controller.Context;
import model.GovernmentRepresentative;
import model.SponsorshipRequest;

import java.util.List;

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
        if(context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.LIST_SPONSORSHIP_REQUESTS_NOT_LOGGED_IN;
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != GovernmentRepresentative.getClass()){
            logStatus = LogStatus.LIST_SPONSORSHIP_REQUESTS_NOT_GOVERNMENT_REPRESENTATIVE;
            return;
        }

        logStatus = LogStatus.LIST_SPONSORSHIP_REQUESTS_SUCCESS;
        this.requestListResult = context.getSponsorshipState().getAllSponsorshipRequest();
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.LIST_SPONSORSHIP_REQUESTS_SUCCESS){
            return requestListResult;
        }
        return null;
    }
}
