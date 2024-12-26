package GUIs;

import db_objs.MyJDBC;
import db_objs.Transaction;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BankingApplicationDialog extends JDialog implements ActionListener {
    private User user;

    private BankingApplicationGUI bankingApplicationGUI;

    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private JTextField enterAmountField, enterUserField;
    private JButton actionButton;
    private JPanel pastTransactionsPanel;
    private ArrayList<Transaction> pastTransactions;

    public BankingApplicationDialog(BankingApplicationGUI bankingApplicationGUI, User user) {
        setSize(400,400);

        //add focus to the dialog
        setModal(true);

        //set location to the center of banking app GUI
        setLocationRelativeTo(bankingApplicationGUI);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        //reference to GUI to update the current balance
        this.bankingApplicationGUI = bankingApplicationGUI;
        //access to user's information to update to database or retrieve data about the user
        this.user = user;


    }

    public void addCurrentBalanceAndAmount(){
        //balance label
        balanceLabel = new JLabel("Balance: $" + user.getCurrentBalance());
        balanceLabel.setBounds(0, 10, getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        //enter amount label
        enterAmountLabel = new JLabel("Enter amount: ");
        enterAmountLabel.setBounds(0, 50, getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        //enter amount field
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15, 80, getWidth() - 50, 40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountField);
    }

    public void addActionButton(String actionButtonType){
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15, 300, getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.addActionListener(this);
        add(actionButton);
    }

    public void addUserField(){
        //enter user label
        enterUserLabel = new JLabel("Enter User: ");
        enterUserLabel.setBounds(0, 160, getWidth() - 20, 20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        //enter user field
        enterUserField = new JTextField();
        enterUserField.setBounds(15, 190, getWidth() - 50, 40);
        enterUserField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterUserField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserField);

    }

    public void addPastTransactionComponents(){
        //containers where storing transactions
        pastTransactionsPanel = new JPanel();

        //make layout 1x1
        pastTransactionsPanel.setLayout(new BoxLayout(pastTransactionsPanel, BoxLayout.Y_AXIS));

        //make the container scrollable
        JScrollPane scrollPane = new JScrollPane(pastTransactionsPanel);
        //display the vertical scroll bar when it is required
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 20, getWidth() - 15, getHeight() - 15);

        //perform database call to retrieve all past transactions and store into an arraylist
        pastTransactions = MyJDBC.getPastTransactions(user);

        //iterate through the list and display in the GUI
        for (int i = 0; i < pastTransactions.size(); i++) {
            //store current transaction
            Transaction pastTransaction = pastTransactions.get(i);

            //create a container to store individual transaction
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            //create transaction type label
            JLabel transactionTypeLabel = new JLabel (pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            //transaction amount label
            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            //transaction date label
            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            //add to the container
            pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST);
            pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST);
            pastTransactionContainer.add(transactionDateLabel, BorderLayout.SOUTH);

            //white background for each container
            pastTransactionContainer.setBackground(Color.WHITE);

            //add border for each transaction container
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));


            //add transaction component to transaction panel
            pastTransactionsPanel.add(pastTransactionContainer);



        }
        //add to the dialog
        add(scrollPane);
    }

    private void handleTransaction(String transactionType, float amountValue){
        Transaction transaction;
        //deposit
        if(transactionType.equalsIgnoreCase("Deposit")){
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amountValue)));

            //create transaction
            //null will be replaced by NOW() in SQL
            transaction = new Transaction(user.getId(),transactionType, new BigDecimal(amountValue), null);
        }
        else {
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amountValue)));
            transaction = new Transaction(user.getId(),transactionType, new BigDecimal(-amountValue), null);

        }

        //update database
        if(MyJDBC.addTransactionToDatabase(transaction) && MyJDBC.updateCurrentBalance(user)){
            //show success dialog
            JOptionPane.showMessageDialog(this, transactionType + " Successfully!");

            resetFieldsAndUpdateCurrentBalance();
        }
        else {
            JOptionPane.showMessageDialog(this, transactionType + " Failed...");
        }
    }

    private void resetFieldsAndUpdateCurrentBalance(){
        enterAmountField.setText("");

        if(enterUserField != null){
            enterUserField.setText("");
        }

        //update current balance on dialog
        balanceLabel.setText("Balance: $" + user.getCurrentBalance());

        //update current balance on main GUI
        bankingApplicationGUI.getCurrentBalanceField().setText("$" + user.getCurrentBalance());

    }

    private void handleTransfer(User user, String transferredUser, float amount){
        if(MyJDBC.transfer(user, transferredUser, amount)){
            JOptionPane.showMessageDialog(this, "Transfer Successfully!");
            resetFieldsAndUpdateCurrentBalance();
        }
        else{
            JOptionPane.showMessageDialog(this, "Transfer Failed...");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        float amountValue = Float.parseFloat(enterAmountField.getText());

        if(buttonPressed.equalsIgnoreCase("Deposit")){
            handleTransaction(buttonPressed, amountValue);
        }
        else {
            //withdraw or transfer

            //make sure money out < current balance
            //return -1: entered amount > current balance
            //return 0: equals
            //return 1: entered amount < current balance
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amountValue));
            if(result < 0){
                JOptionPane.showMessageDialog(this, "Input value is more than the current balance!");
                return;
            }
            //withdraw case
            if(buttonPressed.equalsIgnoreCase("Withdraw")){
                handleTransaction(buttonPressed, amountValue);

            }
            //transfer case
            else{
                String transferredUser = enterUserField.getText();

                //handle transfer
                handleTransfer(user, transferredUser, amountValue);
            }
        }
    }
}
