package entity;

public class Item {
    private String name;
    private final int id;
    private float cost;

    public Item(String name, int id, float cost) {
        this.name = name;
        this.id = id;
        this.cost = cost;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getCost() {
        return cost;
    }
}
