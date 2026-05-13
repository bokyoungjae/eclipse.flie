package game.ui;

import game.data.UserData;
import game.system.SaveSystem;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class StartScreen extends JFrame {
    private static final Random STAR_RNG = new Random(42);
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public StartScreen() {
        setTitle("⚔️ Chronicles of Destiny");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.add(buildMainPanel(), "main");
        cardPanel.add(buildLoginPanel(), "login");
        cardPanel.add(buildRegisterPanel(), "register");

        add(cardPanel);
    }

    // ──────────── MAIN CARD ────────────
    private JPanel buildMainPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(12, 0, 12, 0);

        // Title
        JLabel title = new JLabel("⚔️ Chronicles of Destiny", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(Theme.GOLD);
        gbc.gridy = 0;
        panel.add(title, gbc);

        JLabel sub = new JLabel("~ 운명의 연대기 ~", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.ITALIC, 16));
        sub.setForeground(Theme.TEXT_DIM);
        gbc.gridy = 1;
        panel.add(sub, gbc);

        // Stars deco
        JLabel deco = new JLabel("✨ 🌙 ⭐ 🌟 ✨", SwingConstants.CENTER);
        deco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        gbc.gridy = 2;
        panel.add(deco, gbc);

        // Buttons
        JButton loginBtn = makeBtn("🔑  로그인", Theme.BG_BUTTON);
        JButton registerBtn = makeBtn("📝  회원가입", new Color(40, 70, 50));
        JButton exitBtn = makeBtn("🚪  종료", new Color(80, 20, 20));

        loginBtn.addActionListener(e -> cardLayout.show(cardPanel, "login"));
        registerBtn.addActionListener(e -> cardLayout.show(cardPanel, "register"));
        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridy = 3; panel.add(loginBtn, gbc);
        gbc.gridy = 4; panel.add(registerBtn, gbc);
        gbc.gridy = 5; panel.add(exitBtn, gbc);

        return panel;
    }

    // ──────────── LOGIN CARD ────────────
    private JPanel buildLoginPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(8, 20, 8, 20);

        JLabel title = makeLabel("🔑  로그인", Theme.FONT_TITLE, Theme.GOLD);
        gbc.gridy = 0; panel.add(title, gbc);

        gbc.gridwidth = 1;
        JTextField userField = makeField();
        JPasswordField passField = new JPasswordField(20);
        passField.setBackground(new Color(40,40,70)); passField.setForeground(Theme.TEXT_MAIN);
        passField.setCaretColor(Theme.ACCENT); passField.setFont(Theme.FONT_BODY);
        passField.setBorder(new LineBorder(Theme.ACCENT, 1, true));

        addRow(panel, gbc, 1, "👤  아이디:", userField);
        addRow(panel, gbc, 2, "🔒  비밀번호:", passField);

        JLabel errLabel = new JLabel(" ", SwingConstants.CENTER);
        errLabel.setForeground(Color.RED); errLabel.setFont(Theme.FONT_SMALL);
        gbc.gridy = 3; gbc.gridwidth = 2; panel.add(errLabel, gbc);

        JButton loginBtn = makeBtn("✅  로그인", Theme.BG_SUCCESS);
        JButton backBtn  = makeBtn("◀  뒤로",  Theme.BG_BUTTON);

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());
            UserData ud = SaveSystem.login(user, pass);
            if (ud == null) {
                errLabel.setText("❌ 아이디 또는 비밀번호가 올바르지 않습니다.");
            } else {
                dispose();
                if (ud.isNewPlayer()) {
                    new NicknameCharacterScreen(ud).setVisible(true);
                } else {
                    new GameScreen(ud).setVisible(true);
                }
            }
        });
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "main"));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setOpaque(false);
        btnRow.add(loginBtn); btnRow.add(backBtn);
        gbc.gridy = 4; panel.add(btnRow, gbc);

        return panel;
    }

    // ──────────── REGISTER CARD ────────────
    private JPanel buildRegisterPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(6, 20, 6, 20);

        JLabel title = makeLabel("📝  회원가입", Theme.FONT_TITLE, Theme.GOLD);
        gbc.gridy = 0; panel.add(title, gbc);

        gbc.gridwidth = 1;
        JTextField userField  = makeField();
        JPasswordField passField = makePass();
        JPasswordField pass2Field = makePass();
        JTextField emailField = makeField();

        addRow(panel, gbc, 1, "👤  아이디:", userField);
        addRow(panel, gbc, 2, "🔒  비밀번호:", passField);
        addRow(panel, gbc, 3, "🔒  비번 확인:", pass2Field);
        addRow(panel, gbc, 4, "📧  이메일:", emailField);

        JLabel errLabel = new JLabel(" ", SwingConstants.CENTER);
        errLabel.setForeground(new Color(255, 120, 120)); errLabel.setFont(Theme.FONT_SMALL);
        gbc.gridy = 5; gbc.gridwidth = 2; panel.add(errLabel, gbc);

        JButton regBtn  = makeBtn("✅  가입하기", Theme.BG_SUCCESS);
        JButton backBtn = makeBtn("◀  뒤로", Theme.BG_BUTTON);

        regBtn.addActionListener(e -> {
            String user  = userField.getText().trim();
            String pass  = new String(passField.getPassword());
            String pass2 = new String(pass2Field.getPassword());
            String email = emailField.getText().trim();

            if (user.isEmpty() || pass.isEmpty() || email.isEmpty()) {
                errLabel.setText("⚠️ 모든 항목을 입력해주세요."); return;
            }
            if (user.length() < 3) {
                errLabel.setText("⚠️ 아이디는 3자 이상이어야 합니다."); return;
            }
            if (!pass.equals(pass2)) {
                errLabel.setText("⚠️ 비밀번호가 일치하지 않습니다."); return;
            }
            if (SaveSystem.register(user, pass, email)) {
                JOptionPane.showMessageDialog(this, "🎉 가입 완료! 로그인해주세요.", "성공", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "login");
                userField.setText(""); passField.setText(""); pass2Field.setText(""); emailField.setText("");
                errLabel.setText(" ");
            } else {
                errLabel.setText("❌ 이미 존재하는 아이디입니다.");
            }
        });
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "main"));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setOpaque(false);
        btnRow.add(regBtn); btnRow.add(backBtn);
        gbc.gridy = 6; panel.add(btnRow, gbc);
        return panel;
    }

    // ──────────── helpers ────────────
    private void addRow(JPanel p, GridBagConstraints g, int row, String lbl, JComponent field) {
        g.gridy = row; g.gridx = 0; g.gridwidth = 1; g.anchor = GridBagConstraints.EAST;
        JLabel l = new JLabel(lbl); l.setForeground(Theme.TEXT_DIM); l.setFont(Theme.FONT_BODY);
        p.add(l, g);
        g.gridx = 1; g.anchor = GridBagConstraints.WEST;
        p.add(field, g);
    }

    private JTextField makeField() {
        JTextField f = new JTextField(18);
        f.setBackground(new Color(40,40,70)); f.setForeground(Theme.TEXT_MAIN);
        f.setCaretColor(Theme.ACCENT); f.setFont(Theme.FONT_BODY);
        f.setBorder(new LineBorder(Theme.ACCENT, 1, true));
        return f;
    }
    private JPasswordField makePass() {
        JPasswordField f = new JPasswordField(18);
        f.setBackground(new Color(40,40,70)); f.setForeground(Theme.TEXT_MAIN);
        f.setCaretColor(Theme.ACCENT); f.setFont(Theme.FONT_BODY);
        f.setBorder(new LineBorder(Theme.ACCENT, 1, true));
        return f;
    }

    private JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Theme.TEXT_MAIN);
        b.setFont(new Font("SansSerif", Font.BOLD, 15));
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(200, 42));
        b.setOpaque(true);
        b.addMouseListener(new MouseAdapter() {
            Color orig = bg;
            public void mouseEntered(MouseEvent e) { b.setBackground(orig.brighter()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(orig); }
        });
        return b;
    }

    private JLabel makeLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(font); l.setForeground(color);
        return l;
    }

    // Gradient background panel
    class GradientPanel extends JPanel {
        GradientPanel() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Theme.paintBg(g, getWidth(), getHeight());
            Theme.paintStars(g, getWidth(), getHeight(), STAR_RNG);
            super.paintComponent(g);
        }
    }
}
