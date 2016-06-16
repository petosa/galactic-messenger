package galactic.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The way Socket connections are handled in Java requires a new Socket object
 * for each connection made. How annoying. We have a reference to outbound
 * manager to automatically edit its connections.
 */

public class InboundManager extends Thread {
    private ServerSocket serverSocket;
    private Socket socket;
    private NetworkService networkService;

    public InboundManager(NetworkService networkService) {
        super();
        this.setDaemon(true);
        this.networkService = networkService;

        // Start the ServerSocket that will listen for incoming connections
        try {
            this.serverSocket = new ServerSocket(networkService.getPort());
            System.out.println("Starting InboundManager on port " + serverSocket.getLocalPort() + "...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            // The ServerSocket blocks until an incoming connection gets detected, in which case
            // the ServerSocket returns a Socket that represents that incoming connection
            // essentially converts an incoming connection into a Socket object that we can read from
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Get the host address from the Socket we just generated and create
            // a new Thread for the new connection, to deal with Java's blocking IO
            String remoteAddress = socket.getInetAddress().getHostAddress();
            System.out.println(remoteAddress + " is attempting to join...");
            InboundProducer c = new InboundProducer(networkService, socket);

            // This section checks if a matching OutboundConnection exists, and if not,
            // attempts to create a matching OutboundConnection for the Inbound one
            try {
                if (!networkService.getConnectionStatuses().containsKey(remoteAddress)) {
                    networkService.getOutboundConnections().put(remoteAddress, new Socket(remoteAddress, networkService.getPort()));
                    networkService.getConnectionStatuses().put(remoteAddress, true);
                } else {
                    if (!networkService.getConnectionStatuses().get(remoteAddress)) {
                        networkService.getOutboundConnections().put(remoteAddress, new Socket(remoteAddress, networkService.getPort()));
                        networkService.getConnectionStatuses().put(remoteAddress, true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Log messages based on actual status of the connection
            if (networkService.getInboundConnections().containsKey(remoteAddress)) {
                System.out.println(socket.getInetAddress().getHostAddress() + " has reconnected.");
            } else {
                System.out.println(socket.getInetAddress().getHostAddress() + " has connected.");
            }

            // Start a thread to listen on that connection
            networkService.getInboundConnections().put(remoteAddress, c);
            c.start();
        }
    }
}
