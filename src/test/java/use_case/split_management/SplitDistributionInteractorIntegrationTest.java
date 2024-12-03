package use_case.split_management;

import interface_adapter.split_management.SplitManagementPresenter;
import org.junit.jupiter.api.*;
import data_access.*;
import entity.bill.*;
import entity.item.*;
import entity.split.*;
import entity.users.*;
import use_case.split_management.clear_bill.*;
import use_case.split_management.distribute_bill_even.*;
import use_case.split_management.modify_split.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SplitDistributionInteractorIntegrationTest {

    private File testFile;
    private FileDAO fileDAO;

    @BeforeAll
    public void setup() throws IOException {
        // Create a temporary test file
        testFile = File.createTempFile("test_split_management", ".csv");

        // Write test data to the file
        try (var writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("type,name,id,users/password,items/splits,total");
            writer.newLine();
            writer.write("Bill,TestBill,1,[1;2],[[item1;101;10.0]/[item2;102;20.0]],30.0");
            writer.newLine();
            writer.write("User,User1,1,password,[[4.0;1;101]/[10.0;1;102]]");
            writer.newLine();
            writer.write("User,User2,2,password,[[5.0;1;101]/[10.0;1;102]]");
            writer.newLine();
        }

        // Initialize FileDAO with the temporary file
        fileDAO = new FileDAO(testFile.getAbsolutePath(), new BillFactory(), new CommonUserFactory(),
                new ItemFactory(), new SplitFactory());
    }

    @Test
    public void testClearBill() {
        ClearBillInteractor interactor = new ClearBillInteractor(fileDAO, new SplitManagementPresenter());


        // bill not found
        ClearBillInputData noSuchBillInput = new ClearBillInputData(100);
        interactor.execute(noSuchBillInput);

        // Execute clearing a valid bill
        ClearBillInputData inputData = new ClearBillInputData(1);
        interactor.execute(inputData);


        // Verify the bill's splits are cleared
        User user1 = fileDAO.getUser(1);
        User user2 = fileDAO.getUser(2);

        assertTrue(user1.getSplits().isEmpty(), "User1 splits should be cleared.");
        assertTrue(user2.getSplits().isEmpty(), "User2 splits should be cleared.");
    }

    @Test
    public void testDistributeBillEven() {
        DistributeBillEvenInteractor interactor = new DistributeBillEvenInteractor(fileDAO, new SplitManagementPresenter());

        // Prepare test data
        ArrayList<Integer> usersSplitting = new ArrayList<>(List.of(1, 2));

        // bill not found
        DistributeBillEvenInputData noBillinputData = new DistributeBillEvenInputData(100, usersSplitting);
        interactor.execute(noBillinputData);

        // Execute distribution
        DistributeBillEvenInputData inputData = new DistributeBillEvenInputData(1, usersSplitting);
        interactor.execute(inputData);

        // Verify the bill's items are evenly distributed among users
        User user1 = fileDAO.getUser(1);
        User user2 = fileDAO.getUser(2);

        float totalSplitUser1 = user1.getSplits().stream().map(Split::getAmount).reduce(0.0f, Float::sum);
        float totalSplitUser2 = user2.getSplits().stream().map(Split::getAmount).reduce(0.0f, Float::sum);

        assertEquals(15.0f, totalSplitUser1, "User1 should receive 15.0 total split.");
        assertEquals(15.0f, totalSplitUser2, "User2 should receive 15.0 total split.");
    }

    @Test
    public void testModifySplit() {
        ModifySplitInteractor interactor = new ModifySplitInteractor(fileDAO, new SplitManagementPresenter());

        // Execute a valid modification
        ModifySplitInputData inputData = new ModifySplitInputData(1.0f, 1, 101, 1);
        interactor.execute(inputData);

        // Verify the split is updated
        User user = fileDAO.getUser(1);
        Split updatedSplit = user.getSplits().stream()
                .filter(s -> s.getBillId() == 1 && s.getItemId() == 101)
                .findFirst()
                .orElse(null);

        assertNotNull(updatedSplit, "Split should exist after modification.");
        assertEquals(5.0f, updatedSplit.getAmount(), "Split amount should be updated to 10.0.");
    }

    @Test
    public void testModifySplitEdgeCases() {
        ModifySplitInteractor interactor = new ModifySplitInteractor(fileDAO, new SplitManagementPresenter());

        // Case: user doesnt exist.
        ModifySplitInputData noSuchUserInput = new ModifySplitInputData(50.0f, 1, 101, 100);
        interactor.execute(noSuchUserInput);

        // Case: bill doesnt exist
        ModifySplitInputData noSuchBillInput = new ModifySplitInputData(50.0f, 100, 101, 1);
        interactor.execute(noSuchBillInput);

        // Case: item not in bill
        ModifySplitInputData noSuchItemInput = new ModifySplitInputData(50.0f, 1, 1, 1);
        interactor.execute(noSuchItemInput);

        // Case: Modify split with insufficient funds
        ModifySplitInputData insufficientFundsInput = new ModifySplitInputData(50.0f, 1, 101, 1);
        interactor.execute(insufficientFundsInput);

        // Verify split was not modified
        User user = fileDAO.getUser(1);
        Split originalSplit = user.getSplits().stream()
                .filter(s -> s.getBillId() == 1 && s.getItemId() == 101)
                .findFirst()
                .orElse(null);

        assertNotNull(originalSplit, "Split should still exist.");
        assertEquals(5.0f, originalSplit.getAmount(), "Split amount should remain unchanged due to insufficient funds.");
    }

    @AfterAll
    public void cleanup() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

}
