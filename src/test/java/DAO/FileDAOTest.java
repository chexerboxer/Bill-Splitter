package DAO;


import data_access.DAOhelper;
import data_access.FileDAO;
import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.item.Item;
import entity.item.ItemFactory;
import entity.split.Split;
import entity.split.SplitFactory;
import entity.users.CommonUserFactory;
import entity.users.User;
import entity.users.UserFactory;
import org.junit.Test;

import javax.print.attribute.IntegerSyntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileDAOTest {

    @Test
    public void ItemsExtractionTest(){
        String itemString = "[[test;10;54.4]/[test2;11;55.4]]";
        ItemFactory itemFactory = new ItemFactory();
        Item item1 = itemFactory.create("test",10,54.4f);
        Item item2 = itemFactory.create("test2", 11, 55.4f);
        HashMap<Integer, Item> correctItems = new HashMap<>();
        correctItems.put(10, item1);
        correctItems.put(11, item2);
        DAOhelper helper = new DAOhelper();
        HashMap<Integer, Item> testItems = helper.ItemsExtraction(itemString, new ItemFactory());
        assertTrue(correctItems.get(10).equals(testItems.get(10)));
        assertTrue(correctItems.get(11).equals(testItems.get(11)));


    }

    @Test
    public void ItemsEmptyTest(){
        String itemString = "";
        DAOhelper helper = new DAOhelper();
        HashMap<Integer, Item> testItems = helper.ItemsExtraction(itemString, new ItemFactory());

        assertTrue(testItems.equals(new HashMap<Integer, Item>()));


    }
    @Test
    public void UserExtractionTest(){
        String itemString = "[10;55;43]";
        DAOhelper helper = new DAOhelper();
        ArrayList<Integer> correct = new ArrayList<>();
        correct.add(10);
        correct.add(55);
        correct.add(43);

        assertTrue(correct.equals(helper.UserIdExtraction(itemString)));
    }

    @Test
    public void SplitsExtractionTest(){
        String splitString = "[[22.5;20;54]/[22.5;11;55]]";
        SplitFactory splitFactory = new SplitFactory();
        Split split1 = splitFactory.create(22.5f, 20, 54);
        Split split2 = splitFactory.create(22.5f, 11, 55);
        ArrayList<Split> correctSplits = new ArrayList<>();
        correctSplits.add(split1);
        correctSplits.add(split2);
        DAOhelper helper = new DAOhelper();
        ArrayList<Split> testSplits = helper.SplitsExtraction(splitString, splitFactory);
        assertTrue(correctSplits.get(1).equals(testSplits.get(1)));
        assertTrue(correctSplits.get(0).equals(testSplits.get(0)));


    }

    @Test
    public void ReadingTest() throws IOException {
        UserFactory userFactory = new CommonUserFactory();
        SplitFactory splitFactory = new SplitFactory();
        BillFactory billFactory = new BillFactory();
        ItemFactory itemFactory = new ItemFactory();
        FileDAO fileTest = new FileDAO(System.getProperty("user.dir") + "\\src\\test\\java\\DAO\\test.csv"
                , billFactory, userFactory, itemFactory, splitFactory);

        assertTrue(fileTest.getBill(10).getName().equals("test"));
        assertTrue(fileTest.getUser(12).getSplits().get(0).getAmount()==21.2f);


    }

    @Test
    public void WritingTest() throws IOException{
        File wipeFile = new File(System.getProperty("user.dir") + "\\src\\test\\java\\DAO\\test2.csv");
        wipeFile.delete();
        UserFactory userFactory = new CommonUserFactory();
        SplitFactory splitFactory = new SplitFactory();
        BillFactory billFactory = new BillFactory();
        ItemFactory itemFactory = new ItemFactory();
        FileDAO fileTest = new FileDAO(System.getProperty("user.dir") + "\\src\\test\\java\\DAO\\test2.csv"
                , billFactory, userFactory, itemFactory, splitFactory);
        assertTrue(fileTest.getAllBills().isEmpty());


        ArrayList<Integer> userids = new ArrayList<>();
        userids.add(10);
        userids.add(20);
        HashMap<Integer, Item> items = new HashMap<>();
        items.put(2, itemFactory.create("item1",10,32.2f));
        items.put(2, itemFactory.create("item2", 11, 22.1f));
        Bill bill1 = billFactory.create("test", 10, userids, items, 221.3f);
        Bill bill2 = billFactory.create("test2");
        ArrayList<Split> splits = new ArrayList<>();

        splits.add(splitFactory.create(123,10,11));
        User user1 = userFactory.create("test", 12,"asd2123",splits);
        User user2 = userFactory.create("test2", "tasd");
        User user3 = userFactory.create("test", 13,"asd2123",splits);

        HashMap<Integer, Bill> bills = new HashMap<>();
        bills.put(bill1.getId(), bill1);
        bills.put(bill2.getId(), bill2);
        HashMap<Integer, User> users = new HashMap<>();
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);


        fileTest.setBills(bills);
        fileTest.setUsers(users);
        users.put(user3.getId(), user3);
        fileTest.setUsers(users);

        FileDAO fileTest2 = new FileDAO(System.getProperty("user.dir") + "\\src\\test\\java\\DAO\\test2.csv"
                , billFactory, userFactory, itemFactory, splitFactory);

        assertTrue(fileTest2.getAllBills().size() == fileTest.getAllBills().size());

        assertTrue(fileTest.getUser(user1.getId()).getSplits().get(0).equals(
                fileTest2.getUser(user1.getId()).getSplits().get(0)));
    }

}
