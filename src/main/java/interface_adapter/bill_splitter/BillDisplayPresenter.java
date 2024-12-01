package interface_adapter.bill_splitter;

import data_access.FileDAO;
import entity.bill.Bill;
import entity.users.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class BillDisplayPresenter {


    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private final FileDAO userDataAccessObject;


    public BillDisplayPresenter(ViewManagerModel viewManagerModel,
                                DashboardViewModel dashboardViewModel, FileDAO userDataAccessObject) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
        this.userDataAccessObject = userDataAccessObject;
    }

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

    public void addBill(String username, String newBillName) {
        // create new bill object
        final User creator = userDataAccessObject.get(username);
        final int creatorId = creator.getId();
        final Bill newBill = new Bill(newBillName, creatorId);
        userDataAccessObject.addBill(newBill);

        // dashboard view will automatically update after switching
    }

}
