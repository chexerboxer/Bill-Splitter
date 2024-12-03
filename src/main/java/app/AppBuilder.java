package app;

import java.awt.CardLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.FileDAO;
import entity.bill.BillFactory;
import entity.item.ItemFactory;
import entity.split.SplitFactory;
import entity.users.CommonUserFactory;
import entity.users.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.bill_splitter.BillDisplayPresenter;
import interface_adapter.bill_splitter.BillDisplayViewModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordPresenter;
import interface_adapter.change_password.ChangePasswordViewModel;
import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardPresenter;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.split_management.ClearBillController;
import interface_adapter.split_management.DistributeBillController;
import interface_adapter.split_management.ModifySplitController;
import interface_adapter.split_management.SplitManagementPresenter;
import interface_adapter.upload_receipt.UploadReceiptController;
import interface_adapter.upload_receipt.UploadReceiptPresenter;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInteractor;
import use_case.dashboard.DashboardOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.split_management.SplitManagementOutputBoundary;
import use_case.split_management.clear_bill.ClearBillInputBoundary;
import use_case.split_management.clear_bill.ClearBillInteractor;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInputBoundary;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInteractor;
import use_case.split_management.modify_split.ModifySplitInputBoundary;
import use_case.split_management.modify_split.ModifySplitInteractor;
import use_case.upload_receipt.UploadReceiptInputBoundary;
import use_case.upload_receipt.UploadReceiptInteractor;
import use_case.upload_receipt.UploadReceiptOutputBoundary;
import view.BillDisplayView;
import view.ChangePasswordView;
import view.DashboardView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

