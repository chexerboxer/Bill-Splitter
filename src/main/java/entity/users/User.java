package entity.users;

import entity.split.Split;

import java.util.ArrayList;

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

    ArrayList<Split> getSplits();

    /**
     * @param newSplit the new split added to user.
     */
    void addSplit(Split newSplit);

    /**
     * @param oldSplit the old split removed from user.
     */
    void removeSplit(Split oldSplit);

    /**
     * Return the split amount given item and bill id
     * @param itemId is the id of the item.
     * @param billId is the id of the bill of the item.
     * @return amount of money distributed to this person on this item
     */
    float distributedAmount(int itemId, int billId);
}
