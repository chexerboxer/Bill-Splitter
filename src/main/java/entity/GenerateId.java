package entity;


public interface GenerateId {
    /**
     * Generates a random number to use as an entity's ID. Range of possible IDs will vary depending on the object.
     * @return the newly generated ID
     */
    int generateId();
}