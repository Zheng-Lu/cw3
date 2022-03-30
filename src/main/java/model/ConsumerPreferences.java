package model;

public class ConsumerPreferences {
    private boolean preferAirFiltration;
    private boolean preferOutdoorsOnly;
    private int preferredMaxCapacity;
    private int preferredMaxVenueSize;
    private boolean preferSocialDistancing;

    public ConsumerPreferences(){
        this.preferSocialDistancing = false;
        this.preferAirFiltration = false;
        this.preferOutdoorsOnly = false;
        this.preferredMaxCapacity = Integer.MAX_VALUE;
        this.preferredMaxVenueSize = Integer.MAX_VALUE;
    }
}
