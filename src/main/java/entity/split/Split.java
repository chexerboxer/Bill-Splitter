package entity.split;

import entity.GenerateId;
import entity.item.Item;

import java.util.Random;

/**
 *  The representation of a split in our program.
 *  Splits are a specific amount of money owed by a user from one item in some bill
 */
public class Split {
    private float amount;
    private final int billId;
    private final int itemId;

    // set bounds when generating an ID so it'll always be 9 digits
    private static final int START_ID_RANGE = 100000000;
    private static final int END_ID_RANGE = 999999999;

    public Split(float amount, int billId, int itemId) {
        this.amount = amount;
        this.billId = billId;
        this.itemId = itemId;
    }


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Split split = (Split) obj;
        return Float.compare(split.amount, amount) == 0 &&
                billId == split.billId &&
                itemId == split.itemId;
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