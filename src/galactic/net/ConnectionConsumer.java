package galactic.net;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */

public class ConnectionConsumer extends Thread {
    private AtomicBoolean toggle;
    private BlockingQueue<String> streamQueue;

    public ConnectionConsumer(BlockingQueue<String> streamQueue) {
        this.streamQueue = streamQueue;
        toggle = new AtomicBoolean(true);
    }

    public void terminate() { toggle.set(false); }

    @Override
    public void run() {
        while (toggle.get()) {
            try {
                String s = streamQueue.take();
                System.out.println(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
