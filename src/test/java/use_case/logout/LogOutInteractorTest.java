package use_case.logout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutInteractorTest {
    private LogoutInteractor interactor;
    private MockUserDataAccess userDataAccess;
    private MockOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        userDataAccess = new MockUserDataAccess();
        outputBoundary = new MockOutputBoundary();
        interactor = new LogoutInteractor(userDataAccess, outputBoundary);
    }

    @Test
    void testExecute() {
        // Setup: Simulate a logged-in user
        String currentUsername = "test_user";
        userDataAccess.setCurrentUsername(currentUsername);

        // Execute logout
        LogoutInputData inputData = new LogoutInputData(currentUsername);
        interactor.execute(inputData);

        // Verify: Current username is cleared
        assertNull(userDataAccess.getCurrentUsername(), "Current username should be null after logout");

        // Verify: Output boundary receives correct data
        LogoutOutputData outputData = outputBoundary.getLastSuccessData();
        assertNotNull(outputData, "Output data should not be null");
        assertEquals(currentUsername, outputData.getUsername(), "Username in output data should match the input");
        assertFalse(outputData.isUseCaseFailed(), "Use case should not fail for a valid logout");
    }

    @Test
    void testLogoutWithoutLoggedInUser() {
        // Execute logout with no user currently logged in
        LogoutInputData inputData = new LogoutInputData("nonexistent_user");
        interactor.execute(inputData);

        // Verify: Current username remains null
        assertNull(userDataAccess.getCurrentUsername(), "Current username should remain null");

        // Verify: Output boundary receives correct data
        LogoutOutputData outputData = outputBoundary.getLastSuccessData();
        assertNotNull(outputData, "Output data should not be null");
        assertEquals("nonexistent_user", outputData.getUsername(), "Username in output data should match the input");
        assertFalse(outputData.isUseCaseFailed(), "Use case should not fail even for a nonexistent user");
    }
}

class MockUserDataAccess implements LogoutUserDataAccessInterface {
    private String currentUsername;

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}

class MockOutputBoundary implements LogoutOutputBoundary {
    private LogoutOutputData lastSuccessData;

    @Override
    public void prepareSuccessView(LogoutOutputData outputData) {
        this.lastSuccessData = outputData;
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Not implemented; assume logout can't fail
    }

    public LogoutOutputData getLastSuccessData() {
        return lastSuccessData;
    }
}


