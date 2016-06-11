import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Nick on 6/11/2016.
 */
public class Host implements Runnable{
    @Override
    public void run() {
        ServerSocket server = null;

        try {
            server = new ServerSocket(51012);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Listening on port " + server.getLocalPort());

        Socket serverReader = null;
        try {
            serverReader = server.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataInputStream in = null;
        try {
            in = new DataInputStream(serverReader.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connected to external source: "
                + serverReader.getRemoteSocketAddress());
        while (true) {
            try {
                System.out.println(in.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
