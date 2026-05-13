package game.ui;

import game.data.CharacterData;
import game.data.CharacterData.CharacterClass;
import game.data.UserData;
import game.system.SaveSystem;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NicknameCharacterScreen extends JFrame {
    private static final Random STAR_RNG = new Random(99);
    private UserData user;
    private int selectedIdx = -1;
    private JLabel[] charCards;
    private JTextField nickField;
    private JLabel statusLabel;

    public NicknameCharacterScreen(UserData user) {
        this.user = user;
        setTitle("캐릭터 생성");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 10)) {
            protected void paintComponent(Graphics g) {
                Theme.paintBg(g, getWidth(), getHeight());
                Theme.paintStars(g, getWidth(), getHeight(), STAR_RNG);
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Top: nickname
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        topPanel.setOpaque(false);
        JLabel nickLabel = new JLabel("🧙 닉네임:");
        nickLabel.setForeground(Theme.GOLD); nickLabel.setFont(Theme.FONT_HEADER);
        nickField = new JTextField(16);
        nickField.setBackground(new Color(40,40,70)); nickField.setForeground(Theme.TEXT_MAIN);
        nickField.setFont(Theme.FONT_HEADER); nickField.setCaretColor(Theme.ACCENT);
        nickField.setBorder(new LineBorder(Theme.ACCENT, 1, true));
        topPanel.add(nickLabel); topPanel.add(nickField);
        panel.add(topPanel, BorderLayout.NORTH);

        // Center: character cards
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        JLabel chooseLabel = new JLabel("✨ 캐릭터를 선택하세요 ✨", SwingConstants.CENTER);
        chooseLabel.setForeground(Theme.ACCENT); chooseLabel.setFont(Theme.FONT_HEADER);
        centerPanel.add(chooseLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        cardsPanel.setOpaque(false);
        CharacterClass[] classes = CharacterClass.values();
        charCards = new JLabel[classes.length];

        for (int i = 0; i < classes.length; i++) {
            final int idx = i;
            CharacterClass cc = classes[i];
            JPanel card = buildCharCard(cc, idx);
            cardsPanel.add(card);
        }
        centerPanel.add(cardsPanel, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Bottom
        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botPanel.setOpaque(false);
        statusLabel = new JLabel("캐릭터를 선택하고 닉네임을 입력하세요.", SwingConstants.CENTER);
        statusLabel.setForeground(Theme.TEXT_DIM); statusLabel.setFont(Theme.FONT_BODY);
        JButton startBtn = new JButton("🎮 게임 시작!");
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        startBtn.setBackground(new Color(80, 40, 140));
        startBtn.setForeground(Theme.GOLD);
        startBtn.setFocusPainted(false); startBtn.setBorderPainted(false);
        startBtn.setPreferredSize(new Dimension(200, 45));
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startBtn.addActionListener(e -> startGame());
        botPanel.add(statusLabel); botPanel.add(startBtn);
        panel.add(botPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildCharCard(CharacterClass cc, int idx) {
        JPanel card = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = (selectedIdx == idx) ? new Color(80, 50, 160) : new Color(30, 28, 60);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                if (selectedIdx == idx) {
                    g2.setColor(Theme.ACCENT);
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 18, 18);
                }
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(new EmptyBorder(10, 8, 10, 8));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.insets = new Insets(4, 0, 4, 0);

        JLabel emoji = new JLabel(cc.emoji, SwingConstants.CENTER);
        emoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        g.gridy = 0; card.add(emoji, g);

        JLabel name = new JLabel(cc.name, SwingConstants.CENTER);
        name.setForeground(Theme.GOLD); name.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.gridy = 1; card.add(name, g);

        // Main stat badge
        JLabel mainStatL = new JLabel("★ 주스탯: " + cc.mainStat, SwingConstants.CENTER);
        mainStatL.setForeground(new Color(255,200,80)); mainStatL.setFont(Theme.FONT_SMALL);
        g.gridy = 2; card.add(mainStatL, g);

        addStat(card, g, 3, "❤️ HP",  cc.baseHp);
        addStat(card, g, 4, "⚔️ ATK", cc.baseAtk);
        addStat(card, g, 5, "🛡️ DEF", cc.baseDef);
        addStat(card, g, 6, "💨 SPD", cc.baseSpd);
        addStat(card, g, 7, "💧 MP",  cc.baseMana);


        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIdx = idx;
                repaint();
                updateStatus();
            }
        });

        // Store ref for repaint
        charCards[idx] = new JLabel(); // placeholder ref
        return card;
    }

    private void addStat(JPanel p, GridBagConstraints g, int row, String label, int val) {
        g.gridy = row;
        JLabel l = new JLabel(label + ": " + val, SwingConstants.CENTER);
        l.setForeground(Theme.TEXT_DIM); l.setFont(Theme.FONT_SMALL);
        p.add(l, g);
    }

    private void updateStatus() {
        if (selectedIdx >= 0) {
            statusLabel.setText("✅ " + CharacterClass.values()[selectedIdx].name + " 선택됨");
            statusLabel.setForeground(Theme.HP_GREEN);
        }
    }

    private void startGame() {
        String nick = nickField.getText().trim();
        if (nick.isEmpty()) { statusLabel.setText("⚠️ 닉네임을 입력하세요!"); statusLabel.setForeground(Color.RED); return; }
        if (nick.length() < 2 || nick.length() > 10) { statusLabel.setText("⚠️ 닉네임은 2~10자!"); statusLabel.setForeground(Color.RED); return; }
        if (selectedIdx < 0) { statusLabel.setText("⚠️ 캐릭터를 선택하세요!"); statusLabel.setForeground(Color.RED); return; }

        user.setNickname(nick);
        user.setSelectedCharacterIndex(selectedIdx);
        CharacterData cd = new CharacterData(CharacterClass.values()[selectedIdx]);
        user.setCharacterData(cd);
        SaveSystem.saveUser(user);

        dispose();
        new GameScreen(user).setVisible(true);
    }
}
