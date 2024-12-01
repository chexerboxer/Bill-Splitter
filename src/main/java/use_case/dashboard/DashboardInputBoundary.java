package use_case.dashboard;

import use_case.dashboard.DashboardInputData;

import java.util.HashMap;

public interface DashboardInputBoundary {

    /**
     * Executes the dashboard use case.
     * @param dashboardInputData the input data
     */
    void execute(DashboardInputData dashboardInputData);

    /**
     * Executes the switch back to sign up view use case
     */
    void switchtoSignUpView();

    void switchtoChangePasswordView();

    void switchToBillView(String username, int billId);

    void addBill(HashMap<Integer, String> userBillsData, String username, String newBillName);
}
