package entity;

/**
 * Factory for creating Split objects.
 */
public class SplitFactory {
    public Split create(float amount, String billId, String itemId) {
        return new Split(amount, billId, itemId);
    }
}
