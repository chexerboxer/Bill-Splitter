package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import data_access.FileDAO;
import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.item.Item;
import entity.item.ItemFactory;
import entity.split.Split;
import entity.split.SplitFactory;
import entity.users.CommonUserFactory;
import entity.users.User;
import entity.users.UserFactory;
import interface_adapter.signup.SignupController;
import interface_adapter.split_management.ClearBillController;
import interface_adapter.split_management.DistributeBillController;
import interface_adapter.split_management.ModifySplitController;
import interface_adapter.split_management.SplitManagementPresenter;
import use_case.split_management.SplitManagementOutputBoundary;
import use_case.split_management.clear_bill.ClearBillInputBoundary;
import use_case.split_management.clear_bill.ClearBillInteractor;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInputBoundary;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInteractor;
import use_case.split_management.modify_split.ModifySplitInputBoundary;
import use_case.split_management.modify_split.ModifySplitInteractor;


// TODO refractor into JPanel though shouldnt be bad cuz I removed all the dialogue stuff its still JFrame so I can test it right now
// TODO note: creating billdisplay viewmodel and viewstate shouldnt be bad jsut plug in the bill from the viewstate and the viewmodel stuff isnt long.
public class BillDisplayView extends JFrame {
    private FileDAO userDataAccessObject;
    private Bill bill;
    private ClearBillController clearBillController;
    private DistributeBillController distributeBillController;
    private ModifySplitController modifySplitController;
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private JPanel membersPanel;
    private JPanel itemsPanel;
    private DefaultTableModel tableModel;


    // TODO NEED BILL DISPLAY VIEW MODEL, move the bill into the bill display state thats to be created.
    public BillDisplayView(FileDAO userDataAccessObject, Bill bill) {
        this.userDataAccessObject = userDataAccessObject;
        this.bill = bill;
        setTitle("Billsplitter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createSidebar();
        createMainContent();

        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        setSize(1200, 700);
        setLocationRelativeTo(null);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(230, 230, 230));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

        // User profile section at the top
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setBackground(new Color(230, 230, 230));

        JLabel avatarLabel = new JLabel("☺");
        avatarLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(230, 230, 230));


        // TODO change these username to match.
        JLabel usernameLabel = new JLabel("User123");
        JLabel manageLabel = new JLabel("Manage account");
        manageLabel.setForeground(Color.GRAY);

        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(manageLabel);

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


        // TODO idk whether this is needed or not as this is already the page to manage splits.
        JButton iousBtn = createSidebarButton("IOUs");
        iousBtn.addActionListener(e -> {
            System.out.println("Navigate to IOUs");
        });

        JButton createBillBtn = createSidebarButton("Create new bill");
        createBillBtn.addActionListener(e -> {
            System.out.println("Create new bill");
        });

        // Add components to sidebar in correct order
        sidebarPanel.add(profilePanel);
        sidebarPanel.add(new JSeparator());
        navigationPanel.add(dashboardBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(iousBtn);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between buttons
        navigationPanel.add(createBillBtn);
        sidebarPanel.add(navigationPanel);

        // Add a glue component to push everything to the top
        sidebarPanel.add(Box.createVerticalGlue());
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setMaximumSize(new Dimension(200, 40));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }

