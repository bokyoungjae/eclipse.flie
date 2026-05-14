# ⚔️ Chronicles of Destiny (운명의 연대기)





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
