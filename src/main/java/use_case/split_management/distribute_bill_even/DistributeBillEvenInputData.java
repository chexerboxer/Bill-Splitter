package use_case.split_management.distribute_bill_even;

import entity.users.User;

import java.util.ArrayList;

public class DistributeBillEvenInputData {

    private final ArrayList<Integer> usersSplitting;
    private final int bill_id;

    public ArrayList<Integer> getUserssplitting() {
        return usersSplitting;
    }




    public int getBill_id() {
        return bill_id;
    }


    public DistributeBillEvenInputData(int billId, ArrayList<Integer> usersSplitting) {

        bill_id = billId;

        this.usersSplitting = usersSplitting;
    }


}
