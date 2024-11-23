package use_case.new_bill;

/**
 * Input Boundary for actions which are related to creating a new bill
 */
public interface NewBillInputBoundary {

    /**
     * Executes the NewBill use case.
     * @param newBillInputData the input data
     */
    void execute(NewBillInputData newBillInputData);
}
