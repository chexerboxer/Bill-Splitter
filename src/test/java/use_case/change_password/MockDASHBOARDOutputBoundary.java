package use_case.change_password;

public class MockDASHBOARDOutputBoundary implements ChangePasswordOutputBoundary {
    private String lastErrorMessage;
    private ChangePasswordOutputData lastSuccessData;

    @Override
    public void prepareSuccessView(ChangePasswordOutputData outputData) {
        this.lastSuccessData = outputData;
    }

    @Override
    public void prepareFailView(String errorMessage) {
        this.lastErrorMessage = errorMessage;
    }

    // Accessor methods to validate results during tests
    String getLastErrorMessage() {
        return lastErrorMessage;
    }

    ChangePasswordOutputData getLastSuccessData() {
        return lastSuccessData;
    }

    void switchToLoginView(){

    }

}
