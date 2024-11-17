package entity.users;

import entity.split.Split;

import java.util.ArrayList;

/**
 * Factory for creating users.
 */
public interface UserFactory {
    /**
     * Creates a new User.
     * @param name the name of the new user
     * @param password the password of the new user
     * @return the new user
     */
    User create(String name, String password);

    /**
     * Creates a new User.
     * @param name the name of the new user
     * @param password the password of the new user
     * @param splits the list of split being intialized.
     * @return the new user
     */
    User create(String name, String password, ArrayList<Split> splits);

    /**
     * Creates a new User.
     * @param name the name of the new user
     * @param id the id of the user.
     * @param password the password of the new user
     * @param splits the list of split being intialized.
     * @return the new user
     */
    User create(String name, int id, String password, ArrayList<Split> splits);

}