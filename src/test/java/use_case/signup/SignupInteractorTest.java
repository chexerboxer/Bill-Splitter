package use_case.signup;

import data_access.FileDAO;
import entity.bill.BillFactory;
import entity.item.ItemFactory;
import entity.split.SplitFactory;
import entity.users.CommonUser;
import entity.users.CommonUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

class SignupInteractorTest {
    private SignupInteractor interactor;
    private FileDAO userDataAccess;
    private MockOutputBoundary outputBoundary;
    private CommonUserFactory userFactory;

    @BeforeEach
    void setUp() throws IOException {
        userFactory = new CommonUserFactory();

        try {
            File newfile = new File("src/test/java/use_case/signup/test.csv");
            newfile.delete();
        }catch(Exception e){

        }
        userDataAccess = new FileDAO("src/test/java/use_case/signup/test.csv", new BillFactory(), new CommonUserFactory(),
                new ItemFactory(), new SplitFactory());

        outputBoundary = new MockOutputBoundary();

        interactor = new SignupInteractor(userDataAccess, outputBoundary, userFactory);
    }


    @Test
    void testExecuteSuccessfulSignup2() {
        // Execute: Sign up with valid credentials
        SignupInputData inputData = new SignupInputData("new_user", "password123", "password123");
        interactor.execute(inputData);

        // Verify: User added to data access
        assertTrue(userDataAccess.existsByName("new_user"), "New user should be added to data access.");

    }


    @Test
    void testExecuteUserAlreadyExists() {
        // Setup: Add an existing user
        userDataAccess.addUser(new CommonUser("exsting user", "test"));

        // Execute: Try signing up with the same username
        SignupInputData inputData = new SignupInputData("exsting user", "password123", "password123");
        inputData.getPassword();
        inputData.getRepeatPassword();
        interactor.execute(inputData);

     }

    @Test
    void testExecutePasswordsDoNotMatch() {
        // Execute: Sign up with mismatched passwords
        SignupInputData inputData = new SignupInputData("new_user", "password123", "password321");
        interactor.execute(inputData);

        }

    @Test
    void testExecuteSuccessfulSignup() {
        // Execute: Sign up with valid credentials
        SignupInputData inputData = new SignupInputData("new_user", "password123", "password123");
        interactor.execute(inputData);


        // Verify: User added to data access
        assertTrue(userDataAccess.existsByName("new_user"), "New user should be added to data access.");
    }

    @Test
    void testSwitchToLoginView() {
        // Execute: Switch to login view
        interactor.switchToLoginView();

        // Verify: Login view switch
        assertTrue(outputBoundary.isLoginViewSwitched(), "Login view should be switched.");
    }
}

class MockUserDataAccess implements SignupUserDataAccessInterface {
    private final Set<String> users = new HashSet<>();

    void addUser(String username) {
        users.add(username);
    }

    @Override
    public boolean existsByName(String username) {
        return users.contains(username);
    }

    @Override
    public void save() {
        // No operation for mock
    }
}

class MockOutputBoundary implements SignupOutputBoundary {
    private String lastErrorMessage;
    private SignupOutputData lastSuccessData;
    private boolean loginViewSwitched = false;

    @Override
    public void prepareSuccessView(SignupOutputData outputData) {
        lastSuccessData = outputData;
    }

    @Override
    public void prepareFailView(String errorMessage) {
        lastErrorMessage = errorMessage;
    }

    @Override
    public void switchToLoginView() {
        loginViewSwitched = true;
    }

    String getLastErrorMessage() {
        return lastErrorMessage;
    }

    SignupOutputData getLastSuccessData() {
        return lastSuccessData;
    }

    boolean isLoginViewSwitched() {
        return loginViewSwitched;
    }
}

