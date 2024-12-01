package data_access;

import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.split.Split;
import entity.split.SplitFactory;
import entity.users.CommonUserFactory;
import entity.users.User;
import entity.item.Item;
import entity.item.ItemFactory;
import entity.users.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.split_management.clear_bill.ClearBillDataAccessInterface;
import use_case.split_management.distribute_bill_even.DistributeBillEvenDataAccessInterface;
import use_case.split_management.modify_split.ModifySplitDataAccessInterface;
import use_case.dashboard.DashboardUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileDAO implements FileDAOInterface,
        SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        ModifySplitDataAccessInterface,
        ClearBillDataAccessInterface,
        DistributeBillEvenDataAccessInterface,
        DashboardUserDataAccessInterface {

    private static final String HEADER = "type,name,id,users/password,items/splits,total";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private Map<Integer, Bill> bills = new HashMap<>();
    private Map<Integer, User> users = new HashMap<>();


    /**
     * Constructor to intialize the csv file and then read it.
     *
     * @param csvPath     is the path of the csv file.
     * @param billFactory is the Factory used to create bills.
     * @param userFactory is the factory used to create users.
     * @return the DashboardView created for the provided input classes
     * @throws IOException for reader
     */
    public FileDAO(String csvPath, BillFactory billFactory, UserFactory userFactory,
                   ItemFactory itemFactory, SplitFactory splitFactory) throws IOException {

        csvFile = new File(csvPath);
        headers.put("type", 0);
        headers.put("name", 1);
        headers.put("id", 2);
        headers.put("users", 3);
        headers.put("password", 3);
        headers.put("items", 4);
        headers.put("splits", 4);
        headers.put("total", 5);
        DAOhelper helper = new DAOhelper();

        if (csvFile.length() == 0) {
            save();
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String type = String.valueOf(col[headers.get("type")]);

                    if (type.equals("Bill")) {
                        final String name = String.valueOf(col[headers.get("name")]);
                        final int id = Integer.valueOf(col[headers.get("id")]);

                        // user ids come in format of [id1;id2;id3; ...] *note weird choices cuz
                        // csv took comma ,
                        String userIdsString = String.valueOf(col[headers.get("users")]);
                        ArrayList<Integer> userIds = helper.UserIdExtraction(userIdsString);

                        // items  come in format of [[name1;id1;cost1]/[name2;id2;cost2],...] *note weird choices cuz
                        // csv took comma , decimal take up period .
                        String itemsString = String.valueOf(col[headers.get("items")]);
                        HashMap<Integer, Item> items = helper.ItemsExtraction(itemsString, itemFactory);

                        final float total = Float.valueOf(col[headers.get("total")]);

                        Bill newBill = billFactory.create(name, id, userIds, items, total);

                        bills.put(id, newBill);
                    } else if (type.equals("User")) {
                        final String name = String.valueOf(col[headers.get("name")]);
                        final int id = Integer.valueOf(col[headers.get("id")]);
                        final String password = String.valueOf(col[headers.get("password")]);

                        String splitsString = String.valueOf(col[headers.get("splits")]);
                        // splits  come in format of [[amount1;billid1;itemid1]/[amount2;billid2;itemid2],...] *note weird choices cuz
                        // csv took comma , and decimal takes up period .
                        final ArrayList<Split> splits = helper.SplitsExtraction(splitsString, splitFactory);

                        User newUser = userFactory.create(name, id, password, splits);

                        users.put(id, newUser);
                    } else {
                        throw new RuntimeException(String.format
                                ("type should be%n: %s%but was:%n%s", "Bill or User", type));
                    }
                }
            }
        }


    }


    @Override
    public void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(HEADER);
            writer.newLine();

            for (Bill bill : bills.values()) {
                final String name = bill.getName();
                final int id = bill.getId();

                ArrayList<Integer> userIdOG = bill.getUsers();
                String users = "";
                if (!userIdOG.isEmpty()) {
                    String[] userList = new String[userIdOG.size()];

                    for (int i = 0; i < userIdOG.size(); i++) {
                        userList[i] = String.valueOf(userIdOG.get(i));

                    }
                    users = String.join(";", userList);

                }
                users = String.format("[%s]", users);

                String items = "";
                HashMap<Integer, Item> itemsOG = bill.getItems();
                if (!itemsOG.isEmpty()) {
                    String[] itemList = new String[itemsOG.size()];
                    int index = 0;
                    for (int i : itemsOG.keySet()) {
                        Item item = itemsOG.get(i);
                        itemList[index] = String.format("[%s;%d;%f]", item.getName(), item.getId(), item.getCost());
                        index++;
                    }
                    items = String.join("/", itemList);

                }
                items = String.format("[%s]", items);

                final float totalAmount = bill.getTotalAmount();


                writer.write(String.format("%s,%s,%d,%s,%s,%f",
                        "Bill", name, id, users, items, totalAmount));
                writer.newLine();
            }

            for (User user : users.values()) {
                final String name = user.getName();
                final int id = user.getId();
                final String password = user.getPassword();

                ArrayList<Split> splitsOG = user.getSplits();
                String splits = "";

                if (!splitsOG.isEmpty()) {
                    String[] splitsString = new String[splitsOG.size()];
                    for (int i = 0; i < splitsOG.size(); i++) {
                        Split split = splitsOG.get(i);
                        splitsString[i] =
                                String.format("[%f;%d;%d]", split.getAmount(), split.getBillId(), split.getItemId());
                    }
                    splits = String.join("/", splitsString);

                }
                splits = String.format("[%s]", splits);


                writer.write(String.format("%s,%s,%d,%s,%s", "User", name, id, password, splits));
                writer.newLine();


            }
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public User getByUsername(String username) {
        for (User user : users.values()) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Bill getBill(int id) {
        return bills.get(id);
    }

    @Override
    public Map<Integer, Bill> getAllBills() {
        return bills;
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return users;
    }

    @Override
    public void setUser(int id, User user) {
        users.put(id, user);
        save();
    }

    @Override
    public void setBill(int id, Bill bill) {
        bills.put(id, bill);
        save();
    }

    @Override
    public void setUsers(HashMap<Integer, User> users) {
        this.users = users;
        save();
    }

    @Override
    public void setBills(HashMap<Integer, Bill> bills) {
        this.bills = bills;
        save();
    }

    @Override
    public boolean removeUser(int id) {
        if (users.keySet().contains(id)) {
            users.remove(id);
            save();
            return true;
        }
        save();
        return false;

    }

    @Override
    public boolean removeBill(int id) {
        if (bills.keySet().contains(id)) {
            bills.remove(id);
            save();
            return true;
        }
        save();
        return false;
    }

    @Override
    public boolean existsByName(String username) {
        for (User user : users.values()) {
            if (user.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User get(String username) {
        for (User user : users.values()) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public String getCurrentUsername() {
        return "";
    }

    @Override
    public void setCurrentUsername(String username) {



    }

    @Override
    public void changePassword(User user) {
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            save(); // Persist changes to file
        } else {
            System.out.println(user.getId() + "User not found");
            System.out.println(users);
        }
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        save();
    }

    public void addBill(Bill bill) {
        bills.put(bill.getId(), bill);
        save();
    }

    // checks whether the user is in a bill by their splits and remove if they are not.
    public void checkRemoveUserInBill(int billId, int userId) {
        Bill bill = bills.get(billId);
        User user = users.get(userId);
        for (int itemId : bill.getItems().keySet()) {
            // if the user has split in one of the item of the bill then it doesnt have to be removed.
            if (user.distributedAmount(itemId, billId) > 0) {
                return;
            }
        }

        // the user has no split in the bill, remove it from the bill.
        bill.removeUser(userId);
        setBill(billId, bill);
        save();
    }

    @Override
    public void modifySplit(float amountSplitted, int billId, int itemId, int userId) {

        User user = users.get(userId);


        user.modifySplit(amountSplitted, itemId, billId);
        setUser(userId, user);

        checkRemoveUserInBill(billId, userId);
        save();
    }

    /**
     * Return the list of users attributed to this item.
     *
     * @param itemId is the id of the item.
     * @param billId is the id of the bill of the item.
     * @return list of users attributed to this item.
     */
    public ArrayList<Integer> usersSplittingItem(int itemId, int billId) {
        ArrayList<Integer> usersSplitting = new ArrayList<>();
        for (User user : users.values()) {

            if (user.distributedAmount(itemId, billId) > 0) {
                usersSplitting.add(user.getId());
            }

        }
        return usersSplitting;

    }

    /**
     * Return the undistrubted money on the item
     *
     * @param itemId is the id of the item.
     * @param billId is the id of the bill of the item.
     * @return amount of money yet to be distributed.
     */
    public float undistributedOnItem(int itemId, int billId) {
        Bill bill = bills.get(billId);
        Item item = bill.getItems().get(itemId);
        float total = item.getCost();
        for (User user : users.values()) {
            for (Split split : user.getSplits()) {
                if (split.getItemId() == itemId && split.getBillId() == billId) {
                    total -= split.getAmount();
                }
            }
        }

        return total;

    }

    @Override
    public void clearBill(int billId) {

        for (User user : users.values()) {
            for (int itemId : bills.get(billId).getItems().keySet()) {

                user.removeSplit(itemId, billId);

            }
            setUser(user.getId(), user);

        }

    }

    @Override
    public void distributeBill(int billId, ArrayList<Integer> usersSplitting) {
        Bill bill = bills.get(billId);

        for (Item item : bill.getItems().values()) {

            float amount_to_distribute = undistributedOnItem(item.getId(), billId);
            if (amount_to_distribute >= 0) {
                float amount_per_person = amount_to_distribute / usersSplitting.size();
                for (int userId : usersSplitting) {
                    modifySplit(amount_per_person, billId, item.getId(), userId);
                }


            }

        }
    }
    @Override
    public ArrayList<Bill> getUserBills (User user){
        ArrayList<Bill> userBills = new ArrayList<>();

        for (Bill bill : bills.values()) {
            if (bill.getUsers().contains(user.getId())) {
                userBills.add(bill);
            }
        }

        return userBills;
    }


}






