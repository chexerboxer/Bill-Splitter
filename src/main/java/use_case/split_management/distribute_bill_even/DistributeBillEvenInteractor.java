package use_case.split_management.distribute_bill_even;

import data_access.FileDAO;
import entity.users.User;

import java.util.ArrayList;

public class DistributeBillEvenInteractor implements DistributeBillEvenInputBoundary {
    FileDAO userDataAccessObject;

    public DistributeBillEvenInteractor(FileDAO userDataAccessObject){
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void execute(DistributeBillEvenInputData distributeBillEvenInputData) {

        final int bill_id = distributeBillEvenInputData.getBill_id();
        final ArrayList<Integer> usersSplitting = distributeBillEvenInputData.getUserssplitting();

        // check whether usersSplitting is a subset of existing users.
        boolean usersSplittingContained = true;

        for (int user : usersSplitting){
            if (!userDataAccessObject.getAllUsers().containsKey(user)){
                usersSplittingContained = false;
                break;
            }
        }
        if (!userDataAccessObject.getAllBills().containsKey(bill_id)){
            // TODO fail case

        }else if (!usersSplittingContained){
            // TODO fail case

        }

        else{
            userDataAccessObject.distributeBill(bill_id, usersSplitting);

        }
    }

}
