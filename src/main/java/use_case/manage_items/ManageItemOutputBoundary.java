package use_case.manage_items;

/**
 * The output boundary for the Manage Item Use Case.
 */
public interface ManageItemOutputBoundary {
    /**
     * Prepares the success view for the Manage Item Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ManageItemOutputData outputData);

    /**
     * Prepares the failure view for the Manage Item Use Case.
     * @param error the explanation of the failure
     */
    void prepareFailView(String error);
}
