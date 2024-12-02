package use_case.split_management;

/**
 * The output boundary for the DistributeBillEven Use Case.
 */
public interface SplitManagementOutputBoundary {

    /**
     * Prepares the failure view for the ClearBill Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
