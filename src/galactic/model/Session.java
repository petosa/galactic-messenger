package galactic.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private String sessionIdentifier;
    private List<String> destinationIPs;
    private ArrayList<ChatMessage> messageList;

    public Session(List<String> destinationIPs) {
        this.destinationIPs = destinationIPs;
    }

    public List<String> getDestinationIPs() { return destinationIPs; }

    public ObservableList<ChatMessage> getFXList() { return FXCollections.observableList(messageList); }
}
