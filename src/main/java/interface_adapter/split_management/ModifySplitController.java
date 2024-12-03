package interface_adapter.split_management;

import use_case.split_management.modify_split.ModifySplitInputBoundary;
import use_case.split_management.modify_split.ModifySplitInputData;

public class ModifySplitController {

    private final ModifySplitInputBoundary modifySplitUserInteractor;

    public ModifySplitController(ModifySplitInputBoundary modifySplitUserInteractor) {
        this.modifySplitUserInteractor = modifySplitUserInteractor;
    }

    /**
     * Executes the process of modifying a split for a given bill, item, and user.
     * This method creates an instance of {@link ModifySplitInputData} containing the necessary details
     * and passes it to the interactor for execution.
     *
     * @param amountSplitted The amount that the user is modifying their share to.
     * @param billId The ID of the bill being modified.
     * @param itemId The ID of the item being modified in the split.
     * @param userId The ID of the user modifying their share of the bill.
     */
    public void execute(float amountSplitted, int billId, int itemId, int userId) {
        final ModifySplitInputData modifySplitInputData =
                new ModifySplitInputData(amountSplitted, billId, itemId, userId);
        modifySplitUserInteractor.execute(modifySplitInputData);
    }

}
