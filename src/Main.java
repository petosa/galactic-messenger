import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {

    private static Scanner sc = new Scanner(System.in);
    private static AtomicBoolean connectionEstablished = new AtomicBoolean();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/mainWindow.fxml"));

        primaryStage.setMaxWidth(900);
        primaryStage.setMaxHeight(600);
        primaryStage.setMinWidth(450);
        primaryStage.setMinHeight(300);

        primaryStage.setTitle("Galactic Messenger");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
