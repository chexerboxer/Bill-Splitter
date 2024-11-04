package entity;

public class ItemFactory {
    public Item create(String name, int id, float cost) {
        return new Item(name, id, cost);
    }
}
