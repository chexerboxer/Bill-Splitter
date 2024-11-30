package interface_adapter.upload_receipt;


import use_case.upload_receipt.UploadReceiptInputBoundary;
import use_case.upload_receipt.UploadReceiptInputData;
import use_case.upload_receipt.UploadReceiptInteractor;

import java.io.IOException;

public class UploadReceiptController {

        private final UploadReceiptInputBoundary uploadReceiptUseCaseInteractor;

        public UploadReceiptController(UploadReceiptInputBoundary uploadReceiptUseCaseInteractor) {
            this.uploadReceiptUseCaseInteractor = uploadReceiptUseCaseInteractor;
        }

        /**
         * Executes the Signup Use Case.
         *

         */
        public void execute(String receiptFileName, int billId) throws IOException{
            UploadReceiptInputData uploadReceiptInputData = new UploadReceiptInputData(receiptFileName, billId);
            uploadReceiptUseCaseInteractor.execute(uploadReceiptInputData);

        }
    }

