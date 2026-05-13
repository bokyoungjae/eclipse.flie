⚔️ Chronicles of Destiny (운명의 연대기)
Java Swing 기반 풀스케일 RPG 게임

📦 Eclipse 임포트 방법
Eclipse 실행
File > Import > General > Existing Projects into Workspace
Select root directory → 이 RPGGame 폴더 선택
Finish 클릭
Main.java 우클릭 → Run As > Java Application
🎮 게임 구조
RPGGame/
├── src/
│   └── game/
│       ├── Main.java                  ← 진입점
│       ├── auth/
│       ├── data/
│       │   ├── UserData.java          ← 유저 데이터 모델
│       │   └── CharacterData.java     ← 캐릭터 스탯/레벨/장비
│       ├── dungeon/
│       │   ├── Monster.java           ← 몬스터 모델
│       │   └── MonsterFactory.java    ← 몬스터 생성 팩토리
│       ├── inventory/
│       │   ├── Item.java              ← 아이템 모델
│       │   └── ItemDatabase.java      ← 드롭/상점 아이템 DB
│       ├── system/
│       │   └── SaveSystem.java        ← 자동 저장/로드
│       └── ui/
│           ├── Theme.java             ← 색상/폰트 테마
│           ├── StartScreen.java       ← 시작화면 (로그인/회원가입)
│           ├── NicknameCharacterScreen.java ← 닉네임+캐릭터 선택
│           ├── GameScreen.java        ← 메인 허브
│           ├── DungeonScreen.java     ← 던전 전투
│           ├── TowerScreen.java       ← 무한의 탑 (100층)
│           ├── InventoryScreen.java   ← 인벤토리/장비
│           └── ShopScreen.java        ← 희귀 상점
🕹 주요 기능
🔐 회원 시스템
회원가입 (아이디/비번/이메일)
로그인 후 자동 저장 (홈 디렉토리에 저장)
신규 유저: 닉네임 + 캐릭터 선택
🧙 캐릭터 (5종)
캐릭터	특징
⚔️ 전사	고HP, 균형형
🔮 마법사	고공격, 저HP
🏹 궁수	균형형, 빠른 속도
🛡️ 성기사	고방어, 안정형
🗡️ 암살자	최고속도, 고공격
⚔️ 던전
레벨에 따라 몬스터 강화
일반/강화(Elite) 몬스터 출현
드롭: 장비, 포션, 골드 (확률적)
🏯 무한의 탑 (100층)
5의 배수마다 미니보스 등장
스테이지별 몬스터 강화
클리어한 층은 저장됨
🏪 상점
던전 드롭 불가 희귀 아이템 판매
Epic/Legendary 등급 장비
🎒 인벤토리
장비 착용/해제 (5슬롯)
포션 즉시 사용
아이템 버리기
📈 레벨 시스템
최대 99레벨
초반: 쉬운 경험치 → 고레벨: 지수적 증가
레벨업 시 HP 전체 회복
💾 저장 위치
~/RPGGameSave/users.dat (자동 생성)

⚙️ 요구사항
Java 11 이상 (Java 17 권장)
Eclipse IDE (Java EE 또는 Java Developer)
