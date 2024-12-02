package view.components;

import interface_adapter.bill_splitter.BillDisplayPresenter;
import interface_adapter.bill_splitter.BillDisplayState;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.logout.LogoutController;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {

    // sidebar will be the same among all logged in views
    // TODO: add dashboard controller and add bill usecase to necessary buttons
    public Sidebar(DashboardController dashboardController,
                   ChangePasswordController changePasswordController,
                   LogoutController logoutController,
                   DashboardState currentState) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(230, 230, 230));
        setPreferredSize(new Dimension(200, getHeight()));

        // User profile section at the top
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setBackground(new Color(230, 230, 230));

        JLabel avatarLabel = new JLabel("☺");
        avatarLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(230, 230, 230));

        JLabel usernameLabel = new JLabel(currentState.getUsername());
        userInfoPanel.add(usernameLabel);

        profilePanel.add(avatarLabel);
        profilePanel.add(userInfoPanel);

        // Navigation buttons
        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        navigationPanel.setBackground(new Color(230, 230, 230));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JButton dashboardBtn = createSidebarButton("Dashboard");


        JButton createBillBtn = createSidebarButton("Create new bill");
        createBillBtn.addActionListener(e -> {
            String newBillName = JOptionPane.showInputDialog("New bill name:", "");
            if (e.getSource().equals(createBillBtn)) {
                if (newBillName != null) {
                    dashboardController.addBill(currentState.getUserBillsData(),
                            currentState.getUsername(),
                            newBillName);
                }
            }

        });

        JButton changePwBtn = createSidebarButton("Change password");
        changePwBtn.addActionListener(evt -> {
            String toPassword = JOptionPane.showInputDialog(
                    "New Password:", "");
            if (evt.getSource().equals(changePwBtn)) {
                changePasswordController.execute(
                        currentState.getUsername(),
                        toPassword
                );
            }
        });

        JButton logoutBtn = createSidebarButton("Logout");
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
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(createBillBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(changePwBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
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
//        setBackground(new Color(230, 230, 230));
        setBackground(Color.CYAN);
        setPreferredSize(new Dimension(200, getHeight()));

        // User profile section at the top
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setBackground(new Color(230, 230, 230));

        JLabel avatarLabel = new JLabel("☺");
        avatarLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
//        userInfoPanel.setBackground(new Color(230, 230, 230));

        JLabel usernameLabel = new JLabel(currentState.getUsername());
        userInfoPanel.add(usernameLabel);

        profilePanel.add(avatarLabel);
        profilePanel.add(userInfoPanel);

        // Navigation buttons
        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        navigationPanel.setBackground(new Color(230, 230, 230));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JButton dashboardBtn = createSidebarButton("Dashboard");
        dashboardBtn.addActionListener(e -> {
            if (e.getSource().equals(dashboardBtn)) {
                billDisplayPresenter.switchToDashboardView(currentState.getUsername());

            }
        });

        JButton createBillBtn = createSidebarButton("Create new bill");
        createBillBtn.addActionListener(e -> {
            String newBillName = JOptionPane.showInputDialog("New bill name:", "");
            if (e.getSource().equals(createBillBtn)) {
                if (newBillName != null) {
                    billDisplayPresenter.addBill(currentState.getUsername(), newBillName);
                }
            }
        });

        JButton changePwBtn = createSidebarButton("Change password");
        changePwBtn.addActionListener(evt -> {
            String toPassword = JOptionPane.showInputDialog(
                    "New Password:", "");
            if (evt.getSource().equals(changePwBtn)) {
                changePasswordController.execute(
                        currentState.getUsername(),
                        toPassword
                );
            }
        });

        JButton logoutBtn = createSidebarButton("Logout");
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
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(createBillBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(changePwBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(logoutBtn);
        add(navigationPanel);

        // Add a glue component to push everything to the top
        add(Box.createVerticalGlue());
    }

    private static JButton createSidebarButton(String text){
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setMaximumSize(new Dimension(200, 40));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }
}