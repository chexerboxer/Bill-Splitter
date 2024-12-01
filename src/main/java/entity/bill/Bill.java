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


    public Bill(String name, int creatorId) {
        this.name = name;
        this.id = generateId();
        ArrayList<Integer> users = new ArrayList<>();
        users.add(creatorId);
        this.users = users;
    }

    public Bill(String name, int id, ArrayList<Integer> users, HashMap<Integer, Item> items, float totalAmount){
        this.name = name;
        this.id = id;
        this.users = users;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Bill(String name, int id, ArrayList<Integer> users) {
        this.name = name;
        this.id = id;
        this.users = users;

    }

    public boolean equals(Bill bill){
        return this.name.equals(bill.getName()) && this.id == bill.getId() && this.users == bill.getUsers()
                && this.items == bill.getItems() && totalAmount == bill.getTotalAmount();
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
    public ArrayList<Integer> getUsers() {return users;}
    public HashMap<Integer, Item> getItems() {
        return items;
    }
    public float getTotalAmount(){return totalAmount;}

    public void addUser(int id) {
        users.add(id);
    }
    public void removeUser (int userId) {
        users.remove(userId);
    }

    public void addItem(Item newItem) {
        // TODO: add logic so adding occurrences isn't capital sensitive
        if (items.containsKey(newItem.getId())) {
            newItem.setId(newItem.generateId());
            addItem(newItem);
        }
        // TODO**: add elif to create new items not in database with Item constructor before adding to map
        //  ?? the paramater is an item already though
        else {
            items.put(newItem.getId(), newItem);
        }
        totalAmount = totalAmount + newItem.getCost();
    }
    public void removeItem(int oldItemId) {
        Item oldItem = items.get(oldItemId);
        totalAmount = totalAmount - oldItem.getCost();
        items.remove(oldItemId);
    }

    public void setItem(int itemId, Item item){
        items.put(itemId, item);
    }

}