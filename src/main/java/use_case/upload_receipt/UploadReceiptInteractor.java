package use_case.upload_receipt;

import static app.Constants.API_KEY;
import static app.Constants.BASE_URL;
import static app.Constants.CLIENT_ID;
import static app.Constants.OCR_URI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import data_access.FileDAO;
import entity.bill.Bill;
import entity.item.Item;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Receipt Processor for the Upload Receipt Use Case. Take's in an image as input and returns a ReceiptData Object
 */
public class UploadReceiptInteractor implements UploadReceiptInputBoundary {
    private static final String CREATED_DATE_KEY = "date";
    private static final String CURRENCY_TYPE_KEY = "currency_code";

    private static final String ITEM_LIST_KEY = "line_items";
    private static final String ITEM_NAME_KEY = "description";
    private static final String ITEM_QUANTITY_KEY = "quantity";
    private static final String ITEM_PRICE_KEY = "total";
    private static final String FILE_DATA = "file_data";

    private final FileDAO userDataAccessObject;
    private final UploadReceiptOutputBoundary uploadReceiptPresenter;

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

        final String fileName = uploadReceiptInputData.getReceiptFileName();
        final String data = readReceipt(fileName);
        final OkHttpClient client = new OkHttpClient();
        final Map<String, String> dataRequest = new HashMap<>();
        dataRequest.put(FILE_DATA, data);

        final JSONObject jo = new JSONObject(dataRequest);
        final String json = jo.toString();

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
     * Reads the inputted receipt and converts it into a base 64 encoded String.
     *
     * @param filename the file name of the image
     * @return the file encoded into a base 64 String
     * @throws IOException when abd reading happen
     */
    private String readReceipt(String filename) throws IOException {
        byte[] fileContent = null;
        fileContent = FileUtils.readFileToByteArray(new File(filename));

        final String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }

    /**
     * Private helper method for retrieveOcrData.
     *
     * @param jo the JsonObject created by the API call
     * @return ReceiptData object with relevant info taken from jo
     */
    private ReceiptData generateData(JSONObject jo) {
        ReceiptData receiptData = new ReceiptData();

        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < jo.getJSONArray(ITEM_LIST_KEY).length(); i++) {
            JSONObject joItem = jo.getJSONArray(ITEM_LIST_KEY).getJSONObject(i);
            Item convertedItem = new Item(joItem.getString(ITEM_NAME_KEY), joItem.getFloat(ITEM_PRICE_KEY) * joItem.getFloat(ITEM_QUANTITY_KEY));
            itemList.add(convertedItem);
        }
        receiptData.setItems(itemList);
        return receiptData;
    }
}
