package command;

import controller.Context;
import logging.Logger;
import model.Event;
import model.GovernmentRepresentative;
import model.SponsorshipRequest;
import model.SponsorshipStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RespondSponsorshipCommand extends Object implements ICommand{


    private long requestNumber;
    private int percentToSponsor;
    private Boolean successResult;
    private LogStatus logStatus;

    private enum LogStatus{
        RESPOND_SPONSORSHIP_APPROVE,
        RESPOND_SPONSORSHIP_REJECT,
        RESPOND_SPONSORSHIP_USER_NOT_LOGGED_IN,
        RESPOND_SPONSORSHIP_USER_NOT_GOVERNMENT_REPRESENTATIVE,
        RESPOND_SPONSORSHIP_REQUEST_NOT_FOUND,
        RESPOND_SPONSORSHIP_INVALID_PERCENTAGE,
        RESPOND_SPONSORSHIP_REQUEST_NOT_PENDING,
        RESPOND_SPONSORSHIP_PAYMENT_SUCCESS,
        RESPOND_SPONSORSHIP_PAYMENT_FAILED
    }

    public RespondSponsorshipCommand(long requestNumber,
                                      int percentToSponsor){
        this.requestNumber = requestNumber;
        this.percentToSponsor = percentToSponsor;
    }

    @Override
    public void execute(Context context) {

        // ADD TO LOGGER
        Map<String, Object> info = new HashMap<>();

        if(context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_USER_NOT_LOGGED_IN;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("RespondSponsorshipCommand.execute()",
                    getResult(),info);
            return;
        }

        if (context.getUserState().getCurrentUser().getClass() != GovernmentRepresentative.class){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_USER_NOT_GOVERNMENT_REPRESENTATIVE;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("RespondSponsorshipCommand.execute()",
                    getResult(),info);
            return;
        }

        if (percentToSponsor < 0 || percentToSponsor>100){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_INVALID_PERCENTAGE;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("RespondSponsorshipCommand.execute()",
                    getResult(),info);
            return;
        }

        List<SponsorshipRequest> requests =context.getSponsorshipState().getAllSponsorshipRequest();
        boolean request_found =false;
        for (SponsorshipRequest request: requests) {
            if (request.getRequestNumber() == this.requestNumber) {
                request_found = true;
                break;
            }
        }
        if (!request_found){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_REQUEST_NOT_FOUND;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("RespondSponsorshipCommand.execute()",
                    getResult(),info);
            return;
        }

        if(context.getSponsorshipState().findRequestByNumber(this.requestNumber).getStatus() != SponsorshipStatus.PENDING){
            logStatus = LogStatus.RESPOND_SPONSORSHIP_REQUEST_NOT_PENDING;
            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("RespondSponsorshipCommand.execute()",
                    getResult(),info);
            return;
        }

        Event event = context.getSponsorshipState().findRequestByNumber(requestNumber).getEvent();
        long eventNum = event.getEventNumber();

        if(percentToSponsor == 0){
            this.successResult = false;
            logStatus = LogStatus.RESPOND_SPONSORSHIP_REJECT;

            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("RespondSponsorshipCommand.execute()",
                    getResult(),info);

            context.getSponsorshipState().findRequestByNumber(requestNumber).reject();
            event.getOrganiser().getProviderSystem().recordSponsorshipRejection(eventNum);
        }else{
            int numberOfTotalPerformances = context.getSponsorshipState().findRequestByNumber(this.requestNumber).getEvent().getPerformances().size();
            if(context.getPaymentSystem().processPayment(context.getUserState().getCurrentUser().getPaymentAccountEmail(),
                    context.getSponsorshipState().findRequestByNumber(this.requestNumber).getEvent().getOrganiser().getPaymentAccountEmail(),
                    (double) context.getSponsorshipState().findRequestByNumber(this.requestNumber).getEvent().getNumTickets()*
                            numberOfTotalPerformances
                            *context.getSponsorshipState().findRequestByNumber(this.requestNumber).getEvent().getOriginalTicketPrice()*percentToSponsor/100))
                // the representative can still accept even the event has 0 tickets allocated
            {
                logStatus = LogStatus.RESPOND_SPONSORSHIP_PAYMENT_SUCCESS;
            }else{
                logStatus = LogStatus.RESPOND_SPONSORSHIP_PAYMENT_FAILED;

                info.put("STATUS:",this.logStatus);
                Logger.getInstance().logAction("RespondSponsorshipCommand.execute()",
                        getResult(),info);
                return;
            }

            info.put("STATUS:",this.logStatus);

            this.successResult = true;
            logStatus = LogStatus.RESPOND_SPONSORSHIP_APPROVE;

            info.put("STATUS:",this.logStatus);
            Logger.getInstance().logAction("RespondSponsorshipCommand.execute()",
                    getResult(),info);

            context.getSponsorshipState().findRequestByNumber(requestNumber).accept(percentToSponsor,context.getUserState().getCurrentUser().getPaymentAccountEmail());
            event.getOrganiser().getProviderSystem().recordSponsorshipAcceptance(eventNum,percentToSponsor);
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