/**
 * The AppBuilder class to initialize all the views and controllers needed for the program.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    // thought question: is the hard dependency below a problem?
    private final UserFactory userFactory = new CommonUserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // thought question: is the hard dependency below a problem?
    private final BillFactory billFactory = new BillFactory();
    private final ItemFactory itemFactory = new ItemFactory();
    private final SplitFactory splitFactory = new SplitFactory();
    private final String filePath = "src/main/java/data_access/test.csv";

    private final FileDAO userDataAccessObject = new FileDAO(filePath,
            billFactory,
            userFactory,
            itemFactory,
            splitFactory);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private DashboardViewModel dashboardViewModel;
    private DashboardView dashboardView;
    private LoginView loginView;
    private ChangePasswordView changePasswordView;
    private ChangePasswordViewModel changePasswordViewModel;
    private BillDisplayView billDisplayView;
    private BillDisplayViewModel billDisplayViewModel;

    public AppBuilder() throws IOException {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the Signup View to the application.
     * @return this builder
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    /**
     * Adds the Login View to the application.
     * @return this builder
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Adds the Change Password View to the application, a unique view for the usecase when a user first opens the app.
     * @return this builder
     */
    public AppBuilder addChangePasswordView() {
        changePasswordViewModel = new ChangePasswordViewModel();
        changePasswordView = new ChangePasswordView(changePasswordViewModel);
        cardPanel.add(changePasswordView, changePasswordView.getViewName());
        return this;
    }

    /**
     * Adds the Dashboard View to the application.
     * @return this builder
     */
    public AppBuilder addDashboardView() {
        dashboardViewModel = new DashboardViewModel();
        dashboardView = new DashboardView(dashboardViewModel);
        cardPanel.add(dashboardView, dashboardView.getViewName());
        return this;
    }

    /**
     * Adds the Bill Display View to the application.
     * @return this builder
     */
    public AppBuilder addBillDisplayView() {
        billDisplayViewModel = new BillDisplayViewModel();

        billDisplayView = new BillDisplayView(billDisplayViewModel);
        billDisplayView.setDAO(userDataAccessObject);
        cardPanel.add(billDisplayView, billDisplayView.getViewName());
        return this;
    }

    /**
     * Adds the Signup Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                dashboardViewModel, loginViewModel, signupViewModel, changePasswordViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        final LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Adds the Change Password Use Case to the application.
     * @return this builder
     */
    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary =
                new ChangePasswordPresenter(
                        viewManagerModel, changePasswordViewModel, loginViewModel);

        final ChangePasswordPresenter changePasswordPresenter =
                new ChangePasswordPresenter(
                        viewManagerModel, changePasswordViewModel, loginViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory,
                        changePasswordPresenter);

        final ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);
        changePasswordView.setChangePasswordController(changePasswordController);
        dashboardView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                dashboardViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        dashboardView.setLogoutController(logoutController);
        return this;
    }

    /**
     * Adds the Dashboard Use Cases to the application.
     * @return this builder
     */
    public AppBuilder addDashboardUseCase() {
        final DashboardOutputBoundary dashboardOutputBoundary = new DashboardPresenter(viewManagerModel,
                dashboardViewModel,
                billDisplayViewModel,
                signupViewModel,
                changePasswordViewModel);
        final DashboardInputBoundary dashboardInteractor = new DashboardInteractor(
                userDataAccessObject, dashboardOutputBoundary);

        final DashboardController dashboardController = new DashboardController(dashboardInteractor);
        dashboardView.setDashboardController(dashboardController);
        return this;
    }

    /**
     * Adds the Bill Display Use Cases to the application.
     * @return this builder
     */
    public AppBuilder addBillDisplayUseCase() {
        // set up controllers
        final UploadReceiptOutputBoundary uploadReceiptOutputBoundary = new UploadReceiptPresenter();

        final UploadReceiptInputBoundary uploadReceiptInteractor =
                new UploadReceiptInteractor(userDataAccessObject, uploadReceiptOutputBoundary);

        final UploadReceiptController uploadReceiptController1 = new UploadReceiptController(uploadReceiptInteractor);

        final SplitManagementOutputBoundary splitManagementOutputBoundary = new SplitManagementPresenter();

        final ClearBillInputBoundary clearBillInteractor =
                new ClearBillInteractor(userDataAccessObject, splitManagementOutputBoundary);

        final ClearBillController clearBillController1 = new ClearBillController(clearBillInteractor);

        final DistributeBillEvenInputBoundary distributeBillInteractor =
                new DistributeBillEvenInteractor(userDataAccessObject, splitManagementOutputBoundary);

        final DistributeBillController distributeBillController1 = new DistributeBillController(
                distributeBillInteractor
        );

        final ModifySplitInputBoundary modifySplitInterator =
                new ModifySplitInteractor(userDataAccessObject, splitManagementOutputBoundary);

        final ModifySplitController modifySplitController1 = new ModifySplitController(modifySplitInterator);

        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                dashboardViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);

        final ChangePasswordOutputBoundary changePasswordOutputBoundary =
                new ChangePasswordPresenter(
                        viewManagerModel, changePasswordViewModel, loginViewModel);

        final ChangePasswordPresenter changePasswordPresenter =
                new ChangePasswordPresenter(
                        viewManagerModel, changePasswordViewModel, loginViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory,
                        changePasswordPresenter);

        final ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);

        final BillDisplayPresenter billDisplayPresenter = new BillDisplayPresenter(viewManagerModel,
                dashboardViewModel, userDataAccessObject);

        billDisplayView.setBillDisplayPresenter(billDisplayPresenter);

        billDisplayView.setChangePasswordController(changePasswordController);
        billDisplayView.setLogoutController(logoutController);
        billDisplayView.setClearBillController(clearBillController1);
        billDisplayView.setDistributeBillController(distributeBillController1);
        billDisplayView.setModifySplitController(modifySplitController1);
        billDisplayView.setUploadReceiptController(uploadReceiptController1);

        return this;
    }

    /**
     * Creates the JFrame for the application and initially sets the SignupView to be displayed.
     * @return the application
     */
    public JFrame build() {
        final JFrame application = new JFrame("Bill Splitter");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
