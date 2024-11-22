package use_case.modify_split;

import data_access.FileDAO;
import entity.split.SplitFactory;
import entity.users.User;

public class ModifySplitInteractor implements ModifySplitInputBoundary {
    FileDAO userDataAccessObject;

    public ModifySplitInteractor(AddSplitInputData addSplitInputData, FileDAO userDataAccessObject){
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void execute(AddSplitInputData addSplitInputData) {
        final float amount_splitted = addSplitInputData.getAmount_splitted();
        final int bill_id = addSplitInputData.getBill_id();
        final int item_id = addSplitInputData.getItem_id();
        final int user_id = addSplitInputData.getUser_id();


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
        else if (userDataAccessObject.getUser(user_id).distributedAmount(item_id, bill_id) + amount_splitted < 0){
            // TODO fail case
        }
        else{
            final User user = userDataAccessObject.getUser(user_id);
            SplitFactory splitFactory = new SplitFactory();
            user.addSplit(splitFactory.create(amount_splitted, bill_id, item_id));
            userDataAccessObject.setUser(user_id, user);

        }
    }

}
