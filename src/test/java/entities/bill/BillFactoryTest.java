package entities.bill;

import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BillFactoryTest {
    private BillFactory billFactory;
    private Item mockItem;

    @BeforeEach
    void setUp() {
        billFactory = new BillFactory();
        mockItem = new Item("Test Item", 1, 100.0f);
    }

    @Test
    void testCreateBillWithName() {
        Bill bill = billFactory.create("Test Bill");
        assertNotNull(bill);
        assertEquals("Test Bill", bill.getName(), "Bill name should match the input name");
    }

    @Test
    void testCreateBillWithNameAndCreatorId() {
        Bill bill = billFactory.create("Test Bill", 123456);
        assertNotNull(bill);
        assertEquals("Test Bill", bill.getName());
        assertTrue(bill.getUsers().contains(123456), "Creator ID should be added to the users list");
    }

    @Test
    void testCreateBillWithAllParameters() {
        ArrayList<Integer> users = new ArrayList<>();
        users.add(123456);
        HashMap<Integer, Item> items = new HashMap<>();
        items.put(mockItem.getId(), mockItem);

        Bill bill = billFactory.create("Full Bill", 654321, users, items, 200.0f);
        assertNotNull(bill);
        assertEquals("Full Bill", bill.getName());
        assertEquals(654321, bill.getId());
        assertEquals(users, bill.getUsers());
        assertEquals(items, bill.getItems());
        assertEquals(200.0f, bill.getTotalAmount(), "Total amount should match the input total");
    }
}
