package entity;

import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {
    private BillFactory billFactory; // Using the factory
    private Bill bill;
    private Item mockItem;

    @BeforeEach
    void setUp() {
        billFactory = new BillFactory(); // Instantiate the factory
        mockItem = new Item("Test Item", 1, 100.0f); // Assuming the constructor Item(String name, int id, float cost)
        bill = billFactory.create("Test Bill", 123456); // Use factory to create the bill
    }

    @Test
    void testGenerateId() {
        int id = bill.generateId();
        assertTrue(id >= 100000 && id <= 999999, "Generated ID should be a 6-digit number");
    }

    @Test
    void testAddUser() {
        bill.addUser(654321);
        assertTrue(bill.getUsers().contains(654321), "User should be added to the users list");
    }

    @Test
    void testRemoveUser() {
        bill.addUser(654321);
        bill.removeUser(654321);
        assertFalse(bill.getUsers().contains(654321), "User should be removed from the users list");
    }

    @Test
    void testAddItem() {
        bill.addItem(mockItem);
        assertTrue(bill.getItems().containsKey(mockItem.getId()), "Item should be added to the bill");
        assertEquals(100.0f, bill.getTotalAmount(), "Total amount should reflect the item's cost");
    }

    @Test
    void testRemoveItem() {
        bill.addItem(mockItem);
        bill.removeItem(mockItem.getId());
        assertFalse(bill.getItems().containsKey(mockItem.getId()), "Item should be removed from the bill");
        assertEquals(0.0f, bill.getTotalAmount(), "Total amount should be updated after item removal");
    }

    @Test
    void testSetName() {
        bill.setName("Updated Bill");
        assertEquals("Updated Bill", bill.getName(), "Bill name should be updated");
    }

    @Test
    void testSetItem() {
        bill.addItem(mockItem);
        Item newItem = new Item("New Item", mockItem.getId(), 200.0f);
        bill.setItem(mockItem.getId(), newItem);
        assertEquals(200.0f, bill.getItems().get(mockItem.getId()).getCost(), "Item should be updated");
    }

    @Test
    void testEquality() {
        ArrayList<Integer> users = new ArrayList<>();
        users.add(123456);

        HashMap<Integer, Item> items = new HashMap<>();
        items.put(mockItem.getId(), mockItem);

        Bill anotherBill = billFactory.create("Test Bill", bill.getId(), users, items, 100.0f);
        bill.addItem(mockItem);

        assertTrue(bill.equals(anotherBill), "Bills with identical attributes should be equal");
    }
}
