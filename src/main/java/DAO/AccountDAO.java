package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class AccountDAO {

    /**
     * Inserts (registers) a new account into the Account table.
     * The account_id is set to auto_increment, so there is no need to provide it upon insertion.
     * @return the newly registered account if insertion was successful. Otherwise, null.
     */
    // Insert account -> User Registration
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account (username, password) values (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the specified account from the Account table.
     * @return the corresponding account from the table. Otherwise, null.
     */
    // Get account -> Verify Account -> Login
    public Account getAccount(Account input){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where username = ? and password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, input.getUsername());
            preparedStatement.setString(2, input.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), 
                        rs.getString("username"), 
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Integer> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Integer> accounts = new ArrayList<>();
        try{
            String sql = "select account_id from account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                accounts.add(rs.getInt("account_id"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

}
