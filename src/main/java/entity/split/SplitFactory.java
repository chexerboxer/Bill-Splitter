package entity.split;

/**
 * Factory class for creating {@link Split} objects.
 * This class provides a method to create instances of the {@link Split} class.
 */
public class SplitFactory {

    /**
     * Creates a new {@link Split} with the specified amount, bill ID, and item ID.
     *
     * @param amount The amount of money owed by the user for the item.
     * @param billId The ID of the bill that this split belongs to.
     * @param itemId The ID of the item that this split corresponds to.
     * @return A new {@link Split} object with the specified amount, bill ID, and item ID.
     */
    public Split create(float amount, int billId, int itemId) {
        return new Split(amount, billId, itemId);
    }
}
