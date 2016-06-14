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
    }

    public void terminate() { toggle.set(false); }

    @Override
    public void run() {
        while (toggle.get()) {
            String s = null;
            if ((s = streamQueue.poll()) != null) {
                
            }
        }
    }
}
