package game.ui;

import game.data.CharacterData;
import game.data.UserData;
import game.inventory.Item;
import game.inventory.ItemDatabase;
import game.system.SaveSystem;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class ShopScreen extends JFrame {
    private static final Random STAR_RNG = new Random(33);
    private UserData user;
    private CharacterData cd;
    private GameScreen hub;
    private JLabel goldLabel;

    public ShopScreen(UserData user, GameScreen hub) {
        this.user = user; this.cd = user.getCharacterData(); this.hub = hub;
        setTitle("🏪 상점");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(860, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { exitShop(); }
        });
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(8, 8)) {
            protected void paintComponent(Graphics g) {
                Theme.paintBg(g, getWidth(), getHeight());
                Theme.paintStars(g, getWidth(), getHeight(), STAR_RNG);
                super.paintComponent(g);
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel title = new JLabel("🏪 희귀 상점", SwingConstants.LEFT);
        title.setForeground(Theme.GOLD); title.setFont(Theme.FONT_TITLE);
        top.add(title, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightTop.setOpaque(false);
        goldLabel = new JLabel("💰 " + cd.getGold() + " G");
        goldLabel.setForeground(Theme.GOLD); goldLabel.setFont(Theme.FONT_HEADER);
        JButton backBtn = new JButton("🏠 마을로");
        Theme.styleButton(backBtn);
        backBtn.addActionListener(e -> exitShop());
        rightTop.add(goldLabel); rightTop.add(backBtn);
        top.add(rightTop, BorderLayout.EAST);
        root.add(top, BorderLayout.NORTH);

        // Subtitle
        JLabel sub = new JLabel("✨ 던전에서는 구할 수 없는 희귀 아이템들을 판매합니다!", SwingConstants.CENTER);
        sub.setForeground(Theme.ACCENT); sub.setFont(Theme.FONT_BODY);
        root.add(sub, BorderLayout.CENTER);

        // Shop items
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(new Color(12, 10, 30));

        List<Item> shopItems = ItemDatabase.getShopItems();
        for (int i = 0; i < shopItems.size(); i++) {
            final Item item = shopItems.get(i);
            JPanel row = buildShopRow(item, i);
            itemsPanel.add(row);
        }

        JScrollPane scroll = new JScrollPane(itemsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(12,10,30));
        root.add(scroll, BorderLayout.SOUTH);

        // Rearrange
        root.setLayout(new BorderLayout(8, 8));
        root.add(top, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(sub, BorderLayout.NORTH);
        centerPanel.add(scroll, BorderLayout.CENTER);
        root.add(centerPanel, BorderLayout.CENTER);

        return root;
    }

    private JPanel buildShopRow(Item item, int rowIdx) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        row.setBackground(rowIdx % 2 == 0 ? new Color(22, 20, 52) : new Color(16, 14, 44));
        row.setBorder(new EmptyBorder(8, 12, 8, 12));

        // Left: emoji + info
        JPanel leftPanel = new JPanel(new BorderLayout(8, 0));
        leftPanel.setOpaque(false);

        JLabel emojiLbl = new JLabel(item.getEmoji());
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        leftPanel.add(emojiLbl, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        String rarityColor = item.getRarityColor();
        JLabel nameLbl = new JLabel("<html><font color='" + rarityColor + "'>[" + item.getRarity() + "]</font> <b>" + item.getName() + "</b></html>");
        nameLbl.setForeground(Theme.TEXT_MAIN); nameLbl.setFont(Theme.FONT_BODY);
        JLabel statsLbl = new JLabel("<html><font color='#aaaaaa'>" + item.getStatsText() + "</font>  <font color='#888888'>최소레벨: " + item.getMinLevel() + "</font></html>");
        statsLbl.setFont(Theme.FONT_SMALL);
        infoPanel.add(nameLbl); infoPanel.add(statsLbl);
        leftPanel.add(infoPanel, BorderLayout.CENTER);
        row.add(leftPanel, BorderLayout.CENTER);

        // Right: price + buy button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);
        JLabel priceLbl = new JLabel("💰 " + item.getGoldValue() + "G");
        priceLbl.setForeground(Theme.GOLD); priceLbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton buyBtn = new JButton("구매");
        buyBtn.setBackground(new Color(80, 50, 0));
        buyBtn.setForeground(Theme.GOLD);
        buyBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        buyBtn.setFocusPainted(false); buyBtn.setBorderPainted(false);
        buyBtn.setPreferredSize(new Dimension(70, 32));
        buyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (cd.getLevel() < item.getMinLevel()) {
            buyBtn.setEnabled(false);
            buyBtn.setToolTipText("레벨 " + item.getMinLevel() + " 이상 필요");
        }

        buyBtn.addActionListener(e -> {
            if (!cd.spendGold(item.getGoldValue())) {
                JOptionPane.showMessageDialog(this, "💰 골드가 부족합니다!\n현재: " + cd.getGold() + "G  필요: " + item.getGoldValue() + "G", "골드 부족", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cd.addItem(item);
            goldLabel.setText("💰 " + cd.getGold() + " G");
            SaveSystem.saveUser(user);
            JOptionPane.showMessageDialog(this, "✅ [" + item.getName() + "] 구매 완료!\n인벤토리에 추가되었습니다.", "구매 완료", JOptionPane.INFORMATION_MESSAGE);
        });

        rightPanel.add(priceLbl); rightPanel.add(buyBtn);
        row.add(rightPanel, BorderLayout.EAST);
        return row;
    }

    private void exitShop() { hub.refreshStats(); hub.setVisible(true); dispose(); }
}
