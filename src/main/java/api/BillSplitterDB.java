package api;

import org.json.JSONException;

import entity.Bill;
import entity.User;

/**
 * BillSplitterDataBase interface states all methods the MongoBillSplitterDB have to implement.
 */
public interface BillSplitterDB {
    /**
     * A method that returns the Bill given id is refering to.
     * @param id is the id of the Bill.
     * @return Bill id is refering to.
     * @throws JSONException if an error occurs.
     */
    Bill getBill(int id) throws JSONException;

    /**
     * A method that returns the User given id is refering to.
     * @param id is the id of the User.
     * @return User id is refering to.
     * @throws JSONException if an error occurs.
     */
    User getUser(int id) throws JSONException;

    /**
     * A method that returns all bills in DB.
     * @return array of bills that contains all bills in DB.
     * @throws JSONException if an error occurs.
     */
    Bill[] getBills() throws JSONException;

    /**
     * A method that returns all users in DB.
     * @return array of bills that contains all users in DB.
     * @throws JSONException if an error occurs.
     */
    User[] getUsers() throws JSONException;

    /**
     * A method that adds given Bill to DB and return added bill.
     * @param bill that is being added to DB.
     * @return added bill.
     * @throws JSONException if an error occurs.
     */
    Bill addBill(Bill bill) throws JSONException;

    /**
     * A method that modify Bill with id in DB with new Bill and return new bill.
     * @param bill is the new Bill.
     * @param id id of the bill being modified.
     * @return new bill.
     * @throws JSONException if an error occurs.
     */
    Bill modifyBill(Bill bill, int id) throws JSONException;

    /**
     * A method that removes given Bill to DB.
     * @param id that is being removed to DB.
     * @throws JSONException if an error occurs.
     */
    void removeBill(int id) throws JSONException;

    /**
     * A method that adds given user to DB and return added user.
     * @param user that is being added to DB.
     * @return added user.
     * @throws JSONException if an error occurs.
     */
    User addUser(User user) throws JSONException;

    /**
     * A method that modify user with id in DB with new user and return new user.
     * @param user is the new User.
     * @param id id of the user being modified.
     * @return new user.
     * @throws JSONException if an error occurs.
     */
    User modifyUser(User user, int id) throws JSONException;

    /**
     * A method that removes given user to DB.
     * @param id that is being removed to DB.
     * @throws JSONException if an error occurs.
     */
    void removeUser(int id) throws JSONException;

}
