package use_case.login;

import java.util.HashMap;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final HashMap<Integer, String> userBillsData;
    private final boolean useCaseFailed;

    public LoginOutputData(String username, HashMap<Integer, String> userBillsData, boolean useCaseFailed) {
        this.username = username;
        this.userBillsData = userBillsData;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public HashMap<Integer, String> getUserBillsData() {
        return userBillsData;
    }
}
