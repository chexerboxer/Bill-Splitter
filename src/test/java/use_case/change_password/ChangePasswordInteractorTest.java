package use_case.change_password;

import entity.users.User;
import entity.users.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordInteractorTest {
    private ChangePasswordInteractor interactor;
    private UserFactory userFactory;
    private MockUserDataAccess userDataAccess;
    private MockOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        // Using an anonymous inner class to provide a concrete implementation of UserFactory

        userDataAccess = new MockUserDataAccess();
        outputBoundary = new MockOutputBoundary();

    }

    @Test
    void testExecuteUserNotFound() {
        ChangePasswordInputData inputData = new ChangePasswordInputData("newPassword123", "nonexistent_user");

        boolean result = interactor.execute(inputData);

        assertFalse(result, "Interactor should return false when the user is not found");
        assertEquals("User not found", outputBoundary.getLastErrorMessage(), "Error message should be 'User not found'");
    }

    @Test
    void testExecuteSuccess() {
        User testUser = userFactory.create("valid_user", "oldPassword");
        userDataAccess.addUser(testUser);

        ChangePasswordInputData inputData = new ChangePasswordInputData("newPassword123", "valid_user");

        boolean result = interactor.execute(inputData);

        assertTrue(result, "Interactor should return true on success");
        assertEquals("newPassword123", testUser.getPassword(), "User's password should be updated");
        assertEquals("valid_user", outputBoundary.getLastSuccessData().getUsername(), "Output data username should match");
    }

    @Test
    void testSwitchToLoginView() {
        interactor.switchToLoginView();

        // Verifying via the presenter's behavior
        // ChangePasswordPresenter does not track state changes, so no direct assertion is possible here.
        // You may need to check the side effects in your real application, such as state changes in the `ViewManagerModel`.
    }

    // Mock Implementations of Interfaces
    private static class MockUserDataAccess implements ChangePasswordUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();

        void addUser(User user) {
            users.put(user.getName(), user);
        }

        @Override
        public void changePassword(User user) {
            // Password change logic is directly handled in the User object.
        }

        @Override
        public User get(String username) {
            return users.get(username);
        }
    }

    private static class MockOutputBoundary implements ChangePasswordOutputBoundary {
        private String lastErrorMessage;
        private ChangePasswordOutputData lastSuccessData;

        @Override
        public void prepareSuccessView(ChangePasswordOutputData outputData) {
            lastSuccessData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            lastErrorMessage = errorMessage;
        }

        String getLastErrorMessage() {
            return lastErrorMessage;
        }

        ChangePasswordOutputData getLastSuccessData() {
            return lastSuccessData;
        }
    }
}
