package use_case.login;

import data_access.FileDAO;
import entity.users.User;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
//                            LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");

        }
        else {
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final User user = userDataAccessObject.get(loginInputData.getUsername());
                if (user == null) {
                    loginPresenter.prepareFailView("User not found.");
                    return;
                }
                if (!password.equals(user.getPassword())) {
                    loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
                } else {
                    userDataAccessObject.setCurrentUsername(user.getName());
                    userDataAccessObject.setCurrentUsername(user.getName());
                    final LoginOutputData loginOutputData = new LoginOutputData(user.getName(), false);
                    loginPresenter.prepareSuccessView(loginOutputData);
                }

            }
        }
    }

    @Override
    public void switchtoSignUpView() {
        loginPresenter.switchToSignUpView();
    }

    @Override
    public void switchtoChangePasswordView() {
        loginPresenter.switchToChangePasswordView();
    }
}
