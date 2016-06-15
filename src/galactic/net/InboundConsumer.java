package galactic.net;

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
                String s = networkService.getInboundQueue().take();
                System.out.println(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
