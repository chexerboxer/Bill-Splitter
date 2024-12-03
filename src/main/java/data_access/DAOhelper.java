package data_access;

import java.util.ArrayList;
import java.util.HashMap;

import entity.item.Item;
import entity.item.ItemFactory;
import entity.split.Split;
import entity.split.SplitFactory;

/**
 * DAO helper to parse through the unique csv formatting to extract and format necessary information.
 * See FileDAO.java for csv file formatting breakdown.
 */
public class DAOhelper {
    
    private static final String EMPTY_LIST = "[]";
    private static final String DATA_DIVIDER = ";";

    /**
     * Extracts user IDs from Bill objects in the csv file.
     * @param userIdsString representing a list of user IDs
     * @return userIds list of all userIds in an array list
     */
    public ArrayList<Integer> UserIdExtraction(String userIdsString) {
        
        final ArrayList<Integer> userIds = new ArrayList<>();

        if (!userIdsString.equals(EMPTY_LIST)) {
            // exclude [] on the ends of the line, then split into a list
            final String[] userIdsArr = userIdsString.substring(1, userIdsString.length() - 1).split(DATA_DIVIDER);

            for (String userId : userIdsArr) {
                userIds.add(Integer.valueOf(userId));
            }
        }
        
        return userIds;
    }

    /**
     * Extracts user IDs from Bill objects in the csv file.
     * @param itemsString representing a list of item IDs
     * @param itemFactory to build new item objects
     * @return userIds list of all userIds in an array list
     */
    public HashMap<Integer, Item> ItemsExtraction(String itemsString, ItemFactory itemFactory) {
        final HashMap<Integer, Item> items = new HashMap<>();

        if (!itemsString.equals(EMPTY_LIST)) {
            // exclude [] on the ends of the line, then split into a list of items, surrounded by []
            final String[] unparsedItems = itemsString.substring(1, itemsString.length() - 1).split("/");

            for (String itemString : unparsedItems) {
                // exclude [] on the ends of the line, then split into a list containing item data
                final String[] itemSplit = itemString.substring(1, itemString.length() - 1).split(DATA_DIVIDER);

                // create new items
                final Item item = itemFactory.create(itemSplit[0],
                        Integer.valueOf(itemSplit[1]), Float.valueOf(itemSplit[2]));
                items.put(item.getId(), item);
            }
        }
        return items;
    }

    /**
     * Extracts user IDs from Bill objects in the csv file.
     * @param splitsString representing a list of splits
     * @param splitFactory to build new split objects
     * @return userIds list of all userIds in an array list
     */
    public ArrayList<Split> SplitsExtraction(String splitsString, SplitFactory splitFactory) {
        final ArrayList<Split> splits = new ArrayList<>();

        if (!splitsString.equals(EMPTY_LIST)) {
            // exclude [] on the ends of the line, then split into a list of splits, surrounded by []
            final String[] unparsedSplits = splitsString.substring(1, splitsString.length() - 1).split("/");

            for (String splitString : unparsedSplits) {
                // exclude [] on the ends of the line, then split into a list containing item data
                final String[] splitSplit = splitString.substring(1, splitString.length() - 1).split(DATA_DIVIDER);

                // create new split objects
                final Split split = splitFactory.create(Float.valueOf(splitSplit[0]),
                        Integer.valueOf(splitSplit[1]), Integer.valueOf(splitSplit[2]));
                splits.add(split);
            }
        }
        return splits;
    }
}
