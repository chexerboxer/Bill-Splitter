package interface_adapter.dashboard;

import interface_adapter.ViewManagerModel;
import interface_adapter.bill_splitter.BillDisplayState;
import interface_adapter.change_password.ChangePasswordViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.bill_splitter.BillDisplayViewModel;
import use_case.dashboard.*;
import use_case.dashboard.DashboardInputBoundary;

public class DashboardPresenter implements DashboardOutputBoundary {
    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final ChangePasswordViewModel changePasswordViewModel;
    private final BillDisplayViewModel billDisplayViewModel;


    public DashboardPresenter(ViewManagerModel viewManagerModel,
                          DashboardViewModel dashboardViewModel,
                          BillDisplayViewModel billDisplayViewModel,
                          SignupViewModel signupViewModel,
                          ChangePasswordViewModel changePasswordViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
        this.billDisplayViewModel = billDisplayViewModel;
        this.signupViewModel = signupViewModel;
        this.changePasswordViewModel = changePasswordViewModel;
    }

    @Override
    public void prepareSuccessView(DashboardOutputData response) {
        // On success, switch to unique bill view.

//        final BillDisplayState billDisplayState = billDisplayViewModel.getState();
//        billDisplayState.setUsername(response.getUsername());
//        billDisplayState.setBill(response.getBill());
//        this.billDisplayState.setState(billDisplayState);
//        this.billDisplayState.firePropertyChanged();
//
//        this.viewManagerModel.setState(dashboardViewModel.getViewName());

        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setPasswordError(error);
        dashboardViewModel.firePropertyChanged();
    }

    // keep switch to sign up and change password view since the dashboard sidebar has options to logout
    // and change passwords
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

    public void switchToBillView(int billId) {

        viewManagerModel.setState(billDisplayViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
