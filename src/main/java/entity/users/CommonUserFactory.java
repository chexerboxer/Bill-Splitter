package entity.users;

import java.util.ArrayList;

import entity.split.Split;

/**
 * Factory for creating CommonUser objects.
 */
public class CommonUserFactory implements UserFactory {

    @Override
    public User create(String name, String password) {
        return new CommonUser(name, password);
    }

    @Override
    public User create(String name, String password, ArrayList<Split> splits) {
        return new CommonUser(name, password, splits);
    }

    @Override
    public User create(String name, int id, String password, ArrayList<Split> splits) {
        return new CommonUser(name, id, password, splits);
    }
}
