package interface_adapter.split_management;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.split_management.clear_bill.ClearBillInputBoundary;
import use_case.split_management.clear_bill.ClearBillInputData;

public class ClearBillController {

    private final ClearBillInputBoundary clearBillUserInteractor;

    public ClearBillController(ClearBillInputBoundary clearBillUserInteractor) {
        this.clearBillUserInteractor = clearBillUserInteractor;
    }

    public void execute(int billId){
        final ClearBillInputData clearBillInputData = new ClearBillInputData(billId);
        clearBillUserInteractor.execute(clearBillInputData);
    }
}
