package use_case.manage_items;

/**
 * The Input Data for the Manage Item Use Case.
 */
public class ManageItemInputData {
    private final int billId;
    private final String itemName;
    private final float itemCost;
    private final boolean isAddOperation;
    private final int itemId;

    /**
     * Constructor for adding a new item.
     * @param billId the ID of the bill to add the item to
     * @param itemName the name of the item to add
     * @param itemCost the cost of the item to add
     */
    public ManageItemInputData(int billId, String itemName, float itemCost) {
        this.billId = billId;
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.isAddOperation = true;
        this.itemId = 0;
    }

    /**
     * Constructor for removing an existing item.
     * @param billId the ID of the bill to remove the item from
     * @param itemId the ID of the item to remove
     */
    public ManageItemInputData(int billId, int itemId) {
        this.billId = billId;
        this.itemId = itemId;
        this.isAddOperation = false;
        this.itemName = null;
        this.itemCost = 0;
    }

    public int getBillId() {
        return billId;
    }

    public String getItemName() {
        return itemName;
    }

    public float getItemCost() {
        return itemCost;
    }

    public boolean isAddOperation() {
        return isAddOperation;
    }

    public int getItemId() {
        return itemId;
    }
}