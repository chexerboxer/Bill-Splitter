package interface_adapter.change_password;


public class ChangePasswordState {
    private String newPassword;
    private String confirmPassword;
    private String errorMessage;

    public String getNewPassword() {
        return newPassword;
    }

    public void setCurrentPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
