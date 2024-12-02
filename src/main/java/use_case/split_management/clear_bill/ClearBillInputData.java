package use_case.split_management.clear_bill;

public class ClearBillInputData {


    private final int bill_id;


    public int getBill_id() {
        return bill_id;
    }


    public ClearBillInputData(int billId) {

        bill_id = billId;

    }


}
