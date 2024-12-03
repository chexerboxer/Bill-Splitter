package use_case.upload_receipt;

import java.util.List;

import entity.item.Item;

public class ReceiptData {
    private String date;
    private String currencyType;
    private List<Item> items;

    public void setDate(String date) {
        this.date = date;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
