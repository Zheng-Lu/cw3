package model;

import java.util.ArrayList;
import java.util.List;

public class Consumer extends User {
    private final List<Booking> bookingList = new ArrayList<>();
    private String name, phoneNumber;
    private ConsumerPreferences preferences;

    public Consumer(String name, String email, String phoneNumber, String password, String paymentAccountEmail) {
        super(email, password, paymentAccountEmail);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void addBooking(Booking booking) {
        this.bookingList.add(booking);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public ConsumerPreferences getPreferences() {
        return this.preferences;
    }

    public void setPreference(ConsumerPreferences preferences) {
        this.preferences = preferences;
    }

    public List<Booking> getBookings() {
        return this.bookingList;
    }

    public void notify(String message) {
        System.out.print(message);
    }

    public void setPhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

}
