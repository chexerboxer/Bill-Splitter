package use_case.change_password;

import entity.users.User;
import entity.users.UserFactory;
import interface_adapter.change_password.ChangePasswordPresenter;

/**
 * The Change Password Interactor.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private final ChangePasswordPresenter presenter;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
                                    ChangePasswordOutputBoundary changePasswordOutputBoundary,
                                    UserFactory userFactory,
                                    ChangePasswordPresenter presenter) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
        this.userFactory = userFactory;
        this.presenter = presenter;
    }

    @Override
    public boolean execute(ChangePasswordInputData changePasswordInputData) {
        final User user = userDataAccessObject.get(changePasswordInputData.getUsername());

        if (user == null) {
            userPresenter.prepareFailView("User not found");
            return false;
        }

        user.setPassword(changePasswordInputData.getPassword());
        userDataAccessObject.changePassword(user);

        final ChangePasswordOutputData changePasswordOutputData = new ChangePasswordOutputData(user.getName(),
                                                                                  false);
        userPresenter.prepareSuccessView(changePasswordOutputData);
        return true;
    }

    @Override
    public void switchToLoginView() {
        presenter.switchToLoginView();
    }
}
