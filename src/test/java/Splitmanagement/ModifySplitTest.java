package Splitmanagement;

import data_access.FileDAO;
import entity.split.*;
import entity.users.*;
import entity.bill.*;
import entity.item.*;

import org.junit.Test;

import javax.print.attribute.IntegerSyntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import interface_adapter.split_management.ModifySplitController;
import interface_adapter.split_management.SplitManagementPresenter;
import org.junit.Test;
import use_case.split_management.SplitManagementOutputBoundary;
import use_case.split_management.modify_split.ModifySplitInputBoundary;
import use_case.split_management.modify_split.ModifySplitInteractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ModifySplitTest {

    @Test
    public void ModifySplitTest() throws IOException {
        //test run to see whether it works.

        UserFactory userFactory = new CommonUserFactory();
        SplitFactory splitFactory = new SplitFactory();
        BillFactory billFactory = new BillFactory();
        ItemFactory itemFactory = new ItemFactory();
        FileDAO userDataAccessObject = new FileDAO(System.getProperty("user.dir") + "\\src\\test\\java\\Splitmanagement\\ModifySplitTest.csv"
                , billFactory, userFactory, itemFactory, splitFactory);

        ArrayList<Integer> userids = new ArrayList<>();
        userids.add(10);
        userids.add(11);
        userids.add(12);
        HashMap<Integer, Item> items = new HashMap<>();
        items.put(10, itemFactory.create("item1",10,32.2f));
        items.put(11, itemFactory.create("item2", 11, 42.1f));
        Bill bill1 = billFactory.create("testBillName", 10, userids);
        bill1.addItem(itemFactory.create("item1",10,32.2f));
        bill1.addItem(itemFactory.create("item2", 11, 42.1f));
        ArrayList<Split> splits = new ArrayList<>();
        splits.add(splitFactory.create(12,10,11));
        ArrayList<Split> splits2 = new ArrayList<>();
        splits2.add(splitFactory.create(10,10,10));
        splits2.add(splitFactory.create(12,10,11));
        ArrayList<Split> splits3 = new ArrayList<>();
        splits3.add(splitFactory.create(11,10,10));

        User user1 = userFactory.create("testpersonA", 12,"asd2123",splits);
        User user2 = userFactory.create("testpersonB", 10,"tasd", splits2);
        User user3 = userFactory.create("testpersonC", 11,"tasd", splits3);

        HashMap<Integer, Bill> bills = new HashMap<>();
        bills.put(bill1.getId(), bill1);
        HashMap<Integer, User> users = new HashMap<>();
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user3.getId(), user3);

        userDataAccessObject.setBills(bills);
        userDataAccessObject.setUsers(users);


        // set up controllers
        SplitManagementOutputBoundary splitManagementOutputBoundary = new SplitManagementPresenter();

        final ModifySplitInputBoundary modifySplitInterator =
                new ModifySplitInteractor(userDataAccessObject, splitManagementOutputBoundary);

        final ModifySplitController modifySplitController1 = new ModifySplitController(modifySplitInterator);

        modifySplitController1.execute(10, 10, 10 ,10 );
        assertTrue( userDataAccessObject.getUser(10).getSplits().get(0).getAmount() == 20);

    }

}
