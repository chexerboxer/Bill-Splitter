package use_case.split_management.modify_split;



public interface ModifySplitDataAccessInterface {

    /**
     * Updates the system to add a new split.
     * @param amountSplitted the split of the item to be distributed
     * @param billId the id of the bill of the item
     * @param itemId the id of the item being split
     * @param userId the id of the user being split to.
     */
    void modifySplit(float amountSplitted, int billId, int itemId, int userId);
}
