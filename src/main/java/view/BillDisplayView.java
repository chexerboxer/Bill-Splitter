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
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import data_access.FileDAO;
import entity.bill.Bill;
import entity.bill.BillFactory;
import entity.item.Item;
import entity.item.ItemFactory;
import entity.split.Split;
import entity.split.SplitFactory;
import entity.users.CommonUserFactory;
import entity.users.User;
// components
import interface_adapter.bill_splitter.BillDisplayPresenter;
import interface_adapter.bill_splitter.BillDisplayState;
import interface_adapter.bill_splitter.BillDisplayViewModel;
import view.components.*;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.logout.LogoutController;

import interface_adapter.split_management.ClearBillController;
import interface_adapter.split_management.DistributeBillController;
import interface_adapter.split_management.ModifySplitController;
import interface_adapter.split_management.SplitManagementPresenter;
import interface_adapter.upload_receipt.UploadReceiptController;
import interface_adapter.upload_receipt.UploadReceiptPresenter;
import use_case.split_management.SplitManagementOutputBoundary;
import use_case.split_management.clear_bill.ClearBillInputBoundary;
import use_case.split_management.clear_bill.ClearBillInteractor;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInputBoundary;
import use_case.split_management.distribute_bill_even.DistributeBillEvenInteractor;
import use_case.split_management.modify_split.ModifySplitInputBoundary;
import use_case.split_management.modify_split.ModifySplitInteractor;
import use_case.upload_receipt.UploadReceiptInputBoundary;
import use_case.upload_receipt.UploadReceiptInteractor;
import use_case.upload_receipt.UploadReceiptOutputBoundary;


// TODO refractor into JPanel though shouldnt be bad cuz I removed all the dialogue stuff its still JFrame so I can test it right now
// TODO note: creating billdisplay viewmodel and viewstate shouldnt be bad jsut plug in the bill from the viewstate and the viewmodel stuff isnt long.
public class BillDisplayView extends JPanel implements PropertyChangeListener{
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

    public void drawMainContent (){


        setLayout(new BorderLayout());
        BillDisplayState currentState = billDisplayViewModel.getState();
        sidebarPanel = new Sidebar(billDisplayPresenter, changePasswordController, logoutController, currentState);

        bill = userDataAccessObject.getBill(currentState.getBillId());

        if (bill != null){

            if (sidebarPanel != null) remove(sidebarPanel);

            if (mainContentPanel != null) remove((mainContentPanel));

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
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));


        JLabel titleLabel = new JLabel(bill.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));




        JLabel dateLabel = new JLabel("Total Cost: " + bill.getTotalAmount());
        dateLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(dateLabel);

        // Upload receipt section
        JButton uploadButton = new JButton("Upload a receipt");
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
                        int returnValue = fileChooser.showOpenDialog(null);

                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            java.io.File selectedFile = fileChooser.getSelectedFile();
                            String filepath = selectedFile.getAbsolutePath();
                            try {
                                JOptionPane.showMessageDialog(null, "File uploaded: " + filepath);
                                uploadReceiptController.execute(filepath, bill.getId());


                                // This part of the code makes the parent display redraw itself after updating the DAO.
                                remove(mainContentPanel);
                                createMainContent();
                                add(mainContentPanel, BorderLayout.CENTER);
                                repaint();
                                revalidate();

                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                            }
                        } else {
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

        // Add Members
        JLabel addMembersLabel = new JLabel("Add Member");
        addMembersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JButton addMembersButton = new JButton("+");
        addMembersButton.setFont(new Font("Arial", Font.BOLD, 14));
        addMembersButton.setFocusPainted(false);
        addMembersButton.addActionListener(e -> addMembersEvent(this));



        // Remove Members
        JLabel removeMembersLabel = new JLabel("Remove Member");
        removeMembersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JButton removeMembersButton = new JButton("+");
        removeMembersButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeMembersButton.setFocusPainted(false);
        removeMembersButton.addActionListener(e -> removeMembersEvent(this));

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

        JLabel ClearBillLabel = new JLabel("Clear Splits");
        ClearBillLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton ClearBillButton = new JButton("+");
        ClearBillButton.setFont(new Font("Arial", Font.BOLD, 14));
        ClearBillButton.setFocusPainted(false);
        ClearBillButton.addActionListener(e -> ClearBillEvent(this));

        // Edit price
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

    private void addMembersEvent(JPanel parent) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        JLabel titleLabel = new JLabel("Add Member");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(titleLabel);
        JTextField newMemberField = new JTextField(20);
        mainPanel.add(new JLabel("New Member: "));
        mainPanel.add(newMemberField);

        int result = JOptionPane.showConfirmDialog(
                null,
                mainPanel,
                "Add a New Member",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION){
            String newUserName = newMemberField.getText();
            Map<Integer, User> DAOUserMap = userDataAccessObject.getAllUsers();
            Map<String, Integer> reverseUsers = new HashMap<>();
            for (int key : DAOUserMap.keySet()){
                String userName = DAOUserMap.get(key).getName();
                reverseUsers.put(userName, key);
            }
            if (reverseUsers.containsKey(newUserName)) {
                int userid = reverseUsers.get(newUserName);
                if (bill.getUsers().contains(userid)) {
                    JOptionPane.showMessageDialog(mainPanel, "Member already in bill.");
                } else {
                    int newuserId = reverseUsers.get(newUserName);
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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        JLabel titleLabel = new JLabel("Remove Member");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(titleLabel);

        Map<String, Integer> reverseUsers = new HashMap<>();
        for(int userId : bill.getUsers()){
            String userName = userDataAccessObject.getUser(userId).getName();
            reverseUsers.put(userName, userId);
        }

        final JComboBox<String> userSelection =
                new JComboBox<>(reverseUsers.keySet().toArray(new String[reverseUsers.size()]));
        mainPanel.add(userSelection);

        int result = JOptionPane.showConfirmDialog(
                null,
                mainPanel,
                "Remove item",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION){
            int userId = reverseUsers.get(userSelection.getSelectedItem());
            User user = userDataAccessObject.getUser(userId);
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

    private void ClearBillEvent(JPanel parent) {

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

    private void EditPriceEvent(JPanel parent) {
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


    private void showModifySplitsDialog(JPanel parent) {
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

    private void showAddItemDialog(JPanel parent) {

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

    public void setDAO(FileDAO fileDAO) {
        this.userDataAccessObject = fileDAO;


        // very very horrible coding practice this the DAO must be set before everything is actaully ran
        // so this method has to be called before everything is ran, PLEASAE dont call this unless you are initializing
        // the view.
        drawMainContent();
    }

    public void setUploadReceiptController(UploadReceiptController controller){this.uploadReceiptController = controller;}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("property changed now displaying bill display");
        if (evt.getPropertyName().equals("state")) {
            this.remove(sidebarPanel);
            sidebarPanel = new Sidebar(billDisplayPresenter, changePasswordController, logoutController, this.billDisplayViewModel.getState());





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