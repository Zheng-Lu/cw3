package command;

import controller.Context;
import model.Consumer;
import model.User;

import java.util.Map;
import java.util.Objects;

public class RegisterConsumerCommand extends Object implements ICommand{

    private String name, email, phoneNumber, password, paymentAccountEmail;
    private Consumer newConsumerResult;
    private LogStatus logStatus;

    private enum LogStatus{
        REGISTER_CONSUMER_SUCCESS,
        USER_REGISTER_FIELDS_CANNOT_BE_NULL,
        USER_REGISTER_EMAIL_ALREADY_REGISTERED,
        USER_LOGIN_SUCCESS
    }

    public RegisterConsumerCommand(String name,
                                    String email,
                                    String phoneNumber,
                                    String password,
                                    String paymentAccountEmail){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.paymentAccountEmail = paymentAccountEmail;
    }

    @Override
    public void execute(Context context) {
        if(this.name == null || this.email==null || this.phoneNumber == null || this.password==null || this.paymentAccountEmail == null){
            logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
            return;
        }
        Map<String, User> users = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> userEntry : users.entrySet()){
            if (Objects.equals(userEntry.getValue().getEmail(), this.email)) {
                this.logStatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
                return;
            }
        }
        logStatus = LogStatus.REGISTER_CONSUMER_SUCCESS;
        this.newConsumerResult = (Consumer) context.getUserState().getCurrentUser();
        context.getUserState().addUser(newConsumerResult);
        context.getUserState().setCurrentUser(newConsumerResult);
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.REGISTER_CONSUMER_SUCCESS){
            return newConsumerResult;
        }
        return null;
    }
}
