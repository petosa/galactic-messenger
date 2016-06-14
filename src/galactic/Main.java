package galactic;

import galactic.net.ConnectionConsumer;
import galactic.net.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main extends Application {

    private final int globalPort = 51012;
    private static BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(10000);
    private static Scanner sc = new Scanner(System.in);

    @Override
    public void start(Stage primaryStage) throws Exception {
        ConnectionManager cm = new ConnectionManager(messageQueue, globalPort);
        ConnectionConsumer cc = new ConnectionConsumer(messageQueue);
        cm.start();
        cc.start();

        String[] ips = {"theunixphilosophy.com", "68.132.38.122"};
        List<Socket> clients = new ArrayList<>();
        try {
            for(String ip : ips) {
                clients.add(new Socket(ip, globalPort));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource("view/mainWindow.fxml"));

        primaryStage.setMaxWidth(900);
        primaryStage.setMaxHeight(600);
        primaryStage.setMinWidth(450);
        primaryStage.setMinHeight(300);

        primaryStage.setTitle("Galactic Messenger");
        primaryStage.setScene(new Scene(root, 900, 600));
        //primaryStage.show();

        while (true) {
            try {
                String toSend = sc.nextLine();
                for(Socket s : clients) {
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    out.writeUTF(toSend);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) { launch(args); }

}
