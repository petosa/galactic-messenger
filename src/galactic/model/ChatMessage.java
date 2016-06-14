package galactic.model;

import javafx.scene.control.ListCell;

/**
 * Note that the interface ChatMessage refers to messages as they appear in chatboxes
 */

public interface ChatMessage {
    ListCell<ChatMessage> getListCell();
}
