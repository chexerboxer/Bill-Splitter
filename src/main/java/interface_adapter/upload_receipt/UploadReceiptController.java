package interface_adapter.upload_receipt;

import java.io.IOException;

import use_case.upload_receipt.UploadReceiptInputBoundary;
import use_case.upload_receipt.UploadReceiptInputData;

public class UploadReceiptController {

    private final UploadReceiptInputBoundary uploadReceiptUseCaseInteractor;

    public UploadReceiptController(UploadReceiptInputBoundary uploadReceiptUseCaseInteractor) {
        this.uploadReceiptUseCaseInteractor = uploadReceiptUseCaseInteractor;
    }

    /**
     * Executes the process of uploading a receipt for a given bill.
     * This method creates an instance of {@link UploadReceiptInputData} containing the
     * receipt file name and bill ID, and passes it to the use case interactor for processing.
     *
     * @param receiptFileName The name of the receipt file to be uploaded.
     * @param billId The ID of the bill associated with the receipt.
     * @throws IOException If an I/O error occurs during the receipt upload process.
     */
    public void execute(String receiptFileName, int billId) throws IOException {
        final UploadReceiptInputData uploadReceiptInputData = new UploadReceiptInputData(receiptFileName, billId);
        uploadReceiptUseCaseInteractor.execute(uploadReceiptInputData);

    }
}

