package command;

import controller.Context;
import model.EntertainmentProvider;
import model.TicketedEvent;
import model.User;

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
        Map<String, User> users = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> userEntry : users.entrySet()){
            if (!Objects.equals(userEntry.getValue().getEmail(), this.email)) {
                this.logStatus = LogStatus.USER_LOGIN_EMAIL_NOT_REGISTERED;
            }
            if (!userEntry.getValue().checkPasswordMatch(password)) {
                this.logStatus = LogStatus.USER_LOGIN_WRONG_PASSWORD;
            }
            if (Objects.equals(userEntry.getValue().getEmail(), this.email) && userEntry.getValue().checkPasswordMatch(password)){
                this.logStatus = LogStatus.USER_LOGIN_SUCCESS;
                userResult = (User) userEntry;
                break;
            }
        }
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.USER_LOGIN_SUCCESS){
            return this.userResult;
        }
        return null;
    }
}
