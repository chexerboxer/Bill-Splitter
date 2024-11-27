package use_case.manage_items;

import entity.bill.Bill;

/**
 * DAO for the Manage Item Use Case.
 */
public interface ManageItemDataAccessInterface {
    /**
     * Returns the bill with the given ID.
     * @param billId the ID of the bill to retrieve
     * @return the bill with the given ID
     */
    Bill getBill(int billId);

    /**
     * Saves the updated bill.
     * @param bill the bill to save
     */
    void saveBill(Bill bill);
}