package entities.users;

import entity.split.Split;
import entity.split.SplitFactory;
import entity.users.CommonUser;
import entity.users.CommonUserFactory;
import entity.users.User;
import entity.users.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CommonUserTest {
    private CommonUser user;
    private SplitFactory splitFactory;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        splitFactory = new SplitFactory();
        user = new CommonUser("Test User", "password123");
        userFactory = new CommonUserFactory();

    }

    @Test
    void testGenerateId() {
        assertTrue(user.getId() >= 100000000 && user.getId() <= 999999999, "ID should be a 9-digit number");
    }

    @Test
    void testAddSplit() {
        Split split = splitFactory.create(50.0f, 1, 1);
        user.addSplit(split);

        assertEquals(50.0f, user.distributedAmount(1, 1), "Split amount should match");
        assertEquals(1, user.getSplits().size(), "User should have one split");
    }

    // Test for setName
    @Test
    void testSetName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName(), "The name should be updated to Jane Doe");
    }

    // Test for setPassword
    @Test
    void testSetPassword() {
        user.setPassword("newPassword456");
        assertEquals("newPassword456", user.getPassword(), "The password should be updated to newPassword456");
    }

    // Test for setSplits
    @Test
    void testSetSplits() {
        ArrayList<Split> splits = new ArrayList<>();
        splits.add(splitFactory.create(50.0f, 1, 1));
        user.setSplits(splits);
        assertEquals(1, user.getSplits().size(), "Splits list should have one element");
        assertEquals(50.0f, user.getSplits().get(0).getAmount(), "The amount in the split should be 50.0");
    }


    @Test
    void testRemoveSplit() {
        Split split = splitFactory.create(50.0f, 1, 1);
        user.addSplit(split);
        user.removeSplit(1, 1);

        assertEquals(0, user.distributedAmount(1, 1), "Split should be removed");
        assertTrue(user.getSplits().isEmpty(), "Splits list should be empty");
    }

    @Test
    void testModifySplit() {
        Split split = splitFactory.create(50.0f, 1, 1);
        user.addSplit(split);
        user.modifySplit(20.0f, 1, 1);

        assertEquals(70.0f, user.distributedAmount(1, 1), "Modified amount should match");
    }

    @Test
    void testDistributedAmount() {
        Split split = splitFactory.create(50.0f, 1, 1);
        user.addSplit(split);

        assertEquals(50.0f, user.distributedAmount(1, 1), "Distributed amount should match");
    }

    // Test for addSplit (normal case)
    @Test
    void testAddSplit2() {
        Split split = splitFactory.create(30.0f, 1, 1);
        user.addSplit(split);
        assertEquals(1, user.getSplits().size(), "The splits list should have one element after adding");
        assertEquals(30.0f, user.getSplits().get(0).getAmount(), "The added split should have an amount of 30.0f");
    }

    // Test for addSplit (duplicate case)
    @Test
    void testAddDuplicateSplit() {
        Split split1 = splitFactory.create(30.0f, 1, 1);
        Split split2 = splitFactory.create(20.0f, 1, 1);  // Same billId and itemId
        user.addSplit(split1);
        user.addSplit(split2);

        assertEquals(1, user.getSplits().size(), "The list should only have one split with combined amount");
        assertEquals(50.0f, user.getSplits().get(0).getAmount(), "The amount in the split should be 50.0f after combining");
    }

    // Test for modifySplit (normal case)
    @Test
    void testModifySplit2() {
        Split split = splitFactory.create(30.0f, 1, 1);
        user.addSplit(split);
        user.modifySplit(10.0f, 1, 1); // Increase by 10.0f

        assertEquals(40.0f, user.getSplits().get(0).getAmount(), "The amount in the split should be updated to 40.0f");
    }

    // Test for modifySplit (negative modification)
    @Test
    void testModifySplitNegative() {
        Split split = splitFactory.create(30.0f, 1, 1);
        user.addSplit(split);
        user.modifySplit(-10.0f, 1, 1); // Decrease by 10.0f

        assertEquals(20.0f, user.getSplits().get(0).getAmount(), "The amount in the split should be updated to 20.0f");
    }

    // Test for modifySplit (remove split)
    @Test
    void testModifySplitRemove() {
        Split split = splitFactory.create(10.0f, 1, 1);
        user.addSplit(split);
        user.modifySplit(-10.0f, 1, 1); // Set the amount to 0, should remove the split

        assertTrue(user.getSplits().isEmpty(), "The split should be removed when the amount reaches 0");
    }

    // Test for modifySplit (edge case: small positive modification resulting in zero amount)
    @Test
    void testModifySplitZeroAmount() {
        Split split = splitFactory.create(5.0f, 1, 1);
        user.addSplit(split);
        user.modifySplit(-5.0f, 1, 1); // Should remove the split since the amount becomes zero

        assertTrue(user.getSplits().isEmpty(), "The split should be removed as its amount is reduced to zero");
    }

    // Test for distributedAmount (normal case)
    @Test
    void testDistributedAmount2() {
        Split split = splitFactory.create(30.0f, 1, 1);
        user.addSplit(split);

        float distributedAmount = user.distributedAmount(1, 1);
        assertEquals(30.0f, distributedAmount, "The distributed amount for the given itemId and billId should be 30.0f");
    }

    // Test for distributedAmount (edge case: split not found)
    @Test
    void testDistributedAmountNotFound() {
        float distributedAmount = user.distributedAmount(2, 2); // Non-existent split
        assertEquals(0.0f, distributedAmount, "The distributed amount should be 0.0f for a split that does not exist");
    }


    @Test
    void testEquality() {
        SplitFactory splitFactory = new SplitFactory();
        ArrayList<Split> splits1 = new ArrayList<>();
        splits1.add(splitFactory.create(50.0f, 1, 1));

        ArrayList<Split> splits2 = new ArrayList<>();
        splits2.add(splitFactory.create(50.0f, 1, 1));

        CommonUser user1 = new CommonUser("Test User", "password123", splits1);
        CommonUser user2 = new CommonUser("Test User", user1.getId(), "password123", splits2);

        assertTrue(user1.equals(user2), "Users with identical attributes should be equal");
    }

    @Test
    void testModifySplitCreatesNewSplit() {
        // Call modifySplit on a non-existent split
        user.modifySplit(25.0f, 1, 1);

        // Verify that a new split is created
        assertEquals(1, user.getSplits().size(), "A new split should be created when no matching split exists");
        assertEquals(25.0f, user.getSplits().get(0).getAmount(), "The newly created split should have the correct amount");
        assertEquals(1, user.getSplits().get(0).getItemId(), "The newly created split should have the correct itemId");
        assertEquals(1, user.getSplits().get(0).getBillId(), "The newly created split should have the correct billId");
    }

    @Test
    void testCreateWithAllParameters() {
        ArrayList<Split> splits = new ArrayList<>();
        splits.add(new Split(50.0f, 1, 1));

        User user = userFactory.create("John Doe", 123456789, "password123", splits);

        // Verify the user is created correctly
        assertEquals("John Doe", user.getName(), "User name should match the provided value");
        assertEquals(123456789, user.getId(), "User ID should match the provided value");
        assertEquals("password123", user.getPassword(), "User password should match the provided value");
        assertEquals(splits, user.getSplits(), "User splits should match the provided list");
    }

    @Test
    void testrandomcommonuser() {
        ArrayList<Split> splits = new ArrayList<>();
        userFactory.create("test", "123", splits);
    }

}
