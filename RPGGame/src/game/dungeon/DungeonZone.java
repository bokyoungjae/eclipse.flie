package game.dungeon;
import java.awt.Color;
public class DungeonZone {
    public final String name, bgType, desc1, desc2;
    public final int minLevel, maxLevel, tier;
    public final double expMult, dropMult, goldMult;
    public final Color accentColor;
    public DungeonZone(String name,String bgType,int minLv,int maxLv,double expM,double dropM,double goldM,Color accent,String d1,String d2,int tier){
        this.name=name;this.bgType=bgType;this.minLevel=minLv;this.maxLevel=maxLv;
        this.expMult=expM;this.dropMult=dropM;this.goldMult=goldM;this.accentColor=accent;this.desc1=d1;this.desc2=d2;this.tier=tier;
    }
    public boolean isAvailable(int playerLevel){return playerLevel>=minLevel;}
    public static final DungeonZone[] ALL={
        new DungeonZone("🌿 초원 동굴","forest",1,10,1.0,1.0,1.0,new Color(60,140,60),"Lv.1~10 권장","슬라임, 고블린",0),
        new DungeonZone("🦇 박쥐 동굴","cave",8,20,1.2,1.1,1.1,new Color(80,60,120),"Lv.8~20 권장","박쥐, 해골",1),
        new DungeonZone("💀 해골 무덤","castle",18,35,1.5,1.3,1.3,new Color(100,100,130),"Lv.18~35 권장","해골, 오크",2),
        new DungeonZone("🌲 저주받은 숲","forest",30,50,1.8,1.5,1.5,new Color(30,100,40),"Lv.30~50 권장","오크, 트롤",3),
        new DungeonZone("🏰 흑마성","castle",45,65,2.2,1.8,1.8,new Color(80,20,80),"Lv.45~65 권장","트롤, 드래곤",4),
        new DungeonZone("🔥 화염 심연","abyss",60,80,2.8,2.2,2.2,new Color(160,50,0),"Lv.60~80 권장","드래곤, 데몬",5),
        new DungeonZone("🌑 심연의 틈","abyss",75,90,3.5,2.8,2.8,new Color(60,0,100),"Lv.75~90 권장","데몬, 리치",6),
        new DungeonZone("☠️ 리치의 성채","castle",85,99,4.5,3.5,3.5,new Color(120,0,200),"Lv.85~99 권장","리치, 보스",7),
    };
}