package galactic.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The reason we want to use a producer/consumer solution is so that the port can
 * be constantly monitored, with the least possible downtime; a decent amount of
 * work is required to convert incoming streams/strings into UI message
 * equivalents. Therefore, the corresponding consumer to this producer will
 * perform that requisite action.
 */

public class IncomingMessageProducer extends Thread {

    private AtomicBoolean toggle;
    private BlockingQueue<String> streamQueue;
    private ServerSocket serverSocket;
    private Socket implicitServerClient;
    private DataInputStream inputStream;

    public IncomingMessageProducer(BlockingQueue<String> streamQueue, int port) {
        super();
        this.streamQueue = streamQueue;
        toggle = new AtomicBoolean(true);
        this.setDaemon(true);

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() { toggle.set(false); }

    @Override
    public void run() {
        try {
            implicitServerClient = serverSocket.accept();
            inputStream = new DataInputStream(implicitServerClient.getInputStream());

            while (toggle.get()) {
                streamQueue.put(inputStream.readUTF());
                System.out.println("Successfully added something to our StreamQueue");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
