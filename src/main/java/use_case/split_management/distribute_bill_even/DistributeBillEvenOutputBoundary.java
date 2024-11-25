package use_case.split_management.distribute_bill_even;

import use_case.logout.LogoutOutputData;

/**
 * The output boundary for the DistributeBillEven Use Case.
 */
public interface DistributeBillEvenOutputBoundary {
    /**
     * Prepares the success view for the DistributeBillEven Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(LogoutOutputData outputData);

    /**
     * Prepares the failure view for the DistributeBillEven Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
