package command;

import controller.Context;
import model.EntertainmentProvider;
import model.Event;
import model.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterEntertainmentProviderCommand extends Object implements ICommand{
    private String orgName, orgAddress, paymentAccountEmail, mainRepName, mainRepEmail, password;
    private List<String> otherRepNames, otherRepEmails;
    private EntertainmentProvider newEntertainmentProviderResult;
    private EntertainmentProvider attribute;

    private LogStatus logStatus;
    private enum LogStatus{
        REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS,
        USER_REGISTER_FIELDS_CANNOT_BE_NULL,
        USER_REGISTER_EMAIL_ALREADY_REGISTERED,
        USER_REGISTER_ORG_ALREADY_REGISTERED,
        USER_LOGIN_SUCCESS
    }

    public RegisterEntertainmentProviderCommand(String orgName,
                                                 String orgAddress,
                                                 String paymentAccountEmail,
                                                 String mainRepName,
                                                 String mainRepEmail,
                                                 String password,
                                                 List<String> otherRepNames,
                                                 List<String> otherRepEmails){
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.paymentAccountEmail = paymentAccountEmail;
        this.mainRepName = mainRepName;
        this.mainRepEmail = mainRepEmail;
        this.password = password;
        this.otherRepNames = otherRepNames;
        this.otherRepEmails = otherRepEmails;
    }

    @Override
    public void execute(Context context) {
        if(orgName == null ||
        orgAddress == null ||
        paymentAccountEmail == null ||
        mainRepName == null ||
        mainRepEmail == null ||
        password == null ||
        otherRepNames == null ||
        otherRepEmails == null){
            logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
            return;
        }

        Map<String, User> users = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> userEntry : users.entrySet()){
            if (Objects.equals(userEntry.getValue().getEmail(),this.mainRepEmail)){
                logStatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
                return;
            }
        }

        List<Event> events =context.getEventState().getAllEvents();
        for (Event event:events){
            if(event.getOrganiser().getOrgName() == this.orgName && event.getOrganiser().getOrgAddress() == this.orgAddress){
                logStatus = LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED;
                return;
            }
        }

        logStatus = LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS;
        newEntertainmentProviderResult = new EntertainmentProvider(orgName,orgAddress,paymentAccountEmail,mainRepName,mainRepEmail,password, otherRepNames,otherRepEmails);
        context.getUserState().addUser(newEntertainmentProviderResult);
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS){
            return newEntertainmentProviderResult;
        }
        return null;
    }
}
