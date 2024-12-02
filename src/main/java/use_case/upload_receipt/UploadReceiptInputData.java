package use_case.upload_receipt;

public class UploadReceiptInputData {
    private final String receiptFileName;
    private final int billId;

    public UploadReceiptInputData(String receiptFileName, int billId) {
        this.receiptFileName = receiptFileName;
        this.billId = billId;
    }

    public String getReceiptFileName() {
        return this.receiptFileName;
    }

    public int getBillId() {return this.billId;}
}
