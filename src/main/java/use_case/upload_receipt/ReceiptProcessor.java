package use_case.upload_receipt;


import entity.item.Item;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 */
public class ReceiptProcessor {
    private static final String BASE_URL = "https://api.veryfi.com";
    private static final String CREATED_DATE_KEY = "date";
    private static final String CURRENCY_TYPE_KEY = "currency_code";
    private static final String ITEM_LIST_KEY = "line_items";
    private static final String ITEM_NAME_KEY = "description";
    private static final String ITEM_QUANTITY_KEY = "quantity";
    private static final String ITEM_PRICE_KEY = "total";

    public static void main(String[] args) throws IOException {
        final String filename = "C:\\Users\\andre\\OneDrive\\Pictures\\Documents" +
                "\\University Documents\\CSC207\\Itemized+receipt+example.png";
        ReceiptProcessor receiptProcessor = new ReceiptProcessor();
        final String data = receiptProcessor.readReceipt(filename);
        System.out.println(data);
        ReceiptData receiptData = receiptProcessor.retrieveOcrData(data);
        System.out.println(receiptData);
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

    public ReceiptData retrieveOcrData(String data) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Map<String,String> dataRequest = new HashMap<>();
        dataRequest.put("file_data", data);


        JSONObject jo = new JSONObject(dataRequest);
        String json = jo.toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/v8/partner/documents")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "apikey " + "andreszeran:f78c6b1648552d301fb52dd56eab8558")
                .addHeader("Client-Id", "vrfezBrm32fb5Fhg5u9wRlOUnyJRvZHU3YIrBHp")
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
