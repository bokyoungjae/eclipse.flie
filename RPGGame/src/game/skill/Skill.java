package game.skill;
import game.data.CharacterData.CharacterClass;
import java.awt.Color;
import java.io.Serializable;
import java.util.*;

public class Skill implements Serializable {
    private static final long serialVersionUID = 2L;
    public enum SkillType { ATTACK,AOE,HEAL,SHIELD,DRAIN,DOT,ULTIMATE }

    public final String id,name,emoji,desc;
    public final int unlockLevel,manaCost;
    public final double dmgMult,baseDmgMult,dmgPerLevel;
    public final int healMult;
    public final SkillType type;
    public final Color color;
    public final CharacterClass owner;
    public final int maxSkillLevel;

    public Skill(String id,String name,String emoji,String desc,int unlockLv,int mana,
                 double baseDmg,double dmgPerLv,int heal,SkillType type,Color color,CharacterClass owner){
        this.id=id;this.name=name;this.emoji=emoji;this.desc=desc;
        this.unlockLevel=unlockLv;this.manaCost=mana;
        this.baseDmgMult=baseDmg;this.dmgMult=baseDmg;this.dmgPerLevel=dmgPerLv;
        this.healMult=heal;this.type=type;this.color=color;this.owner=owner;
        this.maxSkillLevel=(type==SkillType.ULTIMATE)?3:5;
    }
    public double getDmgMult(int lv){return lv<=0?baseDmgMult:baseDmgMult+dmgPerLevel*(lv-1);}
    public boolean canUnlock(int playerLevel){return playerLevel>=unlockLevel;}
    public boolean isUnlocked(int level){return level>=unlockLevel;}
    public boolean isUsable(int playerLevel,int skillLv){return canUnlock(playerLevel)&&skillLv>0;}

    public static final Skill[][] ALL=new Skill[5][];
    static{
        ALL[0]=new Skill[]{
            new Skill("w0","일반 공격","⚔️","기본 공격.",1,0,1.0,0.15,0,SkillType.ATTACK,new Color(180,50,50),CharacterClass.WARRIOR),
            new Skill("w1","방패 강타","🛡️","방패로 강타.",10,20,1.8,0.20,0,SkillType.ATTACK,new Color(200,120,40),CharacterClass.WARRIOR),
            new Skill("w2","선풍검","🌀","연속 참격.",30,45,2.5,0.25,0,SkillType.ATTACK,new Color(120,160,255),CharacterClass.WARRIOR),
            new Skill("w3","불굴의 의지","🔥","궁극기+HP회복.",60,90,4.5,0.50,20,SkillType.ULTIMATE,new Color(255,80,0),CharacterClass.WARRIOR),
        };
        ALL[1]=new Skill[]{
            new Skill("m0","마법 화살","✨","마법 기본공격.",1,0,1.0,0.15,0,SkillType.ATTACK,new Color(100,100,220),CharacterClass.MAGE),
            new Skill("m1","파이어볼","🔥","화염 피해.",10,25,2.0,0.25,0,SkillType.ATTACK,new Color(255,100,20),CharacterClass.MAGE),
            new Skill("m2","번개 폭풍","⚡","번개 광역.",30,50,2.8,0.30,0,SkillType.AOE,new Color(200,200,50),CharacterClass.MAGE),
            new Skill("m3","메테오","☄️","운석 낙하.",60,100,5.5,0.60,0,SkillType.ULTIMATE,new Color(255,50,50),CharacterClass.MAGE),
        };
        ALL[2]=new Skill[]{
            new Skill("a0","조준 사격","🏹","정밀 사격.",1,0,1.0,0.15,0,SkillType.ATTACK,new Color(80,180,80),CharacterClass.ARCHER),
            new Skill("a1","관통 화살","🎯","방어 무시.",10,22,1.9,0.22,0,SkillType.ATTACK,new Color(50,220,100),CharacterClass.ARCHER),
            new Skill("a2","화살 폭풍","🌪️","연속 화살.",30,48,2.7,0.28,0,SkillType.AOE,new Color(180,255,100),CharacterClass.ARCHER),
            new Skill("a3","신의 화살","💫","신화 화살+회복.",60,85,5.0,0.55,15,SkillType.ULTIMATE,new Color(100,255,200),CharacterClass.ARCHER),
        };
        ALL[3]=new Skill[]{
            new Skill("p0","성스러운 타격","🔨","신성 망치.",1,0,1.0,0.15,0,SkillType.ATTACK,new Color(220,200,80),CharacterClass.PALADIN),
            new Skill("p1","신성한 빛","✝️","피해+회복.",10,30,2.0,0.20,10,SkillType.DRAIN,new Color(255,240,100),CharacterClass.PALADIN),
            new Skill("p2","심판의 철퇴","⚖️","강타+회복.",30,55,2.6,0.28,15,SkillType.DRAIN,new Color(255,180,50),CharacterClass.PALADIN),
            new Skill("p3","천사의 강림","👼","극딜+대량회복.",60,95,4.0,0.45,40,SkillType.ULTIMATE,new Color(255,255,200),CharacterClass.PALADIN),
        };
        ALL[4]=new Skill[]{
            new Skill("as0","그림자 베기","🗡️","암습.",1,0,1.0,0.18,0,SkillType.ATTACK,new Color(150,0,200),CharacterClass.ASSASSIN),
            new Skill("as1","독 단검","☠️","독 피해.",10,20,2.2,0.25,0,SkillType.DOT,new Color(100,200,0),CharacterClass.ASSASSIN),
            new Skill("as2","연속 찌르기","💨","3회 연속.",30,42,3.0,0.30,0,SkillType.ATTACK,new Color(200,100,255),CharacterClass.ASSASSIN),
            new Skill("as3","사신의 낫","💀","확정 극딜.",60,90,6.0,0.70,0,SkillType.ULTIMATE,new Color(60,0,80),CharacterClass.ASSASSIN),
        };
    }

    public static Skill[] getUnlocked(CharacterClass cc,int level){
        Skill[]all=ALL[cc.ordinal()];int n=0;
        for(Skill s:all)if(s.canUnlock(level))n++;
        Skill[]r=new Skill[n];int i=0;
        for(Skill s:all)if(s.canUnlock(level))r[i++]=s;
        return r;
    }
    public static Skill getNextUnlock(CharacterClass cc,int level){
        for(Skill s:ALL[cc.ordinal()])if(!s.canUnlock(level))return s;
        return null;
    }
    public static Skill[] getUsable(CharacterClass cc,int playerLevel,Map<String,Integer> skillLevels){
        Skill[]all=ALL[cc.ordinal()];int n=0;
        for(Skill s:all){int lv=skillLevels.getOrDefault(s.id,0);if(s.isUsable(playerLevel,lv))n++;}
        Skill[]r=new Skill[n];int i=0;
        for(Skill s:all){int lv=skillLevels.getOrDefault(s.id,0);if(s.isUsable(playerLevel,lv))r[i++]=s;}
        return r;
    }
    public static Skill getNextUnlock(CharacterClass cc,int playerLevel,Map<String,Integer> skillLevels){
        for(Skill s:ALL[cc.ordinal()]){
            int lv=skillLevels.getOrDefault(s.id,0);
            if(s.canUnlock(playerLevel)&&lv==0)return s;
            if(!s.canUnlock(playerLevel))return s;
        }
        return null;
    }
}