package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.TicketedEvent;
import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginCommand extends Object implements ICommand{

    private String email, password;
    private User userResult;

    private LogStatus logStatus;

    private enum LogStatus{
        USER_LOGIN_SUCCESS, USER_LOGIN_EMAIL_NOT_REGISTERED, USER_LOGIN_WRONG_PASSWORD
    };


    public LoginCommand(String email,
                         String password){
        this.email = email;
        this.password = password;
    }

    public void execute(Context context){

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        Map<String, User> users = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> userEntry : users.entrySet()){
            if (Objects.equals(userEntry.getValue().getEmail(), this.email) && userEntry.getValue().checkPasswordMatch(password)){
                this.logStatus = LogStatus.USER_LOGIN_SUCCESS;
                userResult = userEntry.getValue();
                context.getUserState().setCurrentUser(userEntry.getValue());
                break;
            }
            else if (Objects.equals(userEntry.getValue().getEmail(), this.email) && !userEntry.getValue().checkPasswordMatch(password)) {
                this.logStatus = LogStatus.USER_LOGIN_WRONG_PASSWORD;
            }
        }

        if (this.logStatus==null) {
            this.logStatus = LogStatus.USER_LOGIN_EMAIL_NOT_REGISTERED;
        }
        info.put("STATUS:",this.logStatus);
        Logger.getInstance().logAction("LoginCommand.execute()",
                getResult(),info);
    }

    @Override
    public User getResult() {
        if (logStatus == LogStatus.USER_LOGIN_SUCCESS){
            return this.userResult;
        }
        return null;
    }
}
