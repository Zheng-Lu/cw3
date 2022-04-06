package command;

import controller.Context;
import model.Consumer;
import model.Event;
import model.GovernmentRepresentative;
import model.User;

import java.util.List;
import java.util.Map;

public class GovernmentReport2Command extends Object implements ICommand{

    private String orgName;
    private List<Consumer> consumerListResult;

    private LogStatus logStatus;
    private Object GovernmentRepresentative;
    private Object Consumer;

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
        if(context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.GOVERNMENT_REPORT2_NOT_LOGGED_IN;
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() == GovernmentRepresentative.getClass()){
            logStatus = LogStatus.GOVERNMENT_REPORT2_USER_NOT_GOVERNMENT_REPRESENTATIVE;
            return;
        }

        List<Event> events = context.getEventState().getAllEvents();
        boolean organisation_found = false;
        for (Event event: events) {
            if (event.getOrganiser().getOrgName() == this.orgName){
                organisation_found = true;
                break;
            }
        }
        if (!organisation_found){
            logStatus = LogStatus.GOVERNMENT_REPORT2_NO_SUCH_ORGANISATION;
            return;
        }

        logStatus = LogStatus.GOVERNMENT_REPORT2_SUCCESS;
        Map<String, User> users = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> userEntry : users.entrySet()) {
            if (userEntry.getValue().getClass() == Consumer.getClass()){
                this.consumerListResult.add((Consumer) userEntry.getValue());
            }
        }
    }

    @Override
    public List<Consumer> getResult() {
        if(logStatus == LogStatus.GOVERNMENT_REPORT2_SUCCESS){
            return consumerListResult;
        }
        return null;
    }
}
