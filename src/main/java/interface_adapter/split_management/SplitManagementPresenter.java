package interface_adapter.split_management;

import javax.swing.JOptionPane;

import use_case.split_management.SplitManagementOutputBoundary;

public class SplitManagementPresenter implements SplitManagementOutputBoundary {

    // no need constructor because each of the splits use cases doesnt have their own views. they are just
    // pop ups in the bill display view.

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage);
    }
}
