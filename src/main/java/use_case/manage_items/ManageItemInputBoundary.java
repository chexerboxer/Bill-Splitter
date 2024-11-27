package use_case.manage_items;

/**
 * Input Boundary for actions related to managing items in a bill.
 */
public interface ManageItemInputBoundary {
    /**
     * Executes the manage item use case.
     * @param manageItemInputData the input data
     */
    void execute(ManageItemInputData manageItemInputData);
}