package entity;

/**
 *  The representation of a bill in our program.
 */
public class Bill {

    private String name;
    private final int id;

    public Bill(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
