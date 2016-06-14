package galactic.net;

import galactic.Main;
import galactic.model.TextMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The OutboundProducer can be implemented on the main thread without incident
 */
public class OutboundManager extends Thread {
    private AtomicBoolean toggle;
    private BlockingQueue<TextMessage> outboundQueue;
    private Map<String, Socket> outboundConnections;
    private Map<String, Boolean> connectionStatuses;

    public OutboundManager(BlockingQueue<TextMessage> outboundQueue) {
        System.out.println("Starting OutboundManager...");
        toggle = new AtomicBoolean(true);
        this.setDaemon(true);
        this.outboundQueue = outboundQueue;
        this.outboundConnections = Collections.synchronizedMap(new HashMap<>());
        this.connectionStatuses = Collections.synchronizedMap(new HashMap<>());
    }

    public void terminate() { toggle.set(false); }

    public Map<String, Socket> getOutboundConnections() { return outboundConnections; }
    public void setOutboundConnections(Map<String, Socket> outboundConnections) { this.outboundConnections = outboundConnections; }

    public Map<String, Boolean> getConnectionStatuses() { return connectionStatuses; }
    public void setConnectionStatuses(Map<String, Boolean> connectionStatuses) { this.connectionStatuses = connectionStatuses; }

    @Override
    public void run() {
        while (toggle.get()) {
            try {
                TextMessage cm = outboundQueue.take();
                for (String ip : cm.getSession().getDestinationIPs()) {
                    boolean connectionEstablished = true;

                    if (!outboundConnections.containsKey(ip)) {
                        try {
                            Socket connection = new Socket(ip, Main.GLOBAL_PORT);
                            if (connection != null) {
                                outboundConnections.put(ip, connection);
                                connectionStatuses.put(ip, true);
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
                            DataOutputStream out = new DataOutputStream(outboundConnections.get(ip).getOutputStream());
                            out.writeUTF(cm.toString());
                        } catch (SocketException e) {
                            connectionStatuses.put(ip, false);
                            System.out.println("NSE: Your message was not sent to " + ip + ". They may be offline.");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
