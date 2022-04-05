package command;

import controller.Context;

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
      if(context.getUserState().getCurrentUser() == null){
          logStatus = LogStatus.USER_LOGOUT_NOT_LOGGED_IN;
          return;
      }
      logStatus = LogStatus.USER_LOGOUT_SUCCESS;

    }

    public Void getResult(){
        if (logStatus == LogStatus.USER_LOGOUT_SUCCESS) {
            return (java.lang.Void) Void;
        }
        return null;
    }
}
