package galactic.net;

import galactic.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The way Socket connections are handled in Java requires a new Socket object
 * for each connection made. How annoying. We have a reference to outbound
 * manager to automatically edit its connections.
 */

public class InboundManager extends Thread {
    private AtomicBoolean toggle;
    private BlockingQueue<String> inboundQueue;
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, InboundProducer> inboundConnections;
    private OutboundManager outboundManager;

    public InboundManager(BlockingQueue<String> inboundQueue, int port, OutboundManager outboundManager) {
        super();
        this.inboundQueue = inboundQueue;
        toggle = new AtomicBoolean(true);
        this.setDaemon(true);
        inboundConnections = new HashMap<>();
        this.outboundManager = outboundManager;

        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Starting InboundManager on port " + serverSocket.getLocalPort() + "...");
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

            String remoteAddress = socket.getInetAddress().getHostAddress();
            System.out.println(remoteAddress + " is asking to join...");
            InboundProducer c = new InboundProducer(inboundQueue, socket, outboundManager);

            if (!outboundManager.getConnectionStatuses().containsKey(remoteAddress)) {
                try {
                    outboundManager.getOutboundConnections().put(remoteAddress, new Socket(remoteAddress, Main.GLOBAL_PORT));
                    outboundManager.getConnectionStatuses().put(remoteAddress, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (!outboundManager.getConnectionStatuses().get(remoteAddress)) {
                    try {
                        outboundManager.getOutboundConnections().put(remoteAddress, new Socket(remoteAddress, Main.GLOBAL_PORT));
                        outboundManager.getConnectionStatuses().put(remoteAddress, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (inboundConnections.containsKey(remoteAddress)) {
                inboundConnections.put(remoteAddress, c);
                System.out.println(socket.getInetAddress().getHostAddress() + " has reconnected.");
            } else {
                inboundConnections.put(remoteAddress, c);
                System.out.println(socket.getInetAddress().getHostAddress() + " has connected.");
            }

            c.start();
        }
    }
}
