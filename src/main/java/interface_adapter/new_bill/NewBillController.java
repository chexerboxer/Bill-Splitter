package interface_adapter.new_bill;

import use_case.new_bill.NewBillInputBoundary;
import use_case.new_bill.NewBillInputData;

/**
 * Controller for the New Bill Use Case
 */
public class NewBillController {
    private final NewBillInputBoundary userNewBillUseCaseInteractor;

    public NewBillController(NewBillInputBoundary userNewBillUseCaseInteractor) {
        this.userNewBillUseCaseInteractor = userNewBillUseCaseInteractor;
    }

    /**
     * Executes the New Bill Use Case
     * @param billName the name of the bill
     */
    public void execute(String billName){
        final NewBillInputData newBillInputData = new NewBillInputData(billName);

        userNewBillUseCaseInteractor.execute(newBillInputData);
    }
}
