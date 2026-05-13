package game.data;
import game.inventory.Item;
import game.skill.Skill;
import java.io.Serializable;
import java.util.*;

public class CharacterData implements Serializable {
    private static final long serialVersionUID = 5L;

    public enum CharacterClass {
        WARRIOR("전사","⚔️",160,12,6,8,60,"STR","힘이 주 능력치"),
        MAGE("마법사","🔮",80,22,2,12,110,"INT","지력이 주 능력치"),
        ARCHER("궁수","🏹",100,16,4,10,80,"DEX","민첩이 주 능력치"),
        PALADIN("성기사","🛡️",140,13,9,7,70,"STR","힘이 주 능력치"),
        ASSASSIN("암살자","🗡️",90,20,3,15,75,"DEX","민첩이 주 능력치");
        public final String name,emoji,mainStat,statDesc;
        public final int baseHp,baseAtk,baseDef,baseSpd,baseMana;
        CharacterClass(String name,String emoji,int hp,int atk,int def,int spd,int mana,String ms,String sd){
            this.name=name;this.emoji=emoji;this.baseHp=hp;this.baseAtk=atk;this.baseDef=def;this.baseSpd=spd;this.baseMana=mana;this.mainStat=ms;this.statDesc=sd;
        }
    }

    private CharacterClass charClass;
    private int level=1; private long exp=0;
    private int hp,maxHp,mp,maxMp,atk,def,spd; private long gold=0;
    private int statStr=4,statDex=4,statInt=4,statLuk=4,statPoints=0;
    private int skillPoints=0;
    private Map<String,Integer> skillLevels=new HashMap<>();
    private Item[] equipment=new Item[5];
    private List<Item> inventory=new ArrayList<>();
    private int deathPenaltyTurns=0; private double penaltyAtkMult=1.0,penaltyDefMult=1.0;
    private int towerStage=1;

    public CharacterData(CharacterClass cc){
        this.charClass=cc;
        switch(cc){
            case WARRIOR:statStr=10;statDex=4;statInt=2;statLuk=4;break;
            case MAGE:statStr=2;statDex=4;statInt=12;statLuk=2;break;
            case ARCHER:statStr=4;statDex=10;statInt=4;statLuk=6;break;
            case PALADIN:statStr=8;statDex=2;statInt=2;statLuk=4;break;
            case ASSASSIN:statStr=4;statDex=12;statInt=2;statLuk=6;break;
        }
        if(skillLevels==null)skillLevels=new HashMap<>();
        Skill[] all=Skill.ALL[cc.ordinal()];
        if(all!=null&&all.length>0)skillLevels.put(all[0].id,1);
        recalcStats(); hp=maxHp; mp=maxMp;
    }

    private Object readResolve(){
        if(skillLevels==null){skillLevels=new HashMap<>();if(charClass!=null){Skill[]a=Skill.ALL[charClass.ordinal()];if(a!=null&&a.length>0)skillLevels.put(a[0].id,1);}}
        if(inventory==null)inventory=new ArrayList<>();
        if(equipment==null)equipment=new Item[5];
        return this;
    }

    public void recalcStats(){
        int lv=level-1;
        int bHp=charClass.baseHp+lv*14,bMp=charClass.baseMana+lv*4;
        int bAtk=charClass.baseAtk+lv*3,bDef=charClass.baseDef+lv*2,bSpd=charClass.baseSpd+lv;
        maxHp=bHp+statStr*10; maxMp=bMp+statInt*15;
        atk=bAtk+statStr*2+statDex; def=bDef+statStr/2; spd=bSpd+statDex;
        if(charClass==CharacterClass.MAGE)atk=charClass.baseAtk+lv*3+statInt*3;
        for(Item it:equipment){if(it!=null){maxHp+=it.getHpBonus();maxMp+=it.getMpBonus();atk+=it.getAtkBonus();def+=it.getDefBonus();spd+=it.getSpdBonus();}}
        if(hp>maxHp)hp=maxHp; if(mp>maxMp)mp=maxMp;
    }

    /** ★ 초반(1~30) 쉽게 레벨업 */
    public long expNeeded(){
        if(level<=10)  return (long)(40*Math.pow(1.2,level-1));
        if(level<=30)  return (long)(100*Math.pow(1.25,level-10));
        return (long)(500*Math.pow(1.35,level-30));
    }

    public boolean addExp(long gained){
        exp+=gained;
        if(level<99&&exp>=expNeeded()){
            exp-=expNeeded(); level++; statPoints+=5; skillPoints+=1;
            recalcStats(); hp=maxHp; mp=maxMp; return true;
        }
        return false;
    }

    public boolean spendStat(String stat){
        if(statPoints<=0)return false;
        switch(stat){case"STR":statStr++;break;case"DEX":statDex++;break;case"INT":statInt++;break;case"LUK":statLuk++;break;default:return false;}
        statPoints--;recalcStats();return true;
    }

