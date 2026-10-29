package auctionsniper;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

public class Main {
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

        XMPPConnection connection = new XMPPConnection(args[0]);
        connection.connect();
        connection.login(args[1], args[2], "Auction");

        Chat chat = connection.getChatManager().createChat(
                "auction-" + args[3] + "@" + connection.getServiceName() + "/Auction",
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
