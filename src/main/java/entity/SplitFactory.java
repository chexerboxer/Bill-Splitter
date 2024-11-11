package entity;

/**
 * Factory for creating Split objects.
 */
public class SplitFactory {
    public Split create(float amount, int billId, int itemId) {
        return new Split(amount, billId, itemId);
    }
}
