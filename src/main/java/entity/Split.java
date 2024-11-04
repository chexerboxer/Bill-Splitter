package entity;

/**
 *  The representation of a split in our program.
 *  Splits are a specific amount of money owed by a user from one item in some bill
 */
public class Split {
    private float amount;
    private final String billId;
    private final String itemId;

    public Split(float amount, String billId, String itemId) {
        this.amount = amount;
        this.billId = billId;
        this.itemId = itemId;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public String getBillId() {
        return billId;
    }

    public String getItemId() {
        return itemId;
    }
}
