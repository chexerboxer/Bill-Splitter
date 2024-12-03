package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.dashboard.DashboardController;

/**
 * Bill Card component used in Dashboard View for each bill the current user is a member of.
 */
public class BillCardPanel extends JPanel {
    private static final int BORDER_PADDING_TOP_BOTTOM = 10;
    private static final int BORDER_PADDING_SIDES = 15;
    private static final Color BILL_BACKGROUND_COLOUR = new Color(210, 210, 210);
    private static final Color BILL_HOVER_COLOUR = new Color(190, 190, 190);
    private static final int BILL_TITLE_FONT_SIZE = 18;
    private static final int BILL_DELETE_FONT_SIZE = 10;

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
        setBorder(BorderFactory.createEmptyBorder(BORDER_PADDING_TOP_BOTTOM,
                BORDER_PADDING_SIDES,
                BORDER_PADDING_TOP_BOTTOM,
                BORDER_PADDING_SIDES));
        setBackground(BILL_BACKGROUND_COLOUR);

        final JLabel billName = new JLabel(name);
        billName.setFont(new Font("Arial", Font.BOLD, BILL_TITLE_FONT_SIZE));
        add(billName, BorderLayout.NORTH);

        // delete button
        final JButton deleteBill = new JButton("delete (forever!)");
        deleteBill.setFont(new Font("Arial", Font.PLAIN, BILL_DELETE_FONT_SIZE));
        deleteBill.setBackground(Color.BLACK);
        deleteBill.setForeground(Color.WHITE);
        add(deleteBill, BorderLayout.SOUTH);

        deleteBill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (evt.getSource().equals(deleteBill)) {
                    dashboardController.execute(userBillsData, billId);
                }
            }
        }
        );

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            // add hover effect on bill cards to convey to the user they can click on them
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(BILL_HOVER_COLOUR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(BILL_BACKGROUND_COLOUR);
            }

            // make panel into button to navigate to bill display view
            @Override
            public void mouseClicked(MouseEvent e) {
                dashboardController.switchToBillView(username, billId);
            }
        }
        );
    }
}
