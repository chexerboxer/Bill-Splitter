package interface_adapter.bill_splitter;

import data_access.FileDAO;
import entity.bill.Bill;
import entity.users.User;
import interface_adapter.LoggedInPresenter;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.ChangePasswordViewModel;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class BillDisplayPresenter implements LoggedInPresenter {


    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private final FileDAO userDataAccessObject;


    public BillDisplayPresenter(ViewManagerModel viewManagerModel,
                                DashboardViewModel dashboardViewModel, FileDAO userDataAccessObject) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void switchToDashboardView(String username) {

        final User user = userDataAccessObject.get(username);
        final ArrayList<Bill> userBills = userDataAccessObject.getUserBills(user);
        final HashMap<Integer, String> userBillsData = new HashMap<>();

        for (Bill bill: userBills) {
            userBillsData.put(bill.getId(), bill.getName());
        }

        final DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setUsername(username);
        dashboardState.setUserBillsData(userBillsData);
        this.dashboardViewModel.setState(dashboardState);
        this.dashboardViewModel.firePropertyChanged();

        this.viewManagerModel.setState(dashboardViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();


    }

}
