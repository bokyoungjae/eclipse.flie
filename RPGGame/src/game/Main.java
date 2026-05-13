package game;

import game.ui.StartScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) { e.printStackTrace(); }
            new StartScreen().setVisible(true);
        });
    }
}
