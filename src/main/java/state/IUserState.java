package state;

import model.Booking;
import model.Consumer;
import model.EventPerformance;
import model.User;

import java.util.List;
import java.util.Map;

public interface IUserState {

    void addUser(User user);

    Map<String, User> getAllUsers();

    User getCurrentUser();

    void setCurrentUser(User user);

}
