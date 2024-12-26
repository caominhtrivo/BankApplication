package db_objs;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class MyJDBC {
    //data base configurations
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bankapp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Ha123ha456++";

    //if valid, return an obj of user with user information
    public static User validateLogin(String username, String password){
        try{
            //create a connection to database using configuration
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            //create sql query
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );

            //replace ? with values
            preparedStatement.setString(1, username); //1 is the first ?
            preparedStatement.setString(2, password); //2 is the second ?

            //execute query and store in a result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //if the entered login credentials are correct, that means the result set is not null
            //next() return true or false
            //true = query return data and result set points to first row
            //false = // return no data and result set equals to null
            if(resultSet.next()){

                int userID = resultSet.getInt("id");

                //get current balance
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                //return user object
                return new User(userID, username, password, currentBalance );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Query execution failed: " + e.getMessage());

        }

        //not valid user
        return null;
    }

    //true = successful
    public static boolean register(String username, String password){
        try{
            if(!checkUser(username)){
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(username, password, current_balance)" +
                                "VALUES(?, ?, ?)"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, new BigDecimal(0));
                preparedStatement.executeUpdate();
                return true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkUser(String username){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT * FROM users WHERE username = ?"
            );
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public static boolean addTransactionToDatabase(Transaction transaction){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement insertTransaction = connection.prepareStatement(
                    "INSERT INTO transactions(user_id, transaction_type, transaction_amount, transaction_date)" +
                            "VALUES(?, ?, ?, NOW())" //NOW() will insert current date
            );

            insertTransaction.setInt(1, transaction.getUserID());
            insertTransaction.setString(2, transaction.getTransactionType());
            insertTransaction.setBigDecimal(3, transaction.getTransactionAmount());

            //update database
            insertTransaction.executeUpdate();


            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }

    public static boolean updateCurrentBalance(User user){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE id = ?"
            );

            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getId());
            updateBalance.executeUpdate();
            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean transfer(User user, String transferredUsername, float transferAmount){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement queryUser = connection.prepareStatement(
                "SELECT * FROM users WHERE username = ?"
            );

            queryUser.setString(1, transferredUsername);
            ResultSet resultSet = queryUser.executeQuery();

            while(resultSet.next()){
                //transfer
                User transferredUser = new User(
                        resultSet.getInt("id"), transferredUsername, resultSet.getString("password"), resultSet.getBigDecimal("current_balance")
                );

                //create transaction
                Transaction transferTransaction = new Transaction(
                        user.getId(), "Transfer", new BigDecimal(-transferAmount), null
                );

                //belong to the transferred user
                Transaction recievedTransaction = new Transaction(
                        transferredUser.getId(), "Transfer", new BigDecimal(transferAmount), null
                );

                //update transfer user
                transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferredUser);

                //update user current balance
                user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(user);

                //add transaction to database
                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(recievedTransaction);

                return true;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Transaction> getPastTransactions(User user){
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement selectAllTransactions = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id = ?"
            );
            selectAllTransactions.setInt(1, user.getId());
            ResultSet resultSet = selectAllTransactions.executeQuery();

            //iterate through the result
            while(resultSet.next()){
                Transaction transaction = new Transaction(
                        user.getId(), resultSet.getString("transaction_type"), resultSet.getBigDecimal("transaction_amount"), resultSet.getDate("transaction_date")
                );
                //store into arraylist
                pastTransactions.add(transaction);

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return pastTransactions;
    }

}
