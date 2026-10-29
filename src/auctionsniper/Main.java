package auctionsniper;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

public class Main {
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    private static JLabel sniperStatus;

    @SuppressWarnings("unused")
    private static Chat notToBeGCd;

    public static void main(String... args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                sniperStatus = new JLabel("Joining");
                sniperStatus.setName("sniper status");

                JFrame frame = new JFrame("Auction Sniper");
                frame.setName("Auction Sniper Main");
                frame.add(sniperStatus);
                frame.pack();
                frame.setVisible(true);
            }
        });

        XMPPConnection connection = new XMPPConnection(args[ARG_HOSTNAME]);
        connection.connect();
        connection.login(args[ARG_USERNAME], args[ARG_PASSWORD], "Auction");

        Chat chat = connection.getChatManager().createChat(
                "auction-" + args[ARG_ITEM_ID] + "@" + connection.getServiceName() + "/Auction",
                new MessageListener() {
                    public void processMessage(Chat aChat, Message message) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                sniperStatus.setText("Lost");
                            }
                        });
                    }
                });
        notToBeGCd = chat;
        chat.sendMessage(new Message());
    }
}
