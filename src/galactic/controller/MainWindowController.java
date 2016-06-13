package galactic.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import galactic.net.IncomingMessageProducer;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML private Button connectButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert connectButton != null : "id=\"connectButton\" was not injected: check your FXML file 'mainWindow.fxml'.";

        /*IncomingMessageProducer csh = new IncomingMessageProducer();

        connectButton.setOnMouseClicked(event -> csh.terminate());

        csh.setDaemon(true);
        csh.start();*/
    }
}
