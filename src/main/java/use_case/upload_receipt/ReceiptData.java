package use_case.upload_receipt;

import entity.item.Item;

import java.util.List;

public class ReceiptData {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
