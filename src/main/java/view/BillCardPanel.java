package view;


import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.login.LoginState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class BillCardPanel extends JPanel {
    private String name;
    private HashMap<Integer, String> userBillsData;
    private int billId;
    private String username;
    private final DashboardController dashboardController;

    public BillCardPanel(String name,
                         HashMap<Integer, String> userBillsData,
                         int billId,
                         String username,
                         DashboardController dashboardController) {
        this.name = name;
        this.userBillsData = userBillsData;
        this.billId = billId;
        this.username = username;
        this.dashboardController = dashboardController;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setBackground(new Color(210, 210, 210));

        JLabel billName = new JLabel(name);
        billName.setFont(new Font("Arial", Font.BOLD, 18));
        add(billName, BorderLayout.NORTH);

        JButton deleteBill = new JButton("delete (forever!)");
        deleteBill.setFont(new Font("Arial", Font.PLAIN, 10));
        deleteBill.setBackground(Color.BLACK);
        deleteBill.setForeground(Color.WHITE);
        add(deleteBill, BorderLayout.SOUTH);

        deleteBill.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(deleteBill)) {
                            dashboardController.execute(userBillsData, billId);
                        }
                    }
                });

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            // add hover effect on bill cards to convey to the user they can click on them
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(190, 190, 190));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(210, 210, 210));
            }

            // make panel into button to navigate to bill display view
            //TODO: add action listener to use dashboardController to switch to bill display view
            @Override
            public void mouseClicked(MouseEvent e) {

                dashboardController.switchToBillView(username, billId);
            }
        });



    }


}
