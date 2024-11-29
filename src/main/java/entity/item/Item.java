package entity.item;

import entity.GenerateId;

import java.util.Random;

public class Item implements GenerateId {

    private String name;
    private int id;
    private float cost;

    // set bounds when generating an ID so it'll always be 9 digits
    private static final int START_ID_RANGE = 100000000;
    private static final int END_ID_RANGE = 999999999;

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

    public boolean equals(Item item){
        return this.name.equals(item.getName()) && this.id == item.getId() && this.cost == item.getCost();
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
    public void setId(int newId){id = newId;}
}