package game.ui;
import game.data.CharacterData;
import game.data.UserData;
import game.skill.Skill;
import game.system.SaveSystem;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GameScreen extends JFrame {
    private static final Random STAR_RNG=new Random(77);
    private UserData user; private CharacterData cd;
    private JLabel lblLevel,lblGold,lblNick,lblHpNum,lblMpNum,lblHpStat,lblDef,lblSpd,lblAtk;
    private JLabel lblPoints,lblSkillPoints,lblStrVal,lblDexVal,lblIntVal,lblLukVal;
    private JProgressBar hpBar,mpBar,expBar;
    private JPanel characterVisualPanel;

    public GameScreen(UserData user){
        this.user=user;this.cd=user.getCharacterData();
        setTitle("Chronicles of Destiny");setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150,680);setLocationRelativeTo(null);setResizable(false);
        setContentPane(buildContent());
        addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){SaveSystem.saveUser(user);}});
    }

    private JPanel buildContent(){
        JPanel root=new JPanel(new BorderLayout()){protected void paintComponent(Graphics g){Theme.paintBg(g,getWidth(),getHeight());Theme.paintStars(g,getWidth(),getHeight(),STAR_RNG);super.paintComponent(g);}};
        root.setOpaque(false);
        root.add(buildTopBar(),BorderLayout.NORTH);
        JPanel center=new JPanel(new BorderLayout(10,0));center.setOpaque(false);center.setBorder(new EmptyBorder(8,10,8,10));
        center.add(buildLeftPanel(),BorderLayout.WEST);
        center.add(buildRightSide(),BorderLayout.CENTER);
        root.add(center,BorderLayout.CENTER);
        return root;
    }

    private JPanel buildTopBar(){
        JPanel bar=new JPanel(new BorderLayout());bar.setBackground(new Color(8,6,20));bar.setBorder(new EmptyBorder(7,14,7,14));
        lblNick=new JLabel("👤 "+user.getNickname()+"  "+cd.getCharClass().emoji+" "+cd.getCharClass().name);
        lblNick.setForeground(Theme.GOLD);lblNick.setFont(Theme.FONT_HEADER);bar.add(lblNick,BorderLayout.WEST);
        JPanel right=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));right.setOpaque(false);
        lblGold=new JLabel("💰 "+cd.getGold()+" G");lblGold.setForeground(Theme.GOLD);lblGold.setFont(Theme.FONT_HEADER);
        JButton sv=mkBtn("💾 저장",Theme.BG_BUTTON),lg=mkBtn("🚪 로그아웃",new Color(80,20,20));
        sv.addActionListener(e->{SaveSystem.saveUser(user);showMsg("저장!");});
        lg.addActionListener(e->{SaveSystem.saveUser(user);dispose();new StartScreen().setVisible(true);});
        right.add(lblGold);right.add(sv);right.add(lg);bar.add(right,BorderLayout.EAST);return bar;
    }

    private JPanel buildLeftPanel(){
        JPanel p=new JPanel(new BorderLayout(0,6));p.setOpaque(false);p.setPreferredSize(new Dimension(225,0));
        characterVisualPanel=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(16,12,38));g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                BufferedImage img=ImageGen.getCharacterEquipped(cd.getCharClass().name(),cd.getEquipment(),getWidth()-16,getHeight()-16);
                g2.drawImage(img,8,8,null);super.paintComponent(g);
            }
        };
        characterVisualPanel.setPreferredSize(new Dimension(213,295));characterVisualPanel.setOpaque(false);
        p.add(characterVisualPanel,BorderLayout.CENTER);
        p.add(buildCharInfoPanel(),BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildCharInfoPanel(){
        JPanel panel=new JPanel(){protected void paintComponent(Graphics g){g.setColor(new Color(12,9,30));g.fillRoundRect(0,0,getWidth(),getHeight(),12,12);super.paintComponent(g);}};
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));panel.setOpaque(false);panel.setBorder(new EmptyBorder(6,7,6,7));
        lblLevel=cLabel("Lv. "+cd.getLevel(),Theme.GOLD,new Font("SansSerif",Font.BOLD,18));lblLevel.setAlignmentX(0.5f);panel.add(lblLevel);
        panel.add(Box.createVerticalStrut(3));
        panel.add(cLabel("❤️ HP",Theme.HP_RED,Theme.FONT_SMALL));
        hpBar=mkColorBar(Theme.HP_RED,new Color(50,15,15),cd.getMaxHp(),cd.getHp());hpBar.setMaximumSize(new Dimension(210,12));panel.add(hpBar);
        lblHpNum=cLabel(cd.getHp()+"/"+cd.getMaxHp(),Theme.TEXT_DIM,Theme.FONT_SMALL);panel.add(lblHpNum);
        panel.add(cLabel("💧 MP",new Color(80,160,255),Theme.FONT_SMALL));
        mpBar=mkColorBar(new Color(80,160,255),new Color(10,20,55),cd.getMaxMp(),cd.getMp());mpBar.setMaximumSize(new Dimension(210,12));panel.add(mpBar);
        lblMpNum=cLabel(cd.getMp()+"/"+cd.getMaxMp(),Theme.TEXT_DIM,Theme.FONT_SMALL);panel.add(lblMpNum);
        panel.add(cLabel("✨ EXP",Theme.EXP_BLUE,Theme.FONT_SMALL));
        expBar=mkColorBar(Theme.EXP_BLUE,new Color(8,15,50),100,(int)(cd.getExpPercent()*100));expBar.setMaximumSize(new Dimension(210,9));panel.add(expBar);
        panel.add(Box.createVerticalStrut(4));
        panel.add(cLabel("⚔️ 공격력",new Color(255,180,60),Theme.FONT_SMALL));
        lblAtk=new JLabel(String.valueOf(cd.getAtk()),SwingConstants.CENTER);
        lblAtk.setForeground(new Color(255,220,80));lblAtk.setFont(new Font("SansSerif",Font.BOLD,26));
        lblAtk.setAlignmentX(0.5f);lblAtk.setMaximumSize(new Dimension(210,34));panel.add(lblAtk);
        JPanel row4=new JPanel(new GridLayout(1,3,4,0));row4.setOpaque(false);row4.setMaximumSize(new Dimension(210,18));
        lblHpStat=mLbl("HP:"+cd.getMaxHp(),Theme.HP_RED);row4.add(lblHpStat);
        lblDef=mLbl("DEF:"+cd.getDef(),new Color(100,180,255));row4.add(lblDef);
        lblSpd=mLbl("SPD:"+cd.getSpd(),new Color(100,255,150));row4.add(lblSpd);
        panel.add(row4);panel.add(Box.createVerticalStrut(4));
        panel.add(cLabel("─ 능력치 ─",Theme.TEXT_DIM,Theme.FONT_SMALL));
        lblPoints=cLabel("포인트: "+cd.getStatPoints(),Theme.GOLD,Theme.FONT_SMALL);panel.add(lblPoints);
        panel.add(Box.createVerticalStrut(3));
        String ms=cd.getCharClass().mainStat;
        lblStrVal=sv(cd.getStatStr());lblDexVal=sv(cd.getStatDex());lblIntVal=sv(cd.getStatInt());lblLukVal=sv(cd.getStatLuk());
        JLabel[] vl={lblStrVal,lblDexVal,lblIntVal,lblLukVal};
        String[] keys={"STR","DEX","INT","LUK"},desc={"힘","민첩","지력","행운"};
        for(int i=0;i<4;i++){
            final String key=keys[i];final JLabel vlbl=vl[i];boolean im=key.equals(ms);
            JPanel row=new JPanel(new BorderLayout(2,0));row.setOpaque(false);row.setMaximumSize(new Dimension(210,20));
            JLabel nl=new JLabel((im?"★":"")+key+" "+desc[i]);nl.setForeground(im?Theme.GOLD:Theme.TEXT_DIM);nl.setFont(new Font("SansSerif",im?Font.BOLD:Font.PLAIN,10));
            JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,1,0));btns.setOpaque(false);
            JButton minus=sBtn("-",new Color(80,30,30)),plus=sBtn("+",new Color(30,60,120));
            minus.addActionListener(e->{cd.refundStat(key);vlbl.setText(String.valueOf(getStatVal(key)));lblPoints.setText("포인트: "+cd.getStatPoints());rRight();});
            plus.addActionListener(e->{cd.spendStat(key);vlbl.setText(String.valueOf(getStatVal(key)));lblPoints.setText("포인트: "+cd.getStatPoints());rRight();SaveSystem.saveUser(user);});
            btns.add(minus);btns.add(vlbl);btns.add(plus);
            row.add(nl,BorderLayout.WEST);row.add(btns,BorderLayout.EAST);panel.add(row);
        }
        return panel;
    }

    private int getStatVal(String k){return switch(k){case"STR"->cd.getStatStr();case"DEX"->cd.getStatDex();case"INT"->cd.getStatInt();default->cd.getStatLuk();};}

    private JPanel buildRightSide(){
        JPanel p=new JPanel(new BorderLayout(0,0));p.setOpaque(false);
        JLabel title=new JLabel("🏰  모험을 선택하세요",SwingConstants.CENTER);
        title.setForeground(Theme.ACCENT);title.setFont(Theme.FONT_TITLE);title.setBorder(new EmptyBorder(0,0,10,0));
        p.add(title,BorderLayout.NORTH);
        JPanel grid=new JPanel(new GridLayout(2,3,12,12));grid.setOpaque(false);
        grid.add(bCard("⚔️","던전","몬스터·아이템 드롭","EXP·골드",new Color(130,30,30),"dungeon"));
        grid.add(bCard("🏯","무한의 탑","100층 도전","강력한 보스",new Color(30,60,130),"tower"));
        grid.add(bCard("🏪","상점","등급별 장비","전설급 아이템",new Color(100,75,10),"shop"));
        grid.add(bCard("🎒","인벤토리","슬롯별 관리","장비 착용",new Color(25,90,35),"inventory"));
        grid.add(bCard("🔨","강화하기","아이템 강화·합성","+5강 / 합성",new Color(90,50,10),"enhance"));
        grid.add(bCard("⚡","스킬창","스킬 투자","SP로 강화",new Color(60,20,110),"skills"));
        p.add(grid,BorderLayout.CENTER);
        return p;
    }

    private JPanel bCard(String emoji,String title,String d1,String d2,Color base,String action){
        JPanel c=new JPanel(new GridBagLayout()){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setPaint(new GradientPaint(0,0,base.darker(),0,getHeight(),base));g2.fillRoundRect(0,0,getWidth(),getHeight(),18,18);super.paintComponent(g);}};
        c.setOpaque(false);c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));c.setBorder(new EmptyBorder(8,8,8,8));
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.insets=new Insets(3,0,3,0);
        JLabel el=new JLabel(emoji,SwingConstants.CENTER);el.setFont(new Font("Segoe UI Emoji",Font.PLAIN,40));g.gridy=0;c.add(el,g);
        JLabel tl=new JLabel(title,SwingConstants.CENTER);tl.setForeground(Theme.GOLD);tl.setFont(Theme.FONT_HEADER);g.gridy=1;c.add(tl,g);
        for(String d:new String[]{d1,d2}){JLabel dl=new JLabel(d,SwingConstants.CENTER);dl.setForeground(Theme.TEXT_DIM);dl.setFont(Theme.FONT_SMALL);g.gridy++;c.add(dl,g);}
        c.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){navigate(action);}public void mouseEntered(MouseEvent e){c.setBorder(new LineBorder(base.brighter(),2,true));}public void mouseExited(MouseEvent e){c.setBorder(new EmptyBorder(8,8,8,8));}});
        return c;
    }

    private void rRight(){
        if(hpBar==null)return;
        hpBar.setMaximum(cd.getMaxHp());hpBar.setValue(cd.getHp());hpBar.repaint();
        mpBar.setMaximum(cd.getMaxMp());mpBar.setValue(cd.getMp());mpBar.repaint();
        expBar.setValue((int)(cd.getExpPercent()*100));expBar.repaint();
        if(lblHpNum!=null)lblHpNum.setText(cd.getHp()+"/"+cd.getMaxHp());
        if(lblMpNum!=null)lblMpNum.setText(cd.getMp()+"/"+cd.getMaxMp());
        if(lblAtk!=null)lblAtk.setText(String.valueOf(cd.getAtk()));
        if(lblHpStat!=null)lblHpStat.setText("HP:"+cd.getMaxHp());
        if(lblDef!=null)lblDef.setText("DEF:"+cd.getDef());
        if(lblSpd!=null)lblSpd.setText("SPD:"+cd.getSpd());
        if(lblLevel!=null)lblLevel.setText("Lv. "+cd.getLevel());
        if(lblGold!=null)lblGold.setText("💰 "+cd.getGold()+" G");
    }

    public void refreshStats(){
        if(lblLevel==null)return;rRight();
        if(lblStrVal!=null)lblStrVal.setText(String.valueOf(cd.getStatStr()));
        if(lblDexVal!=null)lblDexVal.setText(String.valueOf(cd.getStatDex()));
        if(lblIntVal!=null)lblIntVal.setText(String.valueOf(cd.getStatInt()));
        if(lblLukVal!=null)lblLukVal.setText(String.valueOf(cd.getStatLuk()));
        if(lblPoints!=null)lblPoints.setText("포인트: "+cd.getStatPoints());
        if(characterVisualPanel!=null)characterVisualPanel.repaint();
        SaveSystem.saveUser(user);
    }

    private void navigate(String dest){
        SaveSystem.saveUser(user);
        switch(dest){
            case"dungeon":  {DungeonScreen s=new DungeonScreen(user,this);s.setVisible(true);setVisible(false);break;}
            case"tower":    {TowerScreen s=new TowerScreen(user,this);s.setVisible(true);setVisible(false);break;}
            case"shop":     {ShopScreen s=new ShopScreen(user,this);s.setVisible(true);setVisible(false);break;}
            case"inventory":{InventoryScreen s=new InventoryScreen(user,this);s.setVisible(true);setVisible(false);break;}
            case"enhance":  {EnhanceScreen s=new EnhanceScreen(user,this);s.setVisible(true);setVisible(false);break;}
            case"skills":   {SkillScreen s=new SkillScreen(user,this);s.setVisible(true);setVisible(false);break;}
        }
    }

    private JProgressBar mkColorBar(Color fg,Color bg,int max,int val){
        JProgressBar b=new JProgressBar(0,max){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);int w=getWidth(),h=getHeight();g2.setColor(getBackground());g2.fillRoundRect(0,0,w,h,h,h);if(getMaximum()>0&&getValue()>0){int fw=Math.min(w,Math.max(h,(int)((double)getValue()/getMaximum()*w)));g2.setPaint(new GradientPaint(0,0,getForeground().brighter(),0,h,getForeground().darker()));g2.fillRoundRect(0,0,fw,h,h,h);g2.setColor(new Color(255,255,255,50));g2.fillRoundRect(2,1,fw-4,h/2,h/2,h/2);}g2.setColor(new Color(0,0,0,80));g2.setStroke(new BasicStroke(1f));g2.drawRoundRect(0,0,w-1,h-1,h,h);g2.dispose();}};
        b.setValue(val);b.setForeground(fg);b.setBackground(bg);b.setStringPainted(false);b.setBorderPainted(false);b.setOpaque(false);
        b.setUI(new javax.swing.plaf.basic.BasicProgressBarUI(){public void paint(Graphics g,JComponent c){c.paintComponents(g);}});return b;
    }
    private JLabel cLabel(String t,Color c,Font f){JLabel l=new JLabel(t,SwingConstants.CENTER);l.setForeground(c);l.setFont(f);l.setAlignmentX(0.5f);l.setMaximumSize(new Dimension(214,24));return l;}
    private JLabel mLbl(String t,Color c){JLabel l=new JLabel(t,SwingConstants.CENTER);l.setForeground(c);l.setFont(Theme.FONT_SMALL);return l;}
    private JLabel sv(int v){JLabel l=new JLabel(String.valueOf(v),SwingConstants.CENTER);l.setForeground(Theme.TEXT_MAIN);l.setFont(new Font("SansSerif",Font.BOLD,11));l.setPreferredSize(new Dimension(26,18));return l;}
    private JButton sBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Color.WHITE);b.setFont(new Font("SansSerif",Font.BOLD,10));b.setFocusPainted(false);b.setBorderPainted(false);b.setPreferredSize(new Dimension(20,18));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));b.setOpaque(true);return b;}
    private JButton mkBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Theme.TEXT_MAIN);b.setFont(Theme.FONT_SMALL);b.setFocusPainted(false);b.setBorderPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private void showMsg(String m){JOptionPane.showMessageDialog(this,m,"알림",JOptionPane.INFORMATION_MESSAGE);}
}