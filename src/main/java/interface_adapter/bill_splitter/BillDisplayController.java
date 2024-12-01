package interface_adapter.bill_splitter;

import interface_adapter.LoggedInController;

public class BillDisplayController implements LoggedInController {
    @Override
    public void switchToDashboardView(String username) {
        System.out.println("controller switches views");
    }

}
