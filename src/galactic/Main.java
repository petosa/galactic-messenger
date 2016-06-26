package galactic;

import galactic.model.Session;
import galactic.model.TextMessage;
import galactic.net.NetworkService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends Application {
    private static Scanner sc = new Scanner(System.in);
    private String handle;
    private String password;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Create private
        File f = new File("galactic.keystore");
        if(f.exists() && !f.isDirectory()) {
            System.out.println("Enter your password:");
            password = sc.nextLine();
        } else {
            System.out.println("Welcome to galactic-messenger. Create a password below.");
            password = sc.nextLine();

            String[] cmd = {"-genkeypair", "-alias", "galactic", "-keyalg", "RSA", "-sigalg", "SHA256withRSA", "-dname", "CN=Java",
                    "-storetype", "JKS", "-keypass", password, "-keystore", "galactic.keystore", "-storepass", password};
            try{
                sun.security.tools.keytool.Main.main(cmd);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        System.setProperty("javax.net.ssl.keyStore", "galactic.keystore");
        System.setProperty("javax.net.ssl.trustStore", "galactic.keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", password);

        // We begin the NetworkService which handles all requisite networking behind the scenes
        NetworkService networkService = new NetworkService();

        System.out.println("Type the handle you'd like to be referred to by:");
        handle = sc.nextLine();

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