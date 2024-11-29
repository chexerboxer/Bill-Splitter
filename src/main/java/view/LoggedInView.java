package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.LoggedInState;
import interface_adapter.change_password.LoggedInViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.logout.LogoutController;
import interface_adapter.new_bill.NewBillController;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final JLabel passwordErrorField = new JLabel();
    private final JFileChooser fileChooser = new JFileChooser();
    private ChangePasswordController changePasswordController;
    private NewBillController newBillController;
    private LogoutController logoutController;

    private final JLabel username;

    private final JButton logOut;

    private final JButton addBill;

    private final JButton changePassword;

    private final JButton toIOUs;

    private final JButton upload;


    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);
        this.setLayout(new BorderLayout(20, 20));

        final JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LoggedInState currentState = loggedInViewModel.getState();

        final JLabel usernameInfo = new JLabel("[insert username here]");
        usernameInfo.setFont(new Font("Arial", Font.BOLD, 20));
        username = new JLabel();

        final JPanel buttons = new JPanel(new BorderLayout(100, 100));

        changePassword = new JButton("Change Password");
        addBill = new JButton("Add Bill");
        logOut = new JButton("Log Out");
        toIOUs = new JButton("To IOUs");
        upload = new JButton("Upload");

        changePassword.setFont(new Font("Arial",Font.CENTER_BASELINE, 20));
        addBill.setFont(new Font("Arial",Font.CENTER_BASELINE, 15));
        logOut.setFont(new Font("Arial",Font.CENTER_BASELINE, 20));
        toIOUs.setFont(new Font("Arial",Font.CENTER_BASELINE, 15));
        upload.setFont(new Font("Arial",Font.CENTER_BASELINE, 15));

        changePassword.setPreferredSize(new Dimension(100, 100));
        addBill.setPreferredSize(new Dimension(100, 100));
        logOut.setPreferredSize(new Dimension(100, 100));
        toIOUs.setPreferredSize(new Dimension(100, 100));
        upload.setPreferredSize(new Dimension(100, 100));

        buttons.add(changePassword, BorderLayout.NORTH);
        buttons.add(toIOUs, BorderLayout.WEST);
        buttons.add(addBill, BorderLayout.LINE_END);
        buttons.add(logOut, BorderLayout.SOUTH);
        buttons.add(upload, BorderLayout.CENTER);

        changePassword.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        String toPassword = JOptionPane.showInputDialog(
                                "New Password:", "");
                        if (evt.getSource().equals(changePassword)) {
                            changePasswordController.execute(
                                    currentState.getUsername(),
                                    toPassword
                            );
                        }
                    }
                }
        );
        logOut.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                evt -> {
                    if (evt.getSource().equals(logOut)) {
                        this.logoutController.execute(
                                currentState.getUsername()
                        );
                    }
                }
        );
        upload.addActionListener(
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
                                FileWriter writer = new FileWriter("src/main/java/data_access/receiptfiles.txt", true);
                                writer.write(filepath + "\n");
                                writer.close();
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "File not uploaded");
                        }
                    }
                }
        );
        addBill.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        String inBillName = JOptionPane.showInputDialog(
                                "Bill Name:", "");
//                        if (evt.getSource().equals(addBill)) {
//                            newBillController.execute(inBillName);
//                        }
                        JButton newBillButton = new JButton(inBillName);
                        newBillButton.setFont(new Font("Arial", Font.BOLD, 10));
                        buttons.add(newBillButton, BorderLayout.LINE_START);
                        buttons.revalidate();
                        buttons.repaint();
                    }
                    
                }
        );

        this.add(title, BorderLayout.NORTH);
        this.add(usernameInfo, BorderLayout.WEST);
        this.add(username);

        this.add(passwordErrorField);
        this.add(buttons);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
        }
        else if (evt.getPropertyName().equals("password")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            JOptionPane.showMessageDialog(null, "password updated for " + state.getUsername());
        }

    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setNewBillController(NewBillController newBillController) {
        this.newBillController = newBillController;
    }

    /**
     * Set logout controller class.
     * @param logoutController parameter.
     */

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}
