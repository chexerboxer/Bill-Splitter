package use_case.dashboard;

import entity.bill.Bill;
import entity.users.User;

import java.util.HashMap;

public class DashboardInteractor implements DashboardInputBoundary {
    private final DashboardUserDataAccessInterface userDataAccessObject;
    private final DashboardOutputBoundary dashboardPresenter;

    public DashboardInteractor(DashboardUserDataAccessInterface userDataAccessInterface,
                               DashboardOutputBoundary dashboardOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.dashboardPresenter = dashboardOutputBoundary;
    }

    @Override
    public void execute(DashboardInputData dashboardInputData) {
        final int billId = dashboardInputData.getBillId();

        if (!userDataAccessObject.removeBill(billId)) {
            dashboardPresenter.prepareFailView("Bill couldn't be deleted.");

        }
        else {
            final HashMap<Integer, String> newUserBillsData = dashboardInputData.getUserBillsData();
            newUserBillsData.remove(billId);
            final DashboardOutputData dashboardOutputData = new DashboardOutputData(newUserBillsData,false);
            dashboardPresenter.prepareSuccessView(dashboardOutputData);
            }
        }

    @Override
    public void switchtoSignUpView() {
        dashboardPresenter.switchToSignUpView();
    }

    @Override
    public void switchtoChangePasswordView() {
        dashboardPresenter.switchToChangePasswordView();
    }

    @Override
    public void switchToBillView(String username, int billId) {
        dashboardPresenter.switchToBillView(username,billId);

    }

    @Override
    public void addBill(HashMap<Integer, String> userBillsData, String username, String newBillName) {
        // create new bill object
        final User creator = userDataAccessObject.get(username);
        final int creatorId = creator.getId();
        final Bill newBill = new Bill(newBillName, creatorId);
        userDataAccessObject.addBill(newBill);

        // update userBillsData
        final HashMap<Integer, String> newUserBillsData = userBillsData;
        newUserBillsData.put(newBill.getId(), newBillName);
        final DashboardOutputData dashboardOutputData = new DashboardOutputData(newUserBillsData,false);
        dashboardPresenter.prepareSuccessView(dashboardOutputData);
    }
}
