package use_case.split_management.clear_bill;

import data_access.FileDAO;
import use_case.split_management.SplitManagementOutputBoundary;

public class ClearBillInteractor implements ClearBillInputBoundary {
    private FileDAO userDataAccessObject;
    private SplitManagementOutputBoundary splitManagementPresenter;

    public ClearBillInteractor(FileDAO userDataAccessObject,
                               SplitManagementOutputBoundary splitManagementOutputBoundary) {
        this.userDataAccessObject = userDataAccessObject;
        splitManagementPresenter = splitManagementOutputBoundary;
    }

    @Override
    public void execute(ClearBillInputData clearBillInputData) {
        final int billid = clearBillInputData.getBill_id();

        if (!userDataAccessObject.getAllBills().containsKey(billid)) {
            splitManagementPresenter.prepareFailView("bill not found.");
        }
        else {
            userDataAccessObject.clearBill(billid);

        }
    }

}
