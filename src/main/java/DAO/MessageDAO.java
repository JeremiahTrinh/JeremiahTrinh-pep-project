package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"), 
                            rs.getInt("posted_by"), 
                            rs.getString("message_text"), 
                            rs.getLong("time_posted_epoch"));
                messages.add(msg);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<Message> getAllMessagesForUser(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message where posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"), 
                            rs.getInt("posted_by"), 
                            rs.getString("message_text"), 
                            rs.getLong("time_posted_epoch"));
                messages.add(msg);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessage(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from message where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"), 
                            rs.getInt("posted_by"), 
                            rs.getString("message_text"), 
                            rs.getLong("time_posted_epoch"));
                return msg;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message insertMessage(Message msg){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, msg.getPosted_by());
            preparedStatement.setString(2, msg.getMessage_text());
            preparedStatement.setLong(3, msg.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            Message msg = getMessage(message_id);
            String sql = "delete from message where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            preparedStatement.executeUpdate();
            if(msg != null){
                return msg;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message patchMessage(int message_id, String new_text){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "update message set message_text = ? where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, new_text);
            preparedStatement.setInt(2, message_id);
            preparedStatement.executeUpdate();
            return getMessage(message_id);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
