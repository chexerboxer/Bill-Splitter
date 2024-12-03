package use_case.change_password;

/**
 * The Change Password Use Case.
 */
public interface ChangePasswordInputBoundary {

    /**
     * Executes the Change Password use case with the provided input data.
     *
     * @param changePasswordInputData The input data containing the necessary information to change the password.
     * @return {@code true} if the password change was successful, {@code false} otherwise.
     */
    boolean execute(ChangePasswordInputData changePasswordInputData);

    /**
     * Switches the user interface to the login view.
     * This method is called after the password change process, usually when the user needs to log in again.
     */
    void switchToLoginView();
}
