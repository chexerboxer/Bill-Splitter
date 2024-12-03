package use_case.dashboard;

import java.util.HashMap;

/**
 * The input boundary for the Dashboard use case.
 * This interface defines the contract for the use case that handles the logic
 * related to a user's dashboard, such as viewing bills, switching views, and adding new bills.
 */
public interface DashboardInputBoundary {

    /**
     * Executes the dashboard use case with the provided input data.
     *
     * @param dashboardInputData The input data containing the necessary details for the dashboard use case.
     */
    void execute(DashboardInputData dashboardInputData);

    /**
     * Executes the use case to switch to the sign-up view.
     * This method is called when the user needs to navigate back to the sign-up view.
     */
    void switchtoSignUpView();

    /**
     * Executes the use case to switch to the change password view.
     * This method is called when the user needs to navigate to the change password view.
     */
    void switchtoChangePasswordView();

    /**
     * Executes the use case to switch to a specific bill view.
     *
     * @param username The username of the user whose bill is being viewed.
     * @param billId The ID of the bill to be viewed.
     */
    void switchToBillView(String username, int billId);

    /**
     * Executes the use case to add a new bill to the user's dashboard.
     *
     * @param userBillsData A map containing user IDs as keys and their corresponding bill names as values.
     * @param username The username of the user adding the new bill.
     * @param newBillName The name of the new bill being added.
     */
    void addBill(HashMap<Integer, String> userBillsData, String username, String newBillName);
}
