package interface_adapter.split_management;

import java.util.ArrayList;

import use_case.split_management.distribute_bill_even.DistributeBillEvenInputBoundary;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInputData;

public class DistributeBillController {
    private final DistributeBillEvenInputBoundary distributeBillEvenUserInteractor;

    public DistributeBillController(DistributeBillEvenInputBoundary distributeBillEvenUserInteractor) {
        this.distributeBillEvenUserInteractor = distributeBillEvenUserInteractor;
    }

    /**
     * Executes the process of distributing a bill evenly among the provided users.
     * This method creates an instance of {@link DistributeBillEvenInputData} containing the bill ID
     * and the list of users splitting the bill, and passes it to the interactor for execution.
     * @param billId The ID of the bill to be distributed.
     * @param usersSplitting A list of user IDs representing the users who will split the bill.
     */
    public void execute(int billId, ArrayList<Integer> usersSplitting) {
        final DistributeBillEvenInputData distributeBillEvenInputData =
                new DistributeBillEvenInputData(billId, usersSplitting);
        distributeBillEvenUserInteractor.execute(distributeBillEvenInputData);
    }
}
