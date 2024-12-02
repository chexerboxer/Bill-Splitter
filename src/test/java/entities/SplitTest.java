package entities;

import entity.split.Split;
import entity.split.SplitFactory;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SplitTest {
    private final SplitFactory splitFactory = new SplitFactory();

    @Test
    void testSplitCreation() {
        Split split = splitFactory.create(50.0f, 1, 2);
        assertNotNull(split);
        assertEquals(50.0f, split.getAmount());
        assertEquals(1, split.getBillId());
        assertEquals(2, split.getItemId());
    }

    @Test
    void testEquals() {
        Split split1 = splitFactory.create(100.0f, 3, 4);
        Split split2 = splitFactory.create(100.0f, 3, 4);

        assertEquals(split1, split2, "Splits with the same attributes should be equal");
    }

    @Test
    void testSetAmount() {
        Split split = splitFactory.create(30.0f, 5, 6);
        split.setAmount(60.0f);
        assertEquals(60.0f, split.getAmount());
    }



    @Test
    void testGetters() {
        Split split = splitFactory.create(80.0f, 7, 8);
        assertEquals(80.0f, split.getAmount());
        assertEquals(7, split.getBillId());
        assertEquals(8, split.getItemId());
    }
}
