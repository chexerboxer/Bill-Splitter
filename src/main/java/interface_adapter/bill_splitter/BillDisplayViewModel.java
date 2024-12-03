package interface_adapter.bill_splitter;

import interface_adapter.ViewModel;

public class BillDisplayViewModel extends ViewModel<BillDisplayState> {
    public BillDisplayViewModel() {
        super("bill splitter");
        setState(new BillDisplayState());
    }
}
