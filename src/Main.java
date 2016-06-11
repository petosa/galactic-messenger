import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main extends Application {

    private static Scanner sc = new Scanner(System.in);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args){
        //launch(args);

        Thread t = new Thread(new Host());
        t.start();

        Scanner in = new Scanner(System.in);
        final String SERVERNAME = "127.0.0.1";
        int PORT = 51012;
        Socket client = null;
        try {
            client = new Socket(SERVERNAME, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connecting to " + SERVERNAME +
                " on port " + PORT);
        System.out.println("Just connected to "
                + client.getRemoteSocketAddress());
        while(true) {
            OutputStream outToServer = null;
            try {
                outToServer = client.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataOutputStream out = new DataOutputStream(outToServer);
            try {
                out.writeUTF(in.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
