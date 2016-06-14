package galactic;

import galactic.model.Session;
import galactic.model.TextMessage;
import galactic.net.InboundConsumer;
import galactic.net.InboundManager;
import galactic.net.OutboundManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main extends Application {

    public static final int GLOBAL_PORT = 51012;
    private static BlockingQueue<String> inboundQueue = new ArrayBlockingQueue<>(10000);
    private static BlockingQueue<TextMessage> outboundQueue = new ArrayBlockingQueue<>(10000);
    private static Scanner sc = new Scanner(System.in);
    private String handle;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Type the handle you'd like to be referred to by:");
        handle = sc.nextLine();

        OutboundManager om = new OutboundManager(outboundQueue);
        InboundManager cm = new InboundManager(inboundQueue, GLOBAL_PORT, om);
        InboundConsumer cc = new InboundConsumer(inboundQueue);
        om.start();
        cm.start();
        cc.start();

        System.out.println("Type the IPs you want to connect to, separated by commas:");
        String[] ips = sc.nextLine().split(",");
        for (int i = 0; i < ips.length; i++) {
            ips[i] = ips[i].trim();
        }

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

        while (true) {
            String messageContents = sc.nextLine();
            outboundQueue.put(new TextMessage(handle, messageContents, s));
        }
    }

    public static void main(String[] args) { launch(args); }

}
