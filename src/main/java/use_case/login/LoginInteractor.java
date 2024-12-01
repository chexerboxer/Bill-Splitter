package use_case.login;

import data_access.FileDAO;
import entity.bill.Bill;
import entity.users.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
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
                final ArrayList<Bill> userBills = userDataAccessObject.getUserBills(user);
                final HashMap<Integer, String> userBillsData = new HashMap<>();

                for (Bill bill: userBills) {
                    userBillsData.put(bill.getId(), bill.getName());
                }

                final LoginOutputData loginOutputData = new LoginOutputData(user.getName(), userBillsData, false);
                loginPresenter.prepareSuccessView(loginOutputData);
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
