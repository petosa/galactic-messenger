package galactic.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.List;

public class Session implements Serializable {
    private String sessionIdentifier;
    private List<String> destinationIPs;
    private List<ChatMessage> messageList;

    public Session(List<String> destinationIPs) {
        this.destinationIPs = destinationIPs;
    }

    public List<String> getDestinationIPs() { return destinationIPs; }

    public ObservableList<ChatMessage> getFXList() { return FXCollections.observableList(messageList); }
}
