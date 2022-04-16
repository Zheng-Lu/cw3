package command;

import controller.Context;
import logging.Logger;
import model.Consumer;
import model.ConsumerPreferences;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class UpdateConsumerProfileCommand extends UpdateProfileCommand {

    private final String oldPassword;
    private final String newName;
    private final String newEmail;
    private final String newPhoneNumber;
    private final String newPassword;
    private final String newPaymentAccountEmail;
    private final ConsumerPreferences newPreferences;
    private UpdateConsumerProfileCommand.LogStatus logStatus;

    public UpdateConsumerProfileCommand(String oldPassword,
                                        String newName,
                                        String newEmail,
                                        String newPhoneNumber,
                                        String newPassword, String newPaymentAccountEmail,
                                        ConsumerPreferences newPreferences) {
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
        User user = context.getUserState().getCurrentUser();

        if (this.oldPassword == null || this.newName == null ||
                this.newEmail == null || this.newPhoneNumber == null ||
                this.newPassword == null || this.newPaymentAccountEmail == null || this.newPreferences == null) {
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL;
            this.successResult = false;
        } else if (isProfileUpdateInvalid(context, oldPassword, newEmail)) {
        } else if (user.getClass() != Consumer.class) {
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_CONSUMER;
            this.successResult = false;
        }

        if (this.successResult && super.logStatus == null) {
            user.setEmail(newEmail);
            ((Consumer) user).setName(newName);
            user.setPaymentAccountEmail(newPaymentAccountEmail);
            user.updatePassword(newPassword);
            ((Consumer) user).setPhoneNumber(newPhoneNumber);
            ((Consumer) user).setPreference(newPreferences);
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_SUCCESS;
        }

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();
        info.put("STATUS:", this.logStatus);
        if (super.logStatus != null) {
            info.put("PROFILE_STATUS:", super.logStatus);
        }
        Logger.getInstance().logAction("UpdateConsumerProfileCommand.execute()",
                getResult(), info);
    }

    private enum LogStatus {
        USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL,
        USER_UPDATE_PROFILE_NOT_CONSUMER,
        USER_UPDATE_PROFILE_SUCCESS
    }

}
