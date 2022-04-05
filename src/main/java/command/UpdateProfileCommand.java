package command;

import controller.Context;
import model.User;

import java.util.List;
import java.util.Map;

public abstract class UpdateProfileCommand implements ICommand{
    protected boolean successResult = true;
    protected LogStatus logStatus = null;

    protected enum LogStatus{
        USER_UPDATE_PROFILE_NOT_LOGGED_IN,
        USER_UPDATE_PROFILE_WRONG_PASSWORD,
        USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE
    }

    public UpdateProfileCommand(){}

    protected boolean isProfileUpdateInvalid(Context context,
                                             String oldPassword,
                                             String newEmail){
        User user = context.getUserState().getCurrentUser();
        if (user == null){
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN;
            this.successResult = false;
        }
        else if (!user.checkPasswordMatch(oldPassword)){
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD;
            this.successResult = false;
        }
        else {
            Map<String, User> users = context.getUserState().getAllUsers();
            for (Map.Entry<String, User> userEntry : users.entrySet()){
                if (userEntry.getValue().getEmail().equals(newEmail)){
                    this.logStatus = LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE;
                    this.successResult = false;
                    break;
                }
            }
        }
        return !this.successResult;
    }

    @Override
    public void execute(Context context) {

    }

    @Override
    public Boolean getResult() {
        return this.successResult;
    }
}
