package use_case.change_password;

import entity.users.User;

/**
 * The interface of the DAO for the Change Password Use Case.
 */
public interface ChangePasswordUserDataAccessInterface {

    /**
     * Updates the system to record this user's password.
     * @param user the user whose password is to be updated
     */
    void changePassword(User user);

    /**
     * Retrieves a user by their username.
     * @param username the username to search for
     * @return the user associated with the given username
     */
    User getByUsername(String username);
}
