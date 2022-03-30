package model;

import java.util.Map;

public abstract class Event extends Object{

    private long eventNumber;
    private EntertainmentProvider organiser;
    private String title;
    private EventType type;
    private Map<Long, EventPerformance>performances;
    private EventStatus status;
    private EventType attribute;
    private EventStatus attribute2;

    protected Event(long eventNumber, EntertainmentProvider organiser, String title, EventType type){


    }
}
