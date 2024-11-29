package view;


import javax.swing.*;
import java.awt.*;

public class BillCardPanel extends JPanel {
    private String name;
    private int billId;

    public BillCardPanel(String name, int billId) {
        this.name = name;
        this.billId = billId;

        this.add(new JLabel(name));

        this.setBackground(Color.CYAN);

        this.setMaximumSize(new Dimension(100, 50));


    }
}
