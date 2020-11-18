package myapp.src.main.java.mavenpackage;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Message A class that represents the message that is sent between client and
 * server
 */
public class Message {

    private String type;
    private String user;
    private String message;

    /**
     * <p>Constructor for Message.</p>
     *
     * @param message a String to be converted to a Message object.
     */
    public Message(String message) {
        JSONObject json = (JSONObject) JSONValue.parse(message);
        this.type = (String) json.get("type");
        this.user = (String) json.get("user");
        this.message = (String) json.get("message");
    }

    /**
     * <p>Constructor for Message.</p>
     *
     * @param type identifies if the type is login, logout or math request.
     * @param user identifies the user for Connection.
     */
    public Message(String type, String user) {
        this.type = type;
        this.user = user;
    }

    /**
     * <p>Constructor for Message.</p>
     *
     * @param type
     * @param user
     * @param message
     */
    public Message(String type, String user, String message) {
        this.type = type;
        this.user = user;
        this.message = message;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Getter for the field <code>user</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUser() {
        return user;
    }

    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Converts the JSONObject to a String
     *
     * @return a {@link java.lang.String} object.
     */
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
