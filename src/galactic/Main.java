package galactic;

import galactic.model.Session;
import galactic.model.TextMessage;
import galactic.net.NetworkService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends Application {
    private static Scanner sc = new Scanner(System.in);
    private String handle;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Type the handle you'd like to be referred to by:");
        handle = sc.nextLine();

        // We begin the NetworkService which handles all requisite networking behind the scenes
        NetworkService networkService = new NetworkService();

        System.out.println("Type the IPs you want to connect to, separated by commas:");
        String[] ips = sc.nextLine().split(",");
        for (int i = 0; i < ips.length; i++) {
            ips[i] = InetAddress.getByName(ips[i].trim()).getHostAddress();
        }

        // Pass in the list of IPs to our Session constructor
        Session s = new Session(Arrays.asList(ips));
        System.out.println("Start sending messages to begin:");

        /*Parent root = FXMLLoader.load(getClass().getResource("view/mainWindow.fxml"));

        primaryStage.setMaxWidth(900);
        primaryStage.setMaxHeight(600);
        primaryStage.setMinWidth(450);
        primaryStage.setMinHeight(300);

        primaryStage.setTitle("Galactic Messenger");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();*/

        // Adds messages as entered through Scanner in the console to the OutboundQueue
        while (true) {
            String messageContents = sc.nextLine();
            networkService.getOutboundQueue().put(new TextMessage(handle, messageContents, s));
        }
    }

    public static void main(String[] args) { launch(args); }
}
