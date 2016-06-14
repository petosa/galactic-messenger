package galactic.model;

import javafx.scene.control.ListCell;

/**
 * The TextMessage will be used for standard messages as sent by chat participants; i.e. users
 */

public class TextMessage implements ChatMessage {
    private String handle;
    private String messageContents;
    private Session session;

    public TextMessage(String handle, String messageContents, Session session) {
        this.handle = handle;
        this.messageContents = messageContents;
        this.session = session;
    }

    public String getHandle() { return handle; }
    public void setHandle(String handle) { this.handle = handle; }

    public String getMessageContents() { return messageContents; }
    public void setMessageContents(String messageContents) { this.messageContents = messageContents; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }

    @Override
    public ListCell<ChatMessage> getListCell() { return null; }

    @Override
    public String toString() {
        return handle + ": " + messageContents;
    }
}
