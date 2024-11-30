package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordViewModel;


public class ChangePasswordView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "change password";
    private final ChangePasswordViewModel changePasswordViewModel;
    private ChangePasswordController changePasswordController;
    private final JTextField usernameField = new JTextField(15);
    private final JTextField newPasswordField = new JTextField(15);
    private final JPasswordField confirmPasswordField = new JPasswordField(15);

    private final JLabel errorMessageField =  new JLabel();
    // error = when the new passwords don't match.

    private final JButton clear;
    private final JButton confirm;
    private final JButton toLogin;

    public ChangePasswordView(ChangePasswordViewModel changePasswordViewModel) {
        this.changePasswordViewModel = changePasswordViewModel;
        this.changePasswordViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Change Password");
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameField);
        final LabelTextPanel newPasswordInfo = new LabelTextPanel(
                new JLabel("New Password"), newPasswordField);
        newPasswordInfo.setPreferredSize(new Dimension(150, 30));
        final LabelTextPanel confirmPasswordInfo = new LabelTextPanel(
                new JLabel("Confirm Password"), confirmPasswordField);
        confirmPasswordInfo.setPreferredSize(new Dimension(150, 30));
        final JPanel buttons = new JPanel();

        clear = new JButton("Clear");
        confirm = new JButton("Confirm");
        toLogin = new JButton("Return to Login");
        buttons.add(clear);
        buttons.add(confirm);
        buttons.add(toLogin);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(usernameInfo);
        this.add(newPasswordInfo);
        this.add(confirmPasswordInfo);
        this.add(buttons);

        clear.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        usernameField.setText("");
                        newPasswordField.setText("");
                        confirmPasswordField.setText("");
                    }
                }
        );

        toLogin.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        changePasswordController.switchToLoginView();
                    }
                }
        );

        confirm.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
                            boolean success = changePasswordController.execute(usernameField.getText(), newPasswordField.getText());
                            if (success) {
                                JOptionPane.showMessageDialog(
                                        ChangePasswordView.this,
                                        "Password successfully changed",
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                            }
                            else {
                                JOptionPane.showMessageDialog(
                                        ChangePasswordView.this,
                                        "Username not found",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        } else {
                            JOptionPane.showMessageDialog(
                                    ChangePasswordView.this,
                                    "Passwords don't match",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }
}
