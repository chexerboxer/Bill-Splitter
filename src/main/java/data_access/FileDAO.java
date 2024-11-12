package data_access;

import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.split.SplitFactory;
import entity.users.User;
import entity.users.UserFactory;
import entity.item.Item;
import entity.item.ItemFactory;

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
    private final Map<String, Bill> bills = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();



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
        headers.put("users/password", 3);
        headers.put("items/splits", 4);
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

                    // user ids come in format of [id1,id2,id3, ...]
                    String userIdsString = String.valueOf(col[headers.get("users/password")]);
                    ArrayList<Integer> userIds = helper.UserIdExtraction(userIdsString);

                    // items  come in format of [[name1,id1,cost1].[name2,id2,cost2],...]
                    String itemsString = String.valueOf(col[headers.get("items/splits")]);
                    HashMap<Integer, Item> items = helper.ItemsExtraction(itemsString, itemFactory);

                    }
                    else if (type.equals("User")){
                    // TODO


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
        //TODO
        return null;
    }

    @Override
    public Bill getBill(int id) {
        //TODO
        return null;
    }

    @Override
    public Map<Integer, Bill> getAllBills() {
        //TODO
        return null;
    }

    @Override
    public Map<Integer, Bill> getAllUsers() {
        //TODO
        return null;
    }

    @Override
    public void setUser(int id, User user) {
        //TODO
    }

    @Override
    public void setBill(int id, Bill bill) {
        //TODO
    }
}
