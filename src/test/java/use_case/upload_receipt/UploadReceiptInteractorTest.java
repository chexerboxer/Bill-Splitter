package use_case.upload_receipt;

import entity.users.CommonUserFactory;
import org.junit.jupiter.api.Test;


import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.item.ItemFactory;
import entity.users.UserFactory;
import entity.split.SplitFactory;
import data_access.FileDAO;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UploadReceiptInteractorTest {

    private FileDAO fileDAO;
    private TestPresenter testPresenter;
    private UploadReceiptInteractor interactor;
    private BillFactory billFactory;
    private ItemFactory itemFactory;
    private UserFactory userFactory;

    @BeforeEach
    public void setup() throws IOException {
        billFactory = new BillFactory();
        itemFactory = new ItemFactory();
        userFactory = new CommonUserFactory();
        File testFile = File.createTempFile("test_receipt_upload", ".csv");
        fileDAO = new FileDAO(testFile.getAbsolutePath(), billFactory, userFactory, itemFactory, new SplitFactory());
        testPresenter = new TestPresenter();
        interactor = new UploadReceiptInteractor(fileDAO, testPresenter);

        // Write a valid test CSV
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("type,name,id,users/password,items/splits,total");

        }
        fileDAO.setBill(1, new Bill("test"));
    }

    @Test
    public void testSuccessfulExecution() throws IOException {
        // Create a valid receipt input file


        // Set up the input data
        UploadReceiptInputData inputData = new UploadReceiptInputData("src/test/java/use_case/upload_receipt/itemized-receipt.jpg", 1);
        interactor.execute(inputData);


        // Verify the results
        Bill updatedBill = fileDAO.getBill(1);

    }


    // Presenter for testing
    private static class TestPresenter implements UploadReceiptOutputBoundary {
        public boolean failed = false;
        public String errorMessage;

        @Override
        public void prepareFailView(String errorMessage) {
            this.failed = true;
            this.errorMessage = errorMessage;
        }
    }
}
