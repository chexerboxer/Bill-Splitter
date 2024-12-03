package interface_adapter.dashboard;

import java.util.HashMap;

import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInputData;

public class DashboardController {
    private final DashboardInputBoundary dashboardUseCaseInteractor;

    public DashboardController(DashboardInputBoundary dashboardUseCaseInteractor) {
        this.dashboardUseCaseInteractor = dashboardUseCaseInteractor;
    }

    /**
     * Executes the Dashboard Use Case.
     * @param billId the billId of the bill the user wants to delete
     * @param userBillsData the userBill data needed to update teh dashboard.
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

    /**
     * Switches the application view to the change password screen.
     *
     * This method triggers the logic to update the application's state to display
     * the change password view.
     */
    public void switchToChangePasswordView() {
        dashboardUseCaseInteractor.switchtoChangePasswordView();
    }

    /**
     * Switches the application view to a specific bill view.
     *
     * @param username The username of the user viewing the bill.
     * @param billId   The ID of the bill to be displayed.
     */
    public void switchToBillView(String username, int billId) {
        dashboardUseCaseInteractor.switchToBillView(username, billId);
    }

    /**
     * Adds a new bill to the user's dashboard.
     *
     * @param userBillsData The current mapping of user bills with their IDs and names.
     * @param username      The username of the user creating the new bill.
     * @param newBillName   The name of the new bill to be added.
     */
    public void addBill(HashMap<Integer, String> userBillsData, String username, String newBillName) {
        dashboardUseCaseInteractor.addBill(userBillsData, username, newBillName);
    }

}
