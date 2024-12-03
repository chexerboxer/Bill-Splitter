package entity.bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import entity.GenerateId;
import entity.item.Item;

/**
 *  The representation of a bill in our program.
 */
public class Bill implements GenerateId {

    // set bounds when generating an ID so it'll always be 6 digits
    private static final int START_ID_RANGE = 100000;
    private static final int END_ID_RANGE = 999999;

    private String name;
    private final int id;
    private ArrayList<Integer> users = new ArrayList<>();
    private HashMap<Integer, Item> items = new HashMap<>();
    private float totalAmount;

    public Bill(String name) {
        this.name = name;
        this.id = generateId();
    }

    public Bill(String name, int creatorId) {
        this.name = name;
        this.id = generateId();
        final ArrayList<Integer> users = new ArrayList<>();
        users.add(creatorId);
        this.users = users;
    }

    public Bill(String name, int id, ArrayList<Integer> users, HashMap<Integer, Item> items, float totalAmount) {
        this.name = name;
        this.id = id;
        this.users = users;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Bill bill = (Bill) obj;

        // Check fields for equality
        return this.id == bill.id
                && Float.compare(this.totalAmount, bill.totalAmount) == 0
                && this.name.equals(bill.name)
                // Compares contents of the lists
                && this.users.equals(bill.users)
                // Compares contents of the map
                && this.items.equals(bill.items);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int generateId() {
        final Random random = new Random();
        final int idBound = END_ID_RANGE - START_ID_RANGE + 1;
        return random.nextInt(idBound) + START_ID_RANGE;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getUsers() {
        return users;
    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    /**
     * Adds a user to the bill if not already present.
     *
     * @param id The user ID to add.
     */
    public void addUser(int id) {
        // Prevent duplicate users
        if (!users.contains(id)) {
            users.add(id);
        }
    }

    /**
     * Removes a user from the bill.
     *
     * @param userId The user ID to remove.
     */
    public void removeUser(int userId) {
        // Simplified and handles non-existent users gracefully
        users.removeIf(user -> user == userId);
    }

    /**
     * Adds a new item to the bill. If the item already exists, a new ID is generated for it.
     * The total amount of the bill is updated accordingly.
     *
     * @param newItem The new item to add.
     * @throws IllegalArgumentException If the item is null.
     */
    public void addItem(Item newItem) {
        if (newItem == null) {
            // Optional: You can also silently return or log
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (items.containsKey(newItem.getId())) {
            // Generate a new ID and re-add the item
            newItem.setId(newItem.generateId());
            addItem(newItem);
        }
        else {
            // Add the new item and update the total amount
            items.put(newItem.getId(), newItem);
            totalAmount += newItem.getCost();
        }
    }

    /**
     * Removes an item from the bill by its ID. The total amount is updated accordingly.
     *
     * @param oldItemId The ID of the item to remove.
     */
    public void removeItem(int oldItemId) {
        final Item oldItem = items.get(oldItemId);
        // Check if the item exists
        if (oldItem != null) {
            totalAmount -= oldItem.getCost();
            items.remove(oldItemId);
        }
    }

    /**
     * Sets an item in the bill by its ID.
     *
     * @param itemId The ID of the item to set.
     * @param item The new item to associate with the given ID.
     */
    public void setItem(int itemId, Item item) {
        items.put(itemId, item);
    }
}
