package use_case.dashboard;

import entity.bill.Bill;
import entity.users.User;

import java.util.ArrayList;

public interface DashboardUserDataAccessInterface {

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);

    /**
     * Returns the username of the current user of the application.
     * @return the username of the current user; null indicates that no one is logged into the application.
     */
    String getCurrentUsername();

    /**
     * Returns the bills associated with the user.
     * @param user the user whose bills need to be looked up
     * @return the list of bills the user is a member in
     */
    ArrayList<Bill> getUserBills(User user);

    /**
     * Deletes the bill.
     * @param billId of bill to delete
     */

    boolean removeBill(int billId);

    void addBill(Bill bill);
}
