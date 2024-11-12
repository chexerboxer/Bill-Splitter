package entity.split;

import entity.GenerateId;

import java.util.Random;

/**
 *  The representation of a split in our program.
 *  Splits are a specific amount of money owed by a user from one item in some bill
 */
public class Split implements GenerateId {
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

    public void setAmount(float amount) {
        this.amount = amount;
    }
    @Override
    public int generateId() {
        Random random = new Random();
        int idBound = END_ID_RANGE - START_ID_RANGE + 1;
        int id = random.nextInt(idBound) + START_ID_RANGE;
        return id;
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