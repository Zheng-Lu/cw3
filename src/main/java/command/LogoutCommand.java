package command;

import controller.Context;
import logging.Logger;

import java.util.HashMap;
import java.util.Map;

public class LogoutCommand extends Object implements  ICommand{

    private LogStatus logStatus;
    private Object Void;

    private enum LogStatus{
        USER_LOGOUT_SUCCESS,
        USER_LOGOUT_NOT_LOGGED_IN
    }

    public LogoutCommand(){

    }

    @Override
    public void execute(Context context) {

        // ADD TO LOGGER

        Map<String, Object> info = new HashMap<>();

       if(context.getUserState().getCurrentUser() == null){
           logStatus = LogStatus.USER_LOGOUT_NOT_LOGGED_IN;
           info.put("STATUS:",this.logStatus);
           Logger.getInstance().logAction("LogoutCommand.execute()",
                  getResult(),info);
           return;
       }

       logStatus = LogStatus.USER_LOGOUT_SUCCESS;
       context.getUserState().setCurrentUser(null);

       info.put("STATUS:",this.logStatus);
       Logger.getInstance().logAction("LogoutCommand.execute()",
               getResult(),info);

    }

    public Void getResult(){
        if (logStatus == LogStatus.USER_LOGOUT_SUCCESS) {
            return (java.lang.Void) Void;
        }
        return null;
    }
}
