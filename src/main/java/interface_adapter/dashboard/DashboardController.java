package interface_adapter.dashboard;

import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInputData;

public class DashboardController {
    private final DashboardInputBoundary dashboardUseCaseInteractor;


    public DashboardController(DashboardInputBoundary dashboardUseCaseInteractor) {
        this.dashboardUseCaseInteractor = dashboardUseCaseInteractor;
    }

    /**
     * Executes the Dashboard Use Case
     * @param billId the billId of the bill the user wants to delete
     */
    public void execute(int billId) {
        final DashboardInputData dashboardInputData = new DashboardInputData(billId);

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

    public void switchToBillView(int billId) {
        dashboardUseCaseInteractor.switchToBillView(billId);
    }

}
