package entity.item;

/**
 * Factory class for creating {@link Item} objects.
 * This class provides methods to create instances of the Item class with various parameters.
 */
public class ItemFactory {

    /**
     * Creates a new {@link Item} with the specified name and cost.
     * A unique ID will be generated for the new item.
     *
     * @param name The name of the item.
     * @param cost The cost of the item.
     * @return A new {@link Item} object with the specified name, cost, and a generated ID.
     */
    public Item create(String name, float cost) {
        return new Item(name, cost);
    }

    /**
     * Creates a new {@link Item} with the specified name, ID, and cost.
     *
     * @param name The name of the item.
     * @param id The unique ID of the item.
     * @param cost The cost of the item.
     * @return A new {@link Item} object with the specified name, ID, and cost.
     */
    public Item create(String name, int id, float cost) {
        return new Item(name, id, cost);
    }
}
