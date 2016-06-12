package galactic.model;

import javafx.scene.control.ListCell;

/**
 * The TextMessage will be used for standard messages as sent by chat participants; i.e. users
 */

public class TextMessage implements ChatMessage {
    private String messageContents;

    public TextMessage(String messageContents) {
        this.messageContents = messageContents;
    }

    public String getMessageContents() { return messageContents; }
    public void setMessageContents(String messageContents) { this.messageContents = messageContents; }

    @Override
    public ListCell<ChatMessage> getListCell() {
        return null;
    }
}
