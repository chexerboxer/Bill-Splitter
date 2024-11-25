package use_case.new_bill;

/**
 * DAO for the New Bill Use Case
 */
public interface NewBillUserDataAccessInterface {

    /**
     * Returns the name of the bill.
     * @return the name of the bill
     */
    String getBillName();

    /**
     * Sets the bill name.
     * @param newBillName the new bill name
     */
    void setBillName(String newBillName);
}
