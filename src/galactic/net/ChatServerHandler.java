package galactic.net;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChatServerHandler extends Thread {

    private AtomicBoolean toggle;

    public ChatServerHandler() {
        super();
        toggle = new AtomicBoolean(true);
    }

    public void terminate() { toggle.set(false); }

    @Override
    public void run() {
        while (toggle.get()) {
            System.out.println("absolute gay");
        }
    }
}
