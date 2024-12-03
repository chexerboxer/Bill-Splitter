package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.logout.LogoutController;
import view.components.Sidebar;

/**
 * The View for when the user is logged into the program.
 */
public class DashboardView extends JPanel implements PropertyChangeListener {
    private static final int PADDING = 20;
    private static final int BILL_CARD_PADDING = 10;
    private static final int BILL_CARD_MAX_SIZE = 100;
    private static final int SPACING = 10;

    private static final int SCROLL_BAR_UNIT_INC = 10;
    private static final int SCROLL_BAR_BLOCK_INC = 30;

    private static final int TITLE_FONT_SIZE = 30;

    private static final int COLUMNS_NUM = 3;

    private final String viewName = "dashboard";
    private final DashboardViewModel dashboardViewModel;
    private DashboardController dashboardController;

    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;

    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private JPanel allBillsPanel = new JPanel();

    public DashboardView(DashboardViewModel dashboardViewModel) {
        // connect view to interface adapters
        this.dashboardViewModel = dashboardViewModel;
        this.dashboardViewModel.addPropertyChangeListener(this);
        final DashboardState currentState = dashboardViewModel.getState();

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
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        // title
        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        final JLabel titleLabel = new JLabel("All Your Bills");
        titleLabel.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));

        headerPanel.add(titleLabel);

        // based on the bills the user is logged as being a member of,
        // make a unique card panel for each and add them to the grid layout

        // set rows to 0 to make row number flexible
        allBillsPanel.setLayout(new GridLayout(0, COLUMNS_NUM, BILL_CARD_PADDING, BILL_CARD_PADDING));

        // create bill panels depending on what bills the user is a part of
        settingBills(currentState);

        // nest bill grid into scrollable pane
        final JScrollPane scrollAllBills = new JScrollPane(allBillsPanel);

        // hide scrollbar + adjust speed
        scrollAllBills.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollAllBills.getVerticalScrollBar().setUnitIncrement(SCROLL_BAR_UNIT_INC);
        scrollAllBills.getVerticalScrollBar().setBlockIncrement(SCROLL_BAR_BLOCK_INC);

        // add all panels to the main panel
        mainContentPanel.add(headerPanel);
        mainContentPanel.add(Box.createVerticalStrut(SPACING));
        mainContentPanel.add(scrollAllBills);
    }

    private void settingBills(DashboardState currentState) {
        allBillsPanel.removeAll();

        if (currentState.getUserBillsData() != null) {
            for (int billId: currentState.getUserBillsData().keySet()) {
                final BillCardPanel billCard = new BillCardPanel(currentState.getUserBillsData().get(billId),
                        currentState.getUserBillsData(),
                        billId,
                        currentState.getUsername(),
                        dashboardController);
                billCard.setMaximumSize(new Dimension(getWidth(), BILL_CARD_MAX_SIZE));

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
            sidebarPanel = new Sidebar(dashboardController,
                    changePasswordController,
                    logoutController,
                    this.dashboardViewModel.getState());
            add(sidebarPanel, BorderLayout.WEST);

            revalidate();
            repaint();

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
