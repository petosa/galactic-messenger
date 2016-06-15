package galactic.net;

import galactic.model.TextMessage;

import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The call for a unified control panel. This is basically systemd.
 */
public class NetworkService {

    // Global resources
    private int port;

    // Inbound resources
    private BlockingQueue<String> inboundQueue;
    private Map<String, InboundProducer> inboundConnections;

    // Outbound resources
    private BlockingQueue<TextMessage> outboundQueue;
    private Map<String, Socket> outboundConnections;
    private Map<String, Boolean> connectionStatuses;

    // ThreadServices
    private InboundManager inboundManager;
    private OutboundManager outboundManager;

    // Consumers
    private InboundConsumer inboundConsumer;

    public NetworkService() {
        // Global sets
        this.port = 51012;

        // Inbound sets
        inboundQueue = new ArrayBlockingQueue<>(10000);
        inboundConnections = new HashMap<>();

        // Outbound sets
        outboundQueue = new ArrayBlockingQueue<>(10000);
        outboundConnections = Collections.synchronizedMap(new HashMap<>());
        connectionStatuses = Collections.synchronizedMap(new HashMap<>());

        inboundManager = new InboundManager(this);
        outboundManager = new OutboundManager(this);
        inboundConsumer = new InboundConsumer(this);

        inboundManager.start();
        outboundManager.start();
        inboundConsumer.start();
    }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public BlockingQueue<String> getInboundQueue() { return inboundQueue; }
    public void setInboundQueue(BlockingQueue<String> inboundQueue) { this.inboundQueue = inboundQueue; }

    public Map<String, InboundProducer> getInboundConnections() { return inboundConnections; }
    public void setInboundConnections(Map<String, InboundProducer> inboundConnections) { this.inboundConnections = inboundConnections; }

    public BlockingQueue<TextMessage> getOutboundQueue() { return outboundQueue; }
    public void setOutboundQueue(BlockingQueue<TextMessage> outboundQueue) { this.outboundQueue = outboundQueue; }

    public Map<String, Socket> getOutboundConnections() { return outboundConnections; }
    public void setOutboundConnections(Map<String, Socket> outboundConnections) { this.outboundConnections = outboundConnections; }

    public Map<String, Boolean> getConnectionStatuses() { return connectionStatuses; }
    public void setConnectionStatuses(Map<String, Boolean> connectionStatuses) { this.connectionStatuses = connectionStatuses; }

    public InboundManager getInboundManager() { return inboundManager; }
    public void setInboundManager(InboundManager inboundManager) { this.inboundManager = inboundManager; }

    public OutboundManager getOutboundManager() { return outboundManager; }
    public void setOutboundManager(OutboundManager outboundManager) { this.outboundManager = outboundManager; }
}
