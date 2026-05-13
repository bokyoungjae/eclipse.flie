package game.ui;

import game.data.CharacterData;
import game.data.UserData;
import game.skill.Skill;
import game.system.SaveSystem;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SkillScreen extends JFrame {
    private static final int MAX_PER_STAT = 10;
    private UserData user; private CharacterData cd; private GameScreen hub;
    private JLabel lblPoints,lblSP,lblAtk,lblDef,lblHp,lblMp,lblSpd;
    private JLabel lblStr,lblDex,lblInt,lblLuk;
    private JPanel skillListPanel;

    public SkillScreen(UserData user, GameScreen hub){
        this.user=user;this.cd=user.getCharacterData();this.hub=hub;
        setTitle("⚡ 스킬·능력치");setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1000,660);setLocationRelativeTo(null);setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){exit();}});
        setContentPane(buildContent());
    }

    private JPanel buildContent(){
        JPanel root=new JPanel(new BorderLayout(10,10)){protected void paintComponent(Graphics g){Theme.paintBg(g,getWidth(),getHeight());Theme.paintStars(g,getWidth(),getHeight(),new Random(88));super.paintComponent(g);}};
        root.setOpaque(false);root.setBorder(new EmptyBorder(10,10,10,10));

        JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);
        JLabel title=new JLabel("⚡ 스킬·능력치",SwingConstants.LEFT);title.setForeground(Theme.GOLD);title.setFont(Theme.FONT_TITLE);
        JButton back=mkBtn("🏠 돌아가기",Theme.BG_BUTTON);back.addActionListener(e->exit());
        JPanel tr=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));tr.setOpaque(false);tr.add(back);
        top.add(title,BorderLayout.WEST);top.add(tr,BorderLayout.EAST);
        root.add(top,BorderLayout.NORTH);

        JPanel center=new JPanel(new GridLayout(1,2,14,0));center.setOpaque(false);
        center.add(buildStatPanel());
        center.add(buildSkillPanel());
        root.add(center,BorderLayout.CENTER);
        return root;
    }

    // ── 능력치 패널 ──────────────────────────────────
    private JPanel buildStatPanel(){
        JPanel p=new JPanel(new BorderLayout(0,8));p.setOpaque(false);
        JLabel t=new JLabel("📊 능력치",SwingConstants.CENTER);t.setForeground(Theme.ACCENT);t.setFont(Theme.FONT_HEADER);p.add(t,BorderLayout.NORTH);

        JPanel box=new JPanel(){protected void paintComponent(Graphics g){g.setColor(new Color(12,9,30));g.fillRoundRect(0,0,getWidth(),getHeight(),12,12);super.paintComponent(g);}};
        box.setLayout(new BoxLayout(box,BoxLayout.Y_AXIS));box.setOpaque(false);box.setBorder(new EmptyBorder(12,14,12,14));

        lblPoints=cLbl("스탯 포인트: "+cd.getStatPoints(),Theme.GOLD,Theme.FONT_BODY);box.add(lblPoints);
        box.add(Box.createVerticalStrut(6));

        // 현재 스탯 수치 표시
        JPanel statsRow=new JPanel(new GridLayout(2,3,8,4));statsRow.setOpaque(false);statsRow.setMaximumSize(new Dimension(460,50));
        lblAtk=mLbl("⚔️ ATK: "+cd.getAtk(),new Color(255,220,80));statsRow.add(lblAtk);
        lblHp=mLbl("❤️ HP: "+cd.getMaxHp(),Theme.HP_RED);statsRow.add(lblHp);
        lblMp=mLbl("💧 MP: "+cd.getMaxMp(),new Color(80,160,255));statsRow.add(lblMp);
        lblDef=mLbl("🛡️ DEF: "+cd.getDef(),new Color(100,180,255));statsRow.add(lblDef);
        lblSpd=mLbl("💨 SPD: "+cd.getSpd(),new Color(100,255,150));statsRow.add(lblSpd);
        statsRow.add(mLbl("★ "+cd.getCharClass().mainStat+" 주스탯",Theme.ACCENT));
        box.add(statsRow);box.add(Box.createVerticalStrut(12));

        // 스탯 슬라이더 행 (최대 +10씩)
        String ms=cd.getCharClass().mainStat;
        String[]keys={"STR","DEX","INT","LUK"},descs={"힘 (ATK·HP)","민첩 (SPD·ATK)","지력 (MP·마법)","행운 (드롭률)"};
        lblStr=sv(cd.getStatStr());lblDex=sv(cd.getStatDex());lblInt=sv(cd.getStatInt());lblLuk=sv(cd.getStatLuk());
        JLabel[]vl={lblStr,lblDex,lblInt,lblLuk};

        for(int i=0;i<4;i++){
            final String key=keys[i];final JLabel vlbl=vl[i];boolean im=key.equals(ms);
            JPanel row=new JPanel(new BorderLayout(6,0));row.setOpaque(false);row.setMaximumSize(new Dimension(460,36));row.setBorder(new EmptyBorder(3,0,3,0));

            JLabel nameLbl=new JLabel((im?"★ ":"")+key+" "+descs[i]);
            nameLbl.setForeground(im?Theme.GOLD:Theme.TEXT_DIM);
            nameLbl.setFont(new Font("SansSerif",im?Font.BOLD:Font.PLAIN,12));
            nameLbl.setPreferredSize(new Dimension(160,26));

            JPanel btnGroup=new JPanel(new FlowLayout(FlowLayout.CENTER,3,0));btnGroup.setOpaque(false);
            JButton m10=sBtn("-10",new Color(90,20,20));JButton m1=sBtn("-1",new Color(60,25,25));
            vlbl.setPreferredSize(new Dimension(32,26));vlbl.setFont(new Font("SansSerif",Font.BOLD,14));
            JButton p1=sBtn("+1",new Color(25,55,110));JButton p10=sBtn("+10",new Color(25,70,130));

            m10.addActionListener(e->spendStat(key,vlbl,-10));
            m1.addActionListener(e->spendStat(key,vlbl,-1));
            p1.addActionListener(e->spendStat(key,vlbl,1));
            p10.addActionListener(e->spendStat(key,vlbl,10));

            btnGroup.add(m10);btnGroup.add(m1);btnGroup.add(vlbl);btnGroup.add(p1);btnGroup.add(p10);
            row.add(nameLbl,BorderLayout.WEST);row.add(btnGroup,BorderLayout.EAST);
            box.add(row);
        }

        box.add(Box.createVerticalStrut(10));
        JLabel note=cLbl("※ 스탯 1회 투자 최대 +10  /  - 로 환불 가능",Theme.TEXT_DIM,Theme.FONT_SMALL);box.add(note);
        p.add(box,BorderLayout.CENTER);return p;
    }

    private void spendStat(String key,JLabel vlbl,int amount){
        if(amount>0){
            int toSpend=Math.min(amount,Math.min(cd.getStatPoints(),MAX_PER_STAT));
            for(int i=0;i<toSpend;i++)cd.spendStat(key);
        } else {
            int toRefund=Math.min(-amount,MAX_PER_STAT);
            for(int i=0;i<toRefund;i++)if(!cd.refundStat(key))break;
        }
        int v=switch(key){case"STR"->cd.getStatStr();case"DEX"->cd.getStatDex();case"INT"->cd.getStatInt();default->cd.getStatLuk();};
        vlbl.setText(String.valueOf(v));
        updateStatDisplay();
        SaveSystem.saveUser(user);
    }

    private void updateStatDisplay(){
        if(lblPoints!=null)lblPoints.setText("스탯 포인트: "+cd.getStatPoints());
        if(lblAtk!=null)lblAtk.setText("⚔️ ATK: "+cd.getAtk());
        if(lblHp!=null)lblHp.setText("❤️ HP: "+cd.getMaxHp());
        if(lblMp!=null)lblMp.setText("💧 MP: "+cd.getMaxMp());
        if(lblDef!=null)lblDef.setText("🛡️ DEF: "+cd.getDef());
        if(lblSpd!=null)lblSpd.setText("💨 SPD: "+cd.getSpd());
    }

    // ── 스킬 패널 ────────────────────────────────────
    private JPanel buildSkillPanel(){
        JPanel p=new JPanel(new BorderLayout(0,8));p.setOpaque(false);
        JPanel titleRow=new JPanel(new BorderLayout(6,0));titleRow.setOpaque(false);
        JLabel t=new JLabel("⚡ 스킬 투자",SwingConstants.LEFT);t.setForeground(Theme.ACCENT);t.setFont(Theme.FONT_HEADER);
        lblSP=new JLabel("SP: "+cd.getSkillPoints(),SwingConstants.RIGHT);lblSP.setForeground(new Color(120,220,255));lblSP.setFont(Theme.FONT_HEADER);
        titleRow.add(t,BorderLayout.WEST);titleRow.add(lblSP,BorderLayout.EAST);
        p.add(titleRow,BorderLayout.NORTH);

        skillListPanel=new JPanel();skillListPanel.setLayout(new BoxLayout(skillListPanel,BoxLayout.Y_AXIS));skillListPanel.setOpaque(false);
        rebuildSkillList();
        JScrollPane sc=new JScrollPane(skillListPanel);sc.setBorder(null);sc.setOpaque(false);sc.getViewport().setOpaque(false);sc.getVerticalScrollBar().setUnitIncrement(14);
        p.add(sc,BorderLayout.CENTER);return p;
    }

    private void rebuildSkillList(){
        skillListPanel.removeAll();
        Skill[]all=Skill.ALL[cd.getCharClass().ordinal()];
        for(Skill sk:all){skillListPanel.add(buildSkillCard(sk));skillListPanel.add(Box.createVerticalStrut(6));}
        skillListPanel.revalidate();skillListPanel.repaint();
        if(lblSP!=null)lblSP.setText("SP: "+cd.getSkillPoints());
    }

    private JPanel buildSkillCard(Skill sk){
        int slv=cd.getSkillLevel(sk.id);
        boolean lok=sk.canUnlock(cd.getLevel());
        boolean used=slv>0;
        boolean ci=lok&&slv<sk.maxSkillLevel&&cd.getSkillPoints()>0;
        Color bgc=used?new Color(20,15,45):(lok?new Color(18,14,36):new Color(12,10,24));

        JPanel card=new JPanel(new BorderLayout(8,4)){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgc);g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                if(ci){g2.setColor(new Color(100,220,255,170));g2.setStroke(new BasicStroke(1.8f));g2.drawRoundRect(1,1,getWidth()-3,getHeight()-3,10,10);}
                else if(used){g2.setColor(sk.color.darker());g2.setStroke(new BasicStroke(1.5f));g2.drawRoundRect(1,1,getWidth()-3,getHeight()-3,10,10);}
                super.paintComponent(g);}
        };
        card.setOpaque(false);card.setBorder(new EmptyBorder(10,12,10,12));card.setMaximumSize(new Dimension(460,95));

        JLabel el=new JLabel(sk.emoji,SwingConstants.CENTER);el.setFont(new Font("Segoe UI Emoji",Font.PLAIN,28));el.setPreferredSize(new Dimension(36,36));
        if(!lok)el.setForeground(new Color(60,55,80));
        card.add(el,BorderLayout.WEST);

        JPanel info=new JPanel(new GridLayout(3,1,0,2));info.setOpaque(false);
        JLabel nl=new JLabel(sk.name+(used?" [Lv."+slv+"/"+sk.maxSkillLevel+"]":""));nl.setForeground(used?Theme.GOLD:(lok?new Color(160,150,180):Theme.TEXT_DIM));nl.setFont(new Font("SansSerif",Font.BOLD,13));
        JLabel ml=new JLabel((sk.manaCost>0?"MP -"+sk.manaCost:"MP 무료")+"   "+(used?"데미지 ×"+String.format("%.2f",sk.getDmgMult(slv)):""));ml.setForeground(lok?new Color(80,160,255):new Color(50,70,110));ml.setFont(Theme.FONT_SMALL);
        String ss;Color sc2;
        if(!lok){ss="🔒 Lv."+sk.unlockLevel+" 해금";sc2=new Color(90,80,120);}
        else if(!used){ss="▶ SP를 투자하여 스킬 습득";sc2=new Color(100,220,255);}
        else{ss=used?"✅ 습득됨 — 강화 가능 (Lv."+slv+"→Lv."+(slv+1)+")":"";sc2=new Color(80,200,80);}
        JLabel stl=new JLabel(ss);stl.setForeground(sc2);stl.setFont(Theme.FONT_SMALL);
        info.add(nl);info.add(ml);info.add(stl);
        card.add(info,BorderLayout.CENTER);

        if(lok){
            JPanel btnCol=new JPanel(new GridLayout(2,1,0,4));btnCol.setOpaque(false);
            JButton invest=skBtn("▲ 투자",new Color(40,80,150));
            JButton refund=skBtn("▼ 환불",new Color(80,30,30));
            invest.setEnabled(ci);
            refund.setEnabled(slv>0);
            invest.addActionListener(e->{cd.investSkill(sk.id);rebuildSkillList();});
            refund.addActionListener(e->{cd.refundSkill(sk.id);rebuildSkillList();});
            btnCol.add(invest);btnCol.add(refund);
            card.add(btnCol,BorderLayout.EAST);
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        return card;
    }

    private void exit(){hub.refreshStats();hub.setVisible(true);dispose();}
    private JLabel cLbl(String t,Color c,Font f){JLabel l=new JLabel(t,SwingConstants.CENTER);l.setForeground(c);l.setFont(f);l.setAlignmentX(0.5f);return l;}
    private JLabel mLbl(String t,Color c){JLabel l=new JLabel(t,SwingConstants.LEFT);l.setForeground(c);l.setFont(Theme.FONT_SMALL);return l;}
    private JLabel sv(int v){JLabel l=new JLabel(String.valueOf(v),SwingConstants.CENTER);l.setForeground(Theme.TEXT_MAIN);l.setFont(new Font("SansSerif",Font.BOLD,14));return l;}
    private JButton sBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(new Color(220,220,255));b.setFont(new Font("SansSerif",Font.BOLD,10));b.setFocusPainted(false);b.setBorderPainted(false);b.setPreferredSize(new Dimension(42,24));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));b.setOpaque(true);return b;}
    private JButton skBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Color.WHITE);b.setFont(new Font("SansSerif",Font.BOLD,11));b.setFocusPainted(false);b.setBorderPainted(false);b.setPreferredSize(new Dimension(72,28));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));b.setOpaque(true);return b;}
    private JButton mkBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Theme.TEXT_MAIN);b.setFont(Theme.FONT_SMALL);b.setFocusPainted(false);b.setBorderPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
}