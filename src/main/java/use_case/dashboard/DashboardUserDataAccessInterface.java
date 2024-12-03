package use_case.dashboard;

import java.util.ArrayList;

import entity.bill.Bill;
import entity.users.User;

/**
 * Interface for data access related to dashboard users and their bills.
 * This interface defines the contract for accessing user information, their associated bills,
 * and performing actions such as adding or removing bills.
 */
public interface DashboardUserDataAccessInterface {

    /**
     * Returns the user with the given username.
     *
     * @param username The username to look up.
     * @return The user with the given username.
     */
    User get(String username);

    /**
     * Returns the username of the current user of the application.
     *
     * @return The username of the current user, or {@code null} if no one is logged into the application.
     */
    String getCurrentUsername();

    /**
     * Returns the bills associated with the user.
     *
     * @param user The user whose bills need to be looked up.
     * @return A list of bills the user is a member of.
     */
    ArrayList<Bill> getUserBills(User user);

    /**
     * Deletes the bill with the given bill ID.
     *
     * @param billId The ID of the bill to delete.
     * @return {@code true} if the bill was successfully removed, {@code false} otherwise.
     */
    boolean removeBill(int billId);

    /**
     * Adds a new bill to the system.
     *
     * @param bill The bill to add.
     */
    void addBill(Bill bill);
}
