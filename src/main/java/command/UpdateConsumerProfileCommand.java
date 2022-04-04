package command;

import controller.Context;
import model.Consumer;
import model.ConsumerPreferences;
import model.User;

public class UpdateConsumerProfileCommand extends UpdateProfileCommand{

    private String oldPassword;
    private String newName;
    private String newEmail;
    private String newPhoneNumber;
    private String newPassword;
    private String newPaymentAccountEmail;
    private ConsumerPreferences newPreferences;
    private UpdateConsumerProfileCommand.LogStatus logStatus;

    private enum LogStatus{
        USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL,
        USER_UPDATE_PROFILE_NOT_CONSUMER,
        USER_UPDATE_PROFILE_SUCCESS
    }

    public UpdateConsumerProfileCommand(String oldPassword,
                                        String newName,
                                        String newEmail,
                                        String newPhoneNumber,
                                        String newPassword, String newPaymentAccountEmail,
                                        ConsumerPreferences newPreferences){
        this.oldPassword = oldPassword;
        this.newName = newName;
        this.newEmail = newEmail;
        this.newPhoneNumber = newPhoneNumber;
        this.newPassword = newPassword;
        this.newPaymentAccountEmail = newPaymentAccountEmail;
        this.newPreferences = newPreferences;
    }

    @Override
    public void execute(Context context) {
        if (this.oldPassword == null || this.newName == null ||
                this.newEmail == null || this.newPhoneNumber == null ||
            this.newPassword == null || this.newPaymentAccountEmail == null || this.newPreferences == null){
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL;
            this.successResult = false;
            return;
        }
        if (isProfileUpdateInvalid(context,oldPassword,newEmail)){
            return;
        }
        User user = context.getUserState().getCurrentUser();
        if (user.getClass() != Consumer.class){
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_CONSUMER;
            this.successResult = false;
            return;
        }
        user.setEmail(newEmail);
        ((Consumer) user).setName(newName);
        user.setPaymentAccountEmail(newPaymentAccountEmail);
        user.updatePassword(newPassword);
        ((Consumer) user).setPhoneNumber(newPhoneNumber);
        ((Consumer) user).setPreference(newPreferences);
        this.logStatus = LogStatus.USER_UPDATE_PROFILE_SUCCESS;
    }

}
