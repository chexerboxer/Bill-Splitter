package view;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.logout.LogoutController;

import javax.swing.*;
import java.awt.*;

/**
 * Acts as a template for the various pages the user navigates between while logged in to the bill splitter.
 */
public interface LoggedInPageView {
    // sidebar will be the same among all logged in views
    static void createSidebar(JPanel sidebarPanel,
                              ChangePasswordController changePasswordController,
                              LogoutController logoutController,
                              DashboardState currentState){

        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(230, 230, 230));
        sidebarPanel.setPreferredSize(new Dimension(200, sidebarPanel.getHeight()));

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
        dashboardBtn.addActionListener(e -> {
            System.out.println("Navigate to Dashboard");
        });

        JButton createBillBtn = createSidebarButton("Create new bill");
        createBillBtn.addActionListener(e -> {
            System.out.println("Create new bill");
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
        sidebarPanel.add(profilePanel);
        sidebarPanel.add(new JSeparator());
        navigationPanel.add(dashboardBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(createBillBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(changePwBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(logoutBtn);
        sidebarPanel.add(navigationPanel);

        // Add a glue component to push everything to the top
        sidebarPanel.add(Box.createVerticalGlue());
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
