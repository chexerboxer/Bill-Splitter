package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.logout.LogoutController;

//components
import view.components.Sidebar;

/**
 * The View for when the user is logged into the program.
 */
public class DashboardView extends JPanel implements PropertyChangeListener {

    private final String viewName = "dashboard";
    private final DashboardViewModel dashboardViewModel;
    private DashboardController dashboardController;

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
        setLayout(new BorderLayout());

        sidebarPanel = new Sidebar(dashboardController, changePasswordController, logoutController, currentState);
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
                BillCardPanel billCard = new BillCardPanel(currentState.getUserBillsData().get(billId),
                        currentState.getUserBillsData(),
                        billId,
                        currentState.getUsername(),
                        dashboardController);
                billCard.setMaximumSize(new Dimension(getWidth(),100));

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

            remove(sidebarPanel);
            sidebarPanel = new Sidebar(dashboardController, changePasswordController, logoutController, this.dashboardViewModel.getState());
            add(sidebarPanel, BorderLayout.WEST);

        }

    }

    public String getViewName() {
        return viewName;
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }


}
