package use_case.dashboard;

class MockOutputBoundary implements DashboardOutputBoundary {
    private String lastErrorMessage;
    private DashboardOutputData lastSuccessData;
    private boolean signUpViewSwitched = false;
    private boolean changePasswordViewSwitched = false;
    private String lastBillViewUsername;
    private int lastBillViewId;

    @Override
    public void prepareSuccessView(DashboardOutputData outputData) {
        lastSuccessData = outputData;
    }

    @Override
    public void prepareFailView(String errorMessage) {
        lastErrorMessage = errorMessage;
    }

    @Override
    public void switchToSignUpView() {
        signUpViewSwitched = true;
    }

    @Override
    public void switchToChangePasswordView() {
        changePasswordViewSwitched = true;
    }

    @Override
    public void switchToBillView(String username, int billId) {
        lastBillViewUsername = username;
        lastBillViewId = billId;
    }

    DashboardOutputData getLastSuccessData() {
        return lastSuccessData;
    }

    String getLastErrorMessage() {
        return lastErrorMessage;
    }

    boolean isSignUpViewSwitched() {
        return signUpViewSwitched;
    }

    boolean isChangePasswordViewSwitched() {
        return changePasswordViewSwitched;
    }

    String getLastBillViewUsername() {
        return lastBillViewUsername;
    }

    int getLastBillViewId() {
        return lastBillViewId;
    }
}
