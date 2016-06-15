package galactic.net;

import galactic.model.TextMessage;

/**
 *
 */

public class InboundConsumer extends Thread {
    private NetworkService networkService;

    public InboundConsumer(NetworkService networkService) {
        super();
        this.setDaemon(true);
        this.networkService = networkService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TextMessage tm = networkService.getInboundQueue().take();
                System.out.println(tm);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
