package galactic;

import galactic.net.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main extends Application {

    private static int globalPort = 51012;
    private static BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(10000);
    private static Scanner sc = new Scanner(System.in);

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
        //launch(args);
        ConnectionManager cm = new ConnectionManager(messageQueue, globalPort);
        cm.start();

        Socket client = null;
        try {
            client = new Socket("127.0.0.1", globalPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                out.writeUTF(sc.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
