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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileDAO implements FileDAOInterface{

    private static final String HEADER = "type,name,id,users/password,items/splits,total";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<Integer, Bill> bills = new HashMap<>();
    private final Map<Integer, User> users = new HashMap<>();



    /**
     * Constructor to intialize the csv file and then read it.
     * @param csvPath is the path of the csv file.
     * @param billFactory is the Factory used to create bills.
     * @param userFactory is the factory used to create users.
     * @return the LoggedInView created for the provided input classes
     * @throws IOException for reader
     */
    public FileDAO(String csvPath, BillFactory billFactory, UserFactory userFactory,
                   ItemFactory itemFactory, SplitFactory splitFactory) throws IOException{

        csvFile = new File(csvPath);
        headers.put("type", 0);
        headers.put("name", 1);
        headers.put("id",2);
        headers.put("users", 3);
        headers.put("password", 3);
        headers.put("items", 4);
        headers.put("splits", 4);
        headers.put("total", 5);
        DAOhelper helper = new DAOhelper();
        if (csvFile.length() == 0){
            save();
        }
        else{
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String type = String.valueOf(col[headers.get("type")]);

                    if (type.equals("Bill")){
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
                    }
                    else if (type.equals("User")){
                        final String name = String.valueOf(col[headers.get("name")]);
                        final int id = Integer.valueOf(col[headers.get("id")]);
                        final String password = String.valueOf(col[headers.get("password")]);

                        String splitsString = String.valueOf(col[headers.get("splits")]);
                        // splits  come in format of [[amount1;billid1;itemid1]/[amount2;billid2;itemid2],...] *note weird choices cuz
                        // csv took comma , and decimal takes up period .
                        final ArrayList<Split> splits = helper.SplitsExtraction(splitsString, splitFactory);

                        User newUser = userFactory.create(name, id, password, splits);

                        users.put(id, newUser);
                    }
                    else{
                        throw new RuntimeException(String.format
                                ("type should be%n: %s%but was:%n%s", "Bill or User", type));
                    }
                }
            }
        }


    }


    @Override
    public void save() {
    //TODO

    }

    @Override
    public User getUser(int id) {
        return users.get(id);
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
    }

    @Override
    public void setBill(int id, Bill bill) {
        bills.put(id, bill);
    }
}
