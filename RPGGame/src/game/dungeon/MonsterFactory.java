package game.dungeon;

import java.util.Random;

public class MonsterFactory {
    private static final Random rng = new Random();

    // HP배율, ATK배율, DEF배율
    private static final Object[][] POOL = {
        {"슬라임",      "slime",    1.0, 1.0, 0.8},
        {"고블린",      "goblin",   1.1, 1.1, 0.9},
        {"박쥐",        "bat",      0.9, 1.2, 0.7},
        {"해골 전사",   "skeleton", 1.2, 1.2, 1.0},
        {"오크",        "orc",      1.4, 1.3, 1.1},
        {"트롤",        "troll",    1.6, 1.2, 1.3},
        {"드래곤",      "dragon",   1.8, 1.6, 1.4},
        {"데몬",        "demon",    2.0, 1.8, 1.5},
        {"리치",        "lich",     2.2, 2.0, 1.6},
        {"어비스 군주", "boss",     3.0, 2.5, 2.0},
    };

    public static Monster createZoneMonster(DungeonZone zone, int playerLevel) {
        int tierMin = zone.tier;
        int tierMax = Math.min(zone.tier + 1, POOL.length - 1);
        int tier = tierMin + rng.nextInt(tierMax - tierMin + 1);

        Object[] m = POOL[tier];
        String name   = (String) m[0];
        String imgKey = (String) m[1];
        double hpM    = (double) m[2];
        double atkM   = (double) m[3];
        double defM   = (double) m[4];

        // 일반 던전 성장 곡선
        double scale = 1.0 + (playerLevel - 1) * 0.16;

        int hp  = (int)(55 * scale * hpM)  + rng.nextInt(18);
        int atk = (int)(12 * scale * atkM) + rng.nextInt(5);
        int def = (int)(4  * scale * defM);
        int exp = (int)(22 * scale * zone.expMult);
        long gold = (long)(6 * scale * zone.goldMult) + rng.nextInt(8);

        boolean isElite = rng.nextDouble() < 0.18;
        if (isElite) {
            return new Monster("강화된 " + name, imgKey + "_e",
                    (int)(hp * 2.2), (int)(atk * 1.4), (int)(def * 1.3),
                    (int)(exp * 2.2), gold * 3, Monster.MonsterType.ELITE);
        }
        return new Monster(name, imgKey, hp, atk, def, exp, gold, Monster.MonsterType.NORMAL);
    }

    /**
     * ★ 무한의 탑 난이도 리밸런싱 패치 ★
     * 10층 이상부터 난이도가 체감되도록 지수 성장 공식 도입
     */
    public static Monster createTowerMonster(int stage) {
        boolean boss = (stage % 5 == 0);
        
        // [난이도 핵심] 10층 단위로 기본 능력치가 1.4배씩 곱연산으로 증가 (지수 성장)
        double difficultyJump = Math.pow(1.4, (stage - 1) / 10); 
        // 층당 선형 성장률도 기존 0.13에서 0.18로 상향
        double linearScale = 1.0 + (stage - 1) * 0.18;
        double totalScale = linearScale * difficultyJump;

        // 층수에 따른 몬스터 외형 결정 (10층마다 다음 티어)
        int tier = Math.min((stage - 1) / 10, POOL.length - 1);
        Object[] m = POOL[tier];
        String name   = (String) m[0];
        String imgKey = (String) m[1];
        double hpM    = (double) m[2];
        double atkM   = (double) m[3];
        double defM   = (double) m[4];

        // 기본 베이스 스탯 상향 (초반부터 긴장감 유지)
        int hp  = (int)(100 * totalScale * hpM);
        int atk = (int)(18  * totalScale * atkM);
        int def = (int)(6   * totalScale * defM);
        
        // 보상은 확실하게 (층수가 높을수록 기하급수적으로 증가)
        int exp = (int)(50 * totalScale);
        long gold = (long)(30 * totalScale);

        if (boss) {
            // 5층 단위 보스는 10층 단위 점프와 겹치면 매우 강력해짐
            String bossTitle = (stage % 10 == 0) ? "[층주] " : "[수호자] ";
            return new Monster(bossTitle + name, imgKey + "_e",
                    (int)(hp * 3.5), (int)(atk * 1.6), (int)(def * 1.5),
                    (int)(exp * 4), gold * 5, Monster.MonsterType.TOWER_BOSS);
        }
        
        return new Monster(name, imgKey, hp, atk, def, exp, gold, Monster.MonsterType.NORMAL);
    }
}