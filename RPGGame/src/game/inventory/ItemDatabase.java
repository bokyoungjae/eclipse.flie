package game.inventory;

import java.util.*;
import static game.inventory.Item.ItemType.*;
import static game.inventory.Item.Rarity.*;

public class ItemDatabase {
    private static final Random RNG = new Random();
    private static final List<Item> DROP_POOL  = new ArrayList<>();
    private static final List<Item> SHOP_POOL  = new ArrayList<>();

    private static void drop(String n,String e,Item.ItemType t,Item.Rarity r,int lv,int hp,int mp,int atk,int def,int spd,int heal,long g){
        DROP_POOL.add(new Item(n,e,t,r,lv,hp,mp,atk,def,spd,heal,g));
    }
    private static void shop(String n,String e,Item.ItemType t,Item.Rarity r,int lv,int hp,int mp,int atk,int def,int spd,int heal,long g){
        SHOP_POOL.add(new Item(n,e,t,r,lv,hp,mp,atk,def,spd,heal,g));
    }

    static {
        drop("강철 단검",    "🗡️",WEAPON, RARE,      1, 0,  0, 18, 0, 2, 0,  300);
        drop("마법사의 지팡이","✨",WEAPON, RARE,      5, 0, 30, 22, 0, 0, 0,  500);
        drop("장궁",         "🏹",WEAPON, RARE,      8, 0,  0, 20, 0, 4, 0,  450);
        drop("성검",         "⚔️",WEAPON, EPIC,     15, 0,  0, 35, 3, 0, 0, 1200);
        drop("화염 지팡이",  "🔥",WEAPON, EPIC,     18, 0, 50, 42, 0, 0, 0, 1500);
        drop("독 단검",      "☠️",WEAPON, EPIC,     20, 0,  0, 40, 0, 6, 0, 1400);
        drop("용살자의 검",  "🐉",WEAPON, UNIQUE,   35, 0,  0, 65, 5, 3, 0, 4000);
        drop("번개 지팡이",  "⚡",WEAPON, UNIQUE,   40, 0, 80, 72, 0, 0, 0, 5000);
        drop("신궁의 활",    "🌟",WEAPON, UNIQUE,   38, 0,  0, 62, 0, 8, 0, 4500);
        drop("심판의 검",    "✝️",WEAPON, LEGENDARY,60, 0,  0,110, 8, 5, 0,12000);
        drop("멸망의 지팡이","💀",WEAPON, LEGENDARY,65, 0,120,125, 0, 0, 0,15000);
        drop("철 투구",      "🪖",ARMOR_HEAD, RARE,   1, 40, 0,  0,  6, 0, 0,  250);
        drop("마법사 모자",  "🎩",ARMOR_HEAD, RARE,   5, 20,40,  0,  4, 0, 0,  400);
        drop("가죽 두건",    "🧢",ARMOR_HEAD, RARE,   3, 30, 0,  0,  4, 2, 0,  300);
        drop("강철 투구",    "⛑️",ARMOR_HEAD, EPIC,  12, 80, 0,  0, 12, 0, 0,  900);
        drop("수정 왕관",    "👑",ARMOR_HEAD, EPIC,  18, 50,80,  0,  8, 0, 0, 1300);
        drop("그림자 두건",  "🌑",ARMOR_HEAD, EPIC,  15, 60, 0,  5, 10, 3, 0, 1100);
        drop("용기사 투구",  "🐲",ARMOR_HEAD, UNIQUE,32,160, 0,  0, 22, 0, 0, 4000);
        drop("대마법사 관",  "🔮",ARMOR_HEAD, UNIQUE,38,100,180, 0, 15, 0, 0, 5500);
        drop("어둠의 두건",  "🌙",ARMOR_HEAD, LEGENDARY,58,240, 0, 12, 30, 5, 0,13000);
        drop("가죽 갑옷",    "👕",ARMOR_CHEST, RARE,   1, 60, 0,  0,  8, 0, 0,  350);
        drop("천 로브",      "🥻",ARMOR_CHEST, RARE,   5, 30,60,  0,  5, 0, 0,  450);
        drop("사슬 갑옷",    "🧥",ARMOR_CHEST, RARE,   8, 80, 0,  0, 12, 0, 0,  500);
        drop("플레이트 갑옷","🥋",ARMOR_CHEST, EPIC,  14,150, 0,  0, 20, 0, 0, 1400);
        drop("마법 로브",    "✨",ARMOR_CHEST, EPIC,  18, 80,120, 0, 12, 0, 0, 1800);
        drop("그림자 갑옷",  "🌑",ARMOR_CHEST, EPIC,  16,120, 0, 10, 16, 2, 0, 1600);
        drop("용비늘 갑옷",  "🐉",ARMOR_CHEST, UNIQUE,35,280, 0,  0, 35, 0, 0, 6000);
        drop("신성 로브",    "☀️",ARMOR_CHEST, UNIQUE,42,180,200, 0, 25, 0, 0, 7000);
        drop("멸망의 갑옷",  "💀",ARMOR_CHEST, LEGENDARY,62,420, 0, 20, 55, 0, 0,18000);
        drop("가죽 바지",    "👖",ARMOR_LEGS, RARE,   1, 30, 0,  0,  5, 0, 0,  200);
        drop("철 각반",      "🦺",ARMOR_LEGS, RARE,   6, 55, 0,  0,  8, 0, 0,  350);
        drop("마법 하의",    "🩱",ARMOR_LEGS, RARE,   8, 25,40,  0,  4, 1, 0,  320);
        drop("강철 각반",    "⚙️",ARMOR_LEGS, EPIC,  14, 90, 0,  0, 14, 0, 0,  900);
        drop("폭풍 하의",    "🌀",ARMOR_LEGS, EPIC,  20, 60,80,  0, 10, 3, 0, 1200);
        drop("암흑 각반",    "🌑",ARMOR_LEGS, UNIQUE,34,170, 0,  6, 25, 2, 0, 3800);
        drop("천공 하의",    "🌤️",ARMOR_LEGS, LEGENDARY,55,260,100, 8, 38, 5, 0,12000);
        drop("구리 반지",    "💍",ACCESSORY, RARE,   1, 20, 0,  5,  2, 1, 0,  180);
        drop("마나 목걸이",  "📿",ACCESSORY, RARE,   5, 10,50,  0,  2, 0, 0,  350);
        drop("날카로운 팔찌","🔗",ACCESSORY, RARE,   8,  0, 0, 12,  0, 2, 0,  400);
        drop("용사의 반지",  "⭐",ACCESSORY, EPIC,  14, 50,50, 18,  6, 3, 0, 1500);
        drop("폭발의 목걸이","💥",ACCESSORY, EPIC,  20,  0,80, 25,  0, 0, 0, 1800);
        drop("그림자 팔찌",  "🌑",ACCESSORY, UNIQUE,32, 80,80, 35, 10, 6, 0, 4500);
        drop("신의 반지",    "🌟",ACCESSORY, LEGENDARY,60,150,150, 60, 20,10, 0,20000);
        drop("소형 포션",    "🧪",POTION, RARE,       1, 0,0,0,0,0, 100, 30);
        drop("중형 포션",    "⚗️",POTION, RARE,      15, 0,0,0,0,0, 280, 80);
        drop("대형 포션",    "🔮",POTION, EPIC,      30, 0,0,0,0,0, 600,200);
        drop("마나 포션",    "💧",POTION, RARE,       1, 0,0,0,0,0,   0, 50);
        drop("신성한 포션",  "💊",POTION, UNIQUE,    50, 0,0,0,0,0,1500,800);
        shop("불꽃의 검",    "🔥",WEAPON, EPIC,     25,  0,  0, 48,  4, 0, 0, 3500);
        shop("얼음 지팡이",  "❄️",WEAPON, EPIC,     28,  0, 70, 55,  0, 0, 0, 4200);
        shop("폭풍의 활",    "🌪️",WEAPON, EPIC,    30,  0,  0, 50,  0, 8, 0, 4000);
        shop("천벌의 검",    "⚡",WEAPON, UNIQUE,   45,  0,  0, 80,  6, 4, 0, 9000);
        shop("심연의 지팡이","🌑",WEAPON, UNIQUE,   48,  0,100, 88,  0, 0, 0,11000);
        shop("신의 활",      "🌟",WEAPON, UNIQUE,   50,  0,  0, 85,  0,12, 0,10000);
        shop("파멸의 검",    "💀",WEAPON, LEGENDARY,72,  0,  0,130, 10, 8, 0,25000);
        shop("창조의 지팡이","✨",WEAPON, LEGENDARY,75,  0,150,145,  0, 0, 0,30000);
        shop("투사의 투구",  "⚔️",ARMOR_HEAD, EPIC,   20,100,  0,  8, 14, 0, 0, 2000);
        shop("봉황 왕관",    "🦅",ARMOR_HEAD, UNIQUE, 42,220,120,  0, 28, 0, 0, 8000);
        shop("신성 투구",    "☀️",ARMOR_HEAD, LEGENDARY,68,350,  0,  0, 45, 0, 0,20000);
        shop("마력 갑옷",    "💜",ARMOR_CHEST, EPIC,  22,160, 80, 0, 22, 0, 0, 3500);
        shop("천사 로브",    "👼",ARMOR_CHEST, UNIQUE,45,300,250,  0, 35, 0, 0,12000);
        shop("용황 갑옷",    "🐲",ARMOR_CHEST, LEGENDARY,70,550,  0, 25, 70, 0, 0,35000);
        shop("기사 각반",    "🛡️",ARMOR_LEGS, EPIC,   18,110,  0,  0, 18, 0, 0, 2200);
        shop("폭풍 각반",    "⚡",ARMOR_LEGS, UNIQUE, 40,200, 80,  0, 30, 5, 0, 7500);
        shop("전설 하의",    "🌈",ARMOR_LEGS, LEGENDARY,68,320,100, 10, 48, 8, 0,22000);
        shop("마력 반지",    "💎",ACCESSORY, EPIC,    22, 60, 80, 20,  8, 4, 0, 3800);
        shop("용심 목걸이",  "🐉",ACCESSORY, UNIQUE,  40,120,100, 45, 15, 8, 0,11000);
        shop("불멸의 반지",  "♾️",ACCESSORY, LEGENDARY,72,200,200, 80, 30,15, 0,40000);
        shop("만능 포션",    "🌈",POTION, UNIQUE,    1,  0,0,0,0,0,3000,2000);
        shop("엘릭서",       "⭐",POTION, LEGENDARY, 1,  0,0,0,0,0,9999,8000);
        shop("마나 엘릭서",  "💠",POTION, UNIQUE,    1,  0,0,0,0,0,   0,1500);
    }

