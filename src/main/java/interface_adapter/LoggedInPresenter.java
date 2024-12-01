package interface_adapter;

public interface LoggedInPresenter {

    // make logged-in controller interface that the sidebar component will use to navigate back to the dashboard
    // DashboardController --> method will be empty since feeding the sidebar the dashboard controller means the user
        // is already on the dashboard
    // TODO: BillDisplayController --> implement method that navigates to dash
    void switchToDashboardView(String username);
}
