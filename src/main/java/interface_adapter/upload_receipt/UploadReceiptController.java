package interface_adapter.upload_receipt;


import use_case.upload_receipt.UploadReceiptInputBoundary;
import use_case.upload_receipt.UploadReceiptInputData;

import java.io.IOException;

public class UploadReceiptController {
    public class SignupController {

        private final UploadReceiptInputBoundary uploadReceiptUseCaseInteractor;

        public SignupController(UploadReceiptInputBoundary uploadReceiptUseCaseInteractor) {
            this.uploadReceiptUseCaseInteractor = uploadReceiptUseCaseInteractor;
        }

        /**
         * Executes the Signup Use Case.
         *

         */
        public void execute(UploadReceiptInputData uploadReceiptInputData) throws IOException{

            uploadReceiptUseCaseInteractor.execute(uploadReceiptInputData);

        }
    }
}
