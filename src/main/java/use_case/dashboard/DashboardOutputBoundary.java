package use_case.dashboard;

import use_case.login.LoginOutputData;

public interface DashboardOutputBoundary {
    /**
     * Prepares the success view for the Login Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(DashboardOutputData outputData);

    /**
     * Prepares the failure view for the Login Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Signup View.
     */
    void switchToSignUpView();

    void switchToChangePasswordView();

//    void switchToBillView(int billId);
}
