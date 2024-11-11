package entity.bill;

import entity.GenerateId;
import entity.item.Item;
import entity.users.User;

import java.util.HashMap;
import java.util.Random;

/**
 *  The representation of a bill in our program.
 */
public class Bill implements GenerateId {

    private String name;
    private final int id;
    private HashMap<Integer, User> users = new HashMap<>();
    private HashMap<Item, Integer> items = new HashMap<>();
    private float totalAmount = 0;

    // set bounds when generating an ID so it'll always be 6 digits
    private static final int START_ID_RANGE = 100000;
    private static final int END_ID_RANGE = 999999;


    public Bill(String name) {
        this.name = name;
        this.id = generateId();
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int generateId() {
        Random random = new Random();
        int idBound = END_ID_RANGE - START_ID_RANGE + 1;
        int id = random.nextInt(idBound) + START_ID_RANGE;
        return id;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public HashMap<Integer, User> getUsers() {
        return users;
    }
    public HashMap<Item, Integer> getItems() {
        return items;
    }

    public void addUser(User newUser) {
        users.put(newUser.getId(), newUser);
    }
    public void removeUser (User oldUser) {
        users.remove(oldUser.getId());
    }

    public void addItem(Item newItem) {
        // TODO: add logic so adding occurrences isn't capital sensitive
        if (items.containsKey(newItem)) {
            items.put(newItem, items.get(newItem) + 1);
        }
        // TODO**: add elif to create new items not in database with Item constructor before adding to map
        else {
            items.put(newItem, 1);
        }
        totalAmount = totalAmount + newItem.getCost();
    }
    public void removeItem(Item oldItem) {
        items.remove(oldItem);
        totalAmount = totalAmount - oldItem.getCost();
    }



}