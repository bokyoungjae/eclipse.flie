package game.ui;
import game.data.CharacterData;
import game.data.UserData;
import game.inventory.Item;
import game.system.SaveSystem;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class InventoryScreen extends JFrame {
    private UserData user; private CharacterData cd; private GameScreen hub;
    private JLabel goldLabel; private JPanel itemAreaPanel;
    private Item.ItemType currentFilter=null;
    private static final String[] SLOT_NAMES={"🪖 머리","🥋 몸통","👖 다리","⚔️ 무기","📿 악세"};

    public InventoryScreen(UserData user,GameScreen hub){
        this.user=user;this.cd=user.getCharacterData();this.hub=hub;
        setTitle("🎒 인벤토리");setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1020,660);setLocationRelativeTo(null);setResizable(false);
        addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){exit();}});
        buildAll();
    }

    private void buildAll(){
        JPanel root=new JPanel(new BorderLayout(8,8)){protected void paintComponent(Graphics g){Theme.paintBg(g,getWidth(),getHeight());Theme.paintStars(g,getWidth(),getHeight(),new Random(55));super.paintComponent(g);}};
        root.setOpaque(false);root.setBorder(new EmptyBorder(10,10,10,10));
        JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);
        JLabel title=new JLabel("🎒 인벤토리",SwingConstants.LEFT);title.setForeground(Theme.GOLD);title.setFont(Theme.FONT_TITLE);
        goldLabel=new JLabel("💰 "+cd.getGold()+"G");goldLabel.setForeground(Theme.GOLD);goldLabel.setFont(Theme.FONT_HEADER);
        JButton back=mkBtn("🏠 마을로",Theme.BG_BUTTON);back.addActionListener(e->exit());
        JPanel tr=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));tr.setOpaque(false);tr.add(goldLabel);tr.add(back);
        top.add(title,BorderLayout.WEST);top.add(tr,BorderLayout.EAST);root.add(top,BorderLayout.NORTH);
        JPanel center=new JPanel(new GridLayout(1,2,12,0));center.setOpaque(false);
        center.add(buildEquipSlots());center.add(buildInventoryTabs());root.add(center,BorderLayout.CENTER);
        setContentPane(root);revalidate();repaint();
    }

    private JPanel buildEquipSlots(){
        JPanel p=new JPanel(new BorderLayout(0,8));p.setOpaque(false);
        JLabel t=new JLabel("🛡️ 장착 장비",SwingConstants.CENTER);t.setForeground(Theme.ACCENT);t.setFont(Theme.FONT_HEADER);p.add(t,BorderLayout.NORTH);
        JPanel slots=new JPanel(new GridLayout(5,1,0,8));slots.setOpaque(false);
        Item[]eq=cd.getEquipment();
        for(int i=0;i<5;i++){
            final int si=i;Item it=eq[i];
            JPanel slot=new JPanel(new BorderLayout(8,0)){protected void paintComponent(Graphics g){g.setColor(new Color(18,14,40));g.fillRoundRect(0,0,getWidth(),getHeight(),10,10);super.paintComponent(g);}};
            slot.setOpaque(false);slot.setBorder(new EmptyBorder(8,10,8,10));
            JLabel sn=new JLabel(SLOT_NAMES[i]);sn.setForeground(Theme.TEXT_DIM);sn.setFont(Theme.FONT_SMALL);sn.setPreferredSize(new Dimension(62,20));
            String disp=it==null?"─ 비어있음":"<html><font color='"+it.getRarityColor()+"'>"+it.getDisplayName()+"</font></html>";
            JLabel il=new JLabel(disp);il.setForeground(Theme.TEXT_MAIN);il.setFont(Theme.FONT_SMALL);
            JPanel infoCol=new JPanel(new BorderLayout(0,2));infoCol.setOpaque(false);infoCol.add(il,BorderLayout.NORTH);
            if(it!=null){JLabel sl=new JLabel("<html><font color='#7777bb'>"+it.getStatsText()+"</font></html>");sl.setFont(Theme.FONT_SMALL);infoCol.add(sl,BorderLayout.SOUTH);}
            JButton un=miniBtn("해제",new Color(80,25,25));un.setEnabled(it!=null);
            un.addActionListener(e->{if(cd.getEquipment()[si]!=null){cd.getInventory().add(cd.getEquipment()[si]);cd.getEquipment()[si]=null;cd.recalcStats();SaveSystem.saveUser(user);ImageGen.invalidateCharacterCache(cd.getCharClass().name());buildAll();}});
            slot.add(sn,BorderLayout.WEST);slot.add(infoCol,BorderLayout.CENTER);slot.add(un,BorderLayout.EAST);slots.add(slot);
        }
        p.add(slots,BorderLayout.CENTER);
        JPanel ss=new JPanel(){protected void paintComponent(Graphics g){g.setColor(new Color(12,9,28));g.fillRoundRect(0,0,getWidth(),getHeight(),10,10);super.paintComponent(g);}};
        ss.setOpaque(false);ss.setLayout(new GridLayout(2,3,4,4));ss.setBorder(new EmptyBorder(8,10,8,10));
        ss.add(mLbl("⚔️ ATK: "+cd.getAtk(),new Color(255,220,80)));ss.add(mLbl("🛡️ DEF: "+cd.getDef(),new Color(100,180,255)));ss.add(mLbl("💨 SPD: "+cd.getSpd(),new Color(100,255,150)));
        ss.add(mLbl("❤️ HP: "+cd.getMaxHp(),Theme.HP_RED));ss.add(mLbl("💧 MP: "+cd.getMaxMp(),new Color(80,160,255)));ss.add(mLbl("📦 "+cd.getInventory().size()+"/99",Theme.TEXT_DIM));
        p.add(ss,BorderLayout.SOUTH);return p;
    }

    private JPanel buildInventoryTabs(){
        JPanel p=new JPanel(new BorderLayout(0,6));p.setOpaque(false);
        JPanel tabs=new JPanel(new FlowLayout(FlowLayout.LEFT,3,0));tabs.setOpaque(false);
        String[]tn={"전체","🪖머리","🥋몸통","👖다리","⚔️무기","📿악세","🧪포션"};
        Item.ItemType[]types={null,Item.ItemType.ARMOR_HEAD,Item.ItemType.ARMOR_CHEST,Item.ItemType.ARMOR_LEGS,Item.ItemType.WEAPON,Item.ItemType.ACCESSORY,Item.ItemType.POTION};
        for(int i=0;i<tn.length;i++){final Item.ItemType ft=types[i];boolean active=(currentFilter==ft);
            JButton tb=tabBtn(tn[i]+"("+countType(ft)+")",active);tb.addActionListener(e->{currentFilter=ft;buildAll();});tabs.add(tb);}
        p.add(tabs,BorderLayout.NORTH);
        itemAreaPanel=new JPanel();itemAreaPanel.setLayout(new BoxLayout(itemAreaPanel,BoxLayout.Y_AXIS));itemAreaPanel.setBackground(new Color(10,8,28));
        fillItems();
        JScrollPane sc=new JScrollPane(itemAreaPanel);sc.setBorder(new LineBorder(new Color(40,30,70),1));sc.getViewport().setBackground(new Color(10,8,28));
        p.add(sc,BorderLayout.CENTER);return p;
    }

    private int countType(Item.ItemType t){if(t==null)return cd.getInventory().size();int n=0;for(Item it:cd.getInventory())if(it.getType()==t)n++;return n;}

    private void fillItems(){
        itemAreaPanel.removeAll();List<Item>inv=cd.getInventory();boolean any=false;
        for(int i=0;i<inv.size();i++){Item it=inv.get(i);if(currentFilter!=null&&it.getType()!=currentFilter)continue;any=true;itemAreaPanel.add(buildItemRow(it,i,i%2==0));}
        if(!any||inv.isEmpty()){JLabel e=new JLabel(inv.isEmpty()?"인벤토리 비어있음":"해당 카테고리 없음",SwingConstants.CENTER);e.setForeground(Theme.TEXT_DIM);e.setFont(Theme.FONT_BODY);e.setAlignmentX(0.5f);itemAreaPanel.add(Box.createVerticalStrut(20));itemAreaPanel.add(e);}
        itemAreaPanel.revalidate();itemAreaPanel.repaint();
    }

    private JPanel buildItemRow(Item item,int idx,boolean alt){
        JPanel row=new JPanel(new BorderLayout(6,0));row.setMaximumSize(new Dimension(Integer.MAX_VALUE,54));
        row.setBackground(alt?new Color(20,16,46):new Color(15,12,36));row.setBorder(new EmptyBorder(6,10,6,8));
        JPanel strip=new JPanel();strip.setBackground(Color.decode(item.getRarityColor()));strip.setPreferredSize(new Dimension(4,0));row.add(strip,BorderLayout.WEST);
        JPanel info=new JPanel(new GridLayout(2,1,0,2));info.setOpaque(false);
        JLabel nl=new JLabel("<html><font color='"+item.getRarityColor()+"'>["+item.getRarityName()+"]</font> <b>"+item.getDisplayName()+"</b></html>");nl.setForeground(Theme.TEXT_MAIN);nl.setFont(Theme.FONT_BODY);
        JLabel sl=new JLabel("<html><font color='#8888bb'>"+item.getStatsText()+"</font>  <font color='#ffcc00'>판매: "+item.getSellPrice()+"G</font></html>");sl.setFont(Theme.FONT_SMALL);
        info.add(nl);info.add(sl);row.add(info,BorderLayout.CENTER);
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,4,0));btns.setOpaque(false);
        if(item.getType()==Item.ItemType.POTION){JButton use=miniBtn("사용",new Color(25,100,45));use.addActionListener(e->usePotion(idx));btns.add(use);}
        else if(item.getType()!=Item.ItemType.GOLD_BAG){JButton eq=miniBtn("장착",new Color(35,55,120));eq.addActionListener(e->equip(idx));btns.add(eq);}
        JButton sell=miniBtn("판매",new Color(100,70,0));sell.addActionListener(e->sell(idx));btns.add(sell);
        row.add(btns,BorderLayout.EAST);return row;
    }

    private void equip(int idx){Item it=cd.getInventory().get(idx);int slot=typeToSlot(it.getType());if(slot<0){showMsg("장착 불가");return;}if(cd.getEquipment()[slot]!=null)cd.getInventory().add(cd.getEquipment()[slot]);cd.getEquipment()[slot]=it;cd.getInventory().remove(idx);cd.recalcStats();SaveSystem.saveUser(user);ImageGen.invalidateCharacterCache(cd.getCharClass().name());buildAll();}
    private void sell(int idx){Item it=cd.getInventory().get(idx);long p=it.getSellPrice();if(JOptionPane.showConfirmDialog(this,it.getDisplayName()+" → "+p+"G?","판매",JOptionPane.YES_NO_OPTION)==0){cd.getInventory().remove(idx);cd.addGold(p);goldLabel.setText("💰 "+cd.getGold()+"G");SaveSystem.saveUser(user);buildAll();}}
    private void usePotion(int idx){Item pot=cd.getInventory().get(idx);if(cd.getHp()>=cd.getMaxHp()){showMsg("HP가득!");return;}cd.getInventory().remove(idx);cd.heal(pot.getHealAmount());showMsg("💊 "+pot.getHealAmount()+" HP 회복!");SaveSystem.saveUser(user);buildAll();}
    private int typeToSlot(Item.ItemType t){switch(t){case ARMOR_HEAD:return 0;case ARMOR_CHEST:return 1;case ARMOR_LEGS:return 2;case WEAPON:return 3;case ACCESSORY:return 4;default:return -1;}}
    private void exit(){hub.refreshStats();hub.setVisible(true);dispose();}
    private JButton miniBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Color.WHITE);b.setFont(Theme.FONT_SMALL);b.setFocusPainted(false);b.setBorderPainted(false);b.setPreferredSize(new Dimension(58,28));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private JButton mkBtn(String t,Color c){JButton b=new JButton(t);b.setBackground(c);b.setForeground(Theme.TEXT_MAIN);b.setFont(Theme.FONT_BODY);b.setFocusPainted(false);b.setBorderPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private JButton tabBtn(String t,boolean active){JButton b=mkBtn(t,active?new Color(60,40,110):new Color(30,25,55));if(active)b.setForeground(Theme.GOLD);b.setFont(Theme.FONT_SMALL);b.setPreferredSize(new Dimension(82,26));return b;}
    private JLabel mLbl(String t,Color c){JLabel l=new JLabel(t);l.setForeground(c);l.setFont(Theme.FONT_SMALL);return l;}
    private void showMsg(String m){JOptionPane.showMessageDialog(this,m,"알림",JOptionPane.INFORMATION_MESSAGE);}
}