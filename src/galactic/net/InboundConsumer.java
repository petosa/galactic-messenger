package galactic.net;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */

public class InboundConsumer extends Thread {
    private AtomicBoolean toggle;
    private BlockingQueue<String> inboundQueue;

    public InboundConsumer(BlockingQueue<String> inboundQueue) {
        this.inboundQueue = inboundQueue;
        this.setDaemon(true);
        toggle = new AtomicBoolean(true);
    }

    public void terminate() { toggle.set(false); }

    @Override
    public void run() {
        while (toggle.get()) {
            try {
                String s = inboundQueue.take();
                System.out.println(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
