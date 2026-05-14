[class_diagram (3).html](https://github.com/user-attachments/files/27737514/class_diagram.3.html)# ⚔️ Chronicles of Destiny (운명의 연대기)





## 🗺 기획 의도 (Project Background)

RPG 게임의 핵심 요소인 **캐릭터 성장**, **전투**, **아이템 수집**을 Java Swing 환경에서 구현하여,  
객체지향 설계(OOP)의 실전 적용을 목표로 개발한 프로젝트입니다.

- 회원 시스템(로그인/회원가입)과 **자동 저장/불러오기** 기능으로 플레이어 경험을 지속적으로 유지합니다.  
- `Work` 추상 클래스와 인터페이스를 통해 몬스터·아이템·캐릭터의 공통 구조를 유기적으로 연결했습니다.  
- `ArrayList`, `HashMap`으로 가변 데이터를 구성하고, **파일 입출력 및 예외 처리**로 데이터 안정성을 확보했습니다.

---

## 📅 프로젝트 기간

**2025.10.31 ~ 2025.11.04**

---

## 🚀 Eclipse 임포트 방법

```
1. Eclipse 실행
2. File > Import > General > Existing Projects into Workspace
3. Select root directory → 이 RPGGame 폴더 선택
4. Finish 클릭
5. Main.java 우클릭 → Run As > Java Application
```

---

## 📁 프로젝트 구조

```
RPGGame/
├── src/
│   └── game/
│       ├── Main.java                        ← 진입점
│       ├── auth/                            ← 인증 관련
│       ├── data/
│       │   ├── UserData.java                ← 유저 데이터 모델
│       │   └── CharacterData.java           ← 캐릭터 스탯/레벨/장비
│       ├── dungeon/
│       │   ├── Monster.java                 ← 몬스터 모델
│       │   └── MonsterFactory.java          ← 몬스터 생성 팩토리
│       ├── inventory/
│       │   ├── Item.java                    ← 아이템 모델
│       │   └── ItemDatabase.java            ← 드롭/상점 아이템 DB
│       ├── system/
│       │   └── SaveSystem.java              ← 자동 저장/로드
│       └── ui/
│           ├── Theme.java                   ← 색상/폰트 테마
│           ├── StartScreen.java             ← 시작화면 (로그인/회원가입)
│           ├── NicknameCharacterScreen.java ← 닉네임 + 캐릭터 선택
│           ├── GameScreen.java              ← 메인 허브
│           ├── DungeonScreen.java           ← 던전 전투
│           ├── TowerScreen.java             ← 무한의 탑 (100층)
│           ├── InventoryScreen.java         ← 인벤토리/장비
│           └── ShopScreen.java              ← 희귀 상점
```

---
[<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>RPGGame 클래스 다이어그램</title>
<style>
*{margin:0;padding:0;box-sizing:border-box;}
body{background:#d8e4f0;font-family:'Malgun Gothic','Apple SD Gothic Neo',sans-serif;padding:24px;}
h1{text-align:center;color:#1a3a5c;font-size:17px;margin-bottom:16px;}

.wrap{
  overflow:auto;
  width:100%;
  max-height:90vh;
  border:2px solid #8aaccc;
  border-radius:6px;
}

.canvas{
  position:relative;
  width:2200px;
  height:1860px;
  background:#edf2f7;
}

/* ── 섹션 구분선 (패키지 표시) ── */
.zone{
  position:absolute;
  border:2px dashed #6090b8;
  border-radius:6px;
  pointer-events:none;
}
.zone-lbl{
  position:absolute;
  top:-13px;left:12px;
  font-size:10px;font-weight:bold;
  color:#1a4060;background:#edf2f7;padding:0 5px;
}

/* ── 클래스 박스 ── */
.cls{
  position:absolute;
  border:1.5px solid #4a7aa0;
  border-radius:2px;
  background:#fff;
  font-size:10.5px;
  box-shadow:1px 2px 6px rgba(0,0,0,0.15);
}
.cls-head{
  background:linear-gradient(180deg,#c0d8ee 0%,#85b4d4 100%);
  border-bottom:1px solid #4a7aa0;
  padding:5px 9px;text-align:center;
}
.pkg{font-size:8.5px;color:#234;font-style:italic;}
.ste{font-size:8.5px;color:#234;font-style:italic;}
.cnm{font-size:12.5px;font-weight:bold;color:#0d1f30;line-height:1.4;}

.cls-fld{
  border-bottom:1px solid #b0c8dc;
  padding:4px 8px;background:#f2f7fc;min-height:6px;
}
.cls-mth{padding:4px 8px;background:#f8fcff;}
.f,.m{line-height:1.65;color:#1a2a3a;white-space:nowrap;font-size:10px;}
.a{color:#333;font-weight:bold;margin-right:1px;}
.t{color:#0050aa;}
.r{color:#006600;}

/* ── SVG ── */
svg.rel{
  position:absolute;top:0;left:0;
  width:100%;height:100%;
  pointer-events:none;overflow:visible;
}

/* ── 레이블 ── */
.rl{
  position:absolute;
  font-size:9px;color:#1a3a60;
  background:rgba(237,242,247,0.94);
  padding:0 3px;font-style:italic;white-space:nowrap;
}
.ml{
  position:absolute;
  font-size:9px;font-weight:bold;color:#111;
}

/* 범례 */
.legend{
  position:absolute;bottom:16px;right:16px;
  border:1px solid #8aaccc;border-radius:4px;
  background:#fff;padding:10px 14px;font-size:9.5px;
}
.legend b{display:block;color:#1a3a5c;margin-bottom:4px;font-size:10px;}
.lr{display:flex;align-items:center;gap:7px;margin:3px 0;}
</style>
</head>
<body>
<h1>Chronicles of Destiny — RPGGame 클래스 다이어그램</h1>
<div class="wrap">
<div class="canvas" id="cv">
<svg class="rel" id="sv">
<defs>
  <marker id="A" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
    <polygon points="0,0 9,3.5 0,7" fill="#1a3a5c"/>
  </marker>
  <marker id="D" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
    <polygon points="0,0 9,3.5 0,7" fill="#5588bb"/>
  </marker>
  <marker id="G" markerWidth="12" markerHeight="9" refX="11" refY="4.5" orient="auto">
    <polygon points="0,0 11,4.5 0,9" fill="white" stroke="#1a3a5c" stroke-width="1.5"/>
  </marker>
  <marker id="Cs" markerWidth="12" markerHeight="9" refX="0" refY="4.5" orient="auto">
    <polygon points="0,4.5 6,0 12,4.5 6,9" fill="#1a3a5c"/>
  </marker>
  <marker id="Ao" markerWidth="12" markerHeight="9" refX="0" refY="4.5" orient="auto">
    <polygon points="0,4.5 6,0 12,4.5 6,9" fill="white" stroke="#1a3a5c" stroke-width="1.5"/>
  </marker>
</defs>
</svg>

<!-- ══════════════════════════════════════════
  COLUMN 1  (x=30)   game.data + game.inventory
══════════════════════════════════════════ -->

<!-- game.data zone -->
<div class="zone" style="left:20px;top:20px;width:580px;height:660px;">
  <span class="zone-lbl">game.data</span>
</div>

<!-- UserData -->
<div class="cls" id="ud" style="left:40px;top:45px;width:220px;">
  <div class="cls-head">
    <div class="pkg">game.data</div>
    <div class="cnm">UserData</div>
    <div class="ste">«Serializable»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>username : <span class="t">String</span></div>
    <div class="f"><span class="a">-</span>password : <span class="t">String</span></div>
    <div class="f"><span class="a">-</span>email : <span class="t">String</span></div>
    <div class="f"><span class="a">-</span>nickname : <span class="t">String</span></div>
    <div class="f"><span class="a">-</span>characterData : <span class="t">CharacterData</span></div>
    <div class="f"><span class="a">-</span>selectedCharIdx : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>createdAt : <span class="t">Date</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>getUsername() : <span class="r">String</span></div>
    <div class="m"><span class="a">+</span>getNickname() : <span class="r">String</span></div>
    <div class="m"><span class="a">+</span>setNickname(String)</div>
    <div class="m"><span class="a">+</span>getCharacterData() : <span class="r">CharacterData</span></div>
    <div class="m"><span class="a">+</span>setCharacterData(CharacterData)</div>
    <div class="m"><span class="a">+</span>isNewPlayer() : <span class="r">boolean</span></div>
  </div>
</div>

<!-- CharacterData -->
<div class="cls" id="cd" style="left:40px;top:290px;width:240px;">
  <div class="cls-head">
    <div class="pkg">game.data</div>
    <div class="cnm">CharacterData</div>
    <div class="ste">«Serializable»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>charClass : <span class="t">CharacterClass</span></div>
    <div class="f"><span class="a">-</span>level, exp : <span class="t">int / long</span></div>
    <div class="f"><span class="a">-</span>hp, maxHp, mp, maxMp : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>atk, def, spd : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>gold : <span class="t">long</span></div>
    <div class="f"><span class="a">-</span>statStr/Dex/Int/Luk : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>statPoints, skillPoints : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>skillLevels : <span class="t">Map&lt;String,Integer&gt;</span></div>
    <div class="f"><span class="a">-</span>equipment : <span class="t">Item[5]</span></div>
    <div class="f"><span class="a">-</span>inventory : <span class="t">List&lt;Item&gt;</span></div>
    <div class="f"><span class="a">-</span>towerStage : <span class="t">int</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>recalcStats()</div>
    <div class="m"><span class="a">+</span>addExp(long) : <span class="r">boolean</span></div>
    <div class="m"><span class="a">+</span>spendStat(String) : <span class="r">boolean</span></div>
    <div class="m"><span class="a">+</span>refundStat(String) : <span class="r">boolean</span></div>
    <div class="m"><span class="a">+</span>investSkill(String) : <span class="r">boolean</span></div>
    <div class="m"><span class="a">+</span>refundSkill(String) : <span class="r">boolean</span></div>
    <div class="m"><span class="a">+</span>takeDamage(int) : <span class="r">int</span></div>
    <div class="m"><span class="a">+</span>applyDeathPenalty() : <span class="r">DeathPenalty</span></div>
    <div class="m"><span class="a">+</span>getEffectiveAtk() : <span class="r">int</span></div>
    <div class="m"><span class="a">+</span>isAlive() : <span class="r">boolean</span></div>
  </div>
</div>

<!-- CharacterClass -->
<div class="cls" id="cc" style="left:330px;top:45px;width:200px;">
  <div class="cls-head">
    <div class="ste">«enumeration»</div>
    <div class="cnm">CharacterClass</div>
  </div>
  <div class="cls-fld">
    <div class="f">WARRIOR / MAGE</div>
    <div class="f">ARCHER / PALADIN</div>
    <div class="f">ASSASSIN</div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>name, emoji : <span class="t">String</span></div>
    <div class="m"><span class="a">+</span>mainStat : <span class="t">String</span></div>
    <div class="m"><span class="a">+</span>baseHp, baseAtk, baseDef : <span class="t">int</span></div>
    <div class="m"><span class="a">+</span>baseSpd, baseMana : <span class="t">int</span></div>
  </div>
</div>

<!-- DeathPenalty -->
<div class="cls" id="dp" style="left:330px;top:260px;width:195px;">
  <div class="cls-head">
    <div class="ste">«inner class»</div>
    <div class="cnm">DeathPenalty</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">+</span>goldLost : <span class="t">long</span></div>
    <div class="f"><span class="a">+</span>expLost : <span class="t">long</span></div>
  </div>
  <div class="cls-mth"></div>
</div>

<!-- game.inventory zone -->
<div class="zone" style="left:20px;top:720px;width:580px;height:420px;">
  <span class="zone-lbl">game.inventory</span>
</div>

<!-- Item -->
<div class="cls" id="item" style="left:40px;top:745px;width:240px;">
  <div class="cls-head">
    <div class="pkg">game.inventory</div>
    <div class="cnm">Item</div>
    <div class="ste">«Serializable»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>name, emoji : <span class="t">String</span></div>
    <div class="f"><span class="a">-</span>rarity : <span class="t">Rarity</span></div>
    <div class="f"><span class="a">-</span>type : <span class="t">ItemType</span></div>
    <div class="f"><span class="a">-</span>hpBonus, mpBonus, atkBonus : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>defBonus, spdBonus : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>healAmount, minLevel : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>goldValue : <span class="t">long</span></div>
    <div class="f"><span class="a">-</span>enhanceLevel : <span class="t">int</span>  (0~5)</div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>getEffectiveAtk/Def/Hp() : <span class="r">int</span></div>
    <div class="m"><span class="a">+</span>getEnhanceSuccessRate() : <span class="r">double</span></div>
    <div class="m"><span class="a">+</span>getSellPrice() : <span class="r">long</span></div>
    <div class="m"><span class="a">+</span>getNextRarity() : <span class="r">Rarity</span></div>
    <div class="m"><span class="a">+</span>getDisplayName() : <span class="r">String</span></div>
  </div>
</div>

<!-- Rarity -->
<div class="cls" id="rar" style="left:330px;top:745px;width:185px;">
  <div class="cls-head">
    <div class="ste">«enumeration»</div>
    <div class="cnm">Rarity</div>
  </div>
  <div class="cls-fld">
    <div class="f">RARE / EPIC</div>
    <div class="f">UNIQUE / LEGENDARY</div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>kor, hex : <span class="t">String</span></div>
    <div class="m"><span class="a">+</span>weight : <span class="t">double</span></div>
  </div>
</div>

<!-- ItemType -->
<div class="cls" id="ityp" style="left:330px;top:910px;width:185px;">
  <div class="cls-head">
    <div class="ste">«enumeration»</div>
    <div class="cnm">ItemType</div>
  </div>
  <div class="cls-fld">
    <div class="f">WEAPON</div>
    <div class="f">ARMOR_HEAD/CHEST/LEGS</div>
    <div class="f">ACCESSORY</div>
    <div class="f">POTION / GOLD_BAG</div>
  </div>
</div>

<!-- ItemDatabase -->
<div class="cls" id="idb" style="left:40px;top:1020px;width:240px;">
  <div class="cls-head">
    <div class="pkg">game.inventory</div>
    <div class="cnm">ItemDatabase</div>
    <div class="ste">«utility»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>DROP_POOL : <span class="t">List&lt;Item&gt; $</span></div>
    <div class="f"><span class="a">-</span>SHOP_POOL : <span class="t">List&lt;Item&gt; $</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>rollDrop(int) : <span class="r">Item $</span></div>
    <div class="m"><span class="a">+</span>rollGold(int, boolean) : <span class="r">long $</span></div>
    <div class="m"><span class="a">+</span>rollSynthesized(Rarity, int) : <span class="r">Item $</span></div>
    <div class="m"><span class="a">+</span>getShopItems() : <span class="r">List&lt;Item&gt; $</span></div>
  </div>
</div>

<!-- ══════════════════════════════════════════
  COLUMN 2  (x=680)   game.skill + game.system + game.dungeon
══════════════════════════════════════════ -->

<!-- game.skill zone -->
<div class="zone" style="left:660px;top:20px;width:400px;height:510px;">
  <span class="zone-lbl">game.skill</span>
</div>

<!-- Skill -->
<div class="cls" id="sk" style="left:680px;top:45px;width:230px;">
  <div class="cls-head">
    <div class="pkg">game.skill</div>
    <div class="cnm">Skill</div>
    <div class="ste">«Serializable»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">+</span>id, name, emoji, desc : <span class="t">String</span></div>
    <div class="f"><span class="a">+</span>unlockLevel, manaCost : <span class="t">int</span></div>
    <div class="f"><span class="a">+</span>dmgMult, baseDmgMult : <span class="t">double</span></div>
    <div class="f"><span class="a">+</span>dmgPerLevel : <span class="t">double</span></div>
    <div class="f"><span class="a">+</span>healMult : <span class="t">int</span></div>
    <div class="f"><span class="a">+</span>type : <span class="t">SkillType</span></div>
    <div class="f"><span class="a">+</span>owner : <span class="t">CharacterClass</span></div>
    <div class="f"><span class="a">+</span>maxSkillLevel : <span class="t">int</span></div>
    <div class="f"><span class="a">+</span>ALL : <span class="t">Skill[][] $</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>getDmgMult(int) : <span class="r">double</span></div>
    <div class="m"><span class="a">+</span>canUnlock(int) : <span class="r">boolean</span></div>
    <div class="m"><span class="a">+</span>isUnlocked(int) : <span class="r">boolean</span></div>
    <div class="m"><span class="a">+</span>getUnlocked(CharacterClass,int) : <span class="r">Skill[] $</span></div>
    <div class="m"><span class="a">+</span>getUsable(CharacterClass,int,Map) : <span class="r">Skill[] $</span></div>
  </div>
</div>

<!-- SkillType -->
<div class="cls" id="skt" style="left:680px;top:430px;width:185px;">
  <div class="cls-head">
    <div class="ste">«enumeration»</div>
    <div class="cnm">SkillType</div>
  </div>
  <div class="cls-fld">
    <div class="f">ATTACK / AOE</div>
    <div class="f">HEAL / SHIELD</div>
    <div class="f">DRAIN / DOT / ULTIMATE</div>
  </div>
</div>

<!-- game.system zone -->
<div class="zone" style="left:660px;top:580px;width:400px;height:185px;">
  <span class="zone-lbl">game.system</span>
</div>

<!-- SaveSystem -->
<div class="cls" id="ss" style="left:680px;top:605px;width:250px;">
  <div class="cls-head">
    <div class="pkg">game.system</div>
    <div class="cnm">SaveSystem</div>
    <div class="ste">«utility»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>SAVE_DIR, USERS_FILE : <span class="t">String $</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>loadAllUsers() : <span class="r">Map&lt;String,UserData&gt; $</span></div>
    <div class="m"><span class="a">+</span>saveAllUsers(Map) $</div>
    <div class="m"><span class="a">+</span>register(String,String,String) : <span class="r">boolean $</span></div>
    <div class="m"><span class="a">+</span>login(String,String) : <span class="r">UserData $</span></div>
    <div class="m"><span class="a">+</span>saveUser(UserData) $</div>
  </div>
</div>

<!-- game.dungeon zone -->
<div class="zone" style="left:660px;top:810px;width:400px;height:540px;">
  <span class="zone-lbl">game.dungeon</span>
</div>

<!-- DungeonZone -->
<div class="cls" id="dz" style="left:680px;top:835px;width:230px;">
  <div class="cls-head">
    <div class="pkg">game.dungeon</div>
    <div class="cnm">DungeonZone</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">+</span>name, bgType, desc1, desc2 : <span class="t">String</span></div>
    <div class="f"><span class="a">+</span>minLevel, maxLevel, tier : <span class="t">int</span></div>
    <div class="f"><span class="a">+</span>expMult, dropMult, goldMult : <span class="t">double</span></div>
    <div class="f"><span class="a">+</span>accentColor : <span class="t">Color</span></div>
    <div class="f"><span class="a">+</span>ALL : <span class="t">DungeonZone[] $</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>isAvailable(int) : <span class="r">boolean</span></div>
  </div>
</div>

<!-- Monster -->
<div class="cls" id="mn" style="left:680px;top:1050px;width:225px;">
  <div class="cls-head">
    <div class="pkg">game.dungeon</div>
    <div class="cnm">Monster</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>name, imageKey : <span class="t">String</span></div>
    <div class="f"><span class="a">-</span>hp, maxHp, atk, def : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>expReward : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>goldReward : <span class="t">long</span></div>
    <div class="f"><span class="a">-</span>type : <span class="t">MonsterType</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>takeDamage(int) : <span class="r">int</span></div>
    <div class="m"><span class="a">+</span>attack() : <span class="r">int</span></div>
    <div class="m"><span class="a">+</span>isAlive() : <span class="r">boolean</span></div>
    <div class="m"><span class="a">+</span>getHpPercent() : <span class="r">double</span></div>
  </div>
</div>

<!-- MonsterType -->
<div class="cls" id="mt" style="left:680px;top:1285px;width:185px;">
  <div class="cls-head">
    <div class="ste">«enumeration»</div>
    <div class="cnm">MonsterType</div>
  </div>
  <div class="cls-fld">
    <div class="f">NORMAL / ELITE</div>
    <div class="f">BOSS / TOWER_BOSS</div>
  </div>
</div>

<!-- MonsterFactory -->
<div class="cls" id="mf" style="left:680px;top:1400px;width:250px;">
  <div class="cls-head">
    <div class="pkg">game.dungeon</div>
    <div class="cnm">MonsterFactory</div>
    <div class="ste">«utility»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>POOL : <span class="t">Object[][] $</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>createZoneMonster(DungeonZone,int) : <span class="r">Monster $</span></div>
    <div class="m"><span class="a">+</span>createTowerMonster(int) : <span class="r">Monster $</span></div>
  </div>
</div>

<!-- ══════════════════════════════════════════
  COLUMN 3  (x=1130)   game.ui
══════════════════════════════════════════ -->

<!-- game.ui zone -->
<div class="zone" style="left:1120px;top:20px;width:1060px;height:1820px;">
  <span class="zone-lbl">game.ui</span>
</div>

<!-- Main -->
<div class="cls" id="main" style="left:1140px;top:45px;width:160px;">
  <div class="cls-head">
    <div class="pkg">game</div>
    <div class="cnm">Main</div>
  </div>
  <div class="cls-fld"></div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>main(String[]) : <span class="r">void</span></div>
  </div>
</div>

<!-- ImageGen -->
<div class="cls" id="ig" style="left:1140px;top:170px;width:240px;">
  <div class="cls-head">
    <div class="pkg">game.ui</div>
    <div class="cnm">ImageGen</div>
    <div class="ste">«utility»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>CACHE : <span class="t">Map&lt;String,BufferedImage&gt; $</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>getMonster(String,int,int) : <span class="r">BufferedImage $</span></div>
    <div class="m"><span class="a">+</span>getCharacter(String,int,int) : <span class="r">BufferedImage $</span></div>
    <div class="m"><span class="a">+</span>getCharacterEquipped(...) : <span class="r">BufferedImage $</span></div>
    <div class="m"><span class="a">+</span>getDungeonBg(String,int,int) : <span class="r">BufferedImage $</span></div>
    <div class="m"><span class="a">+</span>getHitEffect(boolean,int,int) : <span class="r">BufferedImage $</span></div>
    <div class="m"><span class="a">+</span>invalidateCharacterCache(String) $</div>
  </div>
</div>

<!-- Theme -->
<div class="cls" id="th" style="left:1140px;top:430px;width:240px;">
  <div class="cls-head">
    <div class="pkg">game.ui</div>
    <div class="cnm">Theme</div>
    <div class="ste">«utility»</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">+</span>BG_DARK~BG_SUCCESS : <span class="t">Color $</span></div>
    <div class="f"><span class="a">+</span>GOLD, ACCENT, HP_RED ... : <span class="t">Color $</span></div>
    <div class="f"><span class="a">+</span>FONT_TITLE~FONT_SMALL : <span class="t">Font $</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>styleButton(JButton) $</div>
    <div class="m"><span class="a">+</span>paintBg(Graphics,int,int) $</div>
    <div class="m"><span class="a">+</span>paintStars(Graphics,int,int,Random) $</div>
  </div>
</div>

<!-- StartScreen -->
<div class="cls" id="st" style="left:1140px;top:640px;width:220px;">
  <div class="cls-head">
    <div class="cnm">StartScreen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>cardPanel : <span class="t">JPanel</span></div>
    <div class="f"><span class="a">-</span>cardLayout : <span class="t">CardLayout</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">-</span>buildMainPanel() : <span class="r">JPanel</span></div>
    <div class="m"><span class="a">-</span>buildLoginPanel() : <span class="r">JPanel</span></div>
    <div class="m"><span class="a">-</span>buildRegisterPanel() : <span class="r">JPanel</span></div>
  </div>
</div>

<!-- NicknameCharacterScreen -->
<div class="cls" id="nc" style="left:1140px;top:840px;width:220px;">
  <div class="cls-head">
    <div class="cnm">NicknameCharacter<br>Screen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>user : <span class="t">UserData</span></div>
    <div class="f"><span class="a">-</span>selectedIdx : <span class="t">int</span></div>
    <div class="f"><span class="a">-</span>nickField : <span class="t">JTextField</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">-</span>buildCharCard(CharacterClass,int)</div>
    <div class="m"><span class="a">-</span>startGame()</div>
  </div>
</div>

<!-- GameScreen -->
<div class="cls" id="gs" style="left:1140px;top:1040px;width:235px;">
  <div class="cls-head">
    <div class="cnm">GameScreen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>user : <span class="t">UserData</span></div>
    <div class="f"><span class="a">-</span>cd : <span class="t">CharacterData</span></div>
    <div class="f"><span class="a">-</span>lblAtk, hpBar, mpBar ... : <span class="t">JLabel</span></div>
    <div class="f"><span class="a">-</span>characterVisualPanel : <span class="t">JPanel</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">+</span>refreshStats()</div>
    <div class="m"><span class="a">-</span>buildLeftPanel() : <span class="r">JPanel</span></div>
    <div class="m"><span class="a">-</span>buildRightSide() : <span class="r">JPanel</span></div>
    <div class="m"><span class="a">-</span>navigate(String)</div>
  </div>
</div>

<!-- DungeonScreen -->
<div class="cls" id="ds" style="left:1440px;top:45px;width:220px;">
  <div class="cls-head">
    <div class="cnm">DungeonScreen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>user : <span class="t">UserData</span></div>
    <div class="f"><span class="a">-</span>cd : <span class="t">CharacterData</span></div>
    <div class="f"><span class="a">-</span>hub : <span class="t">GameScreen</span></div>
    <div class="f"><span class="a">-</span>currentZone : <span class="t">DungeonZone</span></div>
    <div class="f"><span class="a">-</span>currentMonster : <span class="t">Monster</span></div>
    <div class="f"><span class="a">-</span>battleActive : <span class="t">boolean</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">-</span>showZoneSelect()</div>
    <div class="m"><span class="a">-</span>spawnMonster()</div>
    <div class="m"><span class="a">-</span>useSkill(Skill)</div>
    <div class="m"><span class="a">-</span>onMonsterKilled()</div>
    <div class="m"><span class="a">-</span>onPlayerDied()</div>
  </div>
</div>

<!-- TowerScreen -->
<div class="cls" id="ts" style="left:1440px;top:360px;width:220px;">
  <div class="cls-head">
    <div class="cnm">TowerScreen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>user : <span class="t">UserData</span></div>
    <div class="f"><span class="a">-</span>cd : <span class="t">CharacterData</span></div>
    <div class="f"><span class="a">-</span>hub : <span class="t">GameScreen</span></div>
    <div class="f"><span class="a">-</span>currentMonster : <span class="t">Monster</span></div>
    <div class="f"><span class="a">-</span>currentStage : <span class="t">int</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">-</span>startStage()</div>
    <div class="m"><span class="a">-</span>playerAtk(boolean)</div>
    <div class="m"><span class="a">-</span>onKilled() / onDied()</div>
  </div>
</div>

<!-- ShopScreen -->
<div class="cls" id="sh" style="left:1440px;top:620px;width:220px;">
  <div class="cls-head">
    <div class="cnm">ShopScreen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>user : <span class="t">UserData</span></div>
    <div class="f"><span class="a">-</span>cd : <span class="t">CharacterData</span></div>
    <div class="f"><span class="a">-</span>hub : <span class="t">GameScreen</span></div>
    <div class="f"><span class="a">-</span>goldLabel : <span class="t">JLabel</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">-</span>buildShopRow(Item,int) : <span class="r">JPanel</span></div>
    <div class="m"><span class="a">-</span>exitShop()</div>
  </div>
</div>

<!-- InventoryScreen -->
<div class="cls" id="iv" style="left:1440px;top:840px;width:220px;">
  <div class="cls-head">
    <div class="cnm">InventoryScreen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>user : <span class="t">UserData</span></div>
    <div class="f"><span class="a">-</span>cd : <span class="t">CharacterData</span></div>
    <div class="f"><span class="a">-</span>hub : <span class="t">GameScreen</span></div>
    <div class="f"><span class="a">-</span>currentFilter : <span class="t">ItemType</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">-</span>buildInventoryTabs() : <span class="r">JPanel</span></div>
    <div class="m"><span class="a">-</span>equip(int) / sell(int)</div>
  </div>
</div>

<!-- EnhanceScreen -->
<div class="cls" id="en" style="left:1440px;top:1060px;width:220px;">
  <div class="cls-head">
    <div class="cnm">EnhanceScreen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>user : <span class="t">UserData</span></div>
    <div class="f"><span class="a">-</span>cd : <span class="t">CharacterData</span></div>
    <div class="f"><span class="a">-</span>hub : <span class="t">GameScreen</span></div>
    <div class="f"><span class="a">-</span>mergeSelectedSet : <span class="t">Set&lt;Integer&gt;</span></div>
    <div class="f"><span class="a">-</span>showMerge : <span class="t">boolean</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">-</span>doEnhance()</div>
    <div class="m"><span class="a">-</span>doMerge()</div>
  </div>
</div>

<!-- SkillScreen -->
<div class="cls" id="sksc" style="left:1440px;top:1290px;width:220px;">
  <div class="cls-head">
    <div class="cnm">SkillScreen</div>
    <div class="ste">extends JFrame</div>
  </div>
  <div class="cls-fld">
    <div class="f"><span class="a">-</span>user : <span class="t">UserData</span></div>
    <div class="f"><span class="a">-</span>cd : <span class="t">CharacterData</span></div>
    <div class="f"><span class="a">-</span>hub : <span class="t">GameScreen</span></div>
    <div class="f"><span class="a">~</span>MAX_PER_STAT = 10 : <span class="t">int $</span></div>
  </div>
  <div class="cls-mth">
    <div class="m"><span class="a">-</span>buildStatPanel() : <span class="r">JPanel</span></div>
    <div class="m"><span class="a">-</span>buildSkillPanel() : <span class="r">JPanel</span></div>
    <div class="m"><span class="a">-</span>spendStat(String,JLabel,int)</div>
  </div>
</div>

<!-- ── 범례 ── -->
<div class="legend">
  <b>관계 범례</b>
  <div class="lr">
    <svg width="40" height="12"><line x1="0" y1="6" x2="28" y2="6" stroke="#1a3a5c" stroke-width="1.8"/><polygon points="28,6 19,2 19,10" fill="#1a3a5c"/></svg>
    연관 (Association)
  </div>
  <div class="lr">
    <svg width="40" height="12"><line x1="4" y1="6" x2="28" y2="6" stroke="#1a3a5c" stroke-width="1.8"/><polygon points="4,6 10,2 10,10" fill="#1a3a5c"/><polygon points="4,6 10,2 16,6 10,10" fill="#1a3a5c"/></svg>
    합성 (Composition) ◆
  </div>
  <div class="lr">
    <svg width="40" height="12"><line x1="0" y1="6" x2="28" y2="6" stroke="#4a7aa0" stroke-width="1.5" stroke-dasharray="4,2"/><polygon points="28,6 19,2 19,10" fill="#4a7aa0"/></svg>
    의존 (Dependency)
  </div>
  <div class="lr">
    <svg width="40" height="12"><line x1="0" y1="6" x2="26" y2="6" stroke="#1a3a5c" stroke-width="1.8"/><polygon points="26,6 16,2 16,10" fill="white" stroke="#1a3a5c" stroke-width="1.5"/></svg>
    상속 (Generalization)
  </div>
</div>

</div><!-- canvas -->
</div><!-- wrap -->

<script>
const CV=document.getElementById('cv');
const SV=document.getElementById('sv');

// 요소 위치 계산 (canvas 기준)
function R(id){
  const b=document.getElementById(id).getBoundingClientRect();
  const c=CV.getBoundingClientRect();
  return{
    l:b.left-c.left, r:b.left-c.left+b.width,
    t:b.top-c.top,   b:b.top-c.top+b.height,
    x:b.left-c.left+b.width/2, y:b.top-c.top+b.height/2
  };
}

// 직각 꺾인 선
function poly(pts,col,dash,mark,sw){
  const p=document.createElementNS('http://www.w3.org/2000/svg','polyline');
  p.setAttribute('points',pts.map(p=>p.join(',')).join(' '));
  p.setAttribute('stroke',col||'#1a3a5c');
  p.setAttribute('stroke-width',sw||1.6);
  p.setAttribute('fill','none');
  if(dash)p.setAttribute('stroke-dasharray',dash);
  if(mark)p.setAttribute('marker-end',mark);
  SV.appendChild(p);
}

// 레이블
function lb(t,x,y){
  const d=document.createElement('div');
  d.className='rl';d.style.left=x+'px';d.style.top=y+'px';
  d.textContent=t;CV.appendChild(d);
}
function ml(t,x,y){
  const d=document.createElement('div');
  d.className='ml';d.style.left=x+'px';d.style.top=y+'px';
  d.textContent=t;CV.appendChild(d);
}

window.addEventListener('load',()=>{

  // ════════════════════════
  //  COLUMN 1 내부 관계
  // ════════════════════════

  // UserData ◆──── CharacterData  (합성, 수직 직선)
  const UD=R('ud'),CD=R('cd');
  poly([[UD.x,UD.b],[UD.x,CD.t]],'#1a3a5c','','url(#A)',2);
  // 다이아몬드 시작점 표시
  poly([[UD.x-6,UD.b],[UD.x,UD.b+10],[UD.x+6,UD.b],[UD.x,UD.b+10]],'#1a3a5c','','',1.5);
  ml('1',UD.x+4,UD.b+2); ml('1',UD.x+4,CD.t-14);
  lb('characterData',UD.x+6,UD.b+12);

  // CharacterData ──── CharacterClass  (수평)
  const CC=R('cc');
  poly([[CD.r,CD.t+25],[CC.l,CD.t+25]],'#1a3a5c','','url(#A)',1.6);
  lb('charClass',CD.r+4,CD.t+12);

  // CharacterData ──── DeathPenalty  (수평)
  const DP=R('dp');
  poly([[CD.r,CD.t+60],[DP.l,DP.t+20]],'#1a3a5c','','url(#A)',1.6);
  lb('«inner»',CD.r+4,CD.t+48);

  // CharacterData ◆──── Item (equipment)  L자
  const IT=R('item'),CDv=R('cd');
  const midY1=CDv.b+50;
  poly([[CDv.x-20,CDv.b],[CDv.x-20,midY1],[IT.x,midY1],[IT.x,IT.t]],'#1a3a5c','','url(#A)',1.6);
  poly([[CDv.x-20-6,CDv.b],[CDv.x-20,CDv.b+10],[CDv.x-20+6,CDv.b],[CDv.x-20,CDv.b+10]],'#1a3a5c','','',1.5);
  ml('1',CDv.x-18,CDv.b+2); ml('0..*',IT.x+3,IT.t-14);
  lb('equipment / inventory',CDv.x-16,midY1-14);

  // Item ──── Rarity  (수평)
  const RAR=R('rar');
  poly([[IT.r,IT.t+30],[RAR.l,IT.t+30]],'#1a3a5c','','url(#A)',1.6);
  lb('rarity',IT.r+4,IT.t+18);

  // Item ──── ItemType  (꺾임)
  const ITYP=R('ityp');
  poly([[IT.r,IT.t+60],[RAR.l-10,IT.t+60],[RAR.l-10,ITYP.y],[ITYP.l,ITYP.y]],'#1a3a5c','','url(#A)',1.6);
  lb('type',IT.r+4,IT.t+48);

  // ItemDatabase ──── Item  (수직 의존)
  const IDB=R('idb');
  poly([[IDB.x,IDB.t],[IDB.x,IT.b+10],[IT.x+40,IT.b+10],[IT.x+40,IT.b]],'#5588bb','5,3','url(#D)',1.4);
  lb('«create»',IDB.x+4,IDB.t-14);

  // ════════════════════════
  //  COLUMN 2 내부 관계
  // ════════════════════════

  // Skill ──── SkillType  (수직)
  const SK=R('sk'),SKT=R('skt');
  poly([[SK.x,SK.b],[SK.x,SKT.t]],'#1a3a5c','','url(#A)',1.6);
  lb('type',SK.x+4,SK.b+4);

  // Monster ──── MonsterType  (수직)
  const MN=R('mn'),MT=R('mt');
  poly([[MN.x,MN.b],[MN.x,MT.t]],'#1a3a5c','','url(#A)',1.6);
  lb('type',MN.x+4,MN.b+4);

  // DungeonZone ◇──── Monster  (수직 집합)
  const DZ=R('dz');
  poly([[DZ.x,DZ.b],[DZ.x,MN.t]],'#1a3a5c','','url(#A)',1.6);
  poly([[DZ.x-6,DZ.b],[DZ.x,DZ.b+10],[DZ.x+6,DZ.b],[DZ.x,DZ.b+10]],'#1a3a5c','','',1.5);
  ml('1.*',DZ.x+4,DZ.b+2);

  // MonsterFactory ──── Monster  (수직 의존)
  const MF=R('mf');
  poly([[MF.x,MF.t],[MF.x,MT.b+10],[MN.r+10,MT.b+10],[MN.r+10,MN.b],[MN.r,MN.b]],'#5588bb','5,3','url(#D)',1.4);
  lb('«create»',MF.x+4,MF.t-14);

  // MonsterFactory ──── DungeonZone  (꺾임)
  poly([[MF.x-30,MF.t],[MF.x-30,DZ.b+20],[DZ.x+20,DZ.b+20],[DZ.x+20,DZ.b]],'#5588bb','5,3','url(#D)',1.4);
  lb('uses',MF.x-26,MF.t-14);

  // ════════════════════════
  //  COLUMN 1 → COLUMN 2 관계
  // ════════════════════════

  // CharacterData ─── Skill  (수평 의존)
  const CDr=R('cd'),SKl=R('sk');
  const vy1=CDr.t+20;
  poly([[CDr.r,vy1],[SKl.l,vy1]],'#5588bb','5,3','url(#D)',1.4);
  lb('«use» skillLevels',CDr.r+4,vy1-13);

  // CharacterData ─── SaveSystem  (꺾임)
  const SS=R('ss');
  poly([[CDr.r,CDr.t+50],[SS.l,CDr.t+50],[SS.l,SS.t]],'#5588bb','5,3','url(#D)',1.4);
  lb('saveUser',CDr.r+4,CDr.t+38);

  // UserData ─── SaveSystem  (꺾임)
  const UDr=R('ud');
  poly([[UDr.r,UDr.t+15],[SS.l,UDr.t+15],[SS.l,SS.t]],'#5588bb','5,3','url(#D)',1.4);
  lb('login/register',UDr.r+4,UDr.t+3);

  // ════════════════════════
  //  COLUMN 3 내부 관계
  // ════════════════════════

  // Main ──── StartScreen  (수직 의존)
  const MAIN=R('main'),ST=R('st');
  poly([[MAIN.x,MAIN.b],[MAIN.x,MAIN.b+30],[ST.x,MAIN.b+30],[ST.x,ST.t]],'#5588bb','5,3','url(#D)',1.4);
  lb('new StartScreen()',MAIN.x+4,MAIN.b+14);

  // StartScreen → NicknameCharacterScreen  (수직)
  const NC=R('nc');
  poly([[ST.x,ST.b],[ST.x,NC.t]],'#5588bb','5,3','url(#D)',1.4);
  lb('신규 유저',ST.x+4,ST.b+4);

  // NicknameCharacterScreen → GameScreen  (수직)
  const GS=R('gs');
  poly([[NC.x,NC.b],[NC.x,GS.t]],'#5588bb','5,3','url(#D)',1.4);
  lb('캐릭터 생성 완료',NC.x+4,NC.b+4);

  // GameScreen → 하위 화면 6개  (수평 공유 버스)
  //   GameScreen 오른쪽에서 세로 버스 라인 하나를 내리고 각각 수평으로 분기
  const GS2=R('gs');
  const BUS_X=GS2.r+30; // 버스 x 위치
  const screens=[['ds',45],['ts',360],['sh',620],['iv',840],['en',1060],['sksc',1290]];
  // 버스 세로선
  const topBus=GS2.y, botBus=R('sksc').y;
  poly([[GS2.r,GS2.y],[BUS_X,GS2.y],[BUS_X,botBus]],'#5588bb','4,3','',1.3);
  lb('opens',GS2.r+4,GS2.y-13);

  for(const [id] of screens){
    const SC=R(id);
    poly([[BUS_X,SC.y],[SC.l,SC.y]],'#5588bb','4,3','url(#D)',1.3);
  }

  // 하위 화면들 → GameScreen (hub) 복귀
  //   오른쪽에 복귀 버스 라인
  const RET_X=BUS_X+30;
  const topRet=R('ds').y+10, botRet=R('sksc').y+10;
  poly([[RET_X,topRet],[RET_X,botRet]],'#1a3a5c','','',1.3);
  for(const [id] of screens){
    const SC=R(id);
    poly([[SC.r,SC.y+10],[RET_X,SC.y+10]],'#1a3a5c','','',1.3);
  }
  poly([[RET_X,GS2.y+10],[GS2.r,GS2.y+10]],'#1a3a5c','','url(#A)',1.3);
  lb('hub →',RET_X+4,topRet-13);

  // ════════════════════════
  //  COLUMN 2 → COLUMN 3 관계
  // ════════════════════════

  // DungeonZone → DungeonScreen  (수평)
  const DZr=R('dz'),DS=R('ds');
  poly([[DZr.r,DZr.y],[DS.l,DZr.y],[DS.l,DS.t+80],[DS.l,DS.t+80]],'#1a3a5c','','url(#A)',1.6);
  lb('currentZone',DZr.r+4,DZr.y-13);

  // Monster → DungeonScreen  (꺾임)
  const MNr=R('mn'),DSr=R('ds');
  poly([[MNr.r,MNr.y],[MNr.r+30,MNr.y],[MNr.r+30,DSr.t+100],[DSr.l,DSr.t+100]],'#1a3a5c','','url(#A)',1.6);
  lb('currentMonster',MNr.r+32,MNr.y-13);

  // Monster → TowerScreen  (꺾임)
  const TS=R('ts');
  poly([[MNr.r,MNr.y+20],[MNr.r+50,MNr.y+20],[MNr.r+50,TS.t+60],[TS.l,TS.t+60]],'#1a3a5c','','url(#A)',1.6);

  // Skill → DungeonScreen  (의존)
  const SKr=R('sk'),DSd=R('ds');
  poly([[SKr.r,SKr.t+20],[DSd.l,SKr.t+20],[DSd.l,DSd.t+120]],'#5588bb','5,3','url(#D)',1.4);
  lb('useSkill',SKr.r+4,SKr.t+8);

  // Skill → SkillScreen (의존)
  const SKSCd=R('sksc');
  poly([[SKr.r,SKr.y],[SKSCd.l,SKr.y],[SKSCd.l,SKSCd.t]],'#5588bb','5,3','url(#D)',1.4);
  lb('manage',SKr.r+4,SKr.y-13);

  // ItemDatabase → DungeonScreen, ShopScreen, EnhanceScreen (공유 의존 버스)
  const IDBr=R('idb');
  const IBX=IDBr.r+20;
  const SHd=R('sh'),ENd=R('en');
  poly([[IDBr.r,IDBr.y],[IBX,IDBr.y],[IBX,SHd.y]],'#5588bb','5,3','',1.3);
  poly([[IBX,DSd.y+10],[DSd.l,DSd.y+10]],'#5588bb','5,3','url(#D)',1.3);
  poly([[IBX,SHd.y],[SHd.l,SHd.y]],'#5588bb','5,3','url(#D)',1.3);
  poly([[IBX,ENd.y],[ENd.l,ENd.y]],'#5588bb','5,3','url(#D)',1.3);
  lb('rollDrop/rollGold/rollSynth',IDBr.r+4,IDBr.y-13);

});
</script>
</body>
</html>
Uploading class_diagram (3).html…]()

## 🕹 주요 기능

### 🔐 회원 시스템
- 회원가입 (아이디 / 비밀번호 / 이메일)
- 로그인 후 **자동 저장** (홈 디렉토리에 저장)
- 신규 유저: 닉네임 + 캐릭터 선택

---
## 클래스 다이어리 


### 🧙 캐릭터 (5종)

| 캐릭터 | 특징 |
|:------:|:-----|
| ⚔️ 전사 | 고HP, 균형형 |
| 🔮 마법사 | 고공격, 저HP |
| 🏹 궁수 | 균형형, 빠른 속도 |
| 🛡️ 성기사 | 고방어, 안정형 |
| 🗡️ 암살자 | 최고속도, 고공격 |
<img width="1121" height="630" alt="12" src="https://github.com/user-attachments/assets/9722e8f4-3ab4-48bc-a405-b4dc8ca8d4b5" />

---

### ⚔️ 던전
- 레벨에 따라 몬스터 자동 강화
- 일반 / **강화(Elite)** 몬스터 출현
- 드롭: 장비, 포션, 골드 (확률적)
<img width="797" height="547" alt="11" src="https://github.com/user-attachments/assets/7df5da7a-4448-486e-95fb-bbb15d025b2b" />



---

### 🏯 무한의 탑 (100층)
- **5의 배수마다 미니보스** 등장
- 스테이지별 몬스터 점진적 강화
- 클리어한 층은 자동 저장
<img width="975" height="664" alt="13" src="https://github.com/user-attachments/assets/5f0a1c1e-44f6-4d55-bcc8-985f0890f15c" />

---

### 🏪 상점
- 던전 드롭 불가 **희귀 아이템** 판매
- Epic / Legendary 등급 장비 구매 가능
<img width="1130" height="649" alt="14" src="https://github.com/user-attachments/assets/81497018-b9dc-4bff-9141-b74e8b952161" />

---

### 🎒 인벤토리
- 장비 착용 / 해제 (5슬롯)
- 포션 즉시 사용
- 아이템 버리기
<img width="1130" height="649" alt="15" src="https://github.com/user-attachments/assets/2e7ed73d-e363-4f18-92cd-ce416d4055a4" />

---

### 📈 레벨 시스템
- 최대 **99레벨**
- 초반: 쉬운 경험치 → 고레벨: 지수적 증가
- 레벨업 시 HP 전체 회복

---

## 💾 저장 위치

```
~/RPGGameSave/users.dat   ← 자동 생성
```

---

## ⚙️ 요구사항

| 항목 | 내용 |
|:----:|:-----|
| Java | 11 이상 (Java 17 권장) |
| IDE | Eclipse IDE (Java EE 또는 Java Developer) |
