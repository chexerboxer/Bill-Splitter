package use_case.dashboard;

public class DashboardInputData {

    private final int billId;

    public DashboardInputData(int billId) {
        this.billId = billId;
    }

    int getBillId() {
        return billId;
    }
}
