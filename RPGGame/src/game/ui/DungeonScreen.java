package game.ui;

import game.data.CharacterData;
import game.data.CharacterData.DeathPenalty;
import game.dungeon.DungeonZone;
import game.dungeon.Monster;
import game.dungeon.MonsterFactory;
import game.inventory.Item;
import game.inventory.ItemDatabase;
import game.skill.Skill;
import game.data.UserData;
import game.system.SaveSystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DungeonScreen extends JFrame {
    private static final Random RNG = new Random();

    private UserData user;
    private CharacterData cd;
    private GameScreen hub;
    private DungeonZone currentZone;
    private Monster currentMonster;
    private int monstersDefeated = 0;
    private boolean battleActive = false;

    // hit effect timing
    private static final int HIT_MS = 380;
    private long hitEnd = 0;

    // ── UI ──
    private JPanel       battlePanel;
    private MonsterCanvas monsterCanvas;
    private PlayerCanvas  playerCanvas;
    private JTextArea    battleLog;
    private JProgressBar monsterHpBar, playerHpBar, playerMpBar, playerExpBar;
    private JLabel       lblMonsterName, lblMonsterHp;
    private JLabel       lblPlayerHp, lblPlayerMp, lblPlayerLevel;
    private JLabel       lblGold, lblKills, lblPenalty;
    private JPanel       skillPanel;  // dynamic skill buttons
    private JButton      potionBtn, fleeBtn;

    private enum Screen { ZONE_SELECT, BATTLE }
    private Screen screen = Screen.ZONE_SELECT;

    public DungeonScreen(UserData user, GameScreen hub) {
        this.user = user;
        this.cd   = user.getCharacterData();
        this.hub  = hub;
        setTitle("⚔️ 던전");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1050, 760);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { exitDungeon(); }
        });
        showZoneSelect();
    }

    // ═══════════════════════════════════════════════
    //  ZONE SELECT
    // ═══════════════════════════════════════════════
    private void showZoneSelect() {
        screen = Screen.ZONE_SELECT;
        JPanel panel = new JPanel(new BorderLayout(10,10)) {
            protected void paintComponent(Graphics g) {
                Theme.paintBg(g, getWidth(), getHeight());
                Theme.paintStars(g, getWidth(), getHeight(), new Random(12));
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18,18,18,18));

        // Top bar
        JPanel top = new JPanel(new BorderLayout()); top.setOpaque(false);
        JLabel title = new JLabel("⚔️  던전 선택", SwingConstants.LEFT);
        title.setForeground(Theme.GOLD); title.setFont(Theme.FONT_TITLE);
        JLabel info = new JLabel("Lv." + cd.getLevel() + "  💰" + cd.getGold() + "G", SwingConstants.RIGHT);
        info.setForeground(Theme.TEXT_MAIN); info.setFont(Theme.FONT_HEADER);
        JButton back = mkBtn("🏠 마을로", Theme.BG_BUTTON);
        back.addActionListener(e -> exitDungeon());
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); tr.setOpaque(false);
        tr.add(info); tr.add(back);
        top.add(title,BorderLayout.WEST); top.add(tr,BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        JLabel sub = new JLabel("레벨에 맞는 던전을 선택하세요 — 고레벨 던전일수록 EXP·드롭 보너스 증가!", SwingConstants.CENTER);
        sub.setForeground(Theme.ACCENT); sub.setFont(Theme.FONT_BODY);

        JPanel grid = new JPanel(new GridLayout(2,4,14,14)); grid.setOpaque(false);
        for (DungeonZone zone : DungeonZone.ALL) grid.add(buildZoneCard(zone));

        JPanel center = new JPanel(new BorderLayout(0,10)); center.setOpaque(false);
        center.add(sub, BorderLayout.NORTH);
        // Wrap in scroll pane in case cards overflow
        JScrollPane gridScroll = new JScrollPane(grid);
        gridScroll.setBorder(null); gridScroll.setOpaque(false);
        gridScroll.getViewport().setOpaque(false);
        gridScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        center.add(gridScroll, BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);

        setContentPane(panel); revalidate(); repaint();
    }

    private JPanel buildZoneCard(DungeonZone zone) {
        boolean avail = zone.isAvailable(cd.getLevel());
        Color accent = avail ? zone.accentColor : new Color(40,40,60);

        // Use a layered approach: outer JPanel holds the painted bg + inner content
        JPanel card = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw gradient background
                GradientPaint gp = new GradientPaint(0,0,accent.darker().darker(),getWidth(),getHeight(),accent.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                // Dim overlay for locked
                if (!avail) { g2.setColor(new Color(0,0,0,160)); g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16); }
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(220, 140));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.insets = new Insets(4,6,4,6);

        if (avail) {
            // Name label
            JLabel nameL = new JLabel(zone.name, SwingConstants.CENTER);
            nameL.setForeground(new Color(255,220,80)); nameL.setFont(new Font("SansSerif",Font.BOLD,15));
            gc.gridy = 0; card.add(nameL, gc);
            // Divider
            JSeparator sep = new JSeparator(); sep.setForeground(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),150));
            sep.setPreferredSize(new Dimension(180,1));
            gc.gridy = 1; card.add(sep, gc);
            // Desc
            JLabel d1 = new JLabel(zone.desc1, SwingConstants.CENTER);
            d1.setForeground(new Color(200,190,230)); d1.setFont(Theme.FONT_SMALL);
            gc.gridy = 2; card.add(d1, gc);
            JLabel d2 = new JLabel(zone.desc2, SwingConstants.CENTER);
            d2.setForeground(new Color(160,150,195)); d2.setFont(Theme.FONT_SMALL);
            gc.gridy = 3; card.add(d2, gc);
            // EXP/drop multiplier badge
            JLabel badge = new JLabel(String.format("EXP ×%.1f  Drop ×%.1f", zone.expMult, zone.dropMult), SwingConstants.CENTER);
            badge.setForeground(new Color(100,220,120)); badge.setFont(Theme.FONT_SMALL);
            gc.gridy = 4; card.add(badge, gc);
            // ENTER BUTTON — this is the key fix: use a real JButton
            JButton enterBtn = new JButton("▶  입장하기");
            enterBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
            enterBtn.setBackground(accent.darker());
            enterBtn.setForeground(Color.WHITE);
            enterBtn.setFocusPainted(false); enterBtn.setBorderPainted(false);
            enterBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            enterBtn.setPreferredSize(new Dimension(150, 30));
            enterBtn.addActionListener(ev -> enterZone(zone));
            // Also allow clicking anywhere on card
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e){ enterZone(zone); }
                public void mouseEntered(MouseEvent e){ card.setBorder(new LineBorder(accent.brighter(), 2, true)); }
                public void mouseExited(MouseEvent e) { card.setBorder(new EmptyBorder(0,0,0,0)); }
            });
            gc.gridy = 5; card.add(enterBtn, gc);
        } else {
            JLabel nameL = new JLabel(zone.name, SwingConstants.CENTER);
            nameL.setForeground(new Color(120,115,145)); nameL.setFont(new Font("SansSerif",Font.BOLD,14));
            gc.gridy = 0; card.add(nameL, gc);
            JLabel d1 = new JLabel(zone.desc1, SwingConstants.CENTER);
            d1.setForeground(new Color(100,95,120)); d1.setFont(Theme.FONT_SMALL);
            gc.gridy = 1; card.add(d1, gc);
            JLabel d2 = new JLabel(zone.desc2, SwingConstants.CENTER);
            d2.setForeground(new Color(100,95,120)); d2.setFont(Theme.FONT_SMALL);
            gc.gridy = 2; card.add(d2, gc);
            JLabel lockL = new JLabel("🔒 Lv." + zone.minLevel + " 필요", SwingConstants.CENTER);
            lockL.setForeground(new Color(180,160,200)); lockL.setFont(Theme.FONT_BODY);
            gc.gridy = 3; card.add(lockL, gc);
        }
        return card;
    }

    private void enterZone(DungeonZone zone) {
        currentZone = zone; monstersDefeated = 0;
        screen = Screen.BATTLE;
        buildBattleScreen();
        spawnMonster();
    }

    // ═══════════════════════════════════════════════
    //  BATTLE SCREEN BUILD
    // ═══════════════════════════════════════════════
    private void buildBattleScreen() {
        battlePanel = new JPanel(new BorderLayout(5,5));
        battlePanel.setBackground(Theme.BG_DARK);
        battlePanel.setBorder(new EmptyBorder(7,7,7,7));

        battlePanel.add(buildTopBar(),    BorderLayout.NORTH);
        battlePanel.add(buildCenter(),    BorderLayout.CENTER);
        battlePanel.add(buildSkillPanel(),BorderLayout.SOUTH);

        setContentPane(battlePanel); revalidate(); repaint();
    }

    private JPanel buildTopBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,14,4));
        p.setBackground(new Color(8,6,22));

        JLabel zoneLbl = lbl(currentZone.name, currentZone.accentColor, Theme.FONT_HEADER);
        lblPlayerLevel = lbl("Lv."+cd.getLevel(), Theme.GOLD, Theme.FONT_HEADER);
        lblGold  = lbl("💰 "+cd.getGold()+"G", Theme.GOLD, Theme.FONT_BODY);
        lblKills = lbl("⚔️ "+monstersDefeated, Theme.TEXT_DIM, Theme.FONT_BODY);
        lblPenalty = lbl("", new Color(255,80,80), Theme.FONT_SMALL);
        updatePenaltyLabel();

        JButton changeBtn = mkBtn("🗺 던전 변경", new Color(38,38,75));
        changeBtn.addActionListener(e -> { battleActive=false; showZoneSelect(); });
        JButton backBtn = mkBtn("🏠 마을로", Theme.BG_BUTTON);
        backBtn.addActionListener(e -> exitDungeon());

        p.add(zoneLbl); p.add(lblPlayerLevel); p.add(lblGold); p.add(lblKills); p.add(lblPenalty);
        p.add(changeBtn); p.add(backBtn);
        return p;
    }

    private JPanel buildCenter() {
        JPanel panel = new JPanel(new GridLayout(1,3,7,0));
        panel.setOpaque(false);

        // LEFT: monster
        JPanel left = new JPanel(new BorderLayout(0,4)); left.setOpaque(false);
        monsterCanvas = new MonsterCanvas();
        monsterCanvas.setPreferredSize(new Dimension(265,285));
        lblMonsterName = lbl("",Theme.TEXT_MAIN,Theme.FONT_HEADER);
        lblMonsterHp   = lbl("HP: --",Theme.HP_RED,Theme.FONT_SMALL);
        monsterHpBar   = mkBar(Theme.HP_RED,new Color(55,10,10));
        monsterHpBar.setPreferredSize(new Dimension(240,14));
        JPanel mInfo = darkCard(); mInfo.setLayout(new GridBagLayout());
        GridBagConstraints g0=new GridBagConstraints(); g0.gridx=0; g0.insets=new Insets(3,4,3,4);
        g0.gridy=0; mInfo.add(lblMonsterName,g0);
        g0.gridy=1; mInfo.add(lblMonsterHp,g0);
        g0.gridy=2; mInfo.add(monsterHpBar,g0);
        left.add(monsterCanvas,BorderLayout.CENTER); left.add(mInfo,BorderLayout.SOUTH);
        panel.add(left);

        // CENTER: log
        JPanel center = new JPanel(new BorderLayout(0,5)); center.setOpaque(false);
        center.add(
        	    lbl("⚔️ 전투 로그 ⚔️", Theme.ACCENT, Theme.FONT_HEADER),
        	    BorderLayout.NORTH
        	);
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setBackground(new Color(6,4,18));
        battleLog.setForeground(Theme.TEXT_MAIN);
        battleLog.setFont(new Font("Monospaced",Font.PLAIN,12));
        battleLog.setLineWrap(true); battleLog.setWrapStyleWord(true);
        battleLog.setBorder(new EmptyBorder(7,7,7,7));
        JScrollPane sc = new JScrollPane(battleLog);
        sc.setBorder(new LineBorder(new Color(45,30,80),1));
        center.add(sc,BorderLayout.CENTER);
        panel.add(center);

        // RIGHT: player
        JPanel right = new JPanel(new BorderLayout(0,4)); right.setOpaque(false);
        playerCanvas = new PlayerCanvas();
        playerCanvas.setPreferredSize(new Dimension(235,285));

        lblPlayerHp = lbl("HP: "+cd.getHp()+"/"+cd.getMaxHp(), Theme.HP_GREEN, Theme.FONT_SMALL);
        lblPlayerMp = lbl("MP: "+cd.getMp()+"/"+cd.getMaxMp(), new Color(80,160,255), Theme.FONT_SMALL);

        playerHpBar = mkBar(Theme.HP_GREEN, new Color(10,48,10));
        playerHpBar.setMaximum(cd.getMaxHp()); playerHpBar.setValue(cd.getHp());
        playerHpBar.setPreferredSize(new Dimension(215,13));

        playerMpBar = mkBar(new Color(80,160,255),new Color(10,20,60));
        playerMpBar.setMaximum(cd.getMaxMp()); playerMpBar.setValue(cd.getMp());
        playerMpBar.setPreferredSize(new Dimension(215,13));

        playerExpBar = mkBar(Theme.EXP_BLUE, new Color(5,10,40));
        playerExpBar.setMaximum(100); playerExpBar.setValue((int)(cd.getExpPercent()*100));
        playerExpBar.setPreferredSize(new Dimension(215,10));

        JPanel pInfo = darkCard(); pInfo.setLayout(new GridBagLayout());
        GridBagConstraints g2=new GridBagConstraints(); g2.gridx=0; g2.insets=new Insets(2,4,2,4);
        g2.gridy=0; pInfo.add(lbl("👤 "+user.getNickname(),Theme.GOLD,Theme.FONT_HEADER),g2);
        g2.gridy=1; pInfo.add(lblPlayerHp,g2);
        g2.gridy=2; pInfo.add(playerHpBar,g2);
        g2.gridy=3; pInfo.add(lblPlayerMp,g2);
        g2.gridy=4; pInfo.add(playerMpBar,g2);
        g2.gridy=5; pInfo.add(lbl("✨ EXP",Theme.EXP_BLUE,Theme.FONT_SMALL),g2);
        g2.gridy=6; pInfo.add(playerExpBar,g2);

        right.add(playerCanvas,BorderLayout.CENTER); right.add(pInfo,BorderLayout.SOUTH);
        panel.add(right);
        return panel;
    }

    // ── dynamic skill button panel ──
    private JPanel buildSkillPanel() {
        JPanel outer = new JPanel(new BorderLayout(0,3));
        outer.setBackground(new Color(6,4,20));
        outer.setBorder(new EmptyBorder(6,8,6,8));

        skillPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,2));
        skillPanel.setOpaque(false);
        rebuildSkillButtons();

        // Utility buttons
        JPanel util = new JPanel(new FlowLayout(FlowLayout.CENTER,10,2));
        util.setOpaque(false);
        potionBtn = mkActionBtn("🧪  포션", new Color(22,95,40));
        fleeBtn   = mkActionBtn("🏃  도망", new Color(75,65,22));
        potionBtn.addActionListener(e -> usePotion());
        fleeBtn.addActionListener(e -> { log("🏃 도망쳤습니다!"); Timer t=new Timer(350,ev->spawnMonster());t.setRepeats(false);t.start(); });
        util.add(potionBtn); util.add(fleeBtn);

        outer.add(skillPanel, BorderLayout.CENTER);
        outer.add(util, BorderLayout.SOUTH);
        return outer;
    }

    private void rebuildSkillButtons() {
        if (skillPanel == null) return;
        skillPanel.removeAll();

        Skill[] unlocked = Skill.getUsable(cd.getCharClass(), cd.getLevel(), cd.getSkillLevels());
        for (Skill skill : unlocked) {
            JButton btn = buildSkillButton(skill);
            skillPanel.add(btn);
        }

        // Show next unlock hint
        Skill next = Skill.getNextUnlock(cd.getCharClass(), cd.getLevel(), cd.getSkillLevels());
        if (next != null) {
            JLabel hint = new JLabel("  🔒 Lv." + next.unlockLevel + " → " + next.name);
            hint.setForeground(new Color(120,110,160)); hint.setFont(Theme.FONT_SMALL);
            skillPanel.add(hint);
        }
        skillPanel.revalidate(); skillPanel.repaint();
    }

    private JButton buildSkillButton(Skill skill) {
        boolean canUse = cd.getMp() >= skill.manaCost;
        String costStr = skill.manaCost > 0 ? " [MP-" + skill.manaCost + "]" : " [무료]";
        String label = skill.emoji + "  " + skill.name + costStr;

        JButton btn = new JButton(label) {
            protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D)g0;
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = isEnabled() ? skill.color : skill.color.darker().darker();
                GradientPaint gp = new GradientPaint(0,0,base.brighter(),0,getHeight(),base);
                g.setPaint(gp); g.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g.setColor(base.brighter()); g.setStroke(new BasicStroke(1.5f));
                g.drawRoundRect(1,1,getWidth()-3,getHeight()-3,10,10);
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif",Font.BOLD,13));
        btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(160,46));
        btn.setEnabled(canUse && (skill.unlockLevel == 1 || battleActive));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("<html><b>" + skill.name + "</b><br>" + skill.desc + "<br>MP비용: " + skill.manaCost + "</html>");

        if (skill.unlockLevel == 60) {
            // Ultimate – extra glow label
            btn.setPreferredSize(new Dimension(190,50));
        }

        btn.addActionListener(e -> useSkill(skill));
        return btn;
    }

    // ═══════════════════════════════════════════════
    //  BATTLE LOGIC
    // ═══════════════════════════════════════════════
    private void spawnMonster() {
        battleActive = false; setUtilEnabled(false);
        currentMonster = MonsterFactory.createZoneMonster(currentZone, cd.getLevel());
        lblMonsterName.setText(currentMonster.getName());
        monsterCanvas.setMonster(currentMonster.getImageKey(), currentZone.bgType);
        updateMonHp(); updatePlayerBars();
        battleActive = true; setUtilEnabled(true);
        rebuildSkillButtons();
        log("\n🔴 [" + currentMonster.getName() + "] 등장!");
        if (currentMonster.getType() == Monster.MonsterType.ELITE) log("⭐ 강화 몬스터! 보상 ×2.2!");
        if (cd.hasPenalty()) log("⚠️ 패널티 중 (" + cd.getPenaltyTurns() + "마리 남음) – ATK/DEF 30% 감소");
    }

    private void useSkill(Skill skill) {
        if (!battleActive) return;
        if (!cd.spendMana(skill.manaCost)) { log("💧 마나가 부족합니다! (" + skill.manaCost + " 필요)"); return; }

        setUtilEnabled(false);
        updatePlayerBars();

        // Calculate damage
        int baseAtk = cd.getEffectiveAtk();
        int dmg = 0;
        String msg = "";

        if (skill.dmgMult > 0) {
            dmg = (int)(baseAtk * skill.getDmgMult(cd.getSkillLevel(skill.id)) * (0.88 + RNG.nextDouble() * 0.24));
        }

        if (skill.unlockLevel == 60) {
            log("🌟 ═══ 궁극기 발동! ═══ 🌟");
        }
        log(skill.emoji + " [" + skill.name + "] 사용!");

        if (dmg > 0) {
            int dealt = currentMonster.takeDamage(dmg);
            log("💥 [" + currentMonster.getName() + "] 에게 " + dealt + " 데미지!");
            trigMonsterHit();
            updateMonHp();
        }

        // Heal component
        if (skill.healMult > 0) {
            int healAmt = (int)(cd.getMaxHp() * skill.healMult / 100.0);
            cd.heal(healAmt);
            log("💚 +" + healAmt + " HP 회복!");
            playerCanvas.triggerHeal();
        }

        if (!currentMonster.isAlive()) {
            Timer t = new Timer(420, ev -> onMonsterKilled()); t.setRepeats(false); t.start();
        } else {
            Timer t = new Timer(520, ev -> monsterCounterAttack()); t.setRepeats(false); t.start();
        }
    }

    private void monsterCounterAttack() {
        int rawDmg = currentMonster.attack();
        int taken  = cd.takeDamage(rawDmg);
        log("💢 [" + currentMonster.getName() + "] 반격! 🩸 " + taken + " 데미지!");
        trigPlayerHit(); updatePlayerBars();
        if (!cd.isAlive()) {
            Timer t = new Timer(420, ev -> onPlayerDied()); t.setRepeats(false); t.start();
        } else {
            setUtilEnabled(true); rebuildSkillButtons();
        }
    }

    private void onMonsterKilled() {
        battleActive = false;
        monstersDefeated++;
        lblKills.setText("⚔️ " + monstersDefeated);
        log("✅ [" + currentMonster.getName() + "] 처치!");

        // Tick penalty
        if (cd.hasPenalty()) {
            cd.tickPenalty();
            if (!cd.hasPenalty()) log("✨ 패널티가 해제되었습니다!");
            else log("⚠️ 패널티 남음: " + cd.getPenaltyTurns() + "마리");
            updatePenaltyLabel();
        }

        // Gold
        long gold = (long)(ItemDatabase.rollGold(cd.getLevel(), false) * currentZone.goldMult);
        cd.addGold(gold);
        log("💰 +" + gold + "G 획득!");
        lblGold.setText("💰 " + cd.getGold() + "G");

        // EXP + level up
        boolean leveled = cd.addExp(currentMonster.getExpReward());
        if (leveled) {
            log("🎉 ★ 레벨 업! Lv." + cd.getLevel() + " ★");
            log("   HP & MP 완전 회복!");
            lblPlayerLevel.setText("Lv." + cd.getLevel());
            playerCanvas.triggerLevelUp();
            rebuildSkillButtons(); // unlock new skills
        }

        // Drop
        if (RNG.nextDouble() < currentZone.dropMult * 0.65) {
            Item drop = ItemDatabase.rollDrop(cd.getLevel());
            if (drop != null) { cd.addItem(drop); log("🎁 [" + drop.getDisplayName() + "] 획득!"); }
        }

        // MP passive regen on kill
        cd.restoreMp(8);
        updatePlayerBars();
        SaveSystem.saveUser(user);

        Timer t = new Timer(700, ev -> spawnMonster()); t.setRepeats(false); t.start();
    }

    private void onPlayerDied() {
        battleActive = false; setUtilEnabled(false);
        log("━━━━━━━━━━━━━━━━━━━━━━━━");
        log("💀 전투에서 패배했습니다!");
        log("━━━━━━━━━━━━━━━━━━━━━━━━");

        // Apply penalty
        DeathPenalty penalty = cd.applyDeathPenalty();
        log("🔴 [사망 패널티 적용]");
        log("   💰 골드 -" + penalty.goldLost + "G (10%)");
        log("   ✨ 경험치 -" + penalty.expLost + " (5%)");
        log("   ⚠️ 다음 5마리: ATK/DEF 30% 감소");
        log("   ❤️ HP/MP 완전 회복 후 귀환");
        updatePlayerBars(); updatePenaltyLabel();
        SaveSystem.saveUser(user);

        Timer t = new Timer(3500, ev -> exitDungeon()); t.setRepeats(false); t.start();
    }

    private void usePotion() {
        Item pot = null;
        for (Item i : cd.getInventory()) { if (i.getType()==Item.ItemType.POTION){pot=i;break;} }
        if (pot==null) { log("🧪 사용할 포션이 없습니다!"); return; }
        cd.getInventory().remove(pot);
        cd.heal(pot.getHealAmount());
        log("🧪 [" + pot.getName() + "] 사용! +" + pot.getHealAmount() + " HP!");
        updatePlayerBars(); playerCanvas.triggerHeal();
        if (battleActive) {
            Timer t=new Timer(300,ev->monsterCounterAttack()); t.setRepeats(false); t.start();
        }
    }

    // ═══════════════════════════════════════════════
    //  HIT EFFECTS
    // ═══════════════════════════════════════════════
    private void trigMonsterHit() {
        hitEnd = System.currentTimeMillis()+HIT_MS;
        monsterCanvas.startHitFlash();
        Timer t=new Timer(50,null);
        t.addActionListener(ev->{monsterCanvas.repaint();if(System.currentTimeMillis()>hitEnd){monsterCanvas.stopHitFlash();t.stop();}});
        t.start();
    }
    private void trigPlayerHit() {
        hitEnd = System.currentTimeMillis()+HIT_MS;
        playerCanvas.startHitFlash();
        Timer t=new Timer(50,null);
        t.addActionListener(ev->{playerCanvas.repaint();if(System.currentTimeMillis()>hitEnd){playerCanvas.stopHitFlash();t.stop();}});
        t.start();
    }

    // ═══════════════════════════════════════════════
    //  CANVAS COMPONENTS
    // ═══════════════════════════════════════════════
    class MonsterCanvas extends JPanel {
        private String monKey="slime", bgType="cave";
        private boolean flash=false; private float fa=0;
        MonsterCanvas(){setOpaque(false);}
        void setMonster(String k,String bg){monKey=k;bgType=bg;repaint();}
        void startHitFlash(){flash=true;fa=0.8f;}
        void stopHitFlash(){flash=false;repaint();}
        protected void paintComponent(Graphics g0){
            Graphics2D g=(Graphics2D)g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(),h=getHeight();
            g.drawImage(ImageGen.getDungeonBg(bgType,w,h),0,0,null);
            int mw=(int)(w*.72),mh=(int)(h*.72),mx=(w-mw)/2,my=(h-mh)/2+8;
            g.drawImage(ImageGen.getMonster(monKey,mw,mh),mx,my,null);
            if(flash){
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fa*.5f));
                g.setColor(new Color(255,50,50));g.fillOval(mx+mw/4,my+mh/4,mw/2,mh/2);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fa));
                g.drawImage(ImageGen.getHitEffect(false,85,85),mx+mw/2-42,my+mh/2-42,null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                fa=Math.max(0,fa-.13f);
            }
            if(currentMonster!=null){
                int bw=(int)(w*.7),bx=(w-bw)/2,by=h-20;
                g.setColor(new Color(50,8,8));g.fillRoundRect(bx,by,bw,12,6,6);
                double hp=currentMonster.getHpPercent();
                g.setColor(hp>.5?new Color(50,200,60):hp>.25?new Color(230,180,20):new Color(220,40,40));
                g.fillRoundRect(bx,by,(int)(bw*hp),12,6,6);
            }
        }
    }

    class PlayerCanvas extends JPanel {
        private boolean hitF=false,healF=false,lvF=false;
        private float hitAlpha=0,healAlpha=0; private int lvFrames=0;
        PlayerCanvas(){setOpaque(false);}
        void startHitFlash(){hitF=true;hitAlpha=0.85f;}
        void stopHitFlash(){hitF=false;repaint();}
        void triggerHeal(){healF=true;healAlpha=0.7f;Timer t=new Timer(60,null);t.addActionListener(e->{healAlpha-=0.08f;repaint();if(healAlpha<=0){healF=false;t.stop();}});t.start();}
        void triggerLevelUp(){lvF=true;lvFrames=22;Timer t=new Timer(75,null);t.addActionListener(e->{lvFrames--;repaint();if(lvFrames<=0){lvF=false;t.stop();}});t.start();}
        protected void paintComponent(Graphics g0){
            Graphics2D g=(Graphics2D)g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(),h=getHeight();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.35f));
            g.drawImage(currentZone!=null?ImageGen.getDungeonBg(currentZone.bgType,w,h):ImageGen.getDungeonBg("cave",w,h),0,0,null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
            int pw=(int)(w*.68),ph=(int)(h*.80),px=(w-pw)/2,py=(h-ph)/2+2;
            g.drawImage(ImageGen.getCharacterEquipped(cd.getCharClass().name(),cd.getEquipment(),pw,ph),px,py,null);
            if(hitF){
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,hitAlpha*.32f));
                g.setColor(new Color(200,0,0));g.fillRect(0,0,w,h);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,hitAlpha));
                g.drawImage(ImageGen.getHitEffect(true,78,78),px+pw/2-39,py+ph/2-39,null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                hitAlpha=Math.max(0,hitAlpha-.13f);
            }
            if(healF){
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,healAlpha*.4f));
                g.setColor(new Color(50,220,80));g.fillRect(0,0,w,h);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                g.setColor(new Color(100,255,120,200));g.setFont(new Font("SansSerif",Font.BOLD,22));
                g.drawString("HEAL!",w/2-30,h/2-10);
            }
            if(lvF){
                float a=(float)lvFrames/22f*.45f;
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,a));
                g.setColor(new Color(255,230,80));g.fillRect(0,0,w,h);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                g.setColor(new Color(255,240,100));g.setFont(new Font("SansSerif",Font.BOLD,26));
                g.drawString("LEVEL UP!",w/2-65,h/2-5);
                g.setFont(new Font("SansSerif",Font.BOLD,15));
                g.setColor(new Color(200,255,200));g.drawString("HP & MP 완전 회복!",w/2-72,h/2+18);
            }
            g.setColor(new Color(0,0,0,65));g.fillOval(px+pw/4,py+ph-12,pw/2,12);
        }
    }

    // ═══════════════════════════════════════════════
    //  UI HELPERS
    // ═══════════════════════════════════════════════
    private void updateMonHp() {
        if(currentMonster==null)return;
        monsterHpBar.setMaximum(currentMonster.getMaxHp());
        monsterHpBar.setValue(currentMonster.getHp());
        lblMonsterHp.setText("HP: "+currentMonster.getHp()+" / "+currentMonster.getMaxHp());
        monsterCanvas.repaint();
    }

    private void updatePlayerBars() {
        playerHpBar.setMaximum(cd.getMaxHp()); playerHpBar.setValue(cd.getHp());
        lblPlayerHp.setText("HP: "+cd.getHp()+" / "+cd.getMaxHp());
        playerMpBar.setMaximum(cd.getMaxMp()); playerMpBar.setValue(cd.getMp());
        lblPlayerMp.setText("MP: "+cd.getMp()+" / "+cd.getMaxMp());
        playerExpBar.setValue((int)(cd.getExpPercent()*100));
    }

    private void updatePenaltyLabel() {
        if(lblPenalty==null)return;
        if(cd.hasPenalty()) {
            lblPenalty.setText("☠️ 패널티: "+cd.getPenaltyTurns()+"마리 남음");
        } else {
            lblPenalty.setText("");
        }
    }

    private void setUtilEnabled(boolean b) {
        if(potionBtn!=null) potionBtn.setEnabled(b);
        if(fleeBtn!=null) fleeBtn.setEnabled(b);
    }

    private void exitDungeon() {
        SaveSystem.saveUser(user); hub.refreshStats(); hub.setVisible(true); dispose();
    }

    private void log(String m) {
        if(battleLog==null)return;
        battleLog.append(m+"\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    private JLabel lbl(String t,Color c,Font f){JLabel l=new JLabel(t,SwingConstants.CENTER);l.setForeground(c);l.setFont(f);return l;}
    private JPanel darkCard(){JPanel p=new JPanel(){protected void paintComponent(Graphics g){g.setColor(new Color(16,12,40));g.fillRoundRect(0,0,getWidth(),getHeight(),12,12);super.paintComponent(g);}};p.setOpaque(false);return p;}
    private JButton mkBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Theme.TEXT_MAIN);b.setFont(Theme.FONT_SMALL);b.setFocusPainted(false);b.setBorderPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private JButton mkActionBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Color.WHITE);b.setFont(new Font("SansSerif",Font.BOLD,14));b.setFocusPainted(false);b.setBorderPainted(false);b.setPreferredSize(new Dimension(130,44));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}

//JProgressBar를 일관된 스타일로 생성해주는 헬퍼 메서드입니다.
private JProgressBar mkBar(Color foreground, Color background) {
 JProgressBar bar = new JProgressBar();
 bar.setForeground(foreground);        // 바의 채워지는 색상
 bar.setBackground(background);        // 바의 배경 색상
 bar.setBorderPainted(false);          // 테두리 제거 (깔끔하게)
 bar.setStringPainted(false);          // 퍼센트 텍스트 숨김 (라벨로 따로 처리하니까요)
 bar.setOpaque(true);                  // 배경색 보이게 설정
 return bar;
}
}