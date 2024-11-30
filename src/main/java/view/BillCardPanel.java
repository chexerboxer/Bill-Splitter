package view;


import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.login.LoginState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class BillCardPanel extends JPanel {
    private String name;
    private HashMap<Integer, String> userBillsData;
    private int billId;
    private final DashboardController dashboardController;

    public BillCardPanel(String name,
                         HashMap<Integer, String> userBillsData,
                         int billId,
                         DashboardController dashboardController) {
        this.name = name;
        this.userBillsData = userBillsData;
        this.billId = billId;
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

        // add hover effect on bill cards to convey to the user they can click on them
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(190, 190, 190));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(210, 210, 210));
            }
        });

        setPreferredSize(new Dimension(100, 100));

    }


}
