package use_case.upload_receipt;

import java.io.IOException;

public interface ReceiptProcessorInputBoundary {

    String readReceipt(String fileName);

    String getReceiptPath(String fileName);

    ReceiptData retrieveOcrData(String data) throws IOException;

}
