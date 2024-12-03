package entity.item;

import java.util.Random;

import entity.GenerateId;

public class Item implements GenerateId {

    // set bounds when generating an ID so it'll always be 9 digits
    private static final int START_ID_RANGE = 100000000;
    private static final int END_ID_RANGE = 999999999;

    private String name;
    private int id;
    private float cost;

    public Item(String name, float cost) {
        this.name = name;
        this.id = generateId();
        this.cost = cost;
    }

    public Item(String name, int id, float cost) {
        this.name = name;
        this.id = id;
        this.cost = cost;
    }

    /**
     * Checks if the given item is equal to the current item based on name, ID, and cost.
     *
     * @param item The item to compare to.
     * @return true if the items have the same name, ID, and cost; false otherwise.
     */
    public boolean equals(Item item) {
        return this.name.equals(item.getName()) && this.id == item.getId() && this.cost == item.getCost();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int generateId() {
        final Random random = new Random();
        final int idBound = END_ID_RANGE - START_ID_RANGE + 1;
        final int id = random.nextInt(idBound) + START_ID_RANGE;
        return id;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public float getCost() {
        return cost;
    }

    public void setId(int newId) {
        id = newId;
    }

}
