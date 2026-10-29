package auctionsniper.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";

    private final JLabel sniperStatus = new JLabel(STATUS_JOINING);

    public MainWindow() {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        sniperStatus.setName(SNIPER_STATUS_NAME);
        add(sniperStatus);
        pack();
        setVisible(true);
    }

    public void showStatus(String status) {
        sniperStatus.setText(status);
    }
}