    private void createMainContent() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));


        JLabel titleLabel = new JLabel(bill.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));




        JLabel dateLabel = new JLabel("Total Cost: " + bill.getTotalAmount());
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

    private void createMembersSection() {
        membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));

        JLabel membersLabel = new JLabel(String.format("All bill members (Code: %s)", bill.getId()));
        membersLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel memberButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // extract the names out of the list of users stored in the bill.
        ArrayList<Integer> users = bill.getUsers();
        String[] usernames = new String[users.size()];
        for (int i = 0;i < users.size(); i++){
            usernames[i] = userDataAccessObject.getUser(users.get(i)).getName();
        }


        for (String member : usernames) {
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
        JLabel addItemsLabel = new JLabel("Add Items");
        addItemsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton addItemButton = new JButton("+");
        addItemButton.setFont(new Font("Arial", Font.BOLD, 14));
        addItemButton.setFocusPainted(false);
        addItemButton.addActionListener(e -> showAddItemDialog(this));

        headerPanel.add(addItemsLabel);
        headerPanel.add(addItemButton);


        JLabel RemoveItemsLabel = new JLabel("Remove Items");
        RemoveItemsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton RemoveItemButton = new JButton("+");
        RemoveItemButton.setFont(new Font("Arial", Font.BOLD, 14));
        RemoveItemButton.setFocusPainted(false);
        RemoveItemButton.addActionListener(e -> showRemoveItemDialog(this));

        headerPanel.add(RemoveItemsLabel);
        headerPanel.add(RemoveItemButton);


        JLabel ModifySplitsLabel = new JLabel("Modify Splits");
        ModifySplitsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton ModifySplitsButton = new JButton("+");
        ModifySplitsButton.setFont(new Font("Arial", Font.BOLD, 14));
        ModifySplitsButton.setFocusPainted(false);
        ModifySplitsButton.addActionListener(e -> showModifySplitsDialog(this));

        headerPanel.add(ModifySplitsLabel);
        headerPanel.add(ModifySplitsButton);

        JLabel DistributeBillLabel = new JLabel("Distribute Bill");
        DistributeBillLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton DistributeBillButton = new JButton("+");
        DistributeBillButton.setFont(new Font("Arial", Font.BOLD, 14));
        DistributeBillButton.setFocusPainted(false);
        DistributeBillButton.addActionListener(e -> showDistributeBillDialog(this));

        headerPanel.add(DistributeBillLabel);
        headerPanel.add(DistributeBillButton);

        JLabel ClearBillLabel = new JLabel("Clear Bill");
        ClearBillLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton ClearBillButton = new JButton("+");
        ClearBillButton.setFont(new Font("Arial", Font.BOLD, 14));
        ClearBillButton.setFocusPainted(false);
        ClearBillButton.addActionListener(e -> ClearBillEvent(this));

        JLabel EditPriceLabel = new JLabel("Edit Price of an Item");
        EditPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton EditPriceButton = new JButton("+");
        EditPriceButton.setFont(new Font("Arial", Font.BOLD, 14));
        EditPriceButton.setFocusPainted(false);
        EditPriceButton.addActionListener(e -> EditPriceEvent(this));

        headerPanel.add(ClearBillLabel);
        headerPanel.add(ClearBillButton);
        headerPanel.add(EditPriceLabel);
        headerPanel.add(EditPriceButton);

        // Create the table model
        String[] columnNames = {"All Items", "Assigned Splits"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int itemId : bill.getItems().keySet()){
            Item item = bill.getItems().get(itemId);
            String itemcolContent = item.getName() + ": " + item.getCost() + "$";

            ArrayList<Integer> users = userDataAccessObject.usersSplittingItem(itemId, bill.getId());

            ArrayList<String> usersStrings = new ArrayList<>();
            for (int i = 0; i < users.size(); i++){
                // all of these users are returns of usersSplittingItem thus has some split in the item.
                User user = userDataAccessObject.getUser(users.get(i));

                usersStrings.add(user.getName() + ": " + user.distributedAmount(itemId, bill.getId()) + "$");

            }
            // This has to be the most scuffed solution ever lmao
            // This table thing doesnt display multiple lines if there is jsut a line break in the string
            // so i gotta use html to display multiple lines.
            String usercolContent = String.join("<br>", usersStrings);
            usercolContent = "<html>" + usercolContent + "</html>";


            tableModel.addRow(new Object[]{itemcolContent, usercolContent});

        }

        JTable table = new JTable(tableModel);

        // Style the table
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 24));
        table.setRowHeight(80);

        // Create a panel for the table with custom header
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(headerPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        itemsPanel.add(tablePanel, BorderLayout.CENTER);
    }

    private void showRemoveItemDialog(JFrame parent) {
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        JLabel titleLabel = new JLabel("Select Items to remove");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(titleLabel);

        // generate an inverse items which maps the name of the item to its id.
        Map<String, Integer> reverseItems = new HashMap<>();
        for(Map.Entry<Integer, Item> entry : bill.getItems().entrySet()){
            reverseItems.put(entry.getValue().getName(), entry.getKey());
        }

        ArrayList<JCheckBox> checkBoxes =  new ArrayList<>();
        for (String itemName : reverseItems.keySet()){
            JCheckBox checkBox = new JCheckBox(itemName);
            mainPanel.add(checkBox);
            checkBoxes.add(checkBox);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton removeButton = new JButton("Remove!");
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.setPreferredSize(new Dimension(100, 35));

        // Add action listener for create button
        removeButton.addActionListener(e -> {
            // goes through all that is selected then remove them from DAO then repain.

            for (JCheckBox checkBox : checkBoxes){
                if(checkBox.isSelected()){
                    // remove item from bill
                    System.out.println(checkBox.getText());
                    bill.removeItem(reverseItems.get(checkBox.getText()));
                    userDataAccessObject.setBill(bill.getId(), bill);

                    // remove splits that use this item in this bill
                    for (int userId : bill.getUsers()){
                        User user = userDataAccessObject.getUser(userId);
                        user.removeSplit(reverseItems.get(checkBox.getText()), bill.getId());
                        userDataAccessObject.setUser(user.getId(), user);
                    }

                }
            }

            // This part of the code makes the parent display redraw itself after updating the DAO.
            this.remove(mainContentPanel);
            createMainContent();
            parent.add(mainContentPanel, BorderLayout.CENTER);
            parent.repaint();
            parent.revalidate();


            JOptionPane.getRootFrame().dispose();

        });




        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(removeButton);
        mainPanel.add(buttonPanel);


        JOptionPane.showMessageDialog(null, mainPanel,"Remove Items", JOptionPane.INFORMATION_MESSAGE);
    }

    private void ClearBillEvent(JFrame parent) {

        // change the values in DAO
        clearBillController.execute(bill.getId());

        // redraw main content panel with updated values
        this.remove(mainContentPanel);
        createMainContent();
        parent.add(mainContentPanel, BorderLayout.CENTER);
        parent.repaint();
        parent.revalidate();

    }


    private void showDistributeBillDialog(JFrame parent) {
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        JLabel titleLabel = new JLabel("Select people to distribute among");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(titleLabel);


        // Create input fields
        // generate an inverse users which maps the name of the user to its id.
        Map<String, Integer> reverseUsers = new HashMap<>();
        for(int userId : bill.getUsers()){
            String userName = userDataAccessObject.getUser(userId).getName();
            reverseUsers.put(userName, userId);
        }

        ArrayList<JCheckBox> checkBoxes =  new ArrayList<>();
        for (String userName : reverseUsers.keySet()){
            JCheckBox checkBox = new JCheckBox(userName);
            mainPanel.add(checkBox);
            checkBoxes.add(checkBox);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton DistributeButton = new JButton("Distribute!");
        DistributeButton.setBackground(Color.BLACK);
        DistributeButton.setForeground(Color.WHITE);
        DistributeButton.setFocusPainted(false);
        DistributeButton.setBorderPainted(false);
        DistributeButton.setPreferredSize(new Dimension(100, 35));

        // Add action listener for create button
        DistributeButton.addActionListener(e -> {
            // goes through all that is selected then distribute to them then repaint.

            ArrayList<Integer> usersSplitting = new ArrayList<>();
            for (JCheckBox checkBox : checkBoxes){
                if(checkBox.isSelected()){
                    // if the user name is selected, add them to a list then call the controller for distribute.

                    usersSplitting.add(reverseUsers.get(checkBox.getText()));
                }
            }
            distributeBillController.execute(bill.getId(), usersSplitting);


            // This part of the code makes the parent display redraw itself after updating the DAO.
            this.remove(mainContentPanel);
            createMainContent();
            parent.add(mainContentPanel, BorderLayout.CENTER);
            parent.repaint();
            parent.revalidate();


            JOptionPane.getRootFrame().dispose();

        });




        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(DistributeButton);
        mainPanel.add(buttonPanel);



        JOptionPane.showMessageDialog(null, mainPanel,"Distribute Bill", JOptionPane.INFORMATION_MESSAGE);
    }

    private void EditPriceEvent(JFrame parent) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("Select item to edit price of");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(titleLabel);
        Map<String, Integer> reverseItems = new HashMap<>();
        for(Map.Entry<Integer, Item> entry : bill.getItems().entrySet()){
            reverseItems.put(entry.getValue().getName(), entry.getKey());
        }
        final JComboBox<String> itemSelection =
                new JComboBox<>(reverseItems.keySet().toArray(new String[reverseItems.size()]));
        mainPanel.add(itemSelection);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField newPriceField = new JTextField(20);
        inputPanel.add(new JLabel("New Price:"));
        inputPanel.add(newPriceField);

        mainPanel.add(inputPanel);
        int result = JOptionPane.showConfirmDialog(
                null,
                mainPanel,
                "Edit Item Price",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION){
            try {
                float newPrice = Float.valueOf(newPriceField.getText());
                int itemId = reverseItems.get(itemSelection.getSelectedItem());
                Item item = bill.getItems().get(itemId);
                bill.setItem(itemId, item);
                item.setCost(newPrice);
                userDataAccessObject.setBill(bill.getId(), bill);
                this.remove(mainContentPanel);
                createMainContent();
                parent.add(mainContentPanel, BorderLayout.CENTER);
                parent.repaint();
                parent.revalidate();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid price.");
            }
        }
    }


    private void showModifySplitsDialog(JFrame parent) {
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        JLabel titleLabel = new JLabel("Select people to change split value by");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(titleLabel);

        // Create input fields

        // generate an inverse users which maps the name of the user to its id.
        Map<String, Integer> reverseUsers = new HashMap<>();
        for(int userId : bill.getUsers()){
            String userName = userDataAccessObject.getUser(userId).getName();
            reverseUsers.put(userName, userId);
        }

        // generate an inverse items which maps the name of the item to its id.
        Map<String, Integer> reverseItems = new HashMap<>();
        for(Map.Entry<Integer, Item> entry : bill.getItems().entrySet()){
            reverseItems.put(entry.getValue().getName(), entry.getKey());
        }


        // have drop down menu for which user to modify.
        final JComboBox<String> userSelection =
                new JComboBox<>(reverseUsers.keySet().toArray(new String[reverseUsers.size()]));
        mainPanel.add(userSelection);

        // have drop down menu for which item to modify.
        final JComboBox<String> itemSelection =
                new JComboBox<>(reverseItems.keySet().toArray(new String[reverseItems.size()]));
        mainPanel.add(itemSelection);


        // have amount modified by
        JTextField AmountModifiedField = new JTextField("Please enter how much the split is modified here");
        AmountModifiedField.setPreferredSize(new Dimension(300, 30));
        AmountModifiedField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                AmountModifiedField.setText("");
            }
            public void focusLost(FocusEvent e) {
                if(AmountModifiedField.getText().isEmpty())
                    AmountModifiedField.setText("Please enter item cost here");
            }
        });

        mainPanel.add(AmountModifiedField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ModifyButton = new JButton("Modify!");
        ModifyButton.setBackground(Color.BLACK);
        ModifyButton.setForeground(Color.WHITE);
        ModifyButton.setFocusPainted(false);
        ModifyButton.setBorderPainted(false);
        ModifyButton.setPreferredSize(new Dimension(100, 35));

        // Add action listener for create button
        ModifyButton.addActionListener(e -> {
            int userId = reverseUsers.get(userSelection.getSelectedItem());
            int itemId = reverseItems.get(itemSelection.getSelectedItem());

            try{
                float amountModified = Float.valueOf(AmountModifiedField.getText());

                modifySplitController.execute(amountModified, bill.getId(), itemId, userId);


                // This part of the code makes the parent display redraw itself after updating the DAO.
                this.remove(mainContentPanel);
                createMainContent();
                parent.add(mainContentPanel, BorderLayout.CENTER);
                parent.repaint();
                parent.revalidate();


                JOptionPane.getRootFrame().dispose();
            } catch (NumberFormatException ex){
                // This catches when the user input something that is not a float, thus have a pop up saying they
                // gotta input a float.
                JOptionPane.showMessageDialog(parent, "The amount modified is not a number. Try again.");

            }



            JOptionPane.getRootFrame().dispose();

        });




        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(ModifyButton);
        mainPanel.add(buttonPanel);


        JOptionPane.showMessageDialog(null, mainPanel,"Modify Split", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddItemDialog(JFrame parent) {

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        JLabel titleLabel = new JLabel("Add a new item...");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create input fields
        JTextField itemNameField = new JTextField("Please enter item name here");
        itemNameField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        itemNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        itemNameField.setPreferredSize(new Dimension(300, 40));
        itemNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        itemNameField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                itemNameField.setText("");
            }
            public void focusLost(FocusEvent e) {
                if(itemNameField.getText().isEmpty())
                    itemNameField.setText("Please enter item name here");

            }
        });


        JTextField itemCostField = new JTextField("Please enter item cost here");
        itemCostField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        itemCostField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        itemCostField.setPreferredSize(new Dimension(300, 40));
        itemCostField.setFont(new Font("Arial", Font.PLAIN, 14));
        itemCostField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                itemCostField.setText("");
            }
            public void focusLost(FocusEvent e) {
                if(itemCostField.getText().isEmpty())
                    itemCostField.setText("Please enter item cost here");
            }
        });

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createButton = new JButton("Create!");
        createButton.setBackground(Color.BLACK);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setPreferredSize(new Dimension(100, 35));

        // dont need anymore because changed from dialogue to optionpane.

