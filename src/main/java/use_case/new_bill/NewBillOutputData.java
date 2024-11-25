package use_case.new_bill;

/**
 * Output Data for the New Bill Use Case
 */
public class NewBillOutputData {

    private String billName;
    private boolean useCaseFailed;

    public NewBillOutputData(String billName, boolean useCaseFailed) {
        this.billName = billName;
        this.useCaseFailed = useCaseFailed;
    }
    public String getBillName() {
        return billName;
    }
    public boolean isUseCaseFailed(){
        return useCaseFailed;
    }
}
