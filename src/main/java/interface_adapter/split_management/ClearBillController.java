package interface_adapter.split_management;

import use_case.split_management.clear_bill.ClearBillInputBoundary;
import use_case.split_management.clear_bill.ClearBillInputData;

public class ClearBillController {

    private final ClearBillInputBoundary clearBillUserInteractor;

    public ClearBillController(ClearBillInputBoundary clearBillUserInteractor) {
        this.clearBillUserInteractor = clearBillUserInteractor;
    }

    /**
     * Executes the process of clearing a bill based on the provided bill ID.
     * This method creates an instance of {@link ClearBillInputData} containing the bill ID
     * and passes it to the interactor for execution.
     * @param billId The ID of the bill to be cleared.
     */
    public void execute(int billId) {
        final ClearBillInputData clearBillInputData = new ClearBillInputData(billId);
        clearBillUserInteractor.execute(clearBillInputData);
    }
}
