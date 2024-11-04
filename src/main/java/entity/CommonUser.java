package entity;

import java.util.ArrayList;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String password;
    private final ArrayList<Split> splits = new ArrayList<>();

    public CommonUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void add(Split s) {
        this.splits.add(s);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public ArrayList<Split> getSplits() {
        return splits;
    }
}
