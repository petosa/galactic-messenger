package galactic.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The reason we want to use a producer/consumer solution is so that the port can
 * be constantly monitored, with the least possible downtime; a decent amount of
 * work is required to convert incoming streams/strings into UI message
 * equivalents. Therefore, the corresponding consumer to this producer will
 * perform that requisite action.
 */

public class ConnectionProducer extends Thread {

    private AtomicBoolean toggle;
    private BlockingQueue<String> streamQueue;
    private Socket connection;
    private DataInputStream inputStream;

    public ConnectionProducer(BlockingQueue<String> streamQueue, Socket connection) {
        super();
        this.streamQueue = streamQueue;
        toggle = new AtomicBoolean(true);
        this.setDaemon(true);
        this.connection = connection;
    }

    public void terminate() { toggle.set(false); }

    public void setSocket(Socket connection) { this.connection = connection; }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(connection.getInputStream());

            while (toggle.get()) {
                streamQueue.put(inputStream.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
