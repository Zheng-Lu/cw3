package model;

public class NonTicketedEvent extends Event {

    public NonTicketedEvent(long eventNumber, EntertainmentProvider organiser, String title, EventType type) {
        super(eventNumber, organiser, title, type);
    }

}
