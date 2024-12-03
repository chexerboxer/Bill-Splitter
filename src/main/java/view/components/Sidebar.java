package view.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import interface_adapter.bill_splitter.BillDisplayPresenter;
import interface_adapter.bill_splitter.BillDisplayState;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.logout.LogoutController;

/**
 * Initializes Sidebar panel for Dashboard and Bill Display views.
 */
public class Sidebar extends JPanel {
    
    private static final Color BACKGROUND_COLOUR = new Color(230, 230, 230);
    private static final int SIDEBAR_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BORDER_SPACING = 10;
    private static final int BUTTON_SPACING = 5;
    
    private static final int USERNAME_FONT_SIZE = 24;
    private static final int BUTTON_FONT_SIZE = 14;
    private static final String FONT = "Arial";
    
    // sidebar will be the same among all logged in views
    public Sidebar(DashboardController dashboardController,
                   ChangePasswordController changePasswordController,
                   LogoutController logoutController,
                   DashboardState currentState) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BACKGROUND_COLOUR);
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, getHeight()));

        // User profile section at the top
        final JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setBackground(BACKGROUND_COLOUR);

        final JLabel avatarLabel = new JLabel("☺");

        avatarLabel.setFont(new Font(FONT, Font.PLAIN, USERNAME_FONT_SIZE));

        final JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(BACKGROUND_COLOUR);

        final JLabel usernameLabel = new JLabel(currentState.getUsername());
        userInfoPanel.add(usernameLabel);

        profilePanel.add(avatarLabel);
        profilePanel.add(userInfoPanel);

        // Navigation buttons
        final JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        navigationPanel.setBackground(BACKGROUND_COLOUR);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_SPACING, BORDER_SPACING, 0, BORDER_SPACING));

        final JButton dashboardBtn = createSidebarButton("Dashboard");

        final JButton createBillBtn = createSidebarButton("Create new bill");
        createBillBtn.addActionListener(evt -> {
            final String newBillName = JOptionPane.showInputDialog("New bill name:", "");
            if (evt.getSource().equals(createBillBtn)) {
                if (newBillName != null) {
                    dashboardController.addBill(currentState.getUserBillsData(),
                            currentState.getUsername(), newBillName);
                }
            }

        });

        final JButton changePwBtn = createSidebarButton("Change password");
        changePwBtn.addActionListener(evt -> {
            final String toPassword = JOptionPane.showInputDialog(
                    "New Password:", "");
            if (evt.getSource().equals(changePwBtn)) {
                changePasswordController.execute(
                        currentState.getUsername(),
                        toPassword
                );
            }
        });

        final JButton logoutBtn = createSidebarButton("Logout");
        logoutBtn.addActionListener(evt -> {
            if (evt.getSource().equals(logoutBtn)) {
                logoutController.execute(
                        currentState.getUsername()
                );
            }
        });

        // Add components to sidebar in correct order
        add(profilePanel);
        // line break
        add(new JSeparator());
        // create stack of buttons
        navigationPanel.add(dashboardBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, BUTTON_SPACING))); 
        navigationPanel.add(createBillBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, BUTTON_SPACING)));
        navigationPanel.add(changePwBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, BUTTON_SPACING)));
        navigationPanel.add(logoutBtn);
        add(navigationPanel);

        // Add a glue component to push everything to the top
        add(Box.createVerticalGlue());
    }

    public Sidebar(BillDisplayPresenter billDisplayPresenter,
                   ChangePasswordController changePasswordController,
                   LogoutController logoutController,
                   BillDisplayState currentState) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BACKGROUND_COLOUR);
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, getHeight()));

        // User profile section at the top
        final JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setBackground(BACKGROUND_COLOUR);

        final JLabel avatarLabel = new JLabel("☺");
        avatarLabel.setFont(new Font(FONT, Font.PLAIN, USERNAME_FONT_SIZE));

        final JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(BACKGROUND_COLOUR);

        final JLabel usernameLabel = new JLabel(currentState.getUsername());
        userInfoPanel.add(usernameLabel);

        profilePanel.add(avatarLabel);
        profilePanel.add(userInfoPanel);

        // Navigation buttons
        final JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        navigationPanel.setBackground(BACKGROUND_COLOUR);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_SPACING, BORDER_SPACING, 0, BORDER_SPACING));

        final JButton dashboardBtn = createSidebarButton("Dashboard");
        dashboardBtn.addActionListener(evt -> {
            if (evt.getSource().equals(dashboardBtn)) {
                billDisplayPresenter.switchToDashboardView(currentState.getUsername());

            }
        });

        final JButton createBillBtn = createSidebarButton("Create new bill");
        createBillBtn.addActionListener(evt -> {
            final String newBillName = JOptionPane.showInputDialog("New bill name:", "");
            if (evt.getSource().equals(createBillBtn)) {
                if (newBillName != null) {
                    billDisplayPresenter.addBill(currentState.getUsername(), newBillName);
                }
            }
        });

        final JButton changePwBtn = createSidebarButton("Change password");
        changePwBtn.addActionListener(evt -> {
            final String toPassword = JOptionPane.showInputDialog(
                    "New Password:", "");
            if (evt.getSource().equals(changePwBtn)) {
                changePasswordController.execute(
                        currentState.getUsername(),
                        toPassword
                );
            }
        });

        final JButton logoutBtn = createSidebarButton("Logout");
        logoutBtn.addActionListener(evt -> {
            if (evt.getSource().equals(logoutBtn)) {
                logoutController.execute(
                        currentState.getUsername()
                );
            }
        });

        // Add components to sidebar in correct order
        add(profilePanel);
        // line break
        add(new JSeparator());
        // create stack of buttons
        navigationPanel.add(dashboardBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, BUTTON_SPACING))); 
        navigationPanel.add(createBillBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, BUTTON_SPACING))); 
        navigationPanel.add(changePwBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, BUTTON_SPACING))); 
        navigationPanel.add(logoutBtn);
        add(navigationPanel);

        // Add a glue component to push everything to the top
        add(Box.createVerticalGlue());
    }

    private static JButton createSidebarButton(String text) {
        final JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setMaximumSize(new Dimension(SIDEBAR_WIDTH, BUTTON_HEIGHT));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font(FONT, Font.PLAIN, BUTTON_FONT_SIZE));
        return button;
    }
}
