package use_case.upload_receipt;

public interface UploadReceiptOutputBoundary{

/**
 * Prepares the failure view for the ploadReceipt Use Case.
 * @param errorMessage the explanation of the failure
 */
void prepareFailView(String errorMessage);


}
