package interface_adapter.split_management;

import use_case.split_management.clear_bill.ClearBillInputBoundary;
import use_case.split_management.clear_bill.ClearBillInputData;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInputBoundary;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInputData;

import java.util.ArrayList;

public class DistributeBillController  {

    private final DistributeBillEvenInputBoundary distributeBillEvenUserInteractor;


    public DistributeBillController(DistributeBillEvenInputBoundary distributeBillEvenUserInteractor) {
        this.distributeBillEvenUserInteractor = distributeBillEvenUserInteractor;
    }

    public void execute(int billId, ArrayList<Integer> usersSplitting){
        final DistributeBillEvenInputData distributeBillEvenInputData =
                new DistributeBillEvenInputData(billId, usersSplitting);
        distributeBillEvenUserInteractor.execute(distributeBillEvenInputData);
    }
}
