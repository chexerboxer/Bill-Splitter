package use_case.dashboard;

import java.util.HashMap;

public class DashboardInputData {
    private final HashMap<Integer, String> userBillsData;
    private final int billId;

    public DashboardInputData(
                              HashMap<Integer, String> userBillsData,
                              int billId) {
        this.userBillsData = userBillsData;
        this.billId = billId;
    }

    public HashMap<Integer, String> getUserBillsData() {
        return userBillsData;
    }

    int getBillId() {
        return billId;
    }
}
