package state;

import model.GovernmentRepresentative;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class UserState implements IUserState {

    protected User currentUser;
    private final Map<String, User> users;

    public UserState() {
        this.users = new HashMap<>();
        this.currentUser = null;
        registerGovernmentRepresentatives();
    }

    public UserState(IUserState other) {
        this.users = other.getAllUsers();
        this.currentUser = other.getCurrentUser();
    }

    private void registerGovernmentRepresentatives() {
        // register government representatives
        // not call-able during the runtime, only called once when construction
        // Something like this:
        // GovernmentRepresentative gr = new GovernmentRepresentative(........);
        // this.users.put(toString(gr), gr)
        // to add PRE-registered government representatives to the User list

        // add representatives for testing
        GovernmentRepresentative gr = new GovernmentRepresentative("margaret.thatcher@gov.uk", "The Good times  ",
                "financial-payment@gov.uk");
        this.users.put(gr.toString(), gr);
    }

    @Override
    public void addUser(User user) {
        this.users.put(user.toString(), user);
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
