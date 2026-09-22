package auctionsniper;

import auctionsniper.ui.MainWindow;

import javax.swing.SwingUtilities;

public class Main {
    public Main() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        new Main();
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                MainWindow ui = new MainWindow();
            }
        });
    }
}
