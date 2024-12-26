package GUIs;

import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankingApplicationGUI extends BaseFrame implements ActionListener {
    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField() {return currentBalanceField;}
    public BankingApplicationGUI(User user) {
        super("Banking Application", user);
    }

    @Override
    protected void addGUIComponents() {
        //welcome message
        String welcomeMessage = "<html>" +
                "<body style='text-align:center'>" +
                "<b>Hello " + user.getUsername() + "</b><br>" +
                "What would you like to do today?</body><html>";
        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(0, 20, getWidth() - 10, 40);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessageLabel);

        //current balance label
        JLabel currentBalanceLabel = new JLabel("Current Balance");
        currentBalanceLabel.setBounds(0, 80, getWidth() - 10, 30);
        currentBalanceLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        currentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(currentBalanceLabel);

        //current balance field
        currentBalanceField = new JTextField("$" + user.getCurrentBalance());
        currentBalanceField.setBounds(15, 120, getWidth() - 50, 40);
        currentBalanceField.setFont(new Font("Dialog", Font.BOLD, 28));
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        //deposit button
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(15, 180, getWidth() - 50, 50);
        depositButton.setFont(new Font("Dialog", Font.BOLD, 22));
        depositButton.addActionListener(this);
        add(depositButton);

        //withdraw button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(15, 250, getWidth() - 50, 50);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 22));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        //past transaction
        JButton pastTransactionsButton = new JButton("Past Transactions");
        pastTransactionsButton.setBounds(15, 320, getWidth() - 50, 50);
        pastTransactionsButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransactionsButton.addActionListener(this);
        add(pastTransactionsButton);

        //transfer button
        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(15, 390, getWidth() - 50, 50);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 22));
        transferButton.addActionListener(this);
        add(transferButton);

        //log out button
        JButton logOutButton = new JButton("Log out");
        logOutButton.setBounds(15, 500, getWidth() - 50, 50);
        logOutButton.setFont(new Font("Dialog", Font.BOLD, 22));
        logOutButton.addActionListener(this);
        add(logOutButton);


    }

    @Override
    public void actionPerformed(ActionEvent e){
        String buttonPressed = e.getActionCommand();

        //in case user click log out button
        if(buttonPressed.equalsIgnoreCase("Log out")){
            BankingApplicationGUI.this.dispose();
            new LoginGUI().setVisible(true);
            return;
        }
        //other cases
        BankingApplicationDialog bankingApplicationDialog = new BankingApplicationDialog(this, user);
        //set the title base on action
        bankingApplicationDialog.setTitle(buttonPressed);

        if(buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw")
                 || buttonPressed.equalsIgnoreCase("Transfer")){
            //add current amount and amount GUI components to the dialog
            bankingApplicationDialog.addCurrentBalanceAndAmount();

            //add action button
            bankingApplicationDialog.addActionButton(buttonPressed);

            //transfer action
            if (buttonPressed.equalsIgnoreCase("Transfer")) {
                bankingApplicationDialog.addUserField();
            }


        }
        else if(buttonPressed.equalsIgnoreCase("Past Transactions")){
            bankingApplicationDialog.addPastTransactionComponents();
        }

        bankingApplicationDialog.setVisible(true);
    }
}
