package use_case.split_management.clear_bill;

public class ClearBillInputData {

    private final int billid;

    public ClearBillInputData(int billId) {

        billid = billId;

    }

    public int getBill_id() {
        return billid;
    }




}
