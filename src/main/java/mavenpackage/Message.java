package myapp.src.main.java.mavenpackage;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
/**
 * Message
 * A class that represents the message that is sent between client and server
 */
public class Message {

    private String type;
    private String user;
    private String message;

    public Message(String message) {
        JSONObject json = (JSONObject) JSONValue.parse(message);
        this.type = (String)json.get("type");
        this.user = (String)json.get("user");
        this.message = (String)json.get("message");
    }

    public Message(String type, String user) {
        this.type = type;
        this.user = user;
    }

    public Message(String type, String user, String message) {
        this.type = type;
        this.user = user;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getJSONString() {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("type", this.type);
        jsonMessage.put("user", this.user);
        if (this.message != null) {
            jsonMessage.put("message", this.message);
        }
        return jsonMessage.toJSONString();
    }

}
