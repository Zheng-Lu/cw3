package command;

import controller.Context;
import model.GovernmentRepresentative;
import model.SponsorshipRequest;
import model.SponsorshipStatus;

import java.util.List;

public class RespondSponsorshipCommand extends Object implements ICommand{


    private long requestNumber;
    private int percentToSponsor;
    private Boolean successResult;
    private LogStatus logStatus;
    private Object GovernmentRepresentative;

    private enum LogStatus{
        RESPOND_SPONSORSHIP_APPROVE,
        RESPOND_SPONSORSHIP_REJECT,
        RESPOND_SPONSORSHIP_USER_NOT_LOGGED_IN,
        RESPOND_SPONSORSHIP_USER_NOT_GOVERNMENT_REPRESENTATIVE,
        RESPOND_SPONSORSHIP_REQUEST_NOT_FOUND,
        RESPOND_SPONSORSHIP_INVALID_PERCENTAGE,
        RESPOND_SPONSORSHIP_REQUEST_NOT_PENDING,
        RESPOND_SPONSORSHIP_PAYMENT_SUCCESS,
        RESPOND_SPONSORSHIP_PAYMENT_FAILED,
    }

    public RespondSponsorshipCommand(long requestNumber,
                                      int percentToSponsor){
        this.requestNumber = requestNumber;
        this.percentToSponsor = percentToSponsor;
    }

    @Override
    public void execute(Context context) {
        if(context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_USER_NOT_LOGGED_IN;
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != GovernmentRepresentative.getClass()){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_USER_NOT_GOVERNMENT_REPRESENTATIVE;
            return;
        }

        if (percentToSponsor < 0 && percentToSponsor>100){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_INVALID_PERCENTAGE;
            return;
        }

        List<SponsorshipRequest> requests =context.getSponsorshipState().getAllSponsorshipRequest();
        boolean request_found =false;
        for (SponsorshipRequest request: requests) {
            if (request.getRequestNumber() == this.requestNumber){
                request_found = true;
            }
        }
        if (!request_found){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_REQUEST_NOT_FOUND;
            return;
        }

        if(context.getSponsorshipState().findRequestByNumber(this.requestNumber).getStatus() != SponsorshipStatus.PENDING){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_REQUEST_NOT_PENDING;
            return;
        }

        if(percentToSponsor == 0){
            this.successResult = false;
            logStatus = LogStatus.RESPOND_SPONSORSHIP_REJECT;
        }else{
            this.successResult = true;
            logStatus = LogStatus.RESPOND_SPONSORSHIP_APPROVE;
        /*    if(context.getPaymentSystem().processPayment(context.getUserState().getCurrentUser().getPaymentAccountEmail(),
                    context.getSponsorshipState().findRequestByNumber(this.requestNumber).getEvent().getOrganiser().getPaymentAccountEmail(),
                    context.getSponsorshipState().findRequestByNumber(this.requestNumber).getEvent().getNumTickets()*context.getSponsorshipState().findRequestByNumber(this.requestNumber).getEvent().getOriginalTicketPrice()*percentToSponsor/100))
            {
                logStatus = LogStatus.RESPOND_SPONSORSHIP_PAYMENT_SUCCESS;
                return;
            }else{
                logStatus = LogStatus.RESPOND_SPONSORSHIP_PAYMENT_FAILED;
                return;
            }

         */
        }
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.RESPOND_SPONSORSHIP_APPROVE){
            return this.successResult;
        }
        return false;
    }
}
