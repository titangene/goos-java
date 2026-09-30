package test.endtoend.auctionsniper;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class FakeAuctionServer {
    public static final String XMPP_HOSTNAME = "localhost";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(
            String.format(ITEM_ID_AS_LOGIN, itemId),
            AUCTION_PASSWORD,
            AUCTION_RESOURCE
        );
    }

    public void hasReceivedJoinRequestFromSniper() {}

    public void announceClosed() {}

    public void stop() {
        connection.disconnect();
    }
}
