package command;

import controller.Context;
import logging.Logger;
import model.Consumer;
import model.User;

import java.util.HashMap;
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

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if(this.name == null || this.email==null || this.phoneNumber == null || this.password==null || this.paymentAccountEmail == null){
            logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("RegisterConsumerCommand.execute()",
                    getResult(),info);
            return;
        }
        Map<String, User> users = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> userEntry : users.entrySet()){
            if (Objects.equals(userEntry.getValue().getEmail(), this.email)) {
                this.logStatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
                info.put("STATUS:",this.logStatus);
                Logger.getInstance().logAction("RegisterConsumerCommand.execute()",
                        getResult(),info);
                return;
            }
        }

        this.newConsumerResult = new Consumer(this.name, this.email, this.phoneNumber, this.password, this.paymentAccountEmail);
        context.getUserState().addUser(this.newConsumerResult);
        logStatus = LogStatus.REGISTER_CONSUMER_SUCCESS;
        info.put("STATUS:",this.logStatus);

        context.getUserState().setCurrentUser(this.newConsumerResult);
        logStatus = LogStatus.USER_LOGIN_SUCCESS;
        info.put("STATUS:",this.logStatus);

        Logger.getInstance().logAction("RegisterConsumerCommand.execute()",
                getResult(),info);
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.REGISTER_CONSUMER_SUCCESS){
            return newConsumerResult;
        }
        return null;
    }
}
