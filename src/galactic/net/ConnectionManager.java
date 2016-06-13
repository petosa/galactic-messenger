package galactic.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The way Socket connections are handled in Java requires a new Socket object
 * for each connection made. How annoying.
 */

public class ConnectionManager extends Thread {
    private AtomicBoolean toggle;
    private BlockingQueue<String> streamQueue;
    private ServerSocket serverSocket;
    private Socket socket;
    private List<Thread> threadPool;

    public ConnectionManager(BlockingQueue<String> streamQueue, int port) {
        super();
        this.streamQueue = streamQueue;
        toggle = new AtomicBoolean(true);
        this.setDaemon(true);
        threadPool = new ArrayList<>();

        try {
            this.serverSocket = new ServerSocket(port);
            this.serverSocket.setSoTimeout(10000);
            System.out.println("ConnectionManager now running on " + serverSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() { toggle.set(false); }

    @Override
    public void run() {
        while (toggle.get()) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ConnectionProducer c = new ConnectionProducer(streamQueue, socket);
            threadPool.add(c);
            c.start();

            System.out.println("Successfully added client " + socket.getRemoteSocketAddress());
        }
    }
}
