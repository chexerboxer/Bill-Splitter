package use_case.split_management.modify_split;

import use_case.logout.LogoutOutputData;

/**
 * The output boundary for the DistributeBillEven Use Case.
 */
public interface ModifySplitOutputBoundary {
    /**
     * Prepares the success view for the ModifySplitBill Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(LogoutOutputData outputData);

    /**
     * Prepares the failure view for the ModifySplitBill Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
