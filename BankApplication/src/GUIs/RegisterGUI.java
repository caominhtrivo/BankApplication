package GUIs;

import db_objs.MyJDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGUI extends BaseFrame{
    public RegisterGUI() {
        super("Banking Application Register");
    }
    @Override
    protected void addGUIComponents() {
        JLabel bankingApplicationLabel = new JLabel("Banking Application");

        //set location, size, font of label and center the text
        bankingApplicationLabel.setBounds(0, 20, super.getWidth(), 40); //getWidth() returns 420 of BaseFrame
        bankingApplicationLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        bankingApplicationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //add to GUI
        add(bankingApplicationLabel);

        //username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 120, super.getWidth() - 30, 24);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(usernameLabel);

        //username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 160, super.getWidth() - 50, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(usernameField);

        //password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 220, super.getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        //password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 260, super.getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        //re-type password label
        JLabel reTypePasswordLabel = new JLabel("Re-Type Password:");
        reTypePasswordLabel.setBounds(20, 320, super.getWidth() - 50, 24);
        reTypePasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(reTypePasswordLabel);

        //re-type password field
        JPasswordField reTypePasswordField = new JPasswordField();
        reTypePasswordField.setBounds(20, 360, super.getWidth() - 50, 40);
        reTypePasswordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(reTypePasswordField);


        //register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 470, super.getWidth() - 50, 40);
        registerButton.setFont(new Font("Dialog", Font.BOLD, 20));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get username and password
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String reTypePassword = String.valueOf(reTypePasswordField.getPassword());

                if (validateUserInput(username, password, reTypePassword)) {
                    if(MyJDBC.register(username, password)){
                        //success in registering then close register window, launch the login GUI
                        RegisterGUI.this.dispose();

                        LoginGUI loginGUI = new LoginGUI();
                        loginGUI.setVisible(true);

                        //create a result dialog
                        JOptionPane.showMessageDialog(loginGUI, "Register successful");
                    }
                    else{
                        JOptionPane.showMessageDialog(RegisterGUI.this, "Username already exists");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(RegisterGUI.this,
                            "Username must be at least 6 characters\n" +
                                    "and/or Password must match"
                    );
                }

            }
        });
        add(registerButton);

        //login label
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Are you a member? Sign-in here!</a></html>");
        loginLabel.setBounds(0, 520, super.getWidth() - 10, 30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterGUI.this.dispose();
                new LoginGUI().setVisible(true);
            }
        });
        add(loginLabel);
    }

    private boolean validateUserInput(String username, String password, String reTypePassword) {
        if (username.length() == 0 || password.length() == 0 || reTypePassword.length() == 0) {
            return false;
        }
        if (username.length() < 6) return false;

        if (!password.equals(reTypePassword)) return false;

        return true;
    }
}
