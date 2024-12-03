package use_case.dashboard;

import java.util.HashMap;

public class DashboardOutputData {
    private final HashMap<Integer, String> userBillsData;
    private final boolean useCaseFailed;

    public DashboardOutputData(
                              HashMap<Integer, String> userBillsData,
                              boolean useCaseFailed) {
        this.userBillsData = userBillsData;
        this.useCaseFailed = useCaseFailed;
    }

    public HashMap<Integer, String> getUserBillsData() {
        return userBillsData;
    }

}
