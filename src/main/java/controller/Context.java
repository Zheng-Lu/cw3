package controller;

import external.MockPaymentSystem;
import external.PaymentSystem;
import state.*;

public class Context {
    private PaymentSystem paymentSystem;
    private IUserState userState;
    private IEventState eventState;
    private BookingState bookingState;
    private ISponsorshipState sponsorshipState;

    public Context(){
        this.paymentSystem = new MockPaymentSystem();
        this.userState = new UserState();
        this.eventState = new EventState();
        this.bookingState = new BookingState();
        this.sponsorshipState = new SponsorshipState();
    }

    public Context(Context other){
        this.paymentSystem = other.paymentSystem;
        this.userState = other.userState;
        this.eventState = other.eventState;
        this.bookingState = other.bookingState;
        this.sponsorshipState = other.sponsorshipState;
    }

    public PaymentSystem getPaymentSystem(){
        return this.paymentSystem;
    }

    public IUserState getUserState(){
        return this.userState;
    }

    public IEventState getEventState(){
        return this.eventState;
    }

    public ISponsorshipState getSponsorshipState(){
        return this.sponsorshipState;
    }

}
