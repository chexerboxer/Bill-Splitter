package use_case.split_management.distribute_bill_even;

import java.util.ArrayList;

/**
 * Interface for data access related to distributing a bill evenly among users.
 * This interface defines the contract for distributing all items in a bill among a list of users.
 */
public interface DistributeBillEvenDataAccessInterface {

    /**
     * Distributes all items associated with a given bill among the specified users.
     *
     * @param billId The ID of the bill to be distributed.
     * @param usersSplitting A list of user IDs representing the users who will split the bill.
     */
    void distributeBill(int billId, ArrayList<Integer> usersSplitting);
}
