package use_case.split_management.distribute_bill_even;

import java.util.ArrayList;

import data_access.FileDAO;
import use_case.split_management.SplitManagementOutputBoundary;

public class DistributeBillEvenInteractor implements DistributeBillEvenInputBoundary {
    private final FileDAO userDataAccessObject;
    private final SplitManagementOutputBoundary splitManagementPresenter;

    public DistributeBillEvenInteractor(FileDAO userDataAccessObject,
                                        SplitManagementOutputBoundary splitManagementOutputBoundary) {
        this.userDataAccessObject = userDataAccessObject;
        splitManagementPresenter = splitManagementOutputBoundary;
    }

    @Override
    public void execute(DistributeBillEvenInputData distributeBillEvenInputData) {

        final int bill_id = distributeBillEvenInputData.getBill_id();
        final ArrayList<Integer> usersSplitting = distributeBillEvenInputData.getUserssplitting();

        if (!userDataAccessObject.getAllBills().containsKey(bill_id)) {
            splitManagementPresenter.prepareFailView("bill not found.");
        }
        else {
            userDataAccessObject.distributeBill(bill_id, usersSplitting);

        }
    }

}
