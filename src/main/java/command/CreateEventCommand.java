package command;

import controller.Context;
import model.EntertainmentProvider;
import model.EventType;
import model.User;

public abstract class CreateEventCommand implements ICommand{
    protected final String title;
    protected Long eventNumberResult = null;
    protected final EventType type;
    protected LogStatus logStatus;

    protected enum LogStatus{
        CREATE_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER,
        CREATE_EVENT_USER_NOT_LOGGED_IN
    }

    public CreateEventCommand(String title, EventType type) {
        this.title = title;
        this.type = type;
    }

    protected boolean isUserAllowedToCreateEvent(Context context){
        User user = context.getUserState().getCurrentUser();
        if (user == null){
            this.logStatus = LogStatus.CREATE_EVENT_USER_NOT_LOGGED_IN;
            return false;
        }
        if (user.getClass() != EntertainmentProvider.class){
            this.logStatus = LogStatus.CREATE_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER;
            return false;
        }
        return true;
    }

    @Override
    public void execute(Context context) {

    }

    @Override
    public Long getResult() {
        return this.eventNumberResult;
    }
}
