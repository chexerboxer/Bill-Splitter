package interface_adapter.bill_splitter;

import java.util.ArrayList;
import java.util.HashMap;

import data_access.FileDAO;
import entity.bill.Bill;
import entity.users.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;

/**
 * A presenter responsible for handling the logic for displaying and managing bills
 * in the dashboard view.
 */
public class BillDisplayPresenter {

    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private final FileDAO userDataAccessObject;

    /**
     * Constructor for BillDisplayPresenter.
     *
     * @param viewManagerModel      The model managing the view states.
     * @param dashboardViewModel    The view model for the dashboard.
     * @param userDataAccessObject  Data access object for user and bill information.
     */
    public BillDisplayPresenter(final ViewManagerModel viewManagerModel,
                                final DashboardViewModel dashboardViewModel,
                                final FileDAO userDataAccessObject) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
        this.userDataAccessObject = userDataAccessObject;
    }

    /**
     * Switches the view to the dashboard and updates the dashboard state
     * with the user's bills and information.
     *
     * @param username The username of the user whose dashboard is displayed.
     */
    public void switchToDashboardView(final String username) {
        final User user = userDataAccessObject.get(username);
        final ArrayList<Bill> userBills = userDataAccessObject.getUserBills(user);
        final HashMap<Integer, String> userBillsData = new HashMap<>();

        for (final Bill bill : userBills) {
            userBillsData.put(bill.getId(), bill.getName());
        }

        final DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setUsername(username);
        dashboardState.setUserBillsData(userBillsData);

        dashboardViewModel.setState(dashboardState);
        dashboardViewModel.firePropertyChanged();

        viewManagerModel.setState(dashboardViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    /**
     * Adds a new bill for a user and updates the user's data.
     *
     * @param username     The username of the bill creator.
     * @param newBillName  The name of the new bill.
     */
    public void addBill(final String username, final String newBillName) {
        final User creator = userDataAccessObject.get(username);
        final int creatorId = creator.getId();
        final Bill newBill = new Bill(newBillName, creatorId);

        userDataAccessObject.addBill(newBill);

        // Dashboard view automatically updates after switching.
    }
}
