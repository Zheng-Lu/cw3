package command;


import controller.Context;
import logging.LogEntry;
import logging.Logger;
import model.EntertainmentProvider;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateEntertainmentProviderProfileCommand extends UpdateProfileCommand{
    private String oldPassword;
    private String newOrgName;
    private String newOrgAddress;
    private String newPassword;
    private String newPaymentAccountEmail;
    private String newMainRepName;
    private String newMainRepEmail;
    private List<String> newOtherRepNames;
    private List<String> newOtherRepEmails;
    private LogStatus logStatus;

    private enum LogStatus{
        USER_UPDATE_PROFILE_SUCCESS,
        USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL,
        USER_UPDATE_PROFILE_NOT_ENTERTAINMENT_PROVIDER,
        USER_UPDATE_PROFILE_ORG_ALREADY_REGISTERED
    }

    public UpdateEntertainmentProviderProfileCommand(String oldPassword,
                                                     String newOrgName,
                                                     String newOrgAddress,
                                                     String newPassword,
                                                     String newPaymentAccountEmail,
                                                     String newMainRepName,
                                                     String newMainRepEmail,
                                                     List<String> newOtherRepNames,
                                                     List<String> newOtherRepEmails) {
        this.oldPassword = oldPassword;
        this.newOrgName = newOrgName;
        this.newOrgAddress = newOrgAddress;
        this.newPassword = newPassword;
        this.newPaymentAccountEmail = newPaymentAccountEmail;
        this.newMainRepName = newMainRepName;
        this.newMainRepEmail = newMainRepEmail;
        this.newOtherRepNames = newOtherRepNames;
        this.newOtherRepEmails = newOtherRepEmails;
    }

    @Override
    public void execute(Context context) {

        User user = context.getUserState().getCurrentUser();
        Map<String, User> users = context.getUserState().getAllUsers();

        if (this.oldPassword == null || this.newOrgName == null || this.newOrgAddress == null ||
            this.newPassword == null || this.newPaymentAccountEmail == null ||
            this.newMainRepName == null || this.newMainRepEmail == null ||
            this.newOtherRepNames == null || this.newOtherRepEmails == null){
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL;
            this.successResult = false;
        }
        else if (isProfileUpdateInvalid(context,oldPassword, newOrgAddress) ||
                isProfileUpdateInvalid(context,oldPassword,newMainRepEmail)){
        }
        else if (user.getClass() != EntertainmentProvider.class) {
                this.logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_ENTERTAINMENT_PROVIDER;
                this.successResult = false;
        }
        else {
            for (Map.Entry<String, User> userEntry : users.entrySet()) {
                if (userEntry.getValue().getClass() == EntertainmentProvider.class &&
                        ((EntertainmentProvider) userEntry.getValue()).getOrgName().equals(this.newOrgName) &&
                        ((EntertainmentProvider) userEntry.getValue()).getOrgAddress().equals(this.newOrgAddress)
                ) {
                    this.logStatus = LogStatus.USER_UPDATE_PROFILE_ORG_ALREADY_REGISTERED;
                    this.successResult = false;
                    break;
                }
            }
        }

        // super.logStatus = null means no errors occurred for isProfileUpdateInvalid()
        if (this.successResult && super.logStatus == null) {
            user.updatePassword(newPassword);
            user.setPaymentAccountEmail(newPaymentAccountEmail);
            user.setEmail(newMainRepEmail);
            ((EntertainmentProvider) user).setMainRepEmail(newMainRepEmail);
            ((EntertainmentProvider) user).setMainRepName(newMainRepName);
            ((EntertainmentProvider) user).setOrgName(newOrgName);
            ((EntertainmentProvider) user).setOrgAddress(newOrgAddress);
            ((EntertainmentProvider) user).setOtherRepEmails(newOtherRepEmails);
            ((EntertainmentProvider) user).setOtherRepNames(newOtherRepNames);
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_SUCCESS;
        }

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();
        if (this.logStatus != null){
            info.put("STATUS:",this.logStatus);
        }
        if (super.logStatus != null){
            info.put("PROFILE_STATUS:",super.logStatus);
        }
        Logger.getInstance().logAction("UpdateEntertainmentProviderProfileCommand.execute()",
                getResult(),info);
    }
}
