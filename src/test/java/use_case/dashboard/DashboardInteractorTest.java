package use_case.dashboard;

import entity.bill.Bill;
import entity.users.CommonUser;
import entity.users.CommonUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DashboardInteractorTest {
    private DashboardInteractor interactor;
    private MockUserDataAccess userDataAccess;
    private MockOutputBoundary outputBoundary;
    private CommonUserFactory userFactory;

    @BeforeEach
    void setUp() {
        userFactory = new CommonUserFactory();
        userDataAccess = new MockUserDataAccess();
        outputBoundary = new MockOutputBoundary();

        interactor = new DashboardInteractor(userDataAccess, outputBoundary);
    }

    @Test
    void testExecuteSuccess() {
        CommonUser user = (CommonUser) userFactory.create("test_user", "password123");
        userDataAccess.addUser(user);

        Bill bill = new Bill("Test Bill", user.getId());
        userDataAccess.addBill(bill);

        HashMap<Integer, String> userBillsData = new HashMap<>();
        userBillsData.put(bill.getId(), bill.getName());

        DashboardInputData inputData = new DashboardInputData(userBillsData, bill.getId());
        inputData.getUserBillsData();
        interactor.execute(inputData);

    }

    @Test
    void testExecuteFailure() {
        HashMap<Integer, String> userBillsData = new HashMap<>();
        userBillsData.put(1, "Nonexistent Bill");

        DashboardInputData inputData = new DashboardInputData(userBillsData, 1);

        interactor.execute(inputData);

        assertEquals("Bill couldn't be deleted.", outputBoundary.getLastErrorMessage(), "Failure message should be set");
    }

    @Test
    void testSwitchToSignUpView() {
        interactor.switchtoSignUpView();

        assertTrue(outputBoundary.isSignUpViewSwitched(), "Should switch to signup view");
    }

    @Test
    void testSwitchToChangePasswordView() {
        interactor.switchtoChangePasswordView();

        assertTrue(outputBoundary.isChangePasswordViewSwitched(), "Should switch to change password view");
    }

    @Test
    void testSwitchToBillView() {
        interactor.switchToBillView("test_user", 123);

        assertEquals("test_user", outputBoundary.getLastBillViewUsername(), "Username should");
    }

    @Test
    void testAddBill() {
        // Setup
        CommonUser user = (CommonUser) userFactory.create("test_user", "password123");
        userDataAccess.addUser(user);

        HashMap<Integer, String> userBillsData = new HashMap<>();

        // Call addBill method
        interactor.addBill(userBillsData, "test_user", "Test Bill");

        // Verify the new bill is added
        assertEquals(1, userDataAccess.getAddedBills().size(), "One new bill should be added");
        Bill addedBill = userDataAccess.getAddedBills().get(0);

        assertEquals("Test Bill", addedBill.getName(), "Bill name should match");
        assertTrue(userBillsData.containsKey(addedBill.getId()), "UserBillsData should include the new bill");
        assertEquals("Test Bill", userBillsData.get(addedBill.getId()), "Bill name should match in userBillsData");

        // Verify success output data
        DashboardOutputData outputData = outputBoundary.getLastSuccessData();
        assertNotNull(outputData, "Output data should not be null");
        assertEquals("Test Bill", outputData.getUserBillsData().get(addedBill.getId()), "Output data should include the new bill name");
    }

}