package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import data_access.FileDAO;
import entity.bill.Bill;
import entity.item.Item;
import entity.item.ItemFactory;
import entity.split.Split;
import entity.users.User;
import interface_adapter.bill_splitter.BillDisplayPresenter;
import interface_adapter.bill_splitter.BillDisplayState;
import interface_adapter.bill_splitter.BillDisplayViewModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.logout.LogoutController;
import interface_adapter.split_management.ClearBillController;
import interface_adapter.split_management.DistributeBillController;
import interface_adapter.split_management.ModifySplitController;
import interface_adapter.upload_receipt.UploadReceiptController;
import view.components.*;

public class BillDisplayView extends JPanel implements PropertyChangeListener {
    private static final String PLUS = "+"; 
    private static final String ARIAL = "Arial";
    
    private FileDAO userDataAccessObject;
    private Bill bill;
    private UploadReceiptController uploadReceiptController;
    private ClearBillController clearBillController;
    private BillDisplayPresenter billDisplayPresenter;
    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;
    private DistributeBillController distributeBillController;
    private ModifySplitController modifySplitController;
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;

    private final String viewName = "bill splitter";
    private final BillDisplayViewModel billDisplayViewModel;

    private JPanel membersPanel;
    private JPanel itemsPanel;
    private final JFileChooser fileChooser = new JFileChooser();
    private DefaultTableModel tableModel;

    public BillDisplayView(BillDisplayViewModel billDisplayViewModel) {
        this.billDisplayViewModel = billDisplayViewModel;
        this.billDisplayViewModel.addPropertyChangeListener(this);

    }

    /**
     * This method is responsible for drawing the main content of the user interface.
     * It sets the layout, retrieves the current bill information, and updates the
     * main content and sidebar components accordingly.
     * The method performs the following:
     * 1. Sets the layout manager to BorderLayout for the main container.
     * 2. Retrieves the current state of the bill display view model.
     * 3. Fetches the bill associated with the current state.
     * 4. Checks if the bill exists, and if so, updates the main content and sidebar panels.
     * 5. Re-validates and repaints the container to reflect the updated UI components.
     * 6. Adjusts the window size to a preset value (1200x700).
     */
    public void drawMainContent() {

        setLayout(new BorderLayout());
        final BillDisplayState currentState = billDisplayViewModel.getState();
        sidebarPanel = new Sidebar(billDisplayPresenter, changePasswordController, logoutController, currentState);

        bill = userDataAccessObject.getBill(currentState.getBillId());

        if (bill != null) {

            if (sidebarPanel != null) {
                remove(sidebarPanel);
            }

            if (mainContentPanel != null) {
                remove(mainContentPanel);
            }

            createMainContent();

            add(sidebarPanel, BorderLayout.WEST);
            add(mainContentPanel, BorderLayout.CENTER);
            revalidate();
            repaint();

            setSize(1200, 700);
        }

    }

    private void createMainContent() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header section
        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        final JLabel titleLabel = new JLabel(bill.getName());
        titleLabel.setFont(new Font(ARIAL, Font.BOLD, 24));

        final JLabel dateLabel = new JLabel("Total Cost: " + bill.getTotalAmount());
        dateLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(dateLabel);

