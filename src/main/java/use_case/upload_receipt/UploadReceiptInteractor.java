package use_case.upload_receipt;


import data_access.FileDAO;
import entity.bill.Bill;
import entity.item.Item;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static app.Constants.*;

/**
 * Receipt Processor for the Upload Receipt Use Case. Take's in an image as input and returns a ReceiptData Object
 */
public class UploadReceiptInteractor implements UploadReceiptInputBoundary {
    private final FileDAO userDataAccessObject;
    private final UploadReceiptOutputBoundary uploadReceiptPresenter;

    private static final String CREATED_DATE_KEY = "date";
    private static final String CURRENCY_TYPE_KEY = "currency_code";
    private static final String ITEM_LIST_KEY = "line_items";
    private static final String ITEM_NAME_KEY = "description";
    private static final String ITEM_QUANTITY_KEY = "quantity";
    private static final String ITEM_PRICE_KEY = "total";
    public static final String FILE_DATA = "file_data";

    public UploadReceiptInteractor(FileDAO userDataAccessInterface,
                                   UploadReceiptOutputBoundary uploadReceiptOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        uploadReceiptPresenter = uploadReceiptOutputBoundary;
    }

    /**
     * Calls the receiptsOCR API and retrieves the data from the encoded base 64 String.
     *
     * @param uploadReceiptInputData input data for the Upload Receipt Use Case
     * @throws IOException if the API request failed
     */
    @Override
    public void execute(UploadReceiptInputData uploadReceiptInputData) throws IOException {

        String fileName = uploadReceiptInputData.getReceiptFileName();
        final String data = readReceipt(fileName);
        OkHttpClient client = new OkHttpClient();
        Map<String, String> dataRequest = new HashMap<>();
        dataRequest.put(FILE_DATA, data);

        JSONObject jo = new JSONObject(dataRequest);
        String json = jo.toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(BASE_URL + OCR_URI)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "apikey " + API_KEY)
                .addHeader("Client-Id", CLIENT_ID)
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        JSONObject responseJson = new JSONObject(response.body().string());
        ReceiptData extractedData = generateData(responseJson);

        Bill bill = userDataAccessObject.getBill(uploadReceiptInputData.getBillId());

        for (Item item : extractedData.getItems()) {
            bill.addItem(item);
        }
        userDataAccessObject.setBill(uploadReceiptInputData.getBillId(), bill);

    }


    /**
     * Reads the inputted receipt and converts it into a base 64 encoded String
     *
     * @param filename the file name of the image
     * @return the file encoded into a base 64 String
     */
    private String readReceipt(String filename) throws IOException {
        byte[] fileContent = null;
        fileContent = FileUtils.readFileToByteArray(new File(filename));

        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }

    /**
     * Private helper method for retrieveOcrData
     *
     * @param jo the JsonObject created by the API call
     * @return ReceiptData object with relevant info taken from jo
     */
    private ReceiptData generateData(JSONObject jo) {
        ReceiptData receiptData = new ReceiptData();

        if (jo == null) {
            return receiptData;
        }

        receiptData.setDate(jo.getString(CREATED_DATE_KEY));
        receiptData.setCurrencyType(jo.getString(CURRENCY_TYPE_KEY));
        List<Item> itemList = new ArrayList<>();

        if (jo.getJSONArray(ITEM_LIST_KEY) != null) {
            for (int i = 0; i < jo.getJSONArray(ITEM_LIST_KEY).length(); i++) {
                JSONObject joItem = jo.getJSONArray(ITEM_LIST_KEY).getJSONObject(i);
                Item convertedItem = new Item(joItem.getString(ITEM_NAME_KEY), joItem.getFloat(ITEM_PRICE_KEY) * joItem.getFloat(ITEM_QUANTITY_KEY));
                itemList.add(convertedItem);
            }
        }
        receiptData.setItems(itemList);
        return receiptData;
    }
}
