package game.dungeon;

public class Monster {
    public enum MonsterType { NORMAL, ELITE, BOSS, TOWER_BOSS }

    private String name;
    private String imageKey;
    private int hp, maxHp, atk, def;
    private int expReward;
    private long goldReward;
    private MonsterType type;

    public Monster(String name, String imageKey, int hp, int atk, int def,
                   int exp, long gold, MonsterType type) {
        this.name = name; this.imageKey = imageKey;
        this.hp = hp; this.maxHp = hp; this.atk = atk; this.def = def;
        this.expReward = exp; this.goldReward = gold; this.type = type;
    }

    public int takeDamage(int raw) {
        int dmg = Math.max(1, raw - def);
        hp = Math.max(0, hp - dmg);
        return dmg;
    }

    public int attack() {
        int variance = (int)(atk * 0.2);
        return atk - variance + (int)(Math.random() * variance * 2);
    }

    public boolean isAlive()   { return hp > 0; }
    public String getName()    { return name; }
    public String getImageKey(){ return imageKey; }
    public int getHp()         { return hp; }
    public int getMaxHp()      { return maxHp; }
    public int getAtk()        { return atk; }
    public int getDef()        { return def; }
    public int getExpReward()  { return expReward; }
    public long getGoldReward(){ return goldReward; }
    public MonsterType getType(){ return type; }
    public double getHpPercent(){ return (double) hp / maxHp; }
}
