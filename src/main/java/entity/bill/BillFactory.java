package entity.bill;

import entity.item.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Factory for creating Bill objects.
 */
public class BillFactory {

    public Bill create(String name) {
        return new Bill(name);
    }

    public Bill create(String name, int id, ArrayList<Integer> users, HashMap<Integer, Item> items, float totalAmount){
        return new Bill(name, id, users, items, totalAmount);
    }
}