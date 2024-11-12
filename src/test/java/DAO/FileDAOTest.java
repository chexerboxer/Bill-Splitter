package DAO;


import data_access.DAOhelper;
import data_access.FileDAO;
import entity.item.Item;
import entity.item.ItemFactory;
import org.junit.Test;

import javax.print.attribute.IntegerSyntax;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

public class FileDAOTest {

    @Test
    public void ItemsExtractionTest(){
        String itemString = "[[test,10,54].[test2,11,55]]";
        ItemFactory itemFactory = new ItemFactory();
        Item item1 = itemFactory.create("test",10,54);
        Item item2 = itemFactory.create("test2", 11, 55);
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
}
