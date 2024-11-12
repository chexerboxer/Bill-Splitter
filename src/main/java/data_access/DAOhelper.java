package data_access;

import entity.item.Item;
import entity.item.ItemFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class DAOhelper {

    public ArrayList<Integer> UserIdExtraction(String userIdsString){
        ArrayList<Integer> userIds = new ArrayList<Integer>();

        if (!userIdsString.isEmpty()){
        userIdsString = userIdsString.substring(1, userIdsString.length() - 1);

            String[] userIdsArr = userIdsString.split(",");
            for (String userId : userIdsArr){userIds.add(Integer.valueOf(userId));}
        }
        return userIds;
    }

    public HashMap<Integer, Item> ItemsExtraction (String itemsString, ItemFactory itemFactory){
        HashMap<Integer, Item> items = new HashMap<>();
        if (!itemsString.equals("")){
        itemsString = itemsString.substring(1, itemsString.length() - 1);

            String[] unparsedItems= itemsString.split("\\.");
            for (String itemString : unparsedItems){

                itemString = itemString.substring(1, itemString.length() - 1);

                String[] itemSplit = itemString.split(",");

                Item item = itemFactory.create(itemSplit[0],
                        Integer.valueOf(itemSplit[1]), Float.valueOf(itemSplit[2]));
                items.put(item.getId(), item);
            }
        }
        return items;
    }
}
