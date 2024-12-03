package interface_adapter.change_password;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInputData;

/**
 * Controller for the Change Password Use Case.
 */
public class ChangePasswordController {
    private final ChangePasswordInputBoundary userChangePasswordUseCaseInteractor;

    public ChangePasswordController(ChangePasswordInputBoundary userChangePasswordUseCaseInteractor) {
        this.userChangePasswordUseCaseInteractor = userChangePasswordUseCaseInteractor;
    }

    /**
     * Executes the Change Password Use Case.
     *
     * @param password The new password.
     * @param username The user whose password to change.
     * @return true if the password change was successful; false otherwise.
     */
    public boolean execute(String password, String username) {
        final ChangePasswordInputData changePasswordInputData = new ChangePasswordInputData(username, password);

        return userChangePasswordUseCaseInteractor.execute(changePasswordInputData);
    }

    /**
     * Switches the application view to the login screen.
     *
     * This method invokes the necessary logic to update the current application view
     * to the login screen, typically after completing the password change process or
     * when the user opts to navigate back to the login screen.
     */
    public void switchToLoginView() {
        userChangePasswordUseCaseInteractor.switchToLoginView();
    }
}
