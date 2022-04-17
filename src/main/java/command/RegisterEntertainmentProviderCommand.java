package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.Event;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterEntertainmentProviderCommand implements ICommand {
    private final String orgName;
    private final String orgAddress;
    private final String paymentAccountEmail;
    private final String mainRepName;
    private final String mainRepEmail;
    private final String password;
    private final List<String> otherRepNames;
    private final List<String> otherRepEmails;
    private EntertainmentProvider newEntertainmentProviderResult;

    private LogStatus logStatus;

    public RegisterEntertainmentProviderCommand(String orgName,
                                                String orgAddress,
                                                String paymentAccountEmail,
                                                String mainRepName,
                                                String mainRepEmail,
                                                String password,
                                                List<String> otherRepNames,
                                                List<String> otherRepEmails) {
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

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if (orgName == null ||
                orgAddress == null ||
                paymentAccountEmail == null ||
                mainRepName == null ||
                mainRepEmail == null ||
                password == null ||
                otherRepNames == null ||
                otherRepEmails == null) {
            logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
            info.put("STATUS:", this.logStatus);
            Logger.getInstance().logAction("RegisterEntertainmentProviderCommand.execute()",
                    getResult(), info);
            return;
        }

        Map<String, User> users = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> userEntry : users.entrySet()) {
            if (Objects.equals(userEntry.getValue().getEmail(), this.mainRepEmail)) {
                logStatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
                info.put("STATUS:", this.logStatus);
                Logger.getInstance().logAction("RegisterEntertainmentProviderCommand.execute()",
                        getResult(), info);
                return;
            }
        }

        List<Event> events = context.getEventState().getAllEvents();
        for (Event event : events) {
            if (Objects.equals(event.getOrganiser().getOrgName(), this.orgName) && Objects.equals(event.getOrganiser().getOrgAddress(), this.orgAddress)) {
                logStatus = LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED;
                info.put("STATUS:", this.logStatus);
                Logger.getInstance().logAction("RegisterEntertainmentProviderCommand.execute()",
                        getResult(), info);
                return;
            }
        }

        logStatus = LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS;
        this.newEntertainmentProviderResult = new EntertainmentProvider(orgName, orgAddress, paymentAccountEmail, mainRepName, mainRepEmail, password, otherRepNames, otherRepEmails);
        info.put("STATUS:", this.logStatus);
        context.getUserState().addUser(this.newEntertainmentProviderResult);

        context.getUserState().setCurrentUser(this.newEntertainmentProviderResult);
        logStatus = LogStatus.USER_LOGIN_SUCCESS;
        info.put("STATUS:", this.logStatus);

        Logger.getInstance().logAction("RegisterEntertainmentProviderCommand.execute()",
                getResult(), info);

    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.USER_LOGIN_SUCCESS) {
            return newEntertainmentProviderResult;
        }
        return null;
    }

    private enum LogStatus {
        REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS,
        USER_REGISTER_FIELDS_CANNOT_BE_NULL,
        USER_REGISTER_EMAIL_ALREADY_REGISTERED,
        USER_REGISTER_ORG_ALREADY_REGISTERED,
        USER_LOGIN_SUCCESS
    }
}
