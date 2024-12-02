package use_case.split_management.distribute_bill_even;

import data_access.FileDAO;
import use_case.split_management.SplitManagementOutputBoundary;


import java.util.ArrayList;

public class DistributeBillEvenInteractor implements DistributeBillEvenInputBoundary {
    FileDAO userDataAccessObject;
    SplitManagementOutputBoundary splitManagementPresenter;

    public DistributeBillEvenInteractor(FileDAO userDataAccessObject, SplitManagementOutputBoundary splitManagementOutputBoundary){
        this.userDataAccessObject = userDataAccessObject;
        splitManagementPresenter = splitManagementOutputBoundary;
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
            splitManagementPresenter.prepareFailView("bill not found.");

        }else if (!usersSplittingContained){
            splitManagementPresenter.prepareFailView("user not splitting this item.");

        }

        else{
            userDataAccessObject.distributeBill(bill_id, usersSplitting);

        }
    }

}
