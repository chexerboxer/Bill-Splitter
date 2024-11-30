package use_case.dashboard;

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

//    @Override
//    public void switchToBillView(int billId) {
//        dashboardPresenter.switchToBillView(billId);
//
//    }
}
