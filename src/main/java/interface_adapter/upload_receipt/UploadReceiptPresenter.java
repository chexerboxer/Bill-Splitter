package interface_adapter.upload_receipt;

import javax.swing.JOptionPane;

import use_case.upload_receipt.UploadReceiptOutputBoundary;

public class UploadReceiptPresenter implements UploadReceiptOutputBoundary {

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage);
    }
}
