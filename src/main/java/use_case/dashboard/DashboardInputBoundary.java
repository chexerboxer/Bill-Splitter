package use_case.dashboard;

import use_case.dashboard.DashboardInputData;

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

//    void switchToBillView(int billId);
}
