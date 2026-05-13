package game.inventory;
import java.io.Serializable;
public class Item implements Serializable {
    private static final long serialVersionUID = 3L;
    public enum Rarity {
        RARE("희귀","#4488FF",0.55), EPIC("에픽","#AA33CC",0.30),
        UNIQUE("유니크","#FF8800",0.12), LEGENDARY("전설","#FFD700",0.03);
        public final String kor,hex; public final double weight;
        Rarity(String k,String h,double w){kor=k;hex=h;weight=w;}
    }
    public enum ItemType { WEAPON,ARMOR_HEAD,ARMOR_CHEST,ARMOR_LEGS,ACCESSORY,POTION,GOLD_BAG }

    private String name,emoji; private Rarity rarity; private ItemType type;
    private int hpBonus,mpBonus,atkBonus,defBonus,spdBonus,healAmount,minLevel;
    private long goldValue;
    private int enhanceLevel=0; // 0~5

    public Item(String name,String emoji,ItemType type,Rarity rarity,int minLevel,
                int hp,int mp,int atk,int def,int spd,int heal,long gold){
        this.name=name;this.emoji=emoji;this.type=type;this.rarity=rarity;
        this.minLevel=minLevel;this.hpBonus=hp;this.mpBonus=mp;this.atkBonus=atk;
        this.defBonus=def;this.spdBonus=spd;this.healAmount=heal;this.goldValue=gold;
    }

    public int getEnhanceLevel(){return enhanceLevel;}
    public void setEnhanceLevel(int lv){enhanceLevel=Math.max(0,Math.min(5,lv));}
    public double getEnhanceSuccessRate(){return Math.max(0.0,1.0-enhanceLevel*0.20);}

    private int enh(int base){return enhanceLevel==0?base:base+(int)(base*enhanceLevel*0.2);}
    public int getEffectiveAtk(){return enh(atkBonus);}
    public int getEffectiveDef(){return enh(defBonus);}
    public int getEffectiveHp() {return enh(hpBonus);}
    public int getEffectiveMp() {return enh(mpBonus);}
    public int getEffectiveSpd(){return enh(spdBonus);}
    public String getEnhanceTag(){return enhanceLevel>0?" +"+enhanceLevel:"";}

    public long getSellPrice(){
        if(type==ItemType.GOLD_BAG)return goldValue;
        long b;
        switch(rarity){
            case RARE:b=Math.max(100,goldValue/3);break;case EPIC:b=Math.max(500,goldValue/2);break;
            case UNIQUE:b=Math.max(2000,(long)(goldValue*0.6));break;case LEGENDARY:b=Math.max(8000,(long)(goldValue*0.75));break;
            default:b=50;
        }
        return b+b*enhanceLevel/4;
    }
    public Rarity getNextRarity(){
        switch(rarity){case RARE:return Rarity.EPIC;case EPIC:return Rarity.UNIQUE;case UNIQUE:return Rarity.LEGENDARY;default:return Rarity.LEGENDARY;}
    }
    public Item copyWithRarity(Rarity r){
        return new Item(name,emoji,type,r,minLevel,(int)(hpBonus*1.4),(int)(mpBonus*1.4),(int)(atkBonus*1.4),(int)(defBonus*1.4),(int)(spdBonus*1.4),healAmount,(long)(goldValue*1.6));
    }
    public String getRarityColor(){return rarity.hex;}
    public String getRarityName(){return rarity.kor;}
    public String getDisplayName(){return emoji+" "+name+getEnhanceTag();}
    public String getStatsText(){
        if(type==ItemType.GOLD_BAG)return " "+goldValue+"G";
        if(type==ItemType.POTION)return " 회복:"+healAmount;
        StringBuilder sb=new StringBuilder();
        if(getEffectiveHp()>0)sb.append(" HP+").append(getEffectiveHp());
        if(getEffectiveMp()>0)sb.append(" MP+").append(getEffectiveMp());
        if(getEffectiveAtk()>0)sb.append(" ATK+").append(getEffectiveAtk());
        if(getEffectiveDef()>0)sb.append(" DEF+").append(getEffectiveDef());
        if(getEffectiveSpd()>0)sb.append(" SPD+").append(getEffectiveSpd());
        return sb.toString();
    }
    public String getName(){return name;} public String getEmoji(){return emoji;}
    public Rarity getRarity(){return rarity;} public ItemType getType(){return type;}
    public int getHpBonus(){return getEffectiveHp();} public int getMpBonus(){return getEffectiveMp();}
    public int getAtkBonus(){return getEffectiveAtk();} public int getDefBonus(){return getEffectiveDef();}
    public int getSpdBonus(){return getEffectiveSpd();} public int getHealAmount(){return healAmount;}
    public long getGoldValue(){return goldValue;} public int getMinLevel(){return minLevel;}
}