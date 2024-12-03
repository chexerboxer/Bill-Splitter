package use_case.split_management.modify_split;

import data_access.FileDAO;
import use_case.split_management.SplitManagementOutputBoundary;

public class ModifySplitInteractor implements ModifySplitInputBoundary {
    private final FileDAO userDataAccessObject;
    private final SplitManagementOutputBoundary splitManagementPresenter;

    public ModifySplitInteractor(FileDAO userDataAccessObject,
                                 SplitManagementOutputBoundary splitManagementOutputBoundary) {
        this.userDataAccessObject = userDataAccessObject;
        this.splitManagementPresenter = splitManagementOutputBoundary;
    }

    @Override
    public void execute(ModifySplitInputData modifySplitInputData) {
        final float amount_splitted = modifySplitInputData.getAmount_splitted();
        final int bill_id = modifySplitInputData.getBill_id();
        final int item_id = modifySplitInputData.getItem_id();
        final int user_id = modifySplitInputData.getUser_id();

        if (!userDataAccessObject.getAllBills().containsKey(bill_id)) {
            splitManagementPresenter.prepareFailView("bill not found.");
        }
        else if (!userDataAccessObject.getBill(bill_id).getUsers().contains(user_id)) {
            splitManagementPresenter.prepareFailView("user not in bill.");
        }
        else if (!userDataAccessObject.getBill(bill_id).getItems().containsKey(item_id)) {
            splitManagementPresenter.prepareFailView("item not in bill.");
        }
        else if (userDataAccessObject.undistributedOnItem(item_id, bill_id) < amount_splitted) {
            splitManagementPresenter.prepareFailView("not enough left on the item to split.");
        }
        else {

            userDataAccessObject.modifySplit(amount_splitted, bill_id, item_id, user_id);

        }
    }

}
