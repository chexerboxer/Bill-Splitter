package use_case.new_bill;

public class NewBillInputData {
    private String billName;

    public NewBillInputData(String billName) {
        this.billName = billName;
    }

    public String getBillName() {
        return billName;
    }
}
