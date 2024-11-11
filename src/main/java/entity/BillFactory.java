package entity;

/**
 * Factory for creating Bill objects.
 */
public class BillFactory {

    public Bill create(String name) {
        return new Bill(name);
    }
}