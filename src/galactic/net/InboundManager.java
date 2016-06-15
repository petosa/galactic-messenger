package galactic.net;

import galactic.Main;

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
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String remoteAddress = socket.getInetAddress().getHostAddress();
            System.out.println(remoteAddress + " is asking to join...");
            InboundProducer c = new InboundProducer(networkService, socket);

            if (!networkService.getConnectionStatuses().containsKey(remoteAddress)) {
                try {
                    networkService.getOutboundConnections().put(remoteAddress, new Socket(remoteAddress, networkService.getPort()));
                    networkService.getConnectionStatuses().put(remoteAddress, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (!networkService.getConnectionStatuses().get(remoteAddress)) {
                    try {
                        networkService.getOutboundConnections().put(remoteAddress, new Socket(remoteAddress, networkService.getPort()));
                        networkService.getConnectionStatuses().put(remoteAddress, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (networkService.getInboundConnections().containsKey(remoteAddress)) {
                System.out.println(socket.getInetAddress().getHostAddress() + " has reconnected.");
            } else {
                System.out.println(socket.getInetAddress().getHostAddress() + " has connected.");
            }

            networkService.getInboundConnections().put(remoteAddress, c);
            c.start();
        }
    }
}
