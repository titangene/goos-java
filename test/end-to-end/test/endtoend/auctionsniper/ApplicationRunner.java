package test.endtoend.auctionsniper;

import auctionsniper.Main;

public class ApplicationRunner {
    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
    }

    public void showsSniperHasLostAuction() {
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }
}
