package entity;

public class ItemFactory {
    public Item create(String name, float cost) {
        return new Item(name, cost);
    }
}