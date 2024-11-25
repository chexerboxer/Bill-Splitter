package use_case.upload_receipt;


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
 *
 */
public class ReceiptProcessor {
    private static final String CREATED_DATE_KEY = "date";
    private static final String CURRENCY_TYPE_KEY = "currency_code";
    private static final String ITEM_LIST_KEY = "line_items";
    private static final String ITEM_NAME_KEY = "description";
    private static final String ITEM_QUANTITY_KEY = "quantity";
    private static final String ITEM_PRICE_KEY = "total";
    public static final String FILE_DATA = "file_data";

    public static void main(String[] args) throws IOException {
        final String filePath = "src/main/java/data_access/receiptfiles.txt";
        ReceiptProcessor receiptProcessor = new ReceiptProcessor();
        final String filename = receiptProcessor.getReceiptPath(filePath);
        final String data = receiptProcessor.readReceipt(filename);
        System.out.println(filename);
        ReceiptData receiptData = receiptProcessor.retrieveOcrData(data);
    }

    public String readReceipt(String filename){
        byte[] fileContent = null;
        try {
            fileContent = FileUtils.readFileToByteArray(new File(filename));
        } catch (IOException e) {
            System.out.println("File Read Error: "+ e.getMessage());
        } finally{}
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }

    public String getReceiptPath(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String currentLine;
            String lastNontEmptyLine = null;

            while ((currentLine = br.readLine()) != null) {
                currentLine = currentLine.trim();
                if (!currentLine.isEmpty()) {
                    lastNontEmptyLine = currentLine;
                }
            }

            return lastNontEmptyLine;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ReceiptData retrieveOcrData(String data) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Map<String,String> dataRequest = new HashMap<>();
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
        return generateData(responseJson);
    }

    private ReceiptData generateData(JSONObject jo) throws IOException {
        ReceiptData receiptData = new ReceiptData();
        receiptData.setDate(jo.getString(CREATED_DATE_KEY));
        receiptData.setCurrencyType(jo.getString(CURRENCY_TYPE_KEY));
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < jo.getJSONArray(ITEM_LIST_KEY).length(); i++) {
            JSONObject joItem = jo.getJSONArray(ITEM_LIST_KEY).getJSONObject(i);
            Item convertedItem = new Item(joItem.getString(ITEM_NAME_KEY), joItem.getFloat(ITEM_PRICE_KEY));
            itemList.add(convertedItem);
        }
        receiptData.setItems(itemList);
        return receiptData;
    }
}
