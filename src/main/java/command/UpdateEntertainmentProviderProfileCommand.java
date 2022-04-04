package command;


import controller.Context;
import model.EntertainmentProvider;
import model.User;

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
        if (this.oldPassword == null || this.newOrgName == null || this.newOrgAddress == null ||
            this.newPassword == null || this.newPaymentAccountEmail == null ||
            this.newMainRepName == null || this.newMainRepEmail == null ||
            this.newOtherRepNames == null || this.newOtherRepEmails == null){
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL;
            this.successResult = false;
            return;
        }
        if (isProfileUpdateInvalid(context,oldPassword, newOrgAddress) ||
                isProfileUpdateInvalid(context,oldPassword,newMainRepEmail)){
            return;
        }
        User user = context.getUserState().getCurrentUser();
        if (user.getClass() != EntertainmentProvider.class){
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_ENTERTAINMENT_PROVIDER;
            this.successResult = false;
            return;
        }
        Map<String, User> users = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> userEntry : users.entrySet()){
            if (((EntertainmentProvider) userEntry.getValue()).getOrgName().equals(this.newOrgName) &&
                    ((EntertainmentProvider) userEntry.getValue()).getOrgAddress().equals(this.newOrgAddress))
            {
                this.logStatus = LogStatus.USER_UPDATE_PROFILE_ORG_ALREADY_REGISTERED;
                this.successResult = false;
                break;
            }
        }
        user.updatePassword(newPassword);
        user.setPaymentAccountEmail(newPaymentAccountEmail);
        ((EntertainmentProvider) user).setMainRepEmail(newMainRepEmail);
        ((EntertainmentProvider) user).setMainRepName(newMainRepName);
        ((EntertainmentProvider) user).setOrgName(newOrgName);
        ((EntertainmentProvider) user).setOrgAddress(newOrgAddress);
        ((EntertainmentProvider) user).setOtherRepEmails(newOtherRepEmails);
        ((EntertainmentProvider) user).setOtherRepNames(newOtherRepNames);
        this.logStatus = LogStatus.USER_UPDATE_PROFILE_SUCCESS;
    }
}
