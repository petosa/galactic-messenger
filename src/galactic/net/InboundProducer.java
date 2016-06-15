package galactic.net;

import galactic.model.TextMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * The reason we want to use a producer/consumer solution is so that the port can
 * be constantly monitored, with the least possible downtime; a decent amount of
 * work is required to convert incoming streams/strings into UI message
 * equivalents. Therefore, the corresponding consumer to this producer will
 * perform that requisite action.
 */

public class InboundProducer extends Thread {
    private Socket connection;
    private ObjectInputStream inputStream;
    private NetworkService networkService;

    public InboundProducer(NetworkService networkService, Socket connection) {
        super();
        this.setDaemon(true);
        this.connection = connection;
        this.networkService = networkService;
    }

    public Socket getSocket() { return connection; }
    public void setSocket(Socket connection) { this.connection = connection; }

    @Override
    public void run() {
        String remoteAddress = connection.getInetAddress().getHostAddress();

        try {
            while (true) {
                inputStream = new ObjectInputStream(connection.getInputStream());
                networkService.getInboundQueue().put((TextMessage) inputStream.readObject());
            }
        } catch (EOFException | SocketException e) {
            networkService.getConnectionStatuses().put(remoteAddress, false);
            System.out.println(remoteAddress + " has disconnected.");
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
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
