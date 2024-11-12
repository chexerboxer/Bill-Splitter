package entity.item;

public class ItemFactory {
    public Item create(String name, float cost) {
        return new Item(name, cost);
    }

    public Item create(String name, int id, float cost) {
        return new Item(name, id, cost);
    }
}