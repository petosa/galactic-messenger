import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Nick on 6/11/2016.
 */
public class Host implements Runnable{

    private int port;

    public Host(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("I can't host on this port.");
        }
        System.out.println("Listening on port " + server.getLocalPort() + ".");

        Socket serverReader = null;
        //Keep listening
        boolean found = false;
        while(!found) {
            try {
                serverReader = server.accept();
                found = true;
            } catch (IOException e) {
                System.err.println("Still listening...");
            }
        }
        DataInputStream in = null;
        DataOutputStream out = null;

        System.out.println("External source found us: "
                + serverReader.getRemoteSocketAddress());
        while (true) {
            try {
                in = new DataInputStream(serverReader.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println(in.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
