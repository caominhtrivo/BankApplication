package GUIs;

import db_objs.MyJDBC;
import db_objs.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginGUI extends BaseFrame {
    public LoginGUI() {
        super("Banking Application Login");

    }

    @Override
    protected void addGUIComponents() {
        //create banking application label
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

        //login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 470, super.getWidth() - 50, 40);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //get username
                String username = usernameField.getText();
                //get password
                String password = String.valueOf(passwordField.getPassword());

                //validate login
                User user = MyJDBC.validateLogin(username, password);

                // != null means valid user
                if (user != null) {
                    LoginGUI.this.dispose();//close login window

                    //launch Banking Application GUI
                    BankingApplicationGUI bankingApplicationGUI = new BankingApplicationGUI(user);
                    bankingApplicationGUI.setVisible(true);

                    //show success dialog
                    JOptionPane.showMessageDialog(bankingApplicationGUI, "Login Successfully!");

                }
                else{
                    JOptionPane.showMessageDialog(LoginGUI.this, "Login failed, please try again!");
                }
            }
        });
        add(loginButton);

        //register label
        JLabel registerLabel = new JLabel("<html><a href=\"#\">New member? Register here!</a></html>");
        registerLabel.setBounds(0, 520, super.getWidth() - 10, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //when mouse is clicked, launch the register GUI and close login GUI
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                super.mouseClicked(e);
                LoginGUI.this.dispose();
                new RegisterGUI().setVisible(true);
            }
        });

        add(registerLabel);
    }

}
