package test.endtoend.auctionsniper;

import static com.objogate.wl.swing.driver.ComponentDriver.named;
import static com.objogate.wl.swing.driver.ComponentDriver.showingOnScreen;
import static org.hamcrest.CoreMatchers.equalTo;
import static test.endtoend.auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;

import auctionsniper.Main;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";

    private JFrameDriver driver;

    @SuppressWarnings("unchecked")
    public void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new JFrameDriver(
                new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named("Auction Sniper Main"),
                        showingOnScreen()
                ),
                new AWTEventQueueProber(1000, 100)
        );
        showsSniperStatus("Joining");
    }

    public void showsSniperHasLostAuction() {
        showsSniperStatus("Lost");
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    private void showsSniperStatus(String statusText) {
        new JLabelDriver(driver, named("sniper status"))
                .hasText(equalTo(statusText));
    }
}
