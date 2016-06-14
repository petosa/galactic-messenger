package galactic.net;

import java.io.DataInputStream;
import java.io.EOFException;
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

public class InboundProducer extends Thread {

    private AtomicBoolean toggle;
    private BlockingQueue<String> inboundQueue;
    private Socket connection;
    private DataInputStream inputStream;
    private OutboundManager outboundManager;

    public InboundProducer(BlockingQueue<String> inboundQueue, Socket connection, OutboundManager outboundManager) {
        super();
        this.inboundQueue = inboundQueue;
        toggle = new AtomicBoolean(true);
        this.setDaemon(true);
        this.connection = connection;
        this.outboundManager = outboundManager;
    }

    public void terminate() { toggle.set(false); }

    public Socket getSocket() { return connection; }
    public void setSocket(Socket connection) { this.connection = connection; }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(connection.getInputStream());

            while (toggle.get()) {
                inboundQueue.put(inputStream.readUTF());
            }
        } catch (EOFException e) {
            String remoteAddress = connection.getInetAddress().getHostAddress();
            outboundManager.getConnectionStatuses().put(remoteAddress, false);
            System.out.println(remoteAddress + " has disconnected.");
        } catch (SocketException e) {
            String remoteAddress = connection.getInetAddress().getHostAddress();
            outboundManager.getConnectionStatuses().put(remoteAddress, false);
            System.out.println(remoteAddress + " has disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        String theirs = ((InboundProducer) o).getSocket().getInetAddress().toString();
        String ours = this.getSocket().getInetAddress().toString();

        return theirs.equals(ours);
    }
}
