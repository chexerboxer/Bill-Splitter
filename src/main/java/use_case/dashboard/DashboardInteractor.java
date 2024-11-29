package use_case.dashboard;

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
//            userDataAccessObject.setCurrentUsername(user.getName());
//            final LoginOutputData loginOutputData = new LoginOutputData(user.getName(), false);
//            loginPresenter.prepareSuccessView(loginOutputData);
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
    public void switchToBillView(int billId) {
        dashboardPresenter.switchToBillView(billId);

    }
}
