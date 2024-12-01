package interface_adapter.dashboard;

import interface_adapter.LoggedInPresenter;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInputData;

import java.util.HashMap;

public class DashboardController implements LoggedInPresenter {
    private final DashboardInputBoundary dashboardUseCaseInteractor;


    public DashboardController(DashboardInputBoundary dashboardUseCaseInteractor) {
        this.dashboardUseCaseInteractor = dashboardUseCaseInteractor;
    }

    /**
     * Executes the Dashboard Use Case
     * @param billId the billId of the bill the user wants to delete
     */
    public void execute(HashMap<Integer, String> userBillsData, int billId) {
        final DashboardInputData dashboardInputData = new DashboardInputData(userBillsData, billId);

        dashboardUseCaseInteractor.execute(dashboardInputData);
    }

    /**
     * Executes the "switch to signupview" Use Case.
     */
    public void switchToSignUpView() {
        dashboardUseCaseInteractor.switchtoSignUpView();
    }

    public void switchToChangePasswordView() {
        dashboardUseCaseInteractor.switchtoChangePasswordView();
    }

    public void switchToBillView(String username, int billId) {
        dashboardUseCaseInteractor.switchToBillView(username, billId);
    }

    @Override
    public void switchToDashboardView(String username) {
        // does nothing but exists to be implemented by the sidebar
    }

}
