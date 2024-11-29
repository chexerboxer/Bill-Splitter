package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// components
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.logout.LogoutController;

public class BillDisplayView extends JPanel implements LoggedInPageView, PropertyChangeListener {
    private final String viewName = "bill splitter";
    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;

    private JPanel sidebarPanel;
    private JPanel mainContentPanel;

    private JPanel membersPanel;
    private JPanel itemsPanel;
    private DefaultTableModel tableModel;

    public BillDisplayView(DashboardViewModel dashboardViewModel) {
        final DashboardState currentState = dashboardViewModel.getState();

        setLayout(new BorderLayout());

        sidebarPanel = new JPanel();
        LoggedInPageView.createSidebar(sidebarPanel, changePasswordController, logoutController, currentState);

        createMainContent();

        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

    }


    private void createMainContent() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Walgreens Trip");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        LocalDate date = LocalDate.of(2024, 11, 4);
        JLabel dateLabel = new JLabel("Created on " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        dateLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(dateLabel);

        // Upload receipt section
        JPanel uploadPanel = new JPanel();
        uploadPanel.setBorder(new DashBorderRect(1));
        JLabel uploadLabel = new JLabel("Upload a receipt");
        uploadLabel.setForeground(Color.GRAY);
        uploadPanel.add(uploadLabel);

        // Members section
        createMembersSection();

        // Items table
        createItemsTable();

        // Add all sections to main content
        mainContentPanel.add(headerPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(uploadPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(membersPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(itemsPanel);
    }

    // helpers for createMainContent()
    private void createMembersSection() {
        membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));

        JLabel membersLabel = new JLabel("All bill members (Code: XXXXX)");
        membersLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel memberButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] members = {"Jennifer", "Carlson", "Alex", "Amy Long"};

        for (String member : members) {
            JButton memberButton = new JButton(member);
            memberButton.setBackground(Color.BLACK);
            memberButton.setForeground(Color.WHITE);
            memberButton.setBorderPainted(false);
            memberButtonsPanel.add(memberButton);
        }

        membersPanel.add(membersLabel);
        membersPanel.add(memberButtonsPanel);
    }

    private void createItemsTable() {
        itemsPanel = new JPanel(new BorderLayout());

        // Create header panel with title and add button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel allItemsLabel = new JLabel("All Items");
        allItemsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton addButton = new JButton("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddItemDialog());

        headerPanel.add(allItemsLabel);
        headerPanel.add(addButton);

        // Create the table model
        String[] columnNames = {"All Items", "Assigned Splits"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add initial data
        tableModel.addRow(new Object[]{"CHICKEN ALFREDO", "Jennifer, Carlson"});
        tableModel.addRow(new Object[]{"MARGARITA PIZZA", "Jennifer, Carlson, Alex, Amy Long"});
        tableModel.addRow(new Object[]{"ESPRESSO MARTINI X 2", "Jennifer, Amy Long"});

        JTable table = new JTable(tableModel);

        // Style the table
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setRowHeight(40);

        // Create a panel for the table with custom header
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(headerPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        itemsPanel.add(tablePanel, BorderLayout.CENTER);
    }

    private void showAddItemDialog() {
        // Create the dialog
//        JDialog dialog = new JDialog(this, "Add a new item...", true);
//        dialog.setLayout(new BorderLayout());
//        dialog.setSize(400, 300);
//        dialog.setLocationRelativeTo(this);

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        JLabel titleLabel = new JLabel("Add a new item...");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create input fields
        JTextField itemNameField = new JTextField();
        itemNameField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        itemNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        itemNameField.setPreferredSize(new Dimension(300, 40));
        itemNameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField itemCostField = new JTextField();
        itemCostField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        itemCostField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        itemCostField.setPreferredSize(new Dimension(300, 40));
        itemCostField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createButton = new JButton("Create!");
        createButton.setBackground(Color.BLACK);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setPreferredSize(new Dimension(100, 35));

        // Add close button to top-right corner
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
//        closeButton.addActionListener(e -> dialog.dispose());

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.add(closeButton);

        // Add action listener for create button
        createButton.addActionListener(e -> {
            String itemName = itemNameField.getText().trim();
            String itemCost = itemCostField.getText().trim();
            if (!itemName.isEmpty() && !itemCost.isEmpty()) {
                tableModel.addRow(new Object[]{itemName.toUpperCase(), ""});
//                dialog.dispose();
            }
        });

        // Add placeholder text to text fields
        itemNameField.putClientProperty("JTextField.placeholderText", "Item name");
        itemCostField.putClientProperty("JTextField.placeholderText", "Item cost");

        // Add components to main panel
        mainPanel.add(closePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(itemNameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(itemCostField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(createButton);
        mainPanel.add(buttonPanel);

//        dialog.add(mainPanel);
//        dialog.setUndecorated(true);
//        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
//        dialog.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("password")) {
            final DashboardState state = (DashboardState) evt.getNewValue();
            JOptionPane.showMessageDialog(null, "password updated for " + state.getUsername());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    /**
     * Set logout controller class.
     * @param logoutController parameter.
     */

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

}
