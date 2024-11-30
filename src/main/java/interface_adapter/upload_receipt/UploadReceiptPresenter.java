package interface_adapter.upload_receipt;

import use_case.upload_receipt.UploadReceiptOutputBoundary;

import javax.swing.*;

public class UploadReceiptPresenter implements UploadReceiptOutputBoundary {

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage);
    }
}
