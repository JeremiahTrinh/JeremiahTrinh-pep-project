package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService acctService;
    MessageService msgService;

    public SocialMediaController(){
        this.acctService = new AccountService();
        this.msgService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::postAccountRegistrationHandler);
        app.post("/login", this::postAccountLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesForUserHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler to post and create a new message.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(context.body(), Message.class);
        Message result = null;
        List<Integer> users = acctService.getUsers();
        if (msg.getMessage_text().length() > 0 && msg.getMessage_text().length() < 255 && users.indexOf(msg.getPosted_by()) != -1){
            result = msgService.createMessage(msg);
        }
        if(result != null){
            context.json(mapper.writeValueAsString(result));
        }else{
            context.status(400);
        }
    }

    /**
     * Handler to delete a message by its ID.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer id = Integer.valueOf(context.pathParam("message_id"));
        Message result = msgService.deleteMessage(id);
        if(result != null){
            context.json(mapper.writeValueAsString(result));
        }
    }

    /**
     * Handler to get all messages for a user.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesForUserHandler(Context context) throws JsonProcessingException {
        Integer id = Integer.valueOf(context.pathParam("account_id"));
        context.json(msgService.getAllMessagesForUser(id));
    }

    /**
     * Handler to get all messages.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context context) {
        context.json(msgService.getAllMessages());
    }

    /**
     * Handler to get a message by its ID.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer id = Integer.valueOf(context.pathParam("message_id"));
        Message result = msgService.getMessage(id);
        if (result != null){
            context.json(mapper.writeValueAsString(result));
        }
    }

    /**
     * Handler to update a message's text.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void patchMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer id = Integer.valueOf(context.pathParam("message_id"));
        Message text = mapper.readValue(context.body(), Message.class);
        Message msg = null;
        if (msgService.getMessage(id) != null && text.getMessage_text().length() > 0 && text.getMessage_text().length() <= 255){
            msg = msgService.updateMessage(id, text.getMessage_text());
        }
        if (msg != null){
            context.json(mapper.writeValueAsString(msg));
        }else{
            context.status(400);
        }
    }

    /**
     * Handler to post an account login.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acct = mapper.readValue(context.body(), Account.class);
        Account result = acctService.loginUser(acct);
        if(result != null){
            context.json(mapper.writeValueAsString(result));
        }else{
            context.status(401);
        }
    }

    /**
     * Handler to post and register a new account.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountRegistrationHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acct = mapper.readValue(context.body(), Account.class);
        Account added = null;
        // Account username must not be blank and password must be at least 4 characters long
        if (acct.getUsername().length() > 0 && acct.getPassword().length() >= 4){
            added = acctService.registerUser(acct);
        }
        // Account username must not already exist within the database
        if(added != null){
            context.json(mapper.writeValueAsString(added));
        }else{
            context.status(400);
        }
    }

}