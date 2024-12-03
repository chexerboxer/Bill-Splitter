package use_case.dashboard;

import entity.bill.Bill;
import entity.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class MockUserDataAccess implements DashboardUserDataAccessInterface {
    private final HashMap<String, User> users = new HashMap<>();
    private final List<Bill> addedBills = new ArrayList<>();
    private final List<Integer> removedBills = new ArrayList<>();

    void addUser(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public String getCurrentUsername() {
        return "test_user";
    }

    @Override
    public ArrayList<Bill> getUserBills(User user) {
        return new ArrayList<>();
    }

    @Override
    public boolean removeBill(int billId) {
        if (billId % 2 == 0) { // Simulate success for even bill IDs
            removedBills.add(billId);
            return true;
        }
        return false;
    }

    @Override
    public void addBill(Bill bill) {
        addedBills.add(bill);
    }

    List<Bill> getAddedBills() {
        return addedBills;
    }

    List<Integer> getRemovedBills() {
        return removedBills;
    }
}
