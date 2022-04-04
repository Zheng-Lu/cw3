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

    public ConsumerPreferences(boolean preferAirFiltration, boolean preferOutdoorsOnly,
                               int preferredMaxCapacity, int preferredMaxVenueSize,
                               boolean preferSocialDistancing){
        this.preferredMaxVenueSize = preferredMaxVenueSize;
        this.preferredMaxCapacity = preferredMaxCapacity;
        this.preferOutdoorsOnly = preferOutdoorsOnly;
        this.preferAirFiltration = preferAirFiltration;
        this.preferSocialDistancing = preferSocialDistancing;
    }


    /**
     * Test if a performance of an event satisfies the preference of the customer
     * @param performance the performance of an event
     * @return if all preferences are satisfied, return true.
     */
    public boolean satisfyPreferences(EventPerformance performance){
        boolean sizeResult = performance.getVenueSize() <= preferredMaxVenueSize;
        boolean capacityResult = performance.getCapacityLimit() <= preferredMaxCapacity;
        boolean airResult;
        if (!this.preferAirFiltration) {
            airResult = true;
        }
        else {
            airResult = performance.hasAirFiltration();
        }
        boolean distancingResult;
        if (!this.preferSocialDistancing){
            distancingResult = true;
        }
        else {
            distancingResult = performance.hasSocialDistancing();
        }
        boolean outdoorResult;
        if (!this.preferOutdoorsOnly){
            outdoorResult = true;
        }else {
            outdoorResult = performance.isOutdoors();
        }

        return sizeResult && capacityResult && airResult && distancingResult && outdoorResult;
    }
}
