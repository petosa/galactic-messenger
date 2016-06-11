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

        //Declare variables
        final String SERVERNAME = "127.0.0.1";
        final int PORT = 51012;
        final int TIMEOUT = 1000;

        //Start the listener thread
        Thread t = new Thread(new Host(PORT));
        t.start();

        try {
            System.out.println("Searching for a host on " + SERVERNAME + ":" + PORT + "...");
            Socket client = new Socket();
            client.connect(new InetSocketAddress(SERVERNAME, PORT), TIMEOUT);
            runClient(client, SERVERNAME, PORT);
        } catch (IOException e) {
            System.err.println("Could not find a host.");
        }

    }

    public static void runClient(Socket client, String server, int port) {
        final Scanner in = new Scanner(System.in);

        System.out.println("Found a host at " + server + ":" + port + ".");
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
