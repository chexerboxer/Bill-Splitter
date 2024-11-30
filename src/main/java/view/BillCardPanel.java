package view;


import javax.swing.*;
import java.awt.*;

public class BillCardPanel extends JPanel {
    private String name;
    private int billId;

    public BillCardPanel(String name, int billId) {
        this.name = name;
        this.billId = billId;

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
