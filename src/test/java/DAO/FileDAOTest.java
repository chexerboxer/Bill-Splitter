package DAO;


import data_access.DAOhelper;
import data_access.FileDAO;
import entity.bill.BillFactory;
import entity.item.Item;
import entity.item.ItemFactory;
import entity.split.Split;
import entity.split.SplitFactory;
import entity.users.CommonUserFactory;
import entity.users.UserFactory;
import org.junit.Test;

import javax.print.attribute.IntegerSyntax;

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

}
