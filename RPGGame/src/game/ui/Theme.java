package game.ui;

import java.awt.*;

public class Theme {
    // Dark fantasy color palette
    public static final Color BG_DARK    = new Color(10, 10, 25);
    public static final Color BG_PANEL   = new Color(20, 20, 45);
    public static final Color BG_CARD    = new Color(30, 30, 60);
    public static final Color BG_BUTTON  = new Color(50, 40, 90);
    public static final Color BG_HOVER   = new Color(70, 55, 130);
    public static final Color BG_DANGER  = new Color(120, 20, 20);
    public static final Color BG_SUCCESS = new Color(20, 90, 40);

    public static final Color GOLD      = new Color(255, 200, 50);
    public static final Color SILVER    = new Color(200, 200, 220);
    public static final Color TEXT_MAIN = new Color(230, 220, 255);
    public static final Color TEXT_DIM  = new Color(150, 140, 180);
    public static final Color HP_RED    = new Color(220, 50, 50);
    public static final Color HP_GREEN  = new Color(50, 200, 80);
    public static final Color EXP_BLUE  = new Color(80, 130, 255);
    public static final Color ACCENT    = new Color(160, 100, 255);

    public static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD, 28);
    public static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_BODY   = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_EMOJI  = new Font("Segoe UI Emoji", Font.PLAIN, 24);

    public static void styleButton(javax.swing.JButton btn) {
        btn.setBackground(BG_BUTTON);
        btn.setForeground(TEXT_MAIN);
        btn.setFont(FONT_BODY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    public static void styleDangerButton(javax.swing.JButton btn) {
        styleButton(btn);
        btn.setBackground(BG_DANGER);
    }

    public static void styleSuccessButton(javax.swing.JButton btn) {
        styleButton(btn);
        btn.setBackground(BG_SUCCESS);
    }

    /** Draw a gradient background on a Graphics2D */
    public static void paintBg(Graphics g, int w, int h) {
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, new Color(5,5,20), w, h, new Color(30,15,60));
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
    }

    /** Draw stars on background */
    public static void paintStars(Graphics g, int w, int h, java.util.Random rng) {
        g.setColor(new Color(255,255,255,80));
        for (int i = 0; i < 60; i++) {
            int x = rng.nextInt(w), y = rng.nextInt(h);
            g.fillOval(x, y, 2, 2);
        }
    }
}
