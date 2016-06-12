import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientWorker extends Thread {

    private Socket clientSocket;
    private AtomicBoolean connected;

    public ClientWorker(Socket s, AtomicBoolean ab) {
        super();
        clientSocket = s;
        connected = ab;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!connected.get()) {
                    OutputStream outToServer = clientSocket.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outToServer);
                    out.writeUTF("Hello from "
                            + clientSocket.getLocalSocketAddress());
                    connected.set(true);
                }
                InputStream inFromServer = clientSocket.getInputStream();
                DataInputStream in =
                        new DataInputStream(inFromServer);
                System.out.println("Server says " + in.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
