package use_case.split_management.clear_bill;

public interface ClearBillDataAccessInterface {

    /**
     * Remove all splits associated with this bill.
     * @param billId the id of the bill of the item
     */
    void clearBill(int billId);
}
