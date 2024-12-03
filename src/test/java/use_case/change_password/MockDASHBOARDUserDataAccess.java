package use_case.change_password;

import entity.bill.Bill;
import entity.users.CommonUser;
import entity.users.CommonUserFactory;
import entity.users.User;
import interface_adapter.change_password.ChangePasswordPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordInteractorTest {
    private ChangePasswordInteractor interactor;
    private MockDASHBOARDUserDataAccess userDataAccess;
    private MockDASHBOARDOutputBoundary outputBoundary;
    private CommonUserFactory userFactory;

    @BeforeEach
    void setUp() {
        userFactory = new CommonUserFactory();
        userDataAccess = new MockDASHBOARDUserDataAccess();
        outputBoundary = new MockDASHBOARDOutputBoundary();

        interactor = new ChangePasswordInteractor(userDataAccess, outputBoundary, userFactory, new EmptyPresenter());
    }

    @Test
    void testSwitchToLoginView() {
        // Invoke switchToLoginView
        interactor.switchToLoginView();

        // Validate that the method does not throw or cause unexpected behavior.
        // Assuming the presenter's interaction is tested through side effects in a real system.
        // For now, ensuring no exceptions occur and outputBoundary is handled gracefully.
        assertTrue(true, "Switching to login view should not throw an exception.");
    }

    @Test
    void testIsUseCaseFailed() {
        ChangePasswordOutputData successData = new ChangePasswordOutputData("valid_user", false);
        ChangePasswordOutputData failData = new ChangePasswordOutputData("invalid_user", true);

        // Test successful case
        assertFalse(successData.isUseCaseFailed(), "Use case should not be marked as failed for successful output data");

        // Test failure case
        assertTrue(failData.isUseCaseFailed(), "Use case should be marked as failed for error output data");
    }


    @Test
    void testExecuteUserNotFound() {
        ChangePasswordInputData inputData = new ChangePasswordInputData("newPassword123", "nonexistent_user");

        boolean result = interactor.execute(inputData);

        assertFalse(result, "Interactor should return false when the user is not found");
        assertEquals("User not found", outputBoundary.getLastErrorMessage(), "Error message should match");
    }

    @Test
    void testExecuteSuccess() {
        CommonUser user = (CommonUser) userFactory.create("valid_user", "oldPassword");
        userDataAccess.addUser(user);

        ChangePasswordInputData inputData = new ChangePasswordInputData("newPassword123", "valid_user");

        boolean result = interactor.execute(inputData);

        assertTrue(result, "Interactor should return true on success");
        assertEquals("newPassword123", user.getPassword(), "User's password should be updated");
        assertNotNull(outputBoundary.getLastSuccessData(), "Success data should not be null");
        assertEquals("valid_user", outputBoundary.getLastSuccessData().getUsername(), "Username in success data should match");
    }
}

public class MockDASHBOARDUserDataAccess implements ChangePasswordUserDataAccessInterface {
    private final Map<String, CommonUser> users = new HashMap<>();

    void addUser(CommonUser user) {
        users.put(user.getName(), user);
    }

    @Override
    public void changePassword(User user) {
        // Password changes are directly reflected in the `CommonUser` object
    }

    @Override
    public CommonUser getByUsername(String username) {
        return users.get(username);
    }

    public void addBill(Bill bill) {
    }
}


class EmptyPresenter extends ChangePasswordPresenter {
    public EmptyPresenter() {
        super(null, null, null);
    }

    @Override
    public void switchToLoginView() {
        // Intentionally left empty, as this is only a placeholder for testing
    }
}
