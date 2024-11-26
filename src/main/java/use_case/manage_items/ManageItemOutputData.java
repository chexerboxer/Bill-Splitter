package use_case.manage_items;

import entity.item.Item;
import java.util.HashMap;

/**
 * Output Data for the Manage Item Use Case.
 */
public class ManageItemOutputData {
    private final int billId;
    private final HashMap<Integer, Item> updatedItems;
    private final float newTotal;
    private final boolean isAddOperation;

    /**
     * Creates output data for the manage item use case.
     * @param billId the ID of the bill that was modified
     * @param updatedItems the updated map of items in the bill
     * @param newTotal the new total amount of the bill
     * @param isAddOperation whether this was an add operation (true) or remove operation (false)
     */
    public ManageItemOutputData(int billId, HashMap<Integer, Item> updatedItems,
                                float newTotal, boolean isAddOperation) {
        this.billId = billId;
        this.updatedItems = updatedItems;
        this.newTotal = newTotal;
        this.isAddOperation = isAddOperation;
    }

    public int getBillId() {
        return billId;
    }

    public HashMap<Integer, Item> getUpdatedItems() {
        return updatedItems;
    }

    public float getNewTotal() {
        return newTotal;
    }

    public boolean isAddOperation() {
        return isAddOperation;
    }
}