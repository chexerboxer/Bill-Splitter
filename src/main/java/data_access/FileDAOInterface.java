package data_access;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import entity.bill.Bill;
import entity.users.User;


/**
 * DAO for Bill and User data implemented using a File to persist the data.
 */
public interface FileDAOInterface {

    /**
     * Saves the current values to the CSV file
     */
    public void save();

    /**
     * Return the User with the given id.
     * @param id is the id of the user.
     * @return User if existent, return null otherwise.
     */
    public User getUser(int id);

    /**
     * Return the Bill with the given id.
     * @param id is the id of the bill.
     * @return Bill if existent, return null otherwise.
     */
    public Bill getBill(int id);

    /**
     * Return all bills in file.
     * @return Map of all the bills with their id corresponding to the bill.
     */
    public Map<Integer, Bill> getAllBills();

    /**
     * Return all users in file.
     * @return Map of all the users with their id corresponding to the user.
     */
    public Map<Integer, User> getAllUsers();

    /**
     * Update or create user with given id with given user.
     * @param id is the id of the user.
     * @param user is the given user
     */
    public void setUser(int id, User user);

    /**
     * Update or create bill with given id with given bill.
     * @param id is the id of the bill.
     * @param bill is the given bill.
     */
    public void setBill(int id, Bill bill);

    /**
     * Update or create users with given users.
     * @param users is the final users.
     */
    public void setUsers(HashMap<Integer, User> users);

    /**
     * Update or create bill with given id with given bill.
     * @param bills is the final bills.
     */
    public void setBills(HashMap<Integer, Bill> bills);
}
