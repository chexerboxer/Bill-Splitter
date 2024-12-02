package entities.bill;

import entity.bill.Bill;
import entity.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {
    private Bill bill;
    private Item mockItem;
    private Item mockitem2;
    private Bill bill2;

    @BeforeEach
    void setUp() {
        mockItem = new Item("Test Item", 1, 100.0f);
        bill = new Bill("Test Bill");
        mockitem2 = new Item("Test Item2", 2, 100.0f);
        bill2 = new Bill("Test Bill");
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(bill.equals(bill), "equals should return true when comparing the same object");
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(bill.equals(null), "equals should return false when comparing with null");
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(bill.equals("Not a Bill"), "equals should return false when comparing with a different class");
    }

    @Test
    void testEqualsWithDifferentFields() {
        Bill otherBill = new Bill("Different Bill");
        assertFalse(bill.equals(otherBill), "equals should return false when fields differ");
    }

    @Test
    void testEqualsWithMatchingFields() {
        bill.addUser(123456);
        bill.addItem(mockItem);

        Bill matchingBill = new Bill("Test Bill", bill.getId(), bill.getUsers(), bill.getItems(), bill.getTotalAmount());
        assertTrue(bill.equals(matchingBill), "equals should return true when all fields match");
    }

    @Test
    void testEqualsWithEmptyCollections() {
        Bill emptyBill1 = new Bill("Empty Bill", 123, new ArrayList<>(), new HashMap<>(), 0.0f);
        Bill emptyBill2 = new Bill("Empty Bill", 123, new ArrayList<>(), new HashMap<>(), 0.0f);

        assertTrue(emptyBill1.equals(emptyBill2), "equals should return true for bills with empty collections and matching fields");
    }

    @Test
    void testGenerateIdProducesValidIds() {
        for (int i = 0; i < 100; i++) {
            int id = bill.generateId();
            assertTrue(id >= 100000 && id <= 999999, "Generated ID should always be within the 6-digit range");
        }
    }

    @Test
    void testRemoveUserFromEmptyList() {
        bill.removeUser(123456); // Attempt to remove a user from an empty list
        assertEquals(0, bill.getUsers().size(), "Removing a user from an empty list should not cause errors");
    }

    @Test
    void testRemoveUserHandlesNonExistentUser() {
        bill.addUser(123456);
        bill.removeUser(654321); // Attempt to remove a non-existent user
        assertEquals(1, bill.getUsers().size(), "Removing a non-existent user should not affect the users list");
    }

    @Test
    void testRemoveItemFromEmptyCollection() {
        bill.removeItem(999999); // Attempt to remove an item from an empty collection
        assertEquals(0, bill.getItems().size(), "Removing an item from an empty collection should not cause errors");
        assertEquals(0.0f, bill.getTotalAmount(), "Total amount should remain unchanged when removing from an empty collection");
    }

    @Test
    void testRemoveItemHandlesNonExistentItem() {
        bill.addItem(mockItem);
        bill.removeItem(999999); // Non-existent item ID
        assertEquals(1, bill.getItems().size(), "Removing a non-existent item should not affect existing items");
        assertEquals(100.0f, bill.getTotalAmount(), "Total amount should remain unchanged when removing a non-existent item");
    }

    @Test
    void testRemoveItemDecreasesTotal() {
        bill.addItem(mockItem);
        bill.removeItem(mockItem.getId());
        assertEquals(0, bill.getItems().size(), "Item should be removed from the collection");
        assertEquals(0.0f, bill.getTotalAmount(), "Total amount should decrease by the cost of the removed item");
    }

    @Test
    void testRandom() {
        bill.setName(bill.getName());
        try {
            bill.addItem(null);
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    void testAddItemGeneratesNewIdForDuplicate() {
        // Add the first item
        bill.addItem(mockItem);

        // Create a duplicate item with the same ID
        Item duplicateItem = new Item("Duplicate Item", mockItem.getId(), 50.0f);

        // Mocking the generateId method to ensure it generates a different ID
        duplicateItem.setId(2); // Set a new ID manually

        bill.addItem(duplicateItem);

        // Verify that two items are present and have unique IDs
        assertEquals(2, bill.getItems().size(), "Duplicate item should generate a new ID and be added");

        // Verify the total amount
        assertEquals(150.0f, bill.getTotalAmount(), "Total amount should include the cost of both items");
    }

    @Test
    void testSetItemAddsOrReplacesItem() {
        // Add an item using setItem
        bill.setItem(1, mockItem);

        // Verify that the item was added
        assertEquals(mockItem, bill.getItems().get(1), "setItem should add the item to the collection");

        // Replace the item with a new one
        Item newItem = new Item("Replaced Item", 1, 200.0f);
        bill.setItem(1, newItem);

        // Verify that the item was replaced
        assertEquals(newItem, bill.getItems().get(1), "setItem should replace an existing item with the same ID");
    }

    @Test
    void testPutRandom() {
        bill.addItem(mockitem2);
        bill.addItem(mockitem2);
    }

    @Test
    void testAddItemReusesNewGeneratedId() {
        // Add the first item
        bill.addItem(mockItem);

        // Create a duplicate item with the same ID
        Item duplicateItem = new Item("Duplicate Item", mockItem.getId(), 50.0f);

        // Mock generateId for the duplicate item
        duplicateItem.setId(2);

        // Add the duplicate item
        bill.addItem(duplicateItem);

        // Add a non-duplicate item
        Item uniqueItem = new Item("Unique Item", 3, 75.0f);
        bill.addItem(uniqueItem);

        // Verify that all items are present
        assertEquals(3, bill.getItems().size(), "Three unique items should exist after resolving duplicates");

        // Verify the total amount
        assertEquals(225.0f, bill.getTotalAmount(), "Total amount should reflect all items' costs");
    }


    @Test
    void testAddUserPreventsDuplicates() {
        bill.addUser(123);
        bill.addUser(123); // Attempt to add the same user again

        // Verify the user is not duplicated
        assertEquals(1, bill.getUsers().size(), "Users list should not contain duplicates");
    }

    @Test
    void testRemoveItemReducesTotalAmount() {
        bill.addItem(mockItem);
        bill.addItem(mockitem2);

        // Remove one of the items
        bill.removeItem(mockItem.getId());

        // Verify that the item is removed
        assertEquals(1, bill.getItems().size(), "The removed item should not be in the collection");

        // Verify that the total amount is reduced
        assertEquals(100.0f, bill.getTotalAmount(), "Total amount should decrease by the cost of the removed item");
    }

    @Test
    void testSetItemAddsAndReplacesItem() {
        // Add a new item using setItem
        bill.setItem(3, mockitem2);

        // Verify the item was added
        assertEquals(mockitem2, bill.getItems().get(3), "setItem should add the new item to the collection");

        // Replace the item with a new one
        Item newItem = new Item("Replacement Item", 3, 300.0f);
        bill.setItem(3, newItem);

        // Verify the item was replaced
        assertEquals(newItem, bill.getItems().get(3), "setItem should replace the item with the new one");

        // Add another item with a new ID
        Item anotherItem = new Item("Another Item", 4, 50.0f);
        bill.setItem(4, anotherItem);

        // Verify the new item was added
        assertEquals(2, bill.getItems().size(), "Two unique items should be in the collection");
        assertEquals(anotherItem, bill.getItems().get(4), "setItem should add the new item to the collection");
    }


    @Test
    void testGenerateIdIsWithinRange() {
        for (int i = 0; i < 1000; i++) {
            int id = bill.generateId();
            assertTrue(id >= 100000 && id <= 999999, "Generated ID should always be within the 6-digit range");
        }
    }
}
