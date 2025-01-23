package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message msg){
        return messageDAO.insertMessage(msg);
    }

    public Message deleteMessage(int id){
        return messageDAO.deleteMessage(id);
    }

    public List<Message> getAllMessagesForUser(int id){
        return messageDAO.getAllMessagesForUser(id);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessage(int id){
        return messageDAO.getMessage(id);
    }

    public Message updateMessage(int id, String text){
        return messageDAO.patchMessage(id, text);
    }
}
