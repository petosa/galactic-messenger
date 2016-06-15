package galactic.net;

import galactic.Main;
import galactic.model.TextMessage;

import java.io.DataOutputStream;
import java.io.IOException;
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
                TextMessage cm = networkService.getOutboundQueue().take();
                for (String ip : cm.getSession().getDestinationIPs()) {
                    boolean connectionEstablished = true;

                    if (!networkService.getOutboundConnections().containsKey(ip)) {
                        try {
                            Socket connection = new Socket(ip, networkService.getPort());
                            if (connection != null) {
                                networkService.getOutboundConnections().put(ip, connection);
                                networkService.getConnectionStatuses().put(ip, true);
                            } else {
                                connectionEstablished = false;
                                System.out.println("NSE: Your message was not sent to " + ip + ". They may be offline.");
                            }
                        } catch (UnknownHostException e) {
                            connectionEstablished = false;
                            System.out.println("Could not resolve hostname " + ip + "...");
                        } catch (SocketException e) {
                            connectionEstablished = false;
                            System.out.println("SE: Your message was not sent to " + ip + ". They may be offline.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (connectionEstablished) {
                        try {
                            DataOutputStream out = new DataOutputStream(networkService.getOutboundConnections().get(ip).getOutputStream());
                            out.writeUTF(cm.toString());
                        } catch (SocketException e) {
                            networkService.getConnectionStatuses().put(ip, false);
                            System.out.println("NSE: Your message was not sent to " + ip + ". They may be offline.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