//        // Add close button to top-right corner
//        JButton closeButton = new JButton("×");
//        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
//        closeButton.setBorderPainted(false);
//        closeButton.setContentAreaFilled(false);
//        closeButton.setFocusPainted(false);
//        closeButton.addActionListener(e -> JOptionPane.getRootFrame().dispose());
//
//        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        closePanel.add(closeButton);

        // Add action listener for create button
        createButton.addActionListener(e -> {
            String itemName = itemNameField.getText().trim();
            String itemCost = itemCostField.getText().trim();
            if (!itemName.isEmpty() && !itemCost.isEmpty()) {
                try{
                    float itemCostFloat = Float.valueOf(itemCost);

                    ItemFactory itemFactory = new ItemFactory();
                    bill.addItem(itemFactory.create(itemName, itemCostFloat));
                    userDataAccessObject.setBill(bill.getId(), bill);

                    // This part of the code makes the parent display redraw itself after updating the DAO.
                    this.remove(mainContentPanel);
                    createMainContent();
                    parent.add(mainContentPanel, BorderLayout.CENTER);
                    parent.repaint();
                    parent.revalidate();


                    JOptionPane.getRootFrame().dispose();
                } catch (NumberFormatException ex){
                    // This catches when the user input something that is not a float, thus have a pop up saying they
                    // gotta input a float.
                    JOptionPane.showMessageDialog(parent, "The cost is not a number. Try again.");

                }
            }
        });

        // Add placeholder text to text fields
        itemNameField.putClientProperty("JTextField.placeholderText", "Item name");
        itemCostField.putClientProperty("JTextField.placeholderText", "Item cost");

        // Add components to main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(itemNameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(itemCostField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(createButton);
        mainPanel.add(buttonPanel);


        JOptionPane.showMessageDialog(null, mainPanel,"Add Items", JOptionPane.INFORMATION_MESSAGE);

    }

    // Setters for the controllers, TODO add these in the AppBuilder when intializing the BillDisplayView. can refer to main when im doing testing.
    public void setClearBillController(ClearBillController controller) {
        this.clearBillController = controller;
    }

    public void setDistributeBillController(DistributeBillController controller) {
        this.distributeBillController = controller;
    }

    public void setModifySplitController(ModifySplitController controller) {
        this.modifySplitController = controller;
    }

    public static void main(String[] args) throws IOException {


        //test run to see whether it works.

        UserFactory userFactory = new CommonUserFactory();
        SplitFactory splitFactory = new SplitFactory();
        BillFactory billFactory = new BillFactory();
        ItemFactory itemFactory = new ItemFactory();
        FileDAO userDataAccessObject = new FileDAO(System.getProperty("user.dir") + "\\src\\test\\java\\DAO\\BillDisplayViewTest.csv"
                , billFactory, userFactory, itemFactory, splitFactory);

        ArrayList<Integer> userids = new ArrayList<>();
        userids.add(10);
        userids.add(11);
        userids.add(12);
        HashMap<Integer, Item> items = new HashMap<>();
        items.put(10, itemFactory.create("item1",10,32.2f));
        items.put(11, itemFactory.create("item2", 11, 42.1f));
        Bill bill1 = billFactory.create("testBillName", 10, userids);
        bill1.addItem(itemFactory.create("item1",10,32.2f));
        bill1.addItem(itemFactory.create("item2", 11, 42.1f));
        ArrayList<Split> splits = new ArrayList<>();
        splits.add(splitFactory.create(12,10,11));
        ArrayList<Split> splits2 = new ArrayList<>();
        splits2.add(splitFactory.create(10,10,10));
        splits2.add(splitFactory.create(12,10,11));
        ArrayList<Split> splits3 = new ArrayList<>();
        splits3.add(splitFactory.create(11,10,10));

        User user1 = userFactory.create("testpersonA", 12,"asd2123",splits);
        User user2 = userFactory.create("testpersonB", 10,"tasd", splits2);
        User user3 = userFactory.create("testpersonC", 11,"tasd", splits3);

        HashMap<Integer, Bill> bills = new HashMap<>();
        bills.put(bill1.getId(), bill1);
        HashMap<Integer, User> users = new HashMap<>();
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user3.getId(), user3);

        userDataAccessObject.setBills(bills);
        userDataAccessObject.setUsers(users);



        SwingUtilities.invokeLater(() -> {
            BillDisplayView view = new BillDisplayView(userDataAccessObject,userDataAccessObject.getBill(10));

            // set up controllers
            SplitManagementOutputBoundary splitManagementOutputBoundary = new SplitManagementPresenter();

            final ClearBillInputBoundary clearBillInteractor =
                    new ClearBillInteractor(userDataAccessObject, splitManagementOutputBoundary);

            final ClearBillController clearBillController1 = new ClearBillController(clearBillInteractor);

            final DistributeBillEvenInputBoundary distributeBillInteractor =
                    new DistributeBillEvenInteractor(userDataAccessObject, splitManagementOutputBoundary);

            final DistributeBillController distributeBillController1 = new DistributeBillController(distributeBillInteractor);

            final ModifySplitInputBoundary modifySplitInterator =
                    new ModifySplitInteractor(userDataAccessObject, splitManagementOutputBoundary);

            final ModifySplitController modifySplitController1 = new ModifySplitController(modifySplitInterator);

            view.setClearBillController(clearBillController1);

            view.setDistributeBillController(distributeBillController1);

            view.setModifySplitController(modifySplitController1);

            view.setVisible(true);
        });
    }
}

class RoundedBorder extends AbstractBorder {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius/2, this.radius/2, this.radius/2, this.radius/2);
    }
}

class DashBorderRect extends AbstractBorder {
    private int thickness;

    public DashBorderRect(int thickness) {
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,
                new float[]{5.0f}, 0.0f));
        g2d.drawRect(x, y, width - 1, height - 1);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }
}