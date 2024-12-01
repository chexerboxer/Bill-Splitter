package interface_adapter.bill_splitter;

import interface_adapter.LoggedInState;

public class BillDisplayState implements LoggedInState {
    private String username;
    private int billId;


    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
