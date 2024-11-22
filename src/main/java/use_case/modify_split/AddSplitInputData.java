package use_case.modify_split;

public class AddSplitInputData {

    private final float amount_splitted;
    private final int bill_id;
    private final int item_id;
    private final int user_id;


    public float getAmount_splitted() {
        return amount_splitted;
    }

    public int getBill_id() {
        return bill_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public AddSplitInputData(float amountSplitted, int billId, int itemId, int userId) {
        amount_splitted = amountSplitted;
        bill_id = billId;
        item_id = itemId;
        user_id = userId;
    }


}
