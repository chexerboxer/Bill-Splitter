package use_case.manage_items;

import entity.bill.Bill;
import entity.item.Item;
import entity.item.ItemFactory;
import java.util.HashMap;

/**
 * The Manage Item Interactor.
 */
public class ManageItemInteractor implements ManageItemInputBoundary {
    private final ManageItemDataAccessInterface billDataAccess;
    private final ManageItemOutputBoundary manageItemPresenter;
    private final ItemFactory itemFactory;

    /**
     * Creates a new ManageItemInteractor.
     * @param billDataAccess the data access interface for bills
     * @param manageItemPresenter the presenter for the manage item use case
     * @param itemFactory the factory for creating new items
     */
    public ManageItemInteractor(ManageItemDataAccessInterface billDataAccess,
                                ManageItemOutputBoundary manageItemPresenter,
                                ItemFactory itemFactory) {
        this.billDataAccess = billDataAccess;
        this.manageItemPresenter = manageItemPresenter;
        this.itemFactory = itemFactory;
    }

    @Override
    public void execute(ManageItemInputData manageItemInputData) {
        Bill bill = billDataAccess.getBill(manageItemInputData.getBillId());

        if (bill == null) {
            manageItemPresenter.prepareFailView("Bill not found.");
            return;
        }

        if (manageItemInputData.isAddOperation()) {
            Item newItem = itemFactory.create(
                    manageItemInputData.getItemName(),
                    manageItemInputData.getItemCost()
            );
            bill.addItem(newItem);
        } else {
            Item itemToRemove = bill.getItems().get(manageItemInputData.getItemId());
            if (itemToRemove == null) {
                manageItemPresenter.prepareFailView("Item not found.");
                return;
            }
            bill.removeItem(itemToRemove);
        }

        billDataAccess.saveBill(bill);

        ManageItemOutputData outputData = new ManageItemOutputData(
                bill.getId(),
                bill.getItems(),
                bill.getTotalAmount(),
                manageItemInputData.isAddOperation()
        );

        manageItemPresenter.prepareSuccessView(outputData);
    }
}