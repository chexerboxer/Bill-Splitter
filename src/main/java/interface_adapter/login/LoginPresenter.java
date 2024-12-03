package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.ChangePasswordViewModel;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final ChangePasswordViewModel changePasswordViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          DashboardViewModel dashboardViewModel,
                          LoginViewModel loginViewModel,
                          SignupViewModel signupViewModel,
                          ChangePasswordViewModel changePasswordViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.changePasswordViewModel = changePasswordViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, switch to the logged in view.
        final DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setUsername(response.getUsername());
        dashboardState.setUserBillsData(response.getUserBillsData());
        this.dashboardViewModel.setState(dashboardState);
        this.dashboardViewModel.firePropertyChanged();

        this.viewManagerModel.setState(dashboardViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignUpView() {
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToChangePasswordView() {

        viewManagerModel.setState(changePasswordViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
