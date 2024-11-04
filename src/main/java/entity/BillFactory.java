package entity;

/**
 * Factory for creating Bill objects.
 */
public class BillFactory {

    public Bill create(String name, int id) {
        return new Bill(name, id);
    }
}
