import java.io.*;
import java.net.Socket;

public class ClientWorker extends Thread {

    private Socket clientSocket;

    public ClientWorker(Socket s) {
        super();
        clientSocket = s;
    }

    @Override
    public void run() {
        while (true) {
            try {
                OutputStream outToServer = clientSocket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF("Hello from "
                        + clientSocket.getLocalSocketAddress());
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
