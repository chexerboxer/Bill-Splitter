package use_case.split_management.distribute_bill_even;


import entity.users.User;

import java.util.ArrayList;

public interface DistributeBillEvenDataAccessInterface {

    /**
     * distribute all items associated with this bill among given users.
     * @param billId the id of the bill of the item
     */
    void distributeBill(int billId, ArrayList<Integer> usersSplitting);
}
