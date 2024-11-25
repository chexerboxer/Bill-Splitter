package use_case.new_bill;

/**
 * The output boundary for the NewBill Use Case
 */
public interface NewBillOutputBoundary {

    /**
     * prepares the success view for the NewBill Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(NewBillOutputData outputData);

    /**
     * Prepares the failure view for the New Bill USe Case
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
