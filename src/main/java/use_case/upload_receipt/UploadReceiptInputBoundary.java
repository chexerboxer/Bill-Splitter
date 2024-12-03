package use_case.upload_receipt;

import java.io.IOException;

/**
 * Input boundary for the Upload Receipt use case.
 * This interface defines the contract for handling the logic of uploading receipts
 * and processing the corresponding data.
 */
public interface UploadReceiptInputBoundary {

    /**
     * Executes the process of uploading a receipt with the provided input data.
     *
     * @param uploadReceiptInputData The input data containing the receipt file information and related details.
     * @throws IOException If an I/O error occurs during the receipt upload process.
     */
    void execute(UploadReceiptInputData uploadReceiptInputData) throws IOException;
}
