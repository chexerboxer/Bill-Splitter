package use_case.new_bill;

/**
 * The New Bill Interactor
 */
public class NewBillInteractor implements NewBillInputBoundary{
    private NewBillUserDataAccessInterface userDataAccessObject;
    private NewBillOutputBoundary newBillPresenter;

    public NewBillInteractor(NewBillUserDataAccessInterface userDataAccessObject
            , NewBillOutputBoundary NewBillPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.newBillPresenter = NewBillPresenter;
    }

    @Override
    public void execute(NewBillInputData newBillInputData) {
        final String billName = newBillInputData.getBillName();

        //default values
        final boolean useCaseFailed = false;
        userDataAccessObject.setBillName(null);
        final NewBillOutputData newBillOutputData = new NewBillOutputData(billName, useCaseFailed);
        newBillPresenter.prepareSuccessView(newBillOutputData);
    }
}
