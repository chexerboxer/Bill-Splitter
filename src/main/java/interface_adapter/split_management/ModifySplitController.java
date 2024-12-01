package interface_adapter.split_management;

import use_case.split_management.distribute_bill_even.DistributeBillEvenInputBoundary;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInputData;
import use_case.split_management.modify_split.ModifySplitInputBoundary;
import use_case.split_management.modify_split.ModifySplitInputData;
import use_case.split_management.modify_split.ModifySplitInteractor;

import java.util.ArrayList;

public class ModifySplitController {

    private final ModifySplitInputBoundary modifySplitUserInteractor;


    public ModifySplitController(ModifySplitInputBoundary modifySplitUserInteractor) {
        this.modifySplitUserInteractor = modifySplitUserInteractor;
    }

    public void execute(float amountSplitted, int billId, int itemId, int userId){
        final ModifySplitInputData modifySplitInputData =
                new ModifySplitInputData(amountSplitted, billId, itemId, userId);
        modifySplitUserInteractor.execute(modifySplitInputData);
    }

}
