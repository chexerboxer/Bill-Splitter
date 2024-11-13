package interface_adapter.change_password;

import interface_adapter.ViewModel;

/**
 * The View Model for the ChangePassword View.
 */
public class ChangePasswordViewModel extends ViewModel<ChangePasswordState> {

    public ChangePasswordViewModel() {
        super("Change Password");
        setState(new ChangePasswordState());
    }
}