        // Upload receipt section
        final JButton uploadButton = new JButton("Upload a receipt");
        uploadButton.setForeground(Color.GRAY);
        uploadButton.setPreferredSize(new Dimension(mainContentPanel.getWidth(), 50));
        uploadButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        uploadButton.setMinimumSize(new Dimension(100, 50));
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.setBorder(new DashBorderRect(1));
        uploadButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        fileChooser.setDialogTitle("Select a Receipt Image");
                        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                                "Image files (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"));
                        final int returnValue = fileChooser.showOpenDialog(null);

                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            final java.io.File selectedFile = fileChooser.getSelectedFile();
                            final String filepath = selectedFile.getAbsolutePath();
                            try {
                                JOptionPane.showMessageDialog(null, "File uploaded: " + filepath);
                                uploadReceiptController.execute(filepath, bill.getId());

                                // This part of the code makes the parent display redraw itself after updating the DAO.
                                remove(mainContentPanel);
                                createMainContent();
                                add(mainContentPanel, BorderLayout.CENTER);
                                repaint();
                                revalidate();

                            }
                            catch (IOException ext) {
                                JOptionPane.showMessageDialog(null, "Error: " + ext.getMessage());
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "File not uploaded");
                        }
                    }
                });

        // Members section
        createMembersSection();

        // Items table
        createItemsTable();

        // Add all sections to main content
        mainContentPanel.add(headerPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(uploadButton);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(membersPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(itemsPanel);
        revalidate();
        repaint();

    }

    private void createMembersSection() {
        membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));

        final JLabel membersLabel = new JLabel(String.format("All bill members (Code: %s)", bill.getId()));
        membersLabel.setFont(new Font(ARIAL, Font.BOLD, 16));

        final JPanel memberButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // extract the names out of the list of users stored in the bill.
        final ArrayList<Integer> users = bill.getUsers();
        final String[] usernames = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            usernames[i] = userDataAccessObject.getUser(users.get(i)).getName();
        }

        for (String member : usernames) {
            final JButton memberButton = new JButton(member);
            memberButton.setBackground(Color.BLACK);
            memberButton.setForeground(Color.WHITE);
            memberButton.setBorderPainted(false);
            memberButtonsPanel.add(memberButton);
        }

        // Add Members
        final JLabel addMembersLabel = new JLabel("Add Member");
        addMembersLabel.setFont(new Font(ARIAL, Font.BOLD, 14));
        final JButton addMembersButton = new JButton(PLUS);
        addMembersButton.setFont(new Font(ARIAL, Font.BOLD, 14));
        addMembersButton.setFocusPainted(false);
        addMembersButton.addActionListener(evt -> addMembersEvent(this));

        // Remove Members
        final JLabel removeMembersLabel = new JLabel("Remove Member");
        removeMembersLabel.setFont(new Font(ARIAL, Font.BOLD, 14));
        final JButton removeMembersButton = new JButton(PLUS);
        removeMembersButton.setFont(new Font(ARIAL, Font.BOLD, 14));
        removeMembersButton.setFocusPainted(false);
        removeMembersButton.addActionListener(evt -> removeMembersEvent(this));

        memberButtonsPanel.add(addMembersLabel);
        memberButtonsPanel.add(addMembersButton);
        memberButtonsPanel.add(removeMembersLabel);
        memberButtonsPanel.add(removeMembersButton);

        membersPanel.add(membersLabel);
        membersPanel.add(memberButtonsPanel);

    }

    private void createItemsTable() {
        itemsPanel = new JPanel(new BorderLayout());

        // Create header panel with title and add button
        final JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        final JLabel addItemsLabel = new JLabel("Add Items");
        addItemsLabel.setFont(new Font(ARIAL, Font.BOLD, 14));

        final JButton addItemButton = new JButton(PLUS);
        addItemButton.setFont(new Font(ARIAL, Font.BOLD, 14));
        addItemButton.setFocusPainted(false);
        addItemButton.addActionListener(evt -> showAddItemDialog(this));

        headerPanel.add(addItemsLabel);
        headerPanel.add(addItemButton);

        final JLabel removeItemsLabel = new JLabel("Remove Items");
        removeItemsLabel.setFont(new Font(ARIAL, Font.BOLD, 14));

        final JButton removeItemButton = new JButton(PLUS);
        removeItemButton.setFont(new Font(ARIAL, Font.BOLD, 14));
        removeItemButton.setFocusPainted(false);
        removeItemButton.addActionListener(evt -> showRemoveItemDialog(this));

        headerPanel.add(removeItemsLabel);
        headerPanel.add(removeItemButton);

        final JLabel modifySplitsLabel = new JLabel("Modify Splits");
        modifySplitsLabel.setFont(new Font(ARIAL, Font.BOLD, 14));

        final JButton modifySplitsButton = new JButton(PLUS);
        modifySplitsButton.setFont(new Font(ARIAL, Font.BOLD, 14));
        modifySplitsButton.setFocusPainted(false);
        modifySplitsButton.addActionListener(evt -> showModifySplitsDialog(this));

        headerPanel.add(modifySplitsLabel);
        headerPanel.add(modifySplitsButton);

        final JLabel distributeBillLabel = new JLabel("Distribute Bill");
        distributeBillLabel.setFont(new Font(ARIAL, Font.BOLD, 14));

        final JButton distributeBillButton = new JButton(PLUS);
        distributeBillButton.setFont(new Font(ARIAL, Font.BOLD, 14));
        distributeBillButton.setFocusPainted(false);
        distributeBillButton.addActionListener(evt -> showDistributeBillDialog(this));

        headerPanel.add(distributeBillLabel);
        headerPanel.add(distributeBillButton);

        final JLabel clearBillLabel = new JLabel("Clear Splits");
        clearBillLabel.setFont(new Font(ARIAL, Font.BOLD, 14));

        final JButton clearBillButton = new JButton(PLUS);
        clearBillButton.setFont(new Font(ARIAL, Font.BOLD, 14));
        clearBillButton.setFocusPainted(false);
        clearBillButton.addActionListener(evt -> clearBillEvent(this));

        // Edit price
        final JLabel editPriceLabel = new JLabel("Edit Price of an Item");
        editPriceLabel.setFont(new Font(ARIAL, Font.BOLD, 14));

        final JButton editPriceButton = new JButton(PLUS);
        editPriceButton.setFont(new Font(ARIAL, Font.BOLD, 14));
        editPriceButton.setFocusPainted(false);
        editPriceButton.addActionListener(evt -> editPriceEvent(this));

        headerPanel.add(clearBillLabel);
        headerPanel.add(clearBillButton);
        headerPanel.add(editPriceLabel);
        headerPanel.add(editPriceButton);

        // Create the table model
        final String[] columnNames = {"All Items", "Assigned Splits"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int itemId : bill.getItems().keySet()) {
            final Item item = bill.getItems().get(itemId);
            final String itemcolContent = item.getName() + ": " + item.getCost() + "$";

            final ArrayList<Integer> users = userDataAccessObject.usersSplittingItem(itemId, bill.getId());

            final ArrayList<String> usersStrings = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                // all of these users are returns of usersSplittingItem thus has some split in the item.
                final User user = userDataAccessObject.getUser(users.get(i));
                usersStrings.add(user.getName() + ": " + user.distributedAmount(itemId, bill.getId()) + "$");

            }
            // This has to be the most scuffed solution ever lmao
            // This table thing doesnt display multiple lines if there is jsut a line break in the string
            // so i gotta use html to display multiple lines.
            String usercolContent = String.join("<br>", usersStrings);
            usercolContent = "<html>" + usercolContent + "</html>";

            tableModel.addRow(new Object[]{itemcolContent, usercolContent});

        }

        final JTable table = new JTable(tableModel);

        // Style the table
        table.getTableHeader().setFont(new Font(ARIAL, Font.BOLD, 24));
        table.setRowHeight(80);

        // Create a panel for the table with custom header
        final JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(headerPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        itemsPanel.add(tablePanel, BorderLayout.CENTER);
    }

    private void addMembersEvent(JPanel parent) {
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        final JLabel titleLabel = new JLabel("Add Member");
        titleLabel.setFont(new Font(ARIAL, Font.BOLD, 22));
        mainPanel.add(titleLabel);
        final JTextField newMemberField = new JTextField(20);
        mainPanel.add(new JLabel("New Member: "));
        mainPanel.add(newMemberField);

        final int result = JOptionPane.showConfirmDialog(
                null,
                mainPanel,
                "Add a New Member",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            final String newUserName = newMemberField.getText();
            final Map<Integer, User> dataAccessObjectUserMap = userDataAccessObject.getAllUsers();
            final Map<String, Integer> reverseUsers = new HashMap<>();
            for (int key : dataAccessObjectUserMap.keySet()) {
                final String userName = dataAccessObjectUserMap.get(key).getName();
                reverseUsers.put(userName, key);
            }
            if (reverseUsers.containsKey(newUserName)) {
                final int userid = reverseUsers.get(newUserName);
                if (bill.getUsers().contains(userid)) {
                    JOptionPane.showMessageDialog(mainPanel, "Member already in bill.");
                } 
                else {
                    final int newuserId = reverseUsers.get(newUserName);
                    bill.addUser(newuserId);
                    userDataAccessObject.setBill(bill.getId(), bill);

                    this.remove(mainContentPanel);
                    createMainContent();
                    parent.add(mainContentPanel);
                    parent.revalidate();
                    parent.repaint();

                }
            }
            else {
                JOptionPane.showMessageDialog(mainPanel, "Error, user not found.");
            }
        }

    }

    private void removeMembersEvent(JPanel parent) {
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        final JLabel titleLabel = new JLabel("Remove Member");
        titleLabel.setFont(new Font(ARIAL, Font.BOLD, 22));
        mainPanel.add(titleLabel);

        final Map<String, Integer> reverseUsers = new HashMap<>();
        for (int userId : bill.getUsers()) {
            final String userName = userDataAccessObject.getUser(userId).getName();
            reverseUsers.put(userName, userId);
        }

        final JComboBox<String> userSelection =
                new JComboBox<>(reverseUsers.keySet().toArray(new String[reverseUsers.size()]));
        mainPanel.add(userSelection);

        final int result = JOptionPane.showConfirmDialog(
                null,
                mainPanel,
                "Remove item",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            final int userId = reverseUsers.get(userSelection.getSelectedItem());
            final User user = userDataAccessObject.getUser(userId);
            for (Split split : new ArrayList<>(user.getSplits())) {
                if (bill.getItems().containsKey(split.getItemId()) && split.getBillId() == bill.getId()) {
                    user.removeSplit(split.getItemId(), split.getBillId());
                }
            }

            userDataAccessObject.setUser(userId, user);

            bill.removeUser(userId);

            userDataAccessObject.setBill(bill.getId(), bill);

            // the member section is created and called in the mainContent panel already so dont have to change.
            this.remove(mainContentPanel);
            createMainContent();
            parent.add(mainContentPanel);
            parent.revalidate();
            parent.repaint();

        }
    }

    private void showRemoveItemDialog(JPanel parent) {
        // Create main panel with padding
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        final JLabel titleLabel = new JLabel("Select Items to remove");
        titleLabel.setFont(new Font(ARIAL, Font.BOLD, 15));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(titleLabel);

        // generate an inverse items which maps the name of the item to its id.
        final Map<String, Integer> reverseItems = new HashMap<>();
        for (Map.Entry<Integer, Item> entry : bill.getItems().entrySet()) {
            reverseItems.put(entry.getValue().getName(), entry.getKey());
        }

        final ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (String itemName : reverseItems.keySet()) {
            final JCheckBox checkBox = new JCheckBox(itemName);
            mainPanel.add(checkBox);
            checkBoxes.add(checkBox);
        }

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton removeButton = new JButton("Remove!");
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.setPreferredSize(new Dimension(100, 35));

        // Add action listener for create button
        removeButton.addActionListener(evt -> {
            // goes through all that is selected then remove them from DAO then repain.
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    // remove item from bill
                    System.out.println(checkBox.getText());
                    bill.removeItem(reverseItems.get(checkBox.getText()));
                    userDataAccessObject.setBill(bill.getId(), bill);

                    // remove splits that use this item in this bill
                    for (int userId : bill.getUsers()) {
                        final User user = userDataAccessObject.getUser(userId);
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

        JOptionPane.showMessageDialog(null, mainPanel, "Remove Items", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearBillEvent(JPanel parent) {

        // change the values in DAO
        clearBillController.execute(bill.getId());

        // redraw main content panel with updated values
        this.remove(mainContentPanel);
        createMainContent();
        parent.add(mainContentPanel, BorderLayout.CENTER);
        parent.repaint();
        parent.revalidate();

    }

    private void showDistributeBillDialog(JPanel parent) {
        // Create main panel with padding
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        final JLabel titleLabel = new JLabel("Select people to distribute among");
        titleLabel.setFont(new Font(ARIAL, Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(titleLabel);

        // Create input fields
        // generate an inverse users which maps the name of the user to its id.
        final Map<String, Integer> reverseUsers = new HashMap<>();
        for (int userId : bill.getUsers()) {
            final String userName = userDataAccessObject.getUser(userId).getName();
            reverseUsers.put(userName, userId);
        }

        final ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (String userName : reverseUsers.keySet()) {
            final JCheckBox checkBox = new JCheckBox(userName);
            mainPanel.add(checkBox);
            checkBoxes.add(checkBox);
        }

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton distributeButton = new JButton("Distribute!");
        distributeButton.setBackground(Color.BLACK);
        distributeButton.setForeground(Color.WHITE);
        distributeButton.setFocusPainted(false);
        distributeButton.setBorderPainted(false);
        distributeButton.setPreferredSize(new Dimension(100, 35));

        // Add action listener for create button
        distributeButton.addActionListener(evt -> {
            // goes through all that is selected then distribute to them then repaint.

            final ArrayList<Integer> usersSplitting = new ArrayList<>();
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
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
        buttonPanel.add(distributeButton);
        mainPanel.add(buttonPanel);

        JOptionPane.showMessageDialog(null, mainPanel, "Distribute Bill", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editPriceEvent(JPanel parent) {
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        final JLabel titleLabel = new JLabel("Select item to edit price of");
        titleLabel.setFont(new Font(ARIAL, Font.BOLD, 22));
        mainPanel.add(titleLabel);
        final Map<String, Integer> reverseItems = new HashMap<>();
        for (Map.Entry<Integer, Item> entry : bill.getItems().entrySet()) {
            reverseItems.put(entry.getValue().getName(), entry.getKey());
        }
        final JComboBox<String> itemSelection =
                new JComboBox<>(reverseItems.keySet().toArray(new String[reverseItems.size()]));
        mainPanel.add(itemSelection);

        final JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JTextField newPriceField = new JTextField(20);
        inputPanel.add(new JLabel("New Price:"));
        inputPanel.add(newPriceField);

        mainPanel.add(inputPanel);
        final int result = JOptionPane.showConfirmDialog(
                null,
                mainPanel,
                "Edit Item Price",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                final float newPrice = Float.valueOf(newPriceField.getText());
                final int itemId = reverseItems.get(itemSelection.getSelectedItem());
                final Item item = bill.getItems().get(itemId);
                bill.setItem(itemId, item);
                item.setCost(newPrice);
                userDataAccessObject.setBill(bill.getId(), bill);
                this.remove(mainContentPanel);
                createMainContent();
                parent.add(mainContentPanel, BorderLayout.CENTER);
                parent.repaint();
                parent.revalidate();

            }
            catch (NumberFormatException evt) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid price.");
            }
        }
    }

    private void showModifySplitsDialog(JPanel parent) {
        // Create main panel with padding
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        final JLabel titleLabel = new JLabel("Select people to change split value by");
        titleLabel.setFont(new Font(ARIAL, Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(titleLabel);

        // Create input fields

        // generate an inverse users which maps the name of the user to its id.
        final Map<String, Integer> reverseUsers = new HashMap<>();
        for (int userId : bill.getUsers()) {
            final String userName = userDataAccessObject.getUser(userId).getName();
            reverseUsers.put(userName, userId);
        }

        // generate an inverse items which maps the name of the item to its id.
        final Map<String, Integer> reverseItems = new HashMap<>();
        for (Map.Entry<Integer, Item> entry : bill.getItems().entrySet()) {
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
        final JTextField amountModifiedField = new JTextField("Please enter how much the split is modified here");
        amountModifiedField.setPreferredSize(new Dimension(300, 30));
        amountModifiedField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent evt) {
                amountModifiedField.setText("");
            }

            public void focusLost(FocusEvent evt) {
                if (amountModifiedField.getText().isEmpty()) {
                    amountModifiedField.setText("Please enter item cost here");
                }
            }
        });

        mainPanel.add(amountModifiedField);

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton modifyButton = new JButton("Modify!");
        modifyButton.setBackground(Color.BLACK);
        modifyButton.setForeground(Color.WHITE);
        modifyButton.setFocusPainted(false);
        modifyButton.setBorderPainted(false);
        modifyButton.setPreferredSize(new Dimension(100, 35));

        // Add action listener for create button
        modifyButton.addActionListener(evt -> {
            final int userId = reverseUsers.get(userSelection.getSelectedItem());
            final int itemId = reverseItems.get(itemSelection.getSelectedItem());

            try {
                final float amountModified = Float.valueOf(amountModifiedField.getText());

                modifySplitController.execute(amountModified, bill.getId(), itemId, userId);

                // This part of the code makes the parent display redraw itself after updating the DAO.
                this.remove(mainContentPanel);
                createMainContent();
                parent.add(mainContentPanel, BorderLayout.CENTER);
                parent.repaint();
                parent.revalidate();

                JOptionPane.getRootFrame().dispose();
            }
            catch (NumberFormatException ex) {
                // This catches when the user input something that is not a float, thus have a pop up saying they
                // gotta input a float.
                JOptionPane.showMessageDialog(parent, "The amount modified is not a number. Try again.");

            }

            JOptionPane.getRootFrame().dispose();

        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(modifyButton);
        mainPanel.add(buttonPanel);

        JOptionPane.showMessageDialog(null, mainPanel, "Modify Split", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddItemDialog(JPanel parent) {

        // Create main panel with padding
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        final JLabel titleLabel = new JLabel("Add a new item...");
        titleLabel.setFont(new Font(ARIAL, Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create input fields
        final JTextField itemNameField = new JTextField("Please enter item name here");
        itemNameField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        itemNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        itemNameField.setPreferredSize(new Dimension(300, 40));
        itemNameField.setFont(new Font(ARIAL, Font.PLAIN, 14));
        itemNameField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent evt) {
                itemNameField.setText("");
            }

            public void focusLost(FocusEvent evt) {
                if (itemNameField.getText().isEmpty()) {
                    itemNameField.setText("Please enter item name here");
                }
            }
        });

        final JTextField itemCostField = new JTextField("Please enter item cost here");
        itemCostField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        itemCostField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        itemCostField.setPreferredSize(new Dimension(300, 40));
        itemCostField.setFont(new Font(ARIAL, Font.PLAIN, 14));
        itemCostField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent evt) {
                itemCostField.setText("");
            }

            public void focusLost(FocusEvent evt) {
                if (itemCostField.getText().isEmpty()) {
                    itemCostField.setText("Please enter item cost here");
                }
            }
        });

        // Create button panel
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton createButton = new JButton("Create!");
        createButton.setBackground(Color.BLACK);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setPreferredSize(new Dimension(100, 35));

        // dont need anymore because changed from dialogue to optionpane.

//        // Add close button to top-right corner
//        JButton closeButton = new JButton("×");
//        closeButton.setFont(new Font(ARIAL, Font.BOLD, 20));
//        closeButton.setBorderPainted(false);
//        closeButton.setContentAreaFilled(false);
//        closeButton.setFocusPainted(false);
//        closeButton.addActionListener(e -> JOptionPane.getRootFrame().dispose());
//
//        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        closePanel.add(closeButton);

        // Add action listener for create button
        createButton.addActionListener(evt -> {
            final String itemName = itemNameField.getText().trim();
            final String itemCost = itemCostField.getText().trim();
            if (!itemName.isEmpty() && !itemCost.isEmpty()) {
                try {
                    final float itemCostFloat = Float.valueOf(itemCost);

                    final ItemFactory itemFactory = new ItemFactory();
                    bill.addItem(itemFactory.create(itemName, itemCostFloat));
                    userDataAccessObject.setBill(bill.getId(), bill);

                    // This part of the code makes the parent display redraw itself after updating the DAO.
                    this.remove(mainContentPanel);
                    createMainContent();
                    parent.add(mainContentPanel, BorderLayout.CENTER);
                    parent.repaint();
                    parent.revalidate();

                    JOptionPane.getRootFrame().dispose();
                }
                catch (NumberFormatException ex) {
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

        JOptionPane.showMessageDialog(null, mainPanel, "Add Items", JOptionPane.INFORMATION_MESSAGE);

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

    /**
     * Sets the data access object (DAO) for accessing user data.
     * This method assigns the provided FileDAO to the userDataAccessObject.
     * It also triggers the `drawMainContent()` method to initialize and display
     * the main content of the view, but it is important to note that the DAO
     * should be set before any operations are performed.
     * It is strongly recommended to call this method only during the initialization
     * of the view, as the DAO must be set before any other actions take place. 
     * Calling this method after the system has been initialized may lead to
     * unexpected behavior.
     * @param fileDAO the FileDAO instance used to access user data
     */
    public void setDAO(FileDAO fileDAO) {
        this.userDataAccessObject = fileDAO;

        // very very horrible coding practice this the DAO must be set before everything is actaully ran
        // so this method has to be called before everything is ran, PLEASAE dont call this unless you are initializing
        // the view.
        drawMainContent();
    }

    public void setUploadReceiptController(UploadReceiptController controller) {
        this.uploadReceiptController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("property changed now displaying bill display");
        if (evt.getPropertyName().equals("state")) {
            this.remove(sidebarPanel);
            sidebarPanel = new Sidebar(billDisplayPresenter, 
                    changePasswordController, logoutController, this.billDisplayViewModel.getState());

            drawMainContent();
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

    public void setBillDisplayPresenter(BillDisplayPresenter billDisplayPresenter) {
        this.billDisplayPresenter = billDisplayPresenter;
    }

}

