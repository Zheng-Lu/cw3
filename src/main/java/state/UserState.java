package state;

import model.Booking;
import model.Consumer;
import model.EventPerformance;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserState implements IUserState{

    private Map<String, User> users = new HashMap<>();
    protected User currentUser;

    public UserState(){
        this.users = new HashMap<>();
        this.currentUser = null;
        registerGovernmentRepresentatives();
    }

    public UserState(IUserState other){
        this.users = other.getAllUsers();
        this.currentUser = other.getCurrentUser();
    }

    private void registerGovernmentRepresentatives(){
        // register government representatives
        // not call-able during the runtime, just called when construction
        // Something like this:
        // GovernmentRepresentative gr = new GovernmentRepresentative(........);
        // this.users.put(toString(gr), gr)
        // to add PRE-registered government representatives to the User list
    }

    @Override
    public void addUser(User user) {
        this.users.put(user.toString(),user);
    }

    @Override
    public Map<String, User> getAllUsers() {
        return this.users;
    }

    @Override
    public User getCurrentUser() {
        return this.currentUser;
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
