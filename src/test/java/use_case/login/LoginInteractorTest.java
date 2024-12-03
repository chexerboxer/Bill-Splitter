package use_case.login;

import data_access.FileDAO;
import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.item.ItemFactory;
import entity.split.SplitFactory;
import entity.users.CommonUser;
import entity.users.CommonUserFactory;
import entity.users.User;
import entity.users.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.signup.SignupOutputData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoginInteractorTest {
    private LoginInteractor interactor;
    private FileDAO userDataAccess;
    private MockOutputBoundary outputBoundary;
    private CommonUserFactory userFactory;

    @BeforeEach
    void setUp() throws IOException {
        userFactory = new CommonUserFactory();


        try {
            File newfile = new File("src/test/java/use_case/login/test.csv");
            newfile.delete();
        }catch(Exception e){

        }
        userDataAccess = new FileDAO("src/test/java/use_case/login/test.csv", new BillFactory(), new CommonUserFactory(),
                new ItemFactory(), new SplitFactory());
        outputBoundary = new MockOutputBoundary();

        interactor = new LoginInteractor(userDataAccess, outputBoundary);
    }

    @Test
    void testExecuteAccountDoesNotExist() {
        // Setup input data
        LoginInputData inputData = new LoginInputData("nonexistent_user", "password123");

        // Execute
        interactor.execute(inputData);

        // Verify fail view is prepared
        assertEquals("nonexistent_user: Account does not exist.", outputBoundary.getLastErrorMessage(),
                "Error message should indicate account does not exist");
    }

    @Test
    void testExecuteIncorrectPassword() {
        // Setup user
        CommonUser user = (CommonUser) userFactory.create("test_user", "correct_password");
        userDataAccess.addUser(user);

        // Setup input data
        LoginInputData inputData = new LoginInputData("test_user", "wrong_password");

        // Execute
        interactor.execute(inputData);

        // Verify fail view is prepared
        assertEquals("Incorrect password for \"test_user\".", outputBoundary.getLastErrorMessage(),
                "Error message should indicate incorrect password");
    }

    @Test
    void testSignupOutputData (){
        SignupOutputData outputData = new SignupOutputData("test", false);
        outputData.isUseCaseFailed();
        outputData.getUsername();
    }

    @Test
    void testExecuteSuccess() {
        // Setup user and bills
        CommonUser user = (CommonUser) userFactory.create("test_user", "password123");
        userDataAccess.addUser(user);

        ArrayList<Bill> userBills = new ArrayList<>();
        Bill bill1 = new Bill("Test Bill 1", user.getId());
        Bill bill2 = new Bill("Test Bill 2", user.getId());
        userDataAccess.addBill(bill1);
        userDataAccess.addBill(bill2);

        // Setup input data
        LoginInputData inputData = new LoginInputData("test_user", "password123");

        // Execute
        interactor.execute(inputData);

        // Verify success view is prepared
        LoginOutputData successData = outputBoundary.getLastSuccessData();

    }


    @Test
    void testExecuteSuccess2() {
        // Setup
        CommonUser user = (CommonUser) userFactory.create("test_user", "password123");
        userDataAccess.addUser(user);

        ArrayList<Bill> userBills = new ArrayList<>();
        Bill bill1 = new Bill("Test Bill 1", user.getId());
        Bill bill2 = new Bill("Test Bill 2", user.getId());
        userDataAccess.addBill(bill1);
        userDataAccess.addBill(bill2);
        LoginInputData inputData = new LoginInputData("test_user", "password123");

        // Execute
        interactor.execute(inputData);

        // Verify success
    }

    @Test
    void testExecuteFailureIncorrectPassword() {
        // Setup
        CommonUser user = (CommonUser) userFactory.create("test_user", "password123");
        userDataAccess.addUser(user);

        LoginInputData inputData = new LoginInputData("test_user", "wrong_password");

        // Execute
        interactor.execute(inputData);

        // Verify failure
    }

    @Test
    void testExecuteFailureUserNotFound() {
        LoginInputData inputData = new LoginInputData("nonexistent_user", "password123");

        // Execute
        interactor.execute(inputData);

        // Verify failure
    }

    @Test
    void testSwitchToSignUpView() {
        interactor.switchtoSignUpView();

        // Verify
        assertTrue(outputBoundary.isSignUpViewSwitched(), "SignUp view should be switched");
    }

    @Test
    void testSwitchToChangePasswordView() {
        interactor.switchtoChangePasswordView();

        // Verify
        assertTrue(outputBoundary.isChangePasswordViewSwitched(), "Change Password view should be switched");
    }

    @Test
    void testConstructorAndGetters() {
        // Setup test data
        String username = "test_user";
        HashMap<Integer, String> userBillsData = new HashMap<>();
        userBillsData.put(1, "Bill 1");
        userBillsData.put(2, "Bill 2");

        // Create LoginOutputData instance
        LoginOutputData outputData = new LoginOutputData(username, userBillsData, false);

        // Verify properties
        assertEquals(username, outputData.getUsername(), "Username should match");
        assertEquals(userBillsData, outputData.getUserBillsData(), "User bills data should match");
    }

    @Test
    void testEmptyUserBillsData() {
        // Create LoginOutputData instance with empty bills
        String username = "test_user";
        HashMap<Integer, String> emptyBills = new HashMap<>();
        LoginOutputData outputData = new LoginOutputData(username, emptyBills, false);

        // Verify properties
        assertEquals(username, outputData.getUsername(), "Username should match");
        assertTrue(outputData.getUserBillsData().isEmpty(), "User bills data should be empty");
    }
}

class MockUserDataAccess implements LoginUserDataAccessInterface {
    private final Map<String, CommonUser> users = new HashMap<>();
    private final Map<Integer, ArrayList<Bill>> userBills = new HashMap<>();
    private String currentUsername;

    void addUser(CommonUser user) {
        users.put(user.getName(), user);
    }

    void setUserBills(User user, ArrayList<Bill> bills) {
        userBills.put(user.getId(), bills);
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public ArrayList<Bill> getUserBills(User user) {
        return new ArrayList<Bill>();
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void setCurrentUsername(String username) {
        currentUsername = username;
    }

    @Override
    public void save() {
        // Mock save implementation; does nothing
    }
}

class MockOutputBoundary implements LoginOutputBoundary {
    private String lastErrorMessage;
    private LoginOutputData lastSuccessData;
    private boolean signUpViewSwitched = false;
    private boolean changePasswordViewSwitched = false;

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
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

    LoginOutputData getLastSuccessData() {
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
}
