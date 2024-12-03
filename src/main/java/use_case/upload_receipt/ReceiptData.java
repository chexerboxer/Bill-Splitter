package use_case.upload_receipt;

import java.util.List;

import entity.item.Item;

public class ReceiptData {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
