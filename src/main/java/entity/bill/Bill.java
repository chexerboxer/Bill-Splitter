package entity.bill;

import entity.GenerateId;
import entity.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *  The representation of a bill in our program.
 */
public class Bill implements GenerateId {

    private String name;
    private final int id;
    private ArrayList<Integer> users = new ArrayList<>();
    private HashMap<Integer, Item> items = new HashMap<>();
    private float totalAmount = 0;

    // set bounds when generating an ID so it'll always be 6 digits
    private static final int START_ID_RANGE = 100000;
    private static final int END_ID_RANGE = 999999;

    public Bill(String name) {
        this.name = name;
        this.id = generateId();
    }

    public Bill(String name, int creatorId) {
        this.name = name;
        this.id = generateId();
        ArrayList<Integer> users = new ArrayList<>();
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Bill bill = (Bill) obj;

        // Check fields for equality
        return this.id == bill.id
                && Float.compare(this.totalAmount, bill.totalAmount) == 0
                && this.name.equals(bill.name)
                && this.users.equals(bill.users) // Compares contents of the lists
                && this.items.equals(bill.items); // Compares contents of the map
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int generateId() {
        Random random = new Random();
        int idBound = END_ID_RANGE - START_ID_RANGE + 1;
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

    public void addUser(int id) {
        if (!users.contains(id)) { // Prevent duplicate users
            users.add(id);
        }
    }

    public void removeUser(int userId) {
        users.removeIf(user -> user == userId); // Simplified and handles non-existent users gracefully
    }

    public void addItem(Item newItem) {
        if (newItem == null) {
            throw new IllegalArgumentException("Item cannot be null"); // Optional: You can also silently return or log
        }

        if (items.containsKey(newItem.getId())) {
            // Generate a new ID and re-add the item
            newItem.setId(newItem.generateId());
            addItem(newItem);
        } else {
            // Add the new item and update the total amount
            items.put(newItem.getId(), newItem);
            totalAmount += newItem.getCost();
        }
    }



    public void removeItem(int oldItemId) {
        Item oldItem = items.get(oldItemId);
        if (oldItem != null) { // Check if the item exists
            totalAmount -= oldItem.getCost();
            items.remove(oldItemId);
        }
    }

    public void setItem(int itemId, Item item) {
        items.put(itemId, item);
    }
}
