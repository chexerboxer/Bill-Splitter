package use_case.split_management.clear_bill;

import data_access.FileDAO;
import use_case.split_management.SplitManagementOutputBoundary;

public class ClearBillInteractor implements ClearBillInputBoundary {
    FileDAO userDataAccessObject;
    SplitManagementOutputBoundary splitManagementPresenter;
    public ClearBillInteractor(FileDAO userDataAccessObject, SplitManagementOutputBoundary splitManagementOutputBoundary){
        this.userDataAccessObject = userDataAccessObject;
        splitManagementPresenter = splitManagementOutputBoundary;
    }

    @Override
    public void execute(ClearBillInputData clearBillInputData) {

        final int bill_id = clearBillInputData.getBill_id();


        if (!userDataAccessObject.getAllBills().containsKey(bill_id)){
            splitManagementPresenter.prepareFailView("bill not found.");
        }
        else{
            userDataAccessObject.clearBill(bill_id);

        }
    }

}
