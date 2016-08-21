package galactic.net;

import galactic.model.TextMessage;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * The OutboundProducer can be implemented on the main thread without incident
 */
public class OutboundManager extends Thread {
    private NetworkService networkService;

    public OutboundManager(NetworkService networkService) {
        super();
        System.out.println("Starting OutboundManager...");
        this.setDaemon(true);
        this.networkService = networkService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Pop a TextMessage from our outbound message queue
                TextMessage cm = networkService.getOutboundQueue().take();

                // Attempt to establish connections to each of its destination IP addresses
                for (String ip : cm.getSession().getDestinationIPs()) {
                    try {
                        Socket connection = null;

                        // Check if the connection to an IP already exists and act on it
                        if (!networkService.getOutboundConnections().containsKey(ip)) {
                            connection = new Socket(ip, networkService.getPort());
                        } else {
                            connection = networkService.getOutboundConnections().get(ip);
                        }

                        // Immediately check if a connection is live and act on it
                        if (connection.isConnected()) {
                            networkService.getOutboundConnections().put(ip, connection);
                            networkService.getConnectionStatuses().put(ip, true);
                            ObjectOutputStream out = new ObjectOutputStream(networkService.getOutboundConnections().get(ip).getOutputStream());
                            out.writeObject(cm);
                        } else {
                            networkService.getConnectionStatuses().put(ip, false);
                            System.out.println("NSE: Your message could not be sent to " + ip + ". They may be offline.");
                        }
                    } catch (UnknownHostException e) {
                        System.out.println("Could not resolve hostname " + ip + "...");
                    } catch (SocketException e) {
                        networkService.getConnectionStatuses().put(ip, false);
                        System.out.println("SE: Your message was not sent to " + ip + ". They may be offline.");
                    } catch (IOException e) {
                        networkService.getConnectionStatuses().put(ip, false);
                        System.out.println("NSE: Your message was not sent to " + ip + ". They may be offline.");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
