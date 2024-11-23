package use_case.split_management.modify_split;

import data_access.FileDAO;

public class ModifySplitInteractor implements ModifySplitInputBoundary {
    FileDAO userDataAccessObject;

    public ModifySplitInteractor(FileDAO userDataAccessObject){
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void execute(ModifySplitInputData modifySplitInputData) {
        final float amount_splitted = modifySplitInputData.getAmount_splitted();
        final int bill_id = modifySplitInputData.getBill_id();
        final int item_id = modifySplitInputData.getItem_id();
        final int user_id = modifySplitInputData.getUser_id();


        if (!userDataAccessObject.getAllBills().containsKey(bill_id)){
            // TODO fail case
        }
        else if (!userDataAccessObject.getAllUsers().containsKey(user_id)){
            // TODO fail case
        }
        else if (!userDataAccessObject.getBill(bill_id).getItems().containsKey(item_id)){
            // TODO fail case
        }
        else if (userDataAccessObject.undistributedOnItem(item_id, bill_id) < amount_splitted){
            // TODO fail case
        }

        // The modify split interactor allows both positive and negative values.
        else if (userDataAccessObject.getUser(user_id).distributedAmount(item_id, bill_id) + amount_splitted < 0){
            // TODO fail case
        }
        else{

            userDataAccessObject.modifySplit(amount_splitted, bill_id, item_id, user_id);

        }
    }

}
