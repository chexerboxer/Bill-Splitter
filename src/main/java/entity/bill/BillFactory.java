package entity.bill;

import java.util.ArrayList;
import java.util.HashMap;

import entity.item.Item;

/**
 * Factory for creating Bill objects.
 */
public class BillFactory {

    /**
     * Creates a new Bill with the specified name.
     * A unique ID will be generated for the new bill.
     *
     * @param name The name of the bill.
     * @return A new Bill object with the specified name and a generated ID.
     */
    public Bill create(String name) {
        return new Bill(name);
    }

    /**
     * Creates a new Bill with the specified name and the creator's user ID.
     * A unique ID will be generated for the new bill.
     *
     * @param name The name of the bill.
     * @param creatorId The user ID of the bill's creator.
     * @return A new Bill object with the specified name, creator ID, and a generated ID.
     */
    public Bill create(String name, int creatorId) {
        return new Bill(name, creatorId);
    }

    /**
     * Creates a new Bill with the specified parameters.
     * This constructor allows the creation of a Bill with a predefined ID, list of users,
     * items, and total amount.
     *
     * @param name The name of the bill.
     * @param id The unique ID of the bill.
     * @param users A list of user IDs associated with the bill.
     * @param items A map of items associated with the bill.
     * @param totalAmount The total amount of the bill.
     * @return A new Bill object with the specified parameters.
     */
    public Bill create(String name, int id, ArrayList<Integer> users, HashMap<Integer, Item> items, float totalAmount) {
        return new Bill(name, id, users, items, totalAmount);
    }

}
