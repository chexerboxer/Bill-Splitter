package view;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.LoggedInState;
import interface_adapter.change_password.LoggedInViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.logout.LogoutController;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final JLabel passwordErrorField = new JLabel();
    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;

    private final JLabel username;

    private final JButton logOut;

    private final JButton addBill;

    private final JButton changePassword;

    private final JButton toIOUs;

//    private final JTextField passwordInputField = new JTextField(15);
//    private final JButton changePassword;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);
        this.setLayout(new BorderLayout(20, 20));

        final JLabel title = new JLabel("Dashboard");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LoggedInState currentState = loggedInViewModel.getState();

//        final LabelTextPanel passwordInfo = new LabelTextPanel(
//                new JLabel("Password"), passwordInputField);
        final JLabel usernameInfo = new JLabel("[insert username here]");
        username = new JLabel();

        final JPanel buttons = new JPanel(new BorderLayout());

        changePassword = new JButton("Change Password");
        addBill = new JButton("Add Bill");
        logOut = new JButton("Log Out");
        toIOUs = new JButton("To IOUs");

        buttons.add(changePassword, BorderLayout.NORTH);
        buttons.add(toIOUs, BorderLayout.WEST);
        buttons.add(addBill, BorderLayout.LINE_END);
        buttons.add(logOut, BorderLayout.SOUTH);

//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
//
//            private void documentListenerHelper() {
//                final LoggedInState currentState = loggedInViewModel.getState();
//                currentState.setPassword(passwordInputField.getText());
//                loggedInViewModel.setState(currentState);
//            }
//
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                documentListenerHelper();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                documentListenerHelper();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                documentListenerHelper();
//            }
//        });

//        changePassword.addActionListener(
//                // This creates an anonymous subclass of ActionListener and instantiates it.
//                evt -> {
//                    if (evt.getSource().equals(changePassword)) {
//                        final LoggedInState currentState = loggedInViewModel.getState();
//
//                        this.changePasswordController.execute(
//                                currentState.getUsername(),
//                                currentState.getPassword()
//                        );
//                    }
//                }
//        );
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

        this.add(title, BorderLayout.NORTH);
        this.add(usernameInfo, BorderLayout.WEST);
        this.add(username);

//        this.add(passwordInfo);
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

    /**
     * Set logout controller class.
     * @param logoutController parameter.
     */

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}
