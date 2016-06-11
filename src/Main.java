import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;

import java.net.Socket;
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


    public static void main(String[] args) {
        //launch(args);

        final String SERVERNAME = "theunixphilosophy.com";
        int PORT = 51012;
        try {
            System.out.println("Connecting to " + SERVERNAME +
                    " on port " + PORT);
            Socket client = new Socket(SERVERNAME, PORT);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);


            out.writeUTF("Hello from "
                    + client.getLocalSocketAddress());
        }catch(Exception e) {

        }




        ServerSocket server = null;

        try {
            server = new ServerSocket(51012);
        } catch (IOException e) {
            System.out.print("fuk u faguette | ");
            e.printStackTrace();
        }

        System.out.println("The port we will use is " + server.getLocalPort());
        System.out.println("Begin inputting messages you want to send");

        while (true) {
            try {
                Socket serverReader = server.accept();
                DataInputStream in =
                        new DataInputStream(serverReader.getInputStream());

                System.out.println("Just connected to "
                        + serverReader.getRemoteSocketAddress());

                System.out.println(in.readUTF());

                DataOutputStream out =
                        new DataOutputStream(serverReader.getOutputStream());
                out.writeUTF("Fuckinggay says " + sc.nextLine());
                serverReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }


           /* InputStream inFromServer = client.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromServer);
            System.out.println("Server says " + in.readUTF());
            client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }*/

}
