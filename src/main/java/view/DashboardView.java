package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.logout.LogoutController;

/**
 * The View for when the user is logged into the program.
 */
public class DashboardView extends JPanel implements LoggedInPageView, PropertyChangeListener {

    private final String viewName = "dashboard";
    private final DashboardViewModel dashboardViewModel;
    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;

    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private JPanel allBillsPanel = new JPanel();

    private final int COLUMNS_NUM = 3;

    public DashboardView(DashboardViewModel dashboardViewModel) {
        // connect view to interface adapters
        this.dashboardViewModel = dashboardViewModel;
        this.dashboardViewModel.addPropertyChangeListener(this);
        DashboardState currentState = dashboardViewModel.getState();

        // styling
        setSize(1000,700);
        setLayout(new BorderLayout());

        sidebarPanel = new JPanel();
        LoggedInPageView.createSidebar(sidebarPanel, changePasswordController, logoutController, currentState);

        createMainContent(currentState);

        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void createMainContent(DashboardState currentState) {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // title
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("All Your Bills");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerPanel.setMaximumSize(new Dimension(300, 100));

        headerPanel.add(titleLabel);


        // based on the bills the user is logged as being a member of,
            // make a unique card panel for each and add them to the grid layout
        // set rows to 0 to make row number flexible
        allBillsPanel.setLayout(new GridLayout(0, COLUMNS_NUM, 10, 10));

        // create bill panels depending on what bills the user is a part of
        settingBills(currentState);

        // nest bill grid into scrollable pane
        JScrollPane scrollAllBills = new JScrollPane(allBillsPanel);

        // hide scrollbar + adjust speed
        scrollAllBills.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollAllBills.getVerticalScrollBar().setUnitIncrement(10);
        scrollAllBills.getVerticalScrollBar().setBlockIncrement(30);


        // add all panels to the main panel
        mainContentPanel.add(headerPanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(scrollAllBills);
    }

    private void settingBills(DashboardState currentState) {
        allBillsPanel.removeAll();

        if (currentState.getUserBillsData() != null) {
            for (int billId: currentState.getUserBillsData().keySet()) {
                BillCardPanel billCard = new BillCardPanel(currentState.getUserBillsData().get(billId), billId);
                billCard.setPreferredSize(new Dimension(100,100));
                allBillsPanel.add(billCard);
            }
        }

        allBillsPanel.revalidate();
        allBillsPanel.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            this.settingBills(this.dashboardViewModel.getState());

        }

    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }


}
