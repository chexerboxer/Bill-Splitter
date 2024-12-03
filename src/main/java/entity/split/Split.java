package entity.split;

/**
 *  The representation of a split in our program.
 *  Splits are a specific amount of money owed by a user from one item in some bill
 */
public class Split {
    // set bounds when generating an ID so it'll always be 9 digits
    private static final int START_ID_RANGE = 100000000;
    private static final int END_ID_RANGE = 999999999;

    private float amount;
    private final int billId;
    private final int itemId;

    public Split(float amount, int billId, int itemId) {
        this.amount = amount;
        this.billId = billId;
        this.itemId = itemId;
    }

    /**
     * Compares the given object with this split for equality.
     * Two splits are considered equal if they have the same amount, bill ID, and item ID.
     *
     * @param obj The object to compare with.
     * @return true if the given object is equal to this split; false otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Split split = (Split) obj;
        return Float.compare(split.amount, amount) == 0
                && billId == split.billId
                && itemId == split.itemId;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public int getBillId() {
        return billId;
    }

    public int getItemId() {
        return itemId;
    }

}
