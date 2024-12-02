package entities.item;

import entity.item.Item;
import entity.item.ItemFactory;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private final ItemFactory itemFactory = new ItemFactory();

    @Test
    void testItemCreation() {
        Item item = itemFactory.create("Test Item", 100.0f);
        assertNotNull(item);
        assertEquals("Test Item", item.getName());
        assertEquals(100.0f, item.getCost());
    }

    @Test
    void testItemCreationWithId() {
        int customId = 123456789;
        Item item = itemFactory.create("Custom ID Item", customId, 200.0f);
        assertNotNull(item);
        assertEquals("Custom ID Item", item.getName());
        assertEquals(customId, item.getId());
        assertEquals(200.0f, item.getCost());
    }

    @Test
    void testEquals() {
        int customId = 123456789;
        Item item1 = itemFactory.create("Equal Item", customId, 150.0f);
        Item item2 = itemFactory.create("Equal Item", customId, 150.0f);

        System.out.println(item1.equals(item2));
        assertTrue(item1.equals(item2), "Items with the same attributes should be equal");
    }

    @Test
    void testSetName() {
        Item item = itemFactory.create("Old Name", 50.0f);
        item.setName("New Name");
        assertEquals("New Name", item.getName());
    }

    @Test
    void testSetCost() {
        Item item = itemFactory.create("Cost Item", 75.0f);
        item.setCost(100.0f);
        assertEquals(100.0f, item.getCost());
    }
    @Test
    void testSetId() {
        Item item = itemFactory.create("Cost Item", 75.0f);

        // Capture the original ID of the item
        int originalId = item.getId();

        // Generate a new ID (simulate setting it)
        int newId = 123456789;
        item.setId(newId);

        // Assert that the ID has been updated
        assertNotEquals(originalId, item.getId()); // Ensure the ID is different
        assertEquals(newId, item.getId()); // Ensure the ID has been set correctly
    }

}
