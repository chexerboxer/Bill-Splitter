package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

/**
 * The controller for the Login Use Case.
 */
public class LoginController {

    private final LoginInputBoundary loginUseCaseInteractor;

    public LoginController(LoginInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    /**
     * Executes the Login Use Case.
     * @param username the username of the user logging in
     * @param password the password of the user logging in
     */
    public void execute(String username, String password) {
        final LoginInputData loginInputData = new LoginInputData(
                username, password);

        loginUseCaseInteractor.execute(loginInputData);
    }

    /**
     * Executes the "switch to signupview" Use Case.
     */
    public void switchToSignUpView() {
        loginUseCaseInteractor.switchtoSignUpView();
    }

    /**
     * Switches the application view to the change password screen.
     *
     * This method invokes the logic to update the application's state to display
     * the change password view.
     */
    public void switchToChangePasswordView() {
        loginUseCaseInteractor.switchtoChangePasswordView();
    }
}
