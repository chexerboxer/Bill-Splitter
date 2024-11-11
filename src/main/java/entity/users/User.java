package entity.users;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the ID of the user.
     * @return the ID of the user.
     */
    int getId();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

}