    public static Item rollDrop(int playerLevel) {
        if (RNG.nextDouble() > 0.20) return null;
        List<Item> eligible = new ArrayList<>();
        for (Item it : DROP_POOL) {
            if (it.getType() != Item.ItemType.POTION && it.getMinLevel() <= playerLevel) eligible.add(it);
            else if (it.getType() == Item.ItemType.POTION) eligible.add(it);
        }
        if (eligible.isEmpty()) return null;
        double total = 0;
        double[] w = new double[eligible.size()];
        for (int i = 0; i < eligible.size(); i++) {
            Item.Rarity r = eligible.get(i).getRarity();
            double base = r.weight;
            if (r == UNIQUE)    base = playerLevel >= 35 ? 0.12 : 0.03;
            if (r == LEGENDARY) base = playerLevel >= 60 ? 0.03 : 0.005;
            w[i] = base; total += base;
        }
        double roll = RNG.nextDouble() * total, cum = 0;
        for (int i = 0; i < eligible.size(); i++) {
            cum += w[i];
            if (roll < cum) return eligible.get(i);
        }
        return eligible.get(0);
    }

    public static long rollGold(int playerLevel, boolean isBoss) {
        int base = playerLevel * 2 + RNG.nextInt(playerLevel * 2 + 5);
        return isBoss ? base * 4 : base;
    }

    /** 합성용: 특정 등급의 랜덤 아이템 반환 */
    public static Item rollSynthesized(Item.Rarity rarity, int playerLevel) {
        List<Item> pool = new ArrayList<>();
        for (Item it : DROP_POOL)
            if (it.getRarity()==rarity && it.getType()!=Item.ItemType.POTION && it.getMinLevel()<=playerLevel+5) pool.add(it);
        for (Item it : SHOP_POOL)
            if (it.getRarity()==rarity && it.getType()!=Item.ItemType.POTION) pool.add(it);
        if (pool.isEmpty())
            for (Item it : DROP_POOL)
                if (it.getRarity()==rarity && it.getType()!=Item.ItemType.POTION) pool.add(it);
        if (pool.isEmpty()) return DROP_POOL.get(0);
        return pool.get(RNG.nextInt(pool.size()));
    }

    // ★ getShopItems는 딱 한 번만 선언
    public static List<Item> getShopItems() { return new ArrayList<>(SHOP_POOL); }
}