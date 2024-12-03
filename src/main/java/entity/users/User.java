package entity.users;

import java.util.ArrayList;

import entity.split.Split;

/**
 * The representation of a user in our program.
 * This interface defines the basic structure and operations for a user in the system,
 * including managing splits for items and bills.
 */
public interface User {

    /**
     * Returns the username of the user.
     *
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the ID of the user.
     *
     * @return the ID of the user.
     */
    int getId();

    /**
     * Returns the password of the user.
     *
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Gets the list of splits associated with the user.
     *
     * @return the list of splits associated with the user.
     */
    ArrayList<Split> getSplits();

    /**
     * Adds a new split to the user's list of splits.
     *
     * @param newSplit the new split to be added to the user.
     */
    void addSplit(Split newSplit);

    /**
     * Removes a split associated with a specific item and bill from the user's list of splits.
     *
     * @param itemId The ID of the item.
     * @param billId The ID of the bill.
     */
    void removeSplit(int itemId, int billId);

    /**
     * Modifies an existing split by changing the distributed amount for a specific item and bill.
     * If the new amount results in a non-positive value, the split is removed.
     *
     * @param amount_modified The amount by which to modify the split.
     * @param itemId          The ID of the item.
     * @param billId          The ID of the bill.
     */
    void modifySplit(float amount_modified, int itemId, int billId);

    /**
     * Returns the amount of money distributed to this user for a specific item and bill.
     *
     * @param itemId The ID of the item.
     * @param billId The ID of the bill for the item.
     * @return The amount of money distributed to the user for the specified item and bill.
     */
    float distributedAmount(int itemId, int billId);

    /**
     * Sets the password for the user.
     *
     * @param password The new password to set for the user.
     */
    void setPassword(String password);

}
