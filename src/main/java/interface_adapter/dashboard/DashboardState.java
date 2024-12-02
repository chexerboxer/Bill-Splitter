package interface_adapter.dashboard;

import java.util.HashMap;

/**
 * The State information representing the logged-in user.
 */
public class DashboardState {
    private String username = "";
    private String passwordError;
    private HashMap<Integer, String> userBillsData;

    public DashboardState(DashboardState copy) {
        username = copy.username;
        userBillsData = copy.userBillsData;
        passwordError = copy.passwordError;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public DashboardState() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public HashMap<Integer, String> getUserBillsData() {
        return userBillsData;
    }

    public void setUserBillsData(HashMap<Integer, String> userBillsData) {
        this.userBillsData = userBillsData;
    }
}
