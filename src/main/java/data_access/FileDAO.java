package data_access;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.item.Item;
import entity.item.ItemFactory;
import entity.split.Split;
import entity.split.SplitFactory;
import entity.users.User;
import entity.users.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.dashboard.DashboardUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.split_management.clear_bill.ClearBillDataAccessInterface;
import use_case.split_management.distribute_bill_even.DistributeBillEvenDataAccessInterface;
import use_case.split_management.modify_split.ModifySplitDataAccessInterface;

/**
 * Data Access Object for the program's file-based database.
 */
public class FileDAO implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        ModifySplitDataAccessInterface,
        ClearBillDataAccessInterface,
        DistributeBillEvenDataAccessInterface,
        DashboardUserDataAccessInterface {

    private static final String HEADER = "type,name,id,users/password,items/splits,total";
    private static final String NAME_HEADER = "name";
    private static final String ID_HEADER = "id";
    private static final int USER_DATA_INDEX = 3;
    private static final int BILL_COST_DATA_INDEX = 4;
    private static final int COST_TOTAL_INDEX = 5;
    private static final String DATA_BUNDLE_LINE_FORMAT = "[%s]";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private Map<Integer, Bill> bills = new HashMap<>();
    private Map<Integer, User> users = new HashMap<>();

    /**
     * Constructor to initialize the csv file and then read it.
     * @param csvPath     is the path of the csv file.
     * @param billFactory is the factory used to create bills.
     * @param userFactory is the factory used to create users.
     * @param itemFactory is the factory used to create items.
     * @param splitFactory is the factor used to create split objects.
     * @throws IOException for reader
     * @throws RuntimeException for mis-written header
     */
    public FileDAO(String csvPath, BillFactory billFactory, UserFactory userFactory,
                   ItemFactory itemFactory, SplitFactory splitFactory) throws IOException {

        csvFile = new File(csvPath);
        headers.put("type", 0);
        headers.put(NAME_HEADER, 1);
        headers.put(ID_HEADER, 2);
        headers.put("users", USER_DATA_INDEX);
        headers.put("password", USER_DATA_INDEX);
        headers.put("items", BILL_COST_DATA_INDEX);
        headers.put("splits", BILL_COST_DATA_INDEX);
        headers.put("total", COST_TOTAL_INDEX);

        final DAOhelper helper = new DAOhelper();

        if (csvFile.length() == 0) {
            save();
        }
        else {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be: %s but is: %s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String type = String.valueOf(col[headers.get("type")]);

                    if ("Bill".equals(type)) {
                        final String name = String.valueOf(col[headers.get(NAME_HEADER)]);
                        final int id = Integer.valueOf(col[headers.get(ID_HEADER)]);

                        // user ids come in format of [id1;id2;id3; ...]
                        final String userIdsString = String.valueOf(col[headers.get("users")]);
                        final ArrayList<Integer> userIds = helper.UserIdExtraction(userIdsString);

                        // items  come in format of [[name1;id1;cost1]/[name2;id2;cost2],...]
                        final String itemsString = String.valueOf(col[headers.get("items")]);
                        final HashMap<Integer, Item> items = helper.ItemsExtraction(itemsString, itemFactory);

                        final float total = Float.valueOf(col[headers.get("total")]);

                        final Bill newBill = billFactory.create(name, id, userIds, items, total);
                        bills.put(id, newBill);
                    }
                    else if ("User".equals(type)) {
                        final String name = String.valueOf(col[headers.get(NAME_HEADER)]);
                        final int id = Integer.valueOf(col[headers.get(ID_HEADER)]);
                        final String password = String.valueOf(col[headers.get("password")]);

                        final String splitsString = String.valueOf(col[headers.get("splits")]);
                        // splits  come in format of [[amount1;billid1;itemid1]/[amount2;billid2;itemid2],...]
                        final ArrayList<Split> splits = helper.SplitsExtraction(splitsString, splitFactory);

                        final User newUser = userFactory.create(name, id, password, splits);
                        users.put(id, newUser);
                    }
                    else {
                        final String errorFormat = "type should be%n: %s%but was:%n%s";
                        throw new RuntimeException(String.format(errorFormat, "Bill or User", type));
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

                final ArrayList<Integer> userIdOriginal = bill.getUsers();
                String usersString = "";
                if (!userIdOriginal.isEmpty()) {
                    final String[] userList = new String[userIdOriginal.size()];

                    for (int i = 0; i < userIdOriginal.size(); i++) {
                        userList[i] = String.valueOf(userIdOriginal.get(i));
                    }
                    usersString = String.join(";", userList);
                }
                usersString = String.format(DATA_BUNDLE_LINE_FORMAT, usersString);

                String items = "";
                final HashMap<Integer, Item> itemsOriginal = bill.getItems();
                if (!itemsOriginal.isEmpty()) {
                    final String[] itemList = new String[itemsOriginal.size()];
                    int index = 0;
                    
                    for (int i : itemsOriginal.keySet()) {
                        final Item item = itemsOriginal.get(i);
                        itemList[index] = String.format("[%s;%d;%f]", item.getName(), item.getId(), item.getCost());
                        index++;
                    }
                    items = String.join("/", itemList);
                }
                items = String.format(DATA_BUNDLE_LINE_FORMAT, items);

                final float totalAmount = bill.getTotalAmount();

                writer.write(String.format("%s,%s,%d,%s,%s,%f",
                        "Bill", name, id, usersString, items, totalAmount));
                writer.newLine();
            }

            for (User user : users.values()) {
                final String name = user.getName();
                final int id = user.getId();
                final String password = user.getPassword();

                final ArrayList<Split> splitsOriginal = user.getSplits();
                String splits = "";

                if (!splitsOriginal.isEmpty()) {
                    final String[] splitsString = new String[splitsOriginal.size()];
                    for (int i = 0; i < splitsOriginal.size(); i++) {
                        final Split split = splitsOriginal.get(i);
                        splitsString[i] =
                                String.format("[%f;%d;%d]", split.getAmount(), split.getBillId(), split.getItemId());
                    }
                    splits = String.join("/", splitsString);

                }
                splits = String.format(DATA_BUNDLE_LINE_FORMAT, splits);
                
                writer.write(String.format("%s,%s,%d,%s,%s", "User", name, id, password, splits));
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns user with associated ID.
     * @param id numeric ID of the user
     * @return User object associated with ID
     */
    public User getUser(int id) {
        return users.get(id);
    }

    /**
     * Returns bill with associated ID.
     * @param id numeric ID of the bill
     * @return Bill object associated with ID
     */
    public Bill getBill(int id) {
        return bills.get(id);
    }

    public Map<Integer, Bill> getAllBills() {
        return bills;
    }

    public Map<Integer, User> getAllUsers() {
        return users;
    }

    /**
     * Sets a new user in the program.
     * @param id numeric ID of the user
     * @param user User object to be saved
     */
    public void setUser(int id, User user) {
        users.put(id, user);
        save();
    }

    /**
     * Sets a new bill in the program.
     * @param id numeric ID of the bill
     * @param bill Bill object to be saved
     */
    public void setBill(int id, Bill bill) {
        bills.put(id, bill);
        save();
    }

    /**
     * Sets a group of new users in the program.
     * @param users mapping of user IDs to User objects
     */
    public void setUsers(HashMap<Integer, User> users) {
        this.users = users;
        save();
    }

    /**
     * Sets a group of new bills in the program.
     * @param bills mapping of bill IDs to Bill objects
     */
    public void setBills(HashMap<Integer, Bill> bills) {
        this.bills = bills;
        save();
    }

    /**
     * Removes the user with the associated ID.
     * @param id of the user to be removed
     * @return boolean showing status of deletion.
     */
    public boolean removeUser(int id) {
        if (users.keySet().contains(id)) {
            users.remove(id);
            save();
            return true;
        }
        save();
        return false;
    }

    /**
     * Removes the bill with the associated ID.
     * @param id of bill to delete
     * @return boolean showing status of deletion
     */
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

    /**
     * Checks if a user with the associated username is registered in the program.
     * @param username the username to look for
     * @return boolean status of whether the user is in the program
     */
    @Override
    public boolean existsByName(String username) {
        for (User user : users.values()) {
            if (user.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the User object associated with the username.
     * @param username the username to look up
     * @return User object or null if no user with the username exists.
     */
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
            // Persist changes to file
            save();
        }
    }

    /**
     * Adds user given a User object.
     * @param user to be added
     */
    public void addUser(User user) {
        users.put(user.getId(), user);
        save();
    }

    /**
     * Adds bill given a Bill object.
     * @param bill to be added
     */
    public void addBill(Bill bill) {
        bills.put(bill.getId(), bill);
        save();
    }

    /**
     * Checks whether the user is in a bill by their splits and remove if they are not.
     * @param billId bill ID of the bill being interacted with
     * @param userId to check if they have splits
     */
    public void checkRemoveUserInBill(int billId, int userId) {
        final Bill bill = bills.get(billId);
        final User user = users.get(userId);

        for (int itemId : bill.getItems().keySet()) {
            // if the user has split in one of the item of the bill then it doesn't have to be removed.
            if (user.distributedAmount(itemId, billId) > 0) {
                return;
            }
        }

        // at this point, the user has no split in the bill, remove it from the bill.
        bill.removeUser(userId);
        setBill(billId, bill);
        save();
    }

    @Override
    public void modifySplit(float amountSplitted, int billId, int itemId, int userId) {
        final User user = users.get(userId);

        user.modifySplit(amountSplitted, itemId, billId);
        setUser(userId, user);

        checkRemoveUserInBill(billId, userId);
        save();
    }

    /**
     * Return the list of users attributed to this item.
     * @param itemId is the id of the item.
     * @param billId is the id of the bill of the item.
     * @return list of users attributed to this item.
     */
    public ArrayList<Integer> usersSplittingItem(int itemId, int billId) {
        final ArrayList<Integer> usersSplitting = new ArrayList<>();

        for (User user : users.values()) {
            if (user.distributedAmount(itemId, billId) > 0) {
                usersSplitting.add(user.getId());
            }
        }

        return usersSplitting;
    }

    /**
     * Return the undistrubted money on the item.
     *
     * @param itemId is the id of the item.
     * @param billId is the id of the bill of the item.
     * @return amount of money yet to be distributed.
     */
    public float undistributedOnItem(int itemId, int billId) {
        final Bill bill = bills.get(billId);
        final Item item = bill.getItems().get(itemId);
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
        final Bill bill = bills.get(billId);

        for (Item item : bill.getItems().values()) {

            final float amountToDistribute = undistributedOnItem(item.getId(), billId);
            if (amountToDistribute >= 0) {
                final float amountPerPerson = amountToDistribute / usersSplitting.size();

                for (int userId : usersSplitting) {
                    modifySplit(amountPerPerson, billId, item.getId(), userId);
                }
            }
        }
    }
    
    @Override
    public ArrayList<Bill> getUserBills(User user) {
        final ArrayList<Bill> userBills = new ArrayList<>();

        for (Bill bill : bills.values()) {
            if (bill.getUsers().contains(user.getId())) {
                userBills.add(bill);
            }
        }

        return userBills;
    }
}
