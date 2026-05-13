package game.ui;

import game.data.CharacterData;
import game.data.UserData;
import game.dungeon.Monster;
import game.dungeon.MonsterFactory;
import game.inventory.Item;
import game.inventory.ItemDatabase;
import game.skill.Skill;
import game.system.SaveSystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class TowerScreen extends JFrame {
    private static final Random RNG = new Random();
    private UserData user;
    private CharacterData cd;
    private GameScreen hub;
    private Monster currentMonster;
    private int currentStage;
    private boolean battleActive = false;

    // Hit effect state
    private long hitEnd = 0;
    private static final int HIT_MS = 380;

    // UI
    private JTextArea battleLog;
    private JLabel lblStage, lblMonsterName, lblMonsterHp, lblPlayerHp, lblPlayerMp;
    private JProgressBar monsterHpBar, playerHpBar, playerMpBar;
    private JPanel skillPanel; // 스킬 버튼들이 담길 패널
    private JButton attackBtn, potionBtn;
    private MonCanvas monCanvas;
    private PlCanvas plCanvas;

    public TowerScreen(UserData user, GameScreen hub) {
        this.user = user; 
        this.cd = user.getCharacterData(); 
        this.hub = hub;
        this.currentStage = cd.getTowerStage();
        
        setTitle("🏯 무한의 탑");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1000, 720); // 스킬창 공간 확보를 위해 높이 약간 조절
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { exitTower(); }
        });
        
        setContentPane(buildContent());
        startStage();
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(6,6));
        root.setBackground(Theme.BG_DARK);
        root.setBorder(new EmptyBorder(8,8,8,8));
        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildActions(), BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildTopBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        p.setBackground(new Color(8,6,22));
        lblStage = lbl("🏯 " + currentStage + "층", Theme.ACCENT, Theme.FONT_TITLE);
        JLabel lvLbl = lbl("Lv." + cd.getLevel(), Theme.GOLD, Theme.FONT_HEADER);
        JLabel goldLbl = lbl("💰 " + cd.getGold() + "G", Theme.GOLD, Theme.FONT_BODY);
        JButton back = mkBtn("🏠 마을로", Theme.BG_BUTTON);
        back.addActionListener(e -> exitTower());
        p.add(lblStage); p.add(lvLbl); p.add(goldLbl); p.add(back);
        return p;
    }

    private JPanel buildCenter() {
        JPanel p = new JPanel(new GridLayout(1,3,8,0));
        p.setOpaque(false);

        // LEFT: monster canvas
        JPanel left = new JPanel(new BorderLayout(0,4));
        left.setOpaque(false);
        monCanvas = new MonCanvas();
        monCanvas.setPreferredSize(new Dimension(280,300));
        lblMonsterName = lbl("", Theme.TEXT_MAIN, Theme.FONT_HEADER);
        lblMonsterHp   = lbl("HP: --", Theme.HP_RED, Theme.FONT_SMALL);
        monsterHpBar   = mkBar(Theme.HP_RED, new Color(55,10,10));
        monsterHpBar.setPreferredSize(new Dimension(240,14));

        JPanel mInfo = darkCard();
        mInfo.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints(); g.gridx=0; g.insets=new Insets(3,4,3,4);
        g.gridy=0; mInfo.add(lblMonsterName,g);
        g.gridy=1; mInfo.add(lblMonsterHp,g);
        g.gridy=2; mInfo.add(monsterHpBar,g);
        left.add(monCanvas, BorderLayout.CENTER); left.add(mInfo, BorderLayout.SOUTH);
        p.add(left);

        // CENTER: log
        JPanel center = new JPanel(new BorderLayout(0,4));
        center.setOpaque(false);
        center.add(lbl("🏯 무한의 탑 ⚔️", Theme.ACCENT, Theme.FONT_HEADER), BorderLayout.NORTH);
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setBackground(new Color(6,4,18));
        battleLog.setForeground(Theme.TEXT_MAIN);
        battleLog.setFont(new Font("Monospaced",Font.PLAIN,13));
        battleLog.setLineWrap(true); battleLog.setWrapStyleWord(true);
        battleLog.setBorder(new EmptyBorder(8,8,8,8));
        JScrollPane sc = new JScrollPane(battleLog);
        sc.setBorder(new LineBorder(new Color(50,30,90),1));
        center.add(sc, BorderLayout.CENTER);
        p.add(center);

        // RIGHT: player canvas
        JPanel right = new JPanel(new BorderLayout(0,4));
        right.setOpaque(false);
        plCanvas = new PlCanvas();
        plCanvas.setPreferredSize(new Dimension(250,300));

        lblPlayerHp = lbl("HP: " + cd.getHp() + "/" + cd.getMaxHp(), Theme.HP_GREEN, Theme.FONT_SMALL);
        playerHpBar = mkBar(Theme.HP_GREEN, new Color(10,48,10));
        playerHpBar.setMaximum(cd.getMaxHp()); playerHpBar.setValue(cd.getHp());
        playerHpBar.setPreferredSize(new Dimension(220,14));
        lblPlayerMp = lbl("MP: " + cd.getMp() + "/" + cd.getMaxMp(), new Color(80,160,255), Theme.FONT_SMALL);
        playerMpBar = mkBar(new Color(80,160,255), new Color(10,20,55));
        playerMpBar.setMaximum(cd.getMaxMp()); playerMpBar.setValue(cd.getMp());
        playerMpBar.setPreferredSize(new Dimension(220,12));

        JPanel pInfo = darkCard();
        pInfo.setLayout(new GridBagLayout());
        GridBagConstraints g2 = new GridBagConstraints(); g2.gridx=0; g2.insets=new Insets(2,4,2,4);
        g2.gridy=0; pInfo.add(lbl("👤 " + user.getNickname(), Theme.GOLD, Theme.FONT_HEADER),g2);
        g2.gridy=1; pInfo.add(lbl("🚫 탑: 경험치 없음", new Color(200,80,80), Theme.FONT_SMALL),g2);
        g2.gridy=2; pInfo.add(lblPlayerHp,g2);
        g2.gridy=3; pInfo.add(playerHpBar,g2);
        g2.gridy=4; pInfo.add(lblPlayerMp,g2);
        g2.gridy=5; pInfo.add(playerMpBar,g2);

        right.add(plCanvas, BorderLayout.CENTER); right.add(pInfo, BorderLayout.SOUTH);
        p.add(right);
        return p;
    }

    private JPanel buildActions() {
        JPanel outer = new JPanel(new BorderLayout(0,3));
        outer.setBackground(new Color(6,4,20));
        outer.setBorder(new EmptyBorder(6,8,6,8));

        // 1. 스킬 버튼이 생성될 패널
        skillPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        skillPanel.setOpaque(false);
        // 초기화 시 버튼 생성 (나중에 startStage에서도 호출됨)
        rebuildSkillButtons();

        // 2. 기본 액션 버튼 패널 (공격, 포션)
        JPanel basicActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        basicActions.setOpaque(false);
        
        attackBtn = mkActionBtn("⚔️  공격", new Color(160, 30, 30));
        potionBtn = mkActionBtn("🧪  포션", new Color(20, 96, 40));
        
        attackBtn.addActionListener(e -> playerAtk(false));
        potionBtn.addActionListener(e -> usePotion());
        
        basicActions.add(attackBtn);
        basicActions.add(potionBtn);

        outer.add(skillPanel, BorderLayout.CENTER);
        outer.add(basicActions, BorderLayout.SOUTH);
        return outer;
    }

    // ── 던전에서 가져온 핵심 스킬 로직 ──
    private void rebuildSkillButtons() {
        if (skillPanel == null) return;
        skillPanel.removeAll();

        Skill[] unlocked = Skill.getUsable(cd.getCharClass(), cd.getLevel(), cd.getSkillLevels());
        for (Skill skill : unlocked) {
            JButton btn = buildSkillButton(skill);
            skillPanel.add(btn);
        }

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
        btn.setEnabled(canUse && battleActive);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("<html><b>" + skill.name + "</b><br>" + skill.desc + "</html>");

        btn.addActionListener(e -> useSkill(skill));
        return btn;
    }

    private void useSkill(Skill skill) {
        if (!battleActive) return;
        if (!cd.spendMana(skill.manaCost)) { log("💧 마나가 부족합니다!"); return; }

        setBtns(false);
        updatePlHp(); // MP 감소 반영

        int baseAtk = cd.getEffectiveAtk();
        int dmg = (int)(baseAtk * skill.getDmgMult(cd.getSkillLevel(skill.id)) * (0.88 + RNG.nextDouble() * 0.24));

        log(skill.emoji + " [" + skill.name + "] 사용!");
        int dealt = currentMonster.takeDamage(dmg);
        log("💥 [" + currentMonster.getName() + "] 에게 " + dealt + " 데미지!");
        
        trigMonHit(); updateMonHp();

        if (skill.healMult > 0) {
            int healAmt = (int)(cd.getMaxHp() * skill.healMult / 100.0);
            cd.heal(healAmt);
            log("💚 +" + healAmt + " HP 회복!");
        }

        if (!currentMonster.isAlive()) {
            Timer t = new Timer(420, ev -> onKilled()); t.setRepeats(false); t.start();
        } else {
            Timer t = new Timer(520, ev -> monAtk()); t.setRepeats(false); t.start();
        }
    }

    private void startStage() {
        currentMonster = MonsterFactory.createTowerMonster(currentStage);
        lblStage.setText("🏯 " + currentStage + "층");
        setTitle("🏯 무한의 탑 - " + currentStage + "층");
        lblMonsterName.setText(currentMonster.getName());
        monCanvas.setMonKey(currentMonster.getImageKey());
        updateMonHp(); updatePlHp();
        battleActive = true; 
        setBtns(true);
        rebuildSkillButtons(); // 층 시작 시 스킬 버튼 새로고침
        log("\n── " + currentStage + "층 돌입 ──");
        log("🔴 " + currentMonster.getName() + " 등장!");
        if (currentMonster.getType() == Monster.MonsterType.TOWER_BOSS) log("👑 ★ 미니보스! ★");
    }

    private void playerAtk(boolean isSkill) {
        // 기존 playerAtk는 평타용으로 유지 (isSkill 파라미터는 이제 사용 안 함)
        if (!battleActive) return; 
        setBtns(false);
        int dmg = (int)(cd.getAtk() * (0.85 + RNG.nextDouble() * 0.3));
        int dealt = currentMonster.takeDamage(dmg);
        log("⚔️ " + currentMonster.getName() + " 에게 💥 " + dealt + " 데미지!");
        trigMonHit(); updateMonHp();
        if (!currentMonster.isAlive()) {
            Timer t=new Timer(420,e->onKilled()); t.setRepeats(false); t.start();
        } else {
            Timer t=new Timer(520,e->monAtk()); t.setRepeats(false); t.start();
        }
    }

    private void monAtk() {
        int raw=currentMonster.attack(), taken=cd.takeDamage(raw);
        log("💢 " + currentMonster.getName() + " 반격! 🩸 " + taken + " 데미지!");
        trigPlHit(); updatePlHp();
        if (!cd.isAlive()) { Timer t=new Timer(420,e->onDied()); t.setRepeats(false); t.start(); }
        else {
            setBtns(true);
            rebuildSkillButtons(); // 플레이어 턴이 올 때 마나 상태에 따라 스킬 버튼 활성화
        }
    }

    private void onKilled() {
        battleActive=false;
        log("✅ " + currentStage + "층 클리어!");
        long g=ItemDatabase.rollGold(cd.getLevel(), currentMonster.getType()==Monster.MonsterType.TOWER_BOSS);
        cd.addGold(g); log("💰 +" + g + "G  [탑에서는 경험치 없음]");
        Item drop=ItemDatabase.rollDrop(cd.getLevel());
        if(drop!=null){ cd.addItem(drop); log("🎁 "+drop.getDisplayName()+" 획득!"); }
        
        if(currentStage >= 100){ 
            log("🏆 전설의 탑 정복!!! 🏆"); 
            setBtns(false); 
            SaveSystem.saveUser(user); 
            return; 
        }
        currentStage++; 
        cd.setTowerStage(currentStage); 
        SaveSystem.saveUser(user);
        Timer t=new Timer(800,e->startStage()); t.setRepeats(false); t.start();
    }

    private void onDied() {
        battleActive=false; setBtns(false);
        log("💀 패배! 최고 기록: " + currentStage + "층");
        cd.fullHeal(); updatePlHp(); SaveSystem.saveUser(user);
        Timer t=new Timer(2500,e->exitTower()); t.setRepeats(false); t.start();
    }

    private void usePotion() {
        Item pot=null;
        for(Item i:cd.getInventory()) if(i.getType()==Item.ItemType.POTION){pot=i;break;}
        if(pot==null){log("🧪 포션 없음!"); return;}
        cd.getInventory().remove(pot); cd.heal(pot.getHealAmount());
        log("🧪 "+pot.getName()+" +"+pot.getHealAmount()+" HP!"); updatePlHp();
        if(battleActive){Timer t=new Timer(300,e->monAtk());t.setRepeats(false);t.start();}
    }

    private void trigMonHit() {
        hitEnd = System.currentTimeMillis()+HIT_MS;
        monCanvas.startFlash(); monCanvas.repaint();
        Timer t=new Timer(50,null);
        t.addActionListener(e->{monCanvas.repaint(); if(System.currentTimeMillis()>hitEnd){monCanvas.stopFlash();t.stop();}});
        t.start();
    }
    private void trigPlHit() {
        hitEnd = System.currentTimeMillis()+HIT_MS;
        plCanvas.startFlash(); plCanvas.repaint();
        Timer t=new Timer(50,null);
        t.addActionListener(e->{plCanvas.repaint(); if(System.currentTimeMillis()>hitEnd){plCanvas.stopFlash();t.stop();}});
        t.start();
    }

    private void updateMonHp() {
        if(currentMonster==null) return;
        monsterHpBar.setMaximum(currentMonster.getMaxHp()); 
        monsterHpBar.setValue(currentMonster.getHp());
        lblMonsterHp.setText("HP: "+currentMonster.getHp()+"/"+currentMonster.getMaxHp());
        monCanvas.repaint();
    }
    private void updatePlHp() {
        playerHpBar.setMaximum(cd.getMaxHp()); 
        playerHpBar.setValue(cd.getHp());
        lblPlayerHp.setText("HP: "+cd.getHp()+"/"+cd.getMaxHp());
        if(playerMpBar!=null && lblPlayerMp!=null){
            playerMpBar.setMaximum(cd.getMaxMp()); 
            playerMpBar.setValue(cd.getMp());
            lblPlayerMp.setText("MP: "+cd.getMp()+"/"+cd.getMaxMp());
        }
    }
    private void setBtns(boolean b) { 
        attackBtn.setEnabled(b); 
        potionBtn.setEnabled(b); 
        if(skillPanel != null) {
            for(Component c : skillPanel.getComponents()) {
                if(c instanceof JButton) c.setEnabled(b);
            }
        }
    }
    private void exitTower() { hub.refreshStats(); hub.setVisible(true); dispose(); }
    private void log(String m) { if(battleLog==null)return; battleLog.append(m+"\n"); battleLog.setCaretPosition(battleLog.getDocument().getLength()); }

    // ── inner canvas classes ──
    class MonCanvas extends JPanel {
        private String key="slime"; private boolean flash=false; private float fa=0;
        MonCanvas(){setOpaque(false);}
        void setMonKey(String k){key=k; repaint();}
        void startFlash(){flash=true;fa=0.8f;}
        void stopFlash(){flash=false;repaint();}
        protected void paintComponent(Graphics g0){
            Graphics2D g=(Graphics2D)g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(),h=getHeight();
            BufferedImage bg=ImageGen.getDungeonBg("castle",w,h);
            g.drawImage(bg,0,0,null);
            int mw=(int)(w*0.72),mh=(int)(h*0.72);
            int mx=(w-mw)/2,my=(h-mh)/2+8;
            BufferedImage mon=ImageGen.getMonster(key,mw,mh);
            g.drawImage(mon,mx,my,null);
            if(flash){
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fa*0.5f));
                g.setColor(new Color(255,50,50)); g.fillOval(mx+mw/4,my+mh/4,mw/2,mh/2);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fa));
                g.drawImage(ImageGen.getHitEffect(false,88,88),mx+mw/2-44,my+mh/2-44,null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                fa=Math.max(0,fa-0.13f);
            }
            if(currentMonster!=null){
                int bw=(int)(w*0.7); int bx=(w-bw)/2; int by=h-22;
                g.setColor(new Color(50,8,8)); g.fillRoundRect(bx,by,bw,12,6,6);
                int fl=(int)(bw*currentMonster.getHpPercent());
                g.setColor(currentMonster.getHpPercent()>0.5?new Color(50,200,60):currentMonster.getHpPercent()>0.25?new Color(230,180,20):new Color(220,40,40));
                g.fillRoundRect(bx,by,fl,12,6,6);
            }
        }
    }

    class PlCanvas extends JPanel {
        private boolean fla=false; private float fa=0;
        PlCanvas(){setOpaque(false);}
        void startFlash(){fla=true;fa=0.8f;}
        void stopFlash(){fla=false;repaint();}
        protected void paintComponent(Graphics g0){
            Graphics2D g=(Graphics2D)g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(),h=getHeight();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.35f));
            g.drawImage(ImageGen.getDungeonBg("castle",w,h),0,0,null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
            int pw=(int)(w*0.68),ph=(int)(h*0.76);
            int px=(w-pw)/2,py=(h-ph)/2+5;
            g.drawImage(ImageGen.getCharacterEquipped(cd.getCharClass().name(),cd.getEquipment(),pw,ph),px,py,null);
            if(fla){
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fa*0.35f));
                g.setColor(new Color(200,0,0)); g.fillRect(0,0,w,h);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fa));
                g.drawImage(ImageGen.getHitEffect(true,75,75),px+pw/2-38,py+ph/2-38,null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                fa=Math.max(0,fa-0.13f);
            }
            g.setColor(new Color(0,0,0,70)); g.fillOval(px+pw/4,py+ph-12,pw/2,12);
        }
    }

    private JLabel lbl(String t,Color c,Font f){JLabel l=new JLabel(t,SwingConstants.CENTER);l.setForeground(c);l.setFont(f);return l;}
    private JPanel darkCard(){JPanel p=new JPanel(){protected void paintComponent(Graphics g){g.setColor(new Color(16,12,40));g.fillRoundRect(0,0,getWidth(),getHeight(),12,12);super.paintComponent(g);}};p.setOpaque(false);return p;}
    private JButton mkBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Theme.TEXT_MAIN);b.setFont(Theme.FONT_SMALL);b.setFocusPainted(false);b.setBorderPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private JButton mkActionBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Color.WHITE);b.setFont(new Font("SansSerif",Font.BOLD,15));b.setFocusPainted(false);b.setBorderPainted(false);b.setPreferredSize(new Dimension(140,46));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private JProgressBar mkBar(Color fg,Color bg){JProgressBar b=new JProgressBar(0,100);b.setValue(100);b.setForeground(fg);b.setBackground(bg);b.setStringPainted(false);b.setBorderPainted(false);return b;}
}