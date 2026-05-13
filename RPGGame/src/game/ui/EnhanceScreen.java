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
import java.util.*;
import java.util.List;

public class EnhanceScreen extends JFrame {
    private UserData user; private CharacterData cd; private GameScreen hub;
    private JPanel itemListPanel;
    private JLabel resultLabel;
    private int selectedIdx = -1;
    private JLabel goldLabel;
    private boolean showMerge = false;
    private JPanel mainPanel;

    public EnhanceScreen(UserData user, GameScreen hub) {
        this.user=user; this.cd=user.getCharacterData(); this.hub=hub;
        setTitle("🔨 강화·합성");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){exit();}});
        buildAll();
    }

    private void buildAll() {
        mainPanel = new JPanel(new BorderLayout(8,8)){protected void paintComponent(Graphics g){Theme.paintBg(g,getWidth(),getHeight());Theme.paintStars(g,getWidth(),getHeight(),new Random(55));super.paintComponent(g);}};
        mainPanel.setOpaque(false);mainPanel.setBorder(new EmptyBorder(10,10,10,10));

        // TOP
        JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);
        JLabel title=new JLabel("🔨 강화 · 합성",SwingConstants.LEFT);title.setForeground(Theme.GOLD);title.setFont(Theme.FONT_TITLE);
        goldLabel=new JLabel("💰 "+cd.getGold()+"G",SwingConstants.RIGHT);goldLabel.setForeground(Theme.GOLD);goldLabel.setFont(Theme.FONT_HEADER);
        JPanel topRight=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));topRight.setOpaque(false);
        JButton enhBtn=tabBtn("🔨 강화",!showMerge); JButton mergeBtn=tabBtn("🔀 합성",showMerge);
        enhBtn.addActionListener(e->{showMerge=false;buildAll();});
        mergeBtn.addActionListener(e->{showMerge=true;buildAll();});
        JButton back=mkBtn("🏠 돌아가기",Theme.BG_BUTTON);back.addActionListener(e->exit());
        topRight.add(goldLabel);topRight.add(enhBtn);topRight.add(mergeBtn);topRight.add(back);
        top.add(title,BorderLayout.WEST);top.add(topRight,BorderLayout.EAST);
        mainPanel.add(top,BorderLayout.NORTH);

        if (showMerge) buildMergePanel();
        else           buildEnhancePanel();

        setContentPane(mainPanel);revalidate();repaint();
    }

    // ── 강화 패널 ────────────────────────────────────
    private void buildEnhancePanel() {
        JPanel center=new JPanel(new GridLayout(1,2,12,0));center.setOpaque(false);
        center.add(buildItemList(false));
        center.add(buildEnhanceDetail());
        mainPanel.add(center,BorderLayout.CENTER);
    }

    private JPanel buildEnhanceDetail() {
        JPanel p=new JPanel(new BorderLayout(0,10));p.setOpaque(false);
        JPanel infoBox=new JPanel(){protected void paintComponent(Graphics g){g.setColor(new Color(14,10,34));g.fillRoundRect(0,0,getWidth(),getHeight(),12,12);super.paintComponent(g);}};
        infoBox.setLayout(new BoxLayout(infoBox,BoxLayout.Y_AXIS));infoBox.setOpaque(false);infoBox.setBorder(new EmptyBorder(16,16,16,16));

        JLabel h=cLbl("아이템을 선택하세요",Theme.ACCENT,Theme.FONT_HEADER);infoBox.add(h);
        infoBox.add(Box.createVerticalStrut(10));

        resultLabel=cLbl("",Theme.TEXT_MAIN,Theme.FONT_BODY);infoBox.add(resultLabel);

        // 강화 설명
        infoBox.add(Box.createVerticalStrut(14));
        infoBox.add(cLbl("── 강화 확률 ──",Theme.TEXT_DIM,Theme.FONT_SMALL));
        String[] rows={"+0→+1: 100%","+1→+2: 80%","+2→+3: 60%","+3→+4: 40%","+4→+5: 20%"};
        Color[] cols={new Color(100,200,100),new Color(180,220,80),new Color(220,180,60),new Color(220,120,40),new Color(220,60,60)};
        for(int i=0;i<5;i++){JLabel l=cLbl(rows[i],cols[i],Theme.FONT_SMALL);infoBox.add(l);}

        infoBox.add(Box.createVerticalStrut(14));
        JButton enhBtn=bigBtn("🔨 강화하기",new Color(90,50,10));
        enhBtn.addActionListener(e->doEnhance(infoBox,h));
        infoBox.add(enhBtn);

        p.add(infoBox,BorderLayout.CENTER);
        return p;
    }

    private void doEnhance(JPanel infoBox, JLabel headerLbl) {
        if(selectedIdx<0||selectedIdx>=cd.getInventory().size()){showMsg("아이템을 선택하세요.");return;}
        Item item=cd.getInventory().get(selectedIdx);
        if(item.getType()==Item.ItemType.POTION||item.getType()==Item.ItemType.GOLD_BAG){showMsg("포션은 강화할 수 없습니다.");return;}
        if(item.getEnhanceLevel()>=5){showMsg("이미 최대 강화 단계입니다!");return;}

        // 비용: 기본 goldValue * (currentLv+1) * 0.3
        long cost=(long)(item.getGoldValue()*(item.getEnhanceLevel()+1)*0.3);
        if(!cd.spendGold(cost)){showMsg("골드가 부족합니다!\n필요: "+cost+"G");return;}
        goldLabel.setText("💰 "+cd.getGold()+"G");

        double rate=item.getEnhanceSuccessRate();
        boolean success=Math.random()<rate;
        int prevLv=item.getEnhanceLevel();

        if(success){
            item.setEnhanceLevel(prevLv+1);
            resultLabel.setText("✅ 강화 성공! +"+prevLv+" → +"+item.getEnhanceLevel());
            resultLabel.setForeground(new Color(100,255,100));
            headerLbl.setText(item.getDisplayName()+" 강화 완료!");
        } else {
            resultLabel.setText("❌ 강화 실패! (+"+prevLv+" 유지)  비용: "+cost+"G");
            resultLabel.setForeground(new Color(255,100,100));
        }
        SaveSystem.saveUser(user);
        rebuildItemList(false);
        cd.recalcStats();
    }

    // ── 합성 패널 ────────────────────────────────────
    private void buildMergePanel() {
        JPanel center=new JPanel(new GridLayout(1,2,12,0));center.setOpaque(false);
        center.add(buildItemList(true));
        center.add(buildMergeDetail());
        mainPanel.add(center,BorderLayout.CENTER);
    }

    private List<Integer> mergeSelected=new ArrayList<>();

    private JPanel buildMergeDetail() {
        JPanel p=new JPanel(new BorderLayout(0,10));p.setOpaque(false);
        JPanel box=new JPanel(){protected void paintComponent(Graphics g){g.setColor(new Color(14,10,34));g.fillRoundRect(0,0,getWidth(),getHeight(),12,12);super.paintComponent(g);}};
        box.setLayout(new BoxLayout(box,BoxLayout.Y_AXIS));box.setOpaque(false);box.setBorder(new EmptyBorder(16,16,16,16));

        box.add(cLbl("🔀 합성",Theme.ACCENT,Theme.FONT_HEADER));
        box.add(Box.createVerticalStrut(8));
        box.add(cLbl("같은 등급 장비 3개 선택",Theme.TEXT_MAIN,Theme.FONT_BODY));
        box.add(cLbl("→ 상위 등급 랜덤 아이템",new Color(255,200,80),Theme.FONT_BODY));
        box.add(Box.createVerticalStrut(6));
        box.add(cLbl("RARE×3 → EPIC",new Color(170,50,200),Theme.FONT_SMALL));
        box.add(cLbl("EPIC×3 → UNIQUE",new Color(255,136,0),Theme.FONT_SMALL));
        box.add(cLbl("UNIQUE×3 → LEGENDARY",new Color(255,215,0),Theme.FONT_SMALL));
        box.add(Box.createVerticalStrut(12));

        resultLabel=cLbl("",Theme.TEXT_MAIN,Theme.FONT_BODY);box.add(resultLabel);
        box.add(Box.createVerticalStrut(12));

        JButton mergeBtn=bigBtn("🔀 합성하기",new Color(60,20,100));
        mergeBtn.addActionListener(e->doMerge());
        box.add(mergeBtn);
        p.add(box,BorderLayout.CENTER);
        return p;
    }

    private Set<Integer> mergeSelectedSet=new LinkedHashSet<>();

    private JPanel buildItemList(boolean multiSelect) {
        JPanel outer=new JPanel(new BorderLayout(0,6));outer.setOpaque(false);
        JLabel title=new JLabel(multiSelect?"📦 아이템 선택 (같은 등급 3개)":"📦 강화할 아이템 선택",SwingConstants.CENTER);
        title.setForeground(Theme.ACCENT);title.setFont(Theme.FONT_HEADER);outer.add(title,BorderLayout.NORTH);
        itemListPanel=new JPanel();itemListPanel.setLayout(new BoxLayout(itemListPanel,BoxLayout.Y_AXIS));itemListPanel.setBackground(new Color(10,8,28));
        fillItemList(multiSelect);
        JScrollPane sc=new JScrollPane(itemListPanel);sc.setBorder(new LineBorder(new Color(40,30,70),1));sc.getViewport().setBackground(new Color(10,8,28));
        outer.add(sc,BorderLayout.CENTER);
        return outer;
    }

    private void fillItemList(boolean multiSelect) {
        itemListPanel.removeAll();
        List<Item> inv=cd.getInventory();
        if(inv.isEmpty()){JLabel e=new JLabel("인벤토리가 비어있습니다",SwingConstants.CENTER);e.setForeground(Theme.TEXT_DIM);e.setFont(Theme.FONT_BODY);e.setAlignmentX(0.5f);itemListPanel.add(Box.createVerticalStrut(20));itemListPanel.add(e);itemListPanel.revalidate();return;}
        for(int i=0;i<inv.size();i++){
            if(inv.get(i).getType()==Item.ItemType.POTION||inv.get(i).getType()==Item.ItemType.GOLD_BAG)continue;
            final int idx=i; Item item=inv.get(i);
            boolean sel=multiSelect?mergeSelectedSet.contains(idx):(selectedIdx==idx);
            JPanel row=buildItemRow(item,idx,i%2==0,sel,multiSelect);
            row.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){
                if(multiSelect){if(mergeSelectedSet.contains(idx))mergeSelectedSet.remove(idx);else if(mergeSelectedSet.size()<3)mergeSelectedSet.add(idx);rebuildItemList(true);}
                else{selectedIdx=idx;rebuildItemList(false);}
            }});
            itemListPanel.add(row);
        }
        itemListPanel.revalidate();itemListPanel.repaint();
    }

    private JPanel buildItemRow(Item item,int idx,boolean alt,boolean selected,boolean multi){
        JPanel row=new JPanel(new BorderLayout(6,0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
        row.setBackground(selected?new Color(40,30,80):(alt?new Color(20,16,46):new Color(15,12,36)));
        row.setBorder(new EmptyBorder(6,10,6,8));
        if(selected)row.setBorder(new LineBorder(new Color(100,80,200),1));
        JPanel strip=new JPanel();strip.setBackground(Color.decode(item.getRarityColor()));strip.setPreferredSize(new Dimension(4,0));row.add(strip,BorderLayout.WEST);
        JPanel info=new JPanel(new GridLayout(2,1,0,2));info.setOpaque(false);
        String rar="<font color='"+item.getRarityColor()+"'>["+item.getRarityName()+"]</font>";
        JLabel nl=new JLabel("<html>"+rar+" <b>"+item.getDisplayName()+"</b></html>");nl.setForeground(Theme.TEXT_MAIN);nl.setFont(Theme.FONT_BODY);
        JLabel sl=new JLabel("<html><font color='#8888bb'>"+item.getStatsText()+"</font>  <font color='#aaaaaa'>강화: +"+item.getEnhanceLevel()+"/5</font></html>");sl.setFont(Theme.FONT_SMALL);
        info.add(nl);info.add(sl);row.add(info,BorderLayout.CENTER);
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return row;
    }

    private void rebuildItemList(boolean multi){fillItemList(multi);}

    private void doMerge(){
        if(mergeSelectedSet.size()!=3){showMsg("같은 등급의 장비 3개를 선택하세요.");return;}
        List<Item> inv=cd.getInventory();
        Integer[]idxArr=mergeSelectedSet.toArray(new Integer[0]);
        Item a=inv.get(idxArr[0]),b=inv.get(idxArr[1]),c2=inv.get(idxArr[2]);
        if(a.getRarity()!=b.getRarity()||b.getRarity()!=c2.getRarity()){showMsg("같은 등급 3개를 선택해야 합니다!");return;}
        if(a.getRarity()==Item.Rarity.LEGENDARY){showMsg("전설 등급은 합성할 수 없습니다.");return;}
        Item.Rarity nextRar=a.getNextRarity();
        // 3개 제거 (내림차순으로)
        List<Integer> sorted=new ArrayList<>(mergeSelectedSet);sorted.sort(Collections.reverseOrder());
        for(int i:sorted)inv.remove(i);
        mergeSelectedSet.clear();selectedIdx=-1;
        // 상위 등급 랜덤 아이템 생성
        Item result=ItemDatabase.rollSynthesized(nextRar,cd.getLevel());
        inv.add(result);
        resultLabel.setText("✨ 합성 성공! "+result.getDisplayName()+" 획득!");
        resultLabel.setForeground(new Color(255,200,100));
        SaveSystem.saveUser(user);
        rebuildItemList(true);
    }

    private void exit(){hub.refreshStats();hub.setVisible(true);dispose();}
    private JLabel cLbl(String t,Color c,Font f){JLabel l=new JLabel(t,SwingConstants.CENTER);l.setForeground(c);l.setFont(f);l.setAlignmentX(0.5f);return l;}
    private JButton mkBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Theme.TEXT_MAIN);b.setFont(Theme.FONT_SMALL);b.setFocusPainted(false);b.setBorderPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private JButton bigBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Color.WHITE);b.setFont(new Font("SansSerif",Font.BOLD,15));b.setFocusPainted(false);b.setBorderPainted(false);b.setPreferredSize(new Dimension(220,42));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));b.setAlignmentX(0.5f);b.setMaximumSize(new Dimension(220,42));return b;}
    private JButton tabBtn(String t,boolean active){JButton b=mkBtn(t,active?new Color(80,50,130):new Color(40,30,70));if(active)b.setForeground(Theme.GOLD);return b;}
    private void showMsg(String m){JOptionPane.showMessageDialog(this,m,"알림",JOptionPane.INFORMATION_MESSAGE);}
}