package entity;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User, GenerateId {

    private String name;
    private final int id;
    private String password;
    private ArrayList<Split> splits = new ArrayList<>();

    // set bounds when generating an ID so it'll always be 9 digits
    private static final int START_ID_RANGE = 100000000;
    private static final int END_ID_RANGE = 999999999;

    public CommonUser(String name, String password) {
        this.name = name;
        this.id = generateId();
        this.password = password;
    }

    public CommonUser(String name, String password, ArrayList<Split> splits) {
        this.name = name;
        this.id = generateId();
        this.password = password;
        this.splits = splits;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int generateId() {
        Random random = new Random();
        int idBound = END_ID_RANGE - START_ID_RANGE + 1;
        int id = random.nextInt(idBound) + START_ID_RANGE;
        return id;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setSplits(ArrayList<Split> splits) {
        this.splits = splits;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getPassword() {
        return password;
    }
    public ArrayList<Split> getSplits() {
        return splits;
    }

    public void addSplit(Split newSplit) {
        splits.add(newSplit);
    }
    public void removeSplit(Split oldSplit) {
        splits.remove(oldSplit);
    }



}