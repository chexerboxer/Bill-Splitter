package entity.bill;

import entity.GenerateId;
import entity.item.Item;
import entity.users.User;


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

    // for creating new bills from the dashboard, where the current user is automatically added to the bill
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Bill bill = (Bill) obj;

        return this.id == bill.id
                && Float.compare(this.totalAmount, bill.totalAmount) == 0
                && this.name.equals(bill.name)
                && this.users.equals(bill.users)
                && this.items.equals(bill.items);
    }

    @Override
    public int generateId() {
        Random random = new Random();
        int idBound = END_ID_RANGE - START_ID_RANGE + 1;
        int id = random.nextInt(idBound) + START_ID_RANGE;
        return id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
        for (int i=0;i<users.size();i++){
            if (users.get(i) == userId){
                users.remove(i);
                return;
            }
        }


    }

    public void addItem(Item newItem) {
        if (items.containsKey(newItem.getId())) {
            newItem.setId(newItem.generateId());
            addItem(newItem);
        } else {
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