package use_case.split_management.clear_bill;

import data_access.FileDAO;

public class ClearBillInteractor implements ClearBillInputBoundary {
    FileDAO userDataAccessObject;

    public ClearBillInteractor(FileDAO userDataAccessObject){
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void execute(ClearBillInputData clearBillInputData) {

        final int bill_id = clearBillInputData.getBill_id();


        if (!userDataAccessObject.getAllBills().containsKey(bill_id)){
            // TODO fail case
        }
        else{
            userDataAccessObject.clearBill(bill_id);

        }
    }

}
