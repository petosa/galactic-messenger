package galactic.net;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
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
    private DataInputStream inputStream;
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
        try {
            inputStream = new DataInputStream(connection.getInputStream());

            while (true) {
                networkService.getInboundQueue().put(inputStream.readUTF());
            }
        } catch (EOFException e) {
            String remoteAddress = connection.getInetAddress().getHostAddress();
            networkService.getConnectionStatuses().put(remoteAddress, false);
            System.out.println(remoteAddress + " has disconnected.");
        } catch (SocketException e) {
            String remoteAddress = connection.getInetAddress().getHostAddress();
            networkService.getConnectionStatuses().put(remoteAddress, false);
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