    public boolean refundStat(String stat){
        if(statPoints<=0)return false; // 리펀드는 별도 로직 - 여기서는 단순히 감소
        switch(stat){
            case"STR":if(statStr<=1)return false;statStr--;break;
            case"DEX":if(statDex<=1)return false;statDex--;break;
            case"INT":if(statInt<=1)return false;statInt--;break;
            case"LUK":if(statLuk<=1)return false;statLuk--;break;
            default:return false;
        }
        statPoints++;recalcStats();return true;
    }

    public boolean investSkill(String sid){
        if(skillLevels==null)skillLevels=new HashMap<>();
        if(skillPoints<=0)return false;
        Skill t=null;for(Skill s:Skill.ALL[charClass.ordinal()])if(s.id.equals(sid)){t=s;break;}
        if(t==null||!t.canUnlock(level))return false;
        int cur=skillLevels.getOrDefault(sid,0);
        if(cur>=t.maxSkillLevel)return false;
        skillLevels.put(sid,cur+1);skillPoints--;return true;
    }

    public boolean refundSkill(String sid){
        if(skillLevels==null)return false;
        int cur=skillLevels.getOrDefault(sid,0);
        if(cur<=0)return false;
        skillLevels.put(sid,cur-1);skillPoints++;return true;
    }

    public int getSkillLevel(String sid){return skillLevels==null?0:skillLevels.getOrDefault(sid,0);}
    public Map<String,Integer> getSkillLevels(){return skillLevels!=null?skillLevels:new HashMap<>();}

    public void fullHeal(){hp=maxHp;mp=maxMp;}
    public void heal(int a){hp=Math.min(maxHp,hp+a);}
    public void restoreMp(int a){mp=Math.min(maxMp,mp+a);}
    public boolean spendMana(int c){if(mp<c)return false;mp-=c;return true;}
    public boolean isAlive(){return hp>0;}
    public int takeDamage(int raw){int d=Math.max(1,raw-(int)(def*penaltyDefMult));hp=Math.max(0,hp-d);return d;}
    public int getEffectiveAtk(){return(int)(atk*penaltyAtkMult);}

    public DeathPenalty applyDeathPenalty(){
        long gl=(long)(gold*0.10),el=(long)(exp*0.05);
        gold=Math.max(0,gold-gl);exp=Math.max(0,exp-el);
        deathPenaltyTurns=5;penaltyAtkMult=0.70;penaltyDefMult=0.70;fullHeal();
        return new DeathPenalty(gl,el);
    }
    public void tickPenalty(){if(deathPenaltyTurns>0){deathPenaltyTurns--;if(deathPenaltyTurns==0){penaltyAtkMult=1.0;penaltyDefMult=1.0;}}}

    public String getEquipmentVisualKey(){StringBuilder sb=new StringBuilder(charClass.name());for(int i=0;i<equipment.length;i++){Item it=equipment[i];if(it!=null)sb.append("|").append(i).append(":").append(it.getRarity().name().toLowerCase());}return sb.toString();}
    public int getEquipTier(){int max=0;for(Item it:equipment){if(it!=null){int t=it.getRarity().ordinal();if(t>max)max=t;}}return max;}

    public CharacterClass getCharClass(){return charClass;}
    public int getLevel(){return level;} public long getExp(){return exp;}
    public int getHp(){return hp;} public int getMaxHp(){return maxHp;}
    public int getMp(){return mp;} public int getMaxMp(){return maxMp;}
    public int getAtk(){return atk;} public int getDef(){return def;} public int getSpd(){return spd;}
    public long getGold(){return gold;} public void addGold(long g){gold+=g;}
    public boolean spendGold(long g){if(gold>=g){gold-=g;return true;}return false;}
    public int getTowerStage(){return towerStage;} public void setTowerStage(int s){towerStage=s;}
    public List<Item> getInventory(){return inventory;} public Item[] getEquipment(){return equipment;}
    public void addItem(Item i){inventory.add(i);}
    public double getExpPercent(){return(double)exp/expNeeded();}
    public double getPenaltyAtkMult(){return penaltyAtkMult;}
    public boolean hasPenalty(){return deathPenaltyTurns>0;}
    public int getPenaltyTurns(){return deathPenaltyTurns;}
    public int getStatStr(){return statStr;} public int getStatDex(){return statDex;}
    public int getStatInt(){return statInt;} public int getStatLuk(){return statLuk;}
    public int getStatPoints(){return statPoints;}
    public int getSkillPoints(){return skillPoints;}

    public static class DeathPenalty implements Serializable{
        private static final long serialVersionUID=1L;
        public final long goldLost,expLost;
        DeathPenalty(long g,long e){goldLost=g;expLost=e;}
    }
}