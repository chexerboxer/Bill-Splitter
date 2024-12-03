package use_case.login;

/**
 * Input boundary for actions related to logging in.
 * This interface defines the contract for handling user login logic, as well as switching views
 * for sign-up or change password functionalities.
 */
public interface LoginInputBoundary {

    /**
     * Executes the login use case with the provided input data.
     *
     * @param loginInputData The input data containing the user's login credentials.
     */
    void execute(LoginInputData loginInputData);

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
}
