package game.ui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Procedurally generates all game artwork using Java2D.
 * No external image files needed – works in any Eclipse environment.
 */
public class ImageGen {

    private static final Map<String, BufferedImage> CACHE = new HashMap<>();

    // ─────────────────────────────────────────────────
    //  PUBLIC API
    // ─────────────────────────────────────────────────

    public static BufferedImage getMonster(String key, int w, int h) {
        String cacheKey = "monster_" + key + "_" + w + "_" + h;
        return CACHE.computeIfAbsent(cacheKey, k -> drawMonster(key, w, h));
    }

    public static BufferedImage getCharacter(String key, int w, int h) {
        String cacheKey = "char_" + key + "_" + w + "_" + h;
        return CACHE.computeIfAbsent(cacheKey, k -> drawCharacter(key, w, h));
    }

    public static BufferedImage getDungeonBg(String type, int w, int h) {
        String cacheKey = "dunbg_" + type + "_" + w + "_" + h;
        return CACHE.computeIfAbsent(cacheKey, k -> drawDungeonBg(type, w, h));
    }

    public static BufferedImage getHitEffect(boolean isPlayer, int w, int h) {
        return isPlayer ? drawHitRed(w, h) : drawHitYellow(w, h);
    }

    /**
     * Render a character with visible equipment overlays.
     * Equipment tier changes the color/glow of armor pieces on the character.
     */
    public static BufferedImage getCharacterEquipped(String charKey, game.inventory.Item[] equipment, int w, int h) {
        // 장착 효과(오라/파란색 덮임) 제거 - 캐릭터 원본 이미지만 반환
        String ck = "char_" + charKey + "_" + w + "_" + h;
        return CACHE.computeIfAbsent(ck, k -> drawCharacter(charKey, w, h));
    }

    /** Force re-render when equipment changes (clears relevant cache entries) */
    public static void invalidateCharacterCache(String charKey) {
        CACHE.keySet().removeIf(k -> k.startsWith("ceq_" + charKey));
    }

    // 장착 효과 함수들 제거됨 (오라/색상 오버레이 없음)

    public static BufferedImage getDungeonCard(String name, String emoji, String desc1, String desc2,
                                                Color accent, int w, int h) {
        String key = "dcard_" + name + "_" + w;
        return CACHE.computeIfAbsent(key, k -> drawDungeonCard(name, emoji, desc1, desc2, accent, w, h));
    }

    // ─────────────────────────────────────────────────
    //  MONSTER DRAWING
    // ─────────────────────────────────────────────────

    private static BufferedImage drawMonster(String key, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setup(img);
        switch (key) {
            case "slime":      drawSlime(g, w, h, new Color(80, 200, 80), false); break;
            case "slime_e":    drawSlime(g, w, h, new Color(255, 80, 80), true); break;
            case "goblin":     drawGoblin(g, w, h, false); break;
            case "goblin_e":   drawGoblin(g, w, h, true); break;
            case "bat":        drawBat(g, w, h, false); break;
            case "bat_e":      drawBat(g, w, h, true); break;
            case "skeleton":   drawSkeleton(g, w, h, false); break;
            case "skeleton_e": drawSkeleton(g, w, h, true); break;
            case "orc":        drawOrc(g, w, h, false); break;
            case "orc_e":      drawOrc(g, w, h, true); break;
            case "troll":      drawTroll(g, w, h, false); break;
            case "troll_e":    drawTroll(g, w, h, true); break;
            case "dragon":     drawDragon(g, w, h, false); break;
            case "dragon_e":   drawDragon(g, w, h, true); break;
            case "demon":      drawDemon(g, w, h, false); break;
            case "demon_e":    drawDemon(g, w, h, true); break;
            case "lich":       drawLich(g, w, h, false); break;
            case "lich_e":     drawLich(g, w, h, true); break;
            case "boss":       drawBoss(g, w, h); break;
            default:           drawSlime(g, w, h, new Color(80,200,80), false);
        }
        g.dispose();
        return img;
    }

    private static void drawSlime(Graphics2D g, int w, int h, Color base, boolean elite) {
        int cx = w/2, cy = h*2/3, r = Math.min(w,h)*2/5;
        if (elite) drawGlow(g, cx, cy, r+10, new Color(255,50,50,80));
        // Body
        g.setColor(base); g.fillOval(cx-r, cy-r*3/4, r*2, r*3/2);
        // Shine
        g.setColor(new Color(255,255,255,100)); g.fillOval(cx-r/3, cy-r/2, r/3, r/4);
        // Eyes
        g.setColor(Color.WHITE); g.fillOval(cx-r/3-5, cy-r/4, 14, 14);
        g.fillOval(cx+r/3-9, cy-r/4, 14, 14);
        g.setColor(Color.BLACK); g.fillOval(cx-r/3, cy-r/4+3, 8, 8);
        g.fillOval(cx+r/3-4, cy-r/4+3, 8, 8);
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,14)); g.drawString("★",cx-8,cy-r+5); }
    }

    private static void drawGoblin(Graphics2D g, int w, int h, boolean elite) {
        int cx = w/2, bot = h-10;
        Color skin = elite ? new Color(180,80,80) : new Color(100,160,60);
        if (elite) drawGlow(g, cx, bot-40, 45, new Color(255,50,50,80));
        // Body
        g.setColor(skin.darker()); g.fillRoundRect(cx-18, bot-60, 36, 50, 10, 10);
        // Head
        g.setColor(skin); g.fillOval(cx-22, bot-110, 44, 44);
        // Eyes
        g.setColor(Color.RED); g.fillOval(cx-14, bot-96, 10, 10); g.fillOval(cx+4, bot-96, 10, 10);
        // Ears (pointy)
        int[] ex1={cx-22,cx-30,cx-18}; int[] ey1={bot-90,bot-110,bot-100};
        int[] ex2={cx+22,cx+30,cx+18}; int[] ey2={bot-90,bot-110,bot-100};
        g.setColor(skin); g.fillPolygon(ex1,ey1,3); g.fillPolygon(ex2,ey2,3);
        // Mouth/teeth
        g.setColor(Color.WHITE); g.fillRect(cx-8, bot-82, 5, 8); g.fillRect(cx+3, bot-82, 5, 8);
        // Weapon
        g.setColor(new Color(120,80,40)); g.fillRect(cx+16, bot-80, 6, 60);
        g.setColor(Color.GRAY); g.fillPolygon(new int[]{cx+16,cx+22,cx+19},new int[]{bot-80,bot-80,bot-100},3);
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,14)); g.drawString("★",cx-8,bot-115); }
    }

    private static void drawBat(Graphics2D g, int w, int h, boolean elite) {
        int cx = w/2, cy = h/2;
        Color c = elite ? new Color(150,0,150) : new Color(60,20,80);
        if (elite) drawGlow(g, cx, cy, 50, new Color(180,0,255,70));
        // Wings
        int[] wx1={cx,cx-70,cx-30,cx-10}; int[] wy1={cy,cy-20,cy+20,cy-5};
        int[] wx2={cx,cx+70,cx+30,cx+10}; int[] wy2={cy,cy-20,cy+20,cy-5};
        g.setColor(c); g.fillPolygon(wx1,wy1,4); g.fillPolygon(wx2,wy2,4);
        // Body
        g.setColor(c.darker()); g.fillOval(cx-18,cy-15,36,36);
        // Head
        g.setColor(c); g.fillOval(cx-14,cy-32,28,28);
        // Ears
        g.fillPolygon(new int[]{cx-14,cx-20,cx-6},new int[]{cy-32,cy-52,cy-40},3);
        g.fillPolygon(new int[]{cx+14,cx+20,cx+6},new int[]{cy-32,cy-52,cy-40},3);
        // Eyes
        g.setColor(new Color(255,80,0)); g.fillOval(cx-9,cy-28,8,8); g.fillOval(cx+1,cy-28,8,8);
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,14)); g.drawString("★",cx-8,cy-58); }
    }

    private static void drawSkeleton(Graphics2D g, int w, int h, boolean elite) {
        int cx = w/2, bot = h-10;
        Color bone = elite ? new Color(220,180,255) : new Color(220,220,200);
        if (elite) drawGlow(g, cx, bot-55, 55, new Color(180,100,255,80));
        // Body ribs
        g.setColor(bone);
        for(int i=0;i<4;i++) g.fillRoundRect(cx-15, bot-100+i*14, 30, 10, 4, 4);
        // Legs
        g.fillRect(cx-14, bot-50, 10, 50); g.fillRect(cx+4, bot-50, 10, 50);
        // Arms
        g.fillRect(cx-30, bot-110, 12, 50); g.fillRect(cx+18, bot-110, 12, 50);
        // Head
        g.fillOval(cx-20, bot-148, 40, 40);
        // Eye sockets
        g.setColor(new Color(0,0,0,200)); g.fillOval(cx-14, bot-140, 12, 14); g.fillOval(cx+2, bot-140, 12, 14);
        // Glowing eyes
        g.setColor(elite ? new Color(200,0,255) : new Color(0,255,150)); g.fillOval(cx-11,bot-137,6,8); g.fillOval(cx+5,bot-137,6,8);
        // Jaw
        g.setColor(bone); g.fillRoundRect(cx-14, bot-113, 28, 10, 3, 3);
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,14)); g.drawString("★",cx-8,bot-158); }
    }

    private static void drawOrc(Graphics2D g, int w, int h, boolean elite) {
        int cx = w/2, bot = h-10;
        Color skin = elite ? new Color(0,140,60) : new Color(80,140,60);
        if (elite) drawGlow(g, cx, bot-60, 60, new Color(0,255,80,70));
        // Body (bulky)
        g.setColor(skin.darker()); g.fillRoundRect(cx-28, bot-80, 56, 70, 12, 12);
        // Arms (big)
        g.setColor(skin); g.fillRoundRect(cx-52, bot-78, 26, 55, 10, 10);
        g.fillRoundRect(cx+26, bot-78, 26, 55, 10, 10);
        // Head
        g.setColor(skin); g.fillOval(cx-26, bot-130, 52, 52);
        // Brow ridge
        g.setColor(skin.darker()); g.fillRect(cx-24, bot-118, 48, 10);
        // Eyes (red)
        g.setColor(Color.RED); g.fillOval(cx-16, bot-115, 12, 12); g.fillOval(cx+4, bot-115, 12, 12);
        // Tusks
        g.setColor(Color.WHITE); g.fillPolygon(new int[]{cx-10,cx-6,cx-14},new int[]{bot-88,bot-88,bot-72},3);
        g.fillPolygon(new int[]{cx+10,cx+6,cx+14},new int[]{bot-88,bot-88,bot-72},3);
        // Weapon (axe)
        g.setColor(new Color(100,70,30)); g.fillRect(cx+36, bot-110, 8, 90);
        g.setColor(Color.GRAY); g.fillPolygon(new int[]{cx+36,cx+36,cx+60,cx+65},new int[]{bot-110,bot-75,bot-85,bot-100},4);
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,14)); g.drawString("★",cx-8,bot-140); }
    }

    private static void drawTroll(Graphics2D g, int w, int h, boolean elite) {
        int cx = w/2, bot = h-8;
        Color skin = elite ? new Color(100,60,200) : new Color(80,100,60);
        if (elite) drawGlow(g, cx, bot-70, 75, new Color(100,50,255,80));
        // Large body
        g.setColor(skin); g.fillRoundRect(cx-36, bot-100, 72, 90, 16, 16);
        // Huge arms
        g.fillRoundRect(cx-65, bot-95, 32, 65, 14, 14);
        g.fillRoundRect(cx+33, bot-95, 32, 65, 14, 14);
        // Big head
        g.fillOval(cx-30, bot-148, 60, 55);
        // Nose
        g.setColor(skin.darker()); g.fillOval(cx-8, bot-120, 16, 18);
        // Eyes
        g.setColor(elite ? Color.MAGENTA : new Color(255,200,0)); g.fillOval(cx-18,bot-138,14,14); g.fillOval(cx+4,bot-138,14,14);
        g.setColor(Color.BLACK); g.fillOval(cx-14,bot-136,8,8); g.fillOval(cx+8,bot-136,8,8);
        // Club
        g.setColor(new Color(80,50,20)); g.fillRect(cx+42, bot-120, 12, 90);
        g.fillOval(cx+36, bot-130, 26, 26);
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,14)); g.drawString("★",cx-8,bot-160); }
    }

    private static void drawDragon(Graphics2D g, int w, int h, boolean elite) {
        int cx = w/2, cy = h/2;
        Color body = elite ? new Color(200,50,0) : new Color(150,20,20);
        if (elite) drawGlow(g, cx, cy+10, 80, new Color(255,80,0,90));
        // Wings
        int[] wx1={cx-10,cx-w/2+5,cx-40,cx-15}; int[] wy1={cy-20,cy-60,cy+10,cy+5};
        int[] wx2={cx+10,cx+w/2-5,cx+40,cx+15}; int[] wy2={cy-20,cy-60,cy+10,cy+5};
        g.setColor(body.darker()); g.fillPolygon(wx1,wy1,4); g.fillPolygon(wx2,wy2,4);
        // Body
        g.setColor(body); g.fillOval(cx-35, cy-10, 70, 60);
        // Neck+Head
        g.fillRoundRect(cx-16, cy-55, 32, 50, 10, 10);
        g.fillOval(cx-22, cy-78, 44, 36);
        // Horns
        g.setColor(new Color(50,30,0)); g.fillPolygon(new int[]{cx-20,cx-28,cx-14},new int[]{cy-78,cy-105,cy-95},3);
        g.fillPolygon(new int[]{cx+20,cx+28,cx+14},new int[]{cy-78,cy-105,cy-95},3);
        // Eyes (fire)
        g.setColor(new Color(255,200,0)); g.fillOval(cx-14,cy-72,12,12); g.fillOval(cx+2,cy-72,12,12);
        g.setColor(new Color(255,50,0)); g.fillOval(cx-11,cy-69,6,6); g.fillOval(cx+5,cy-69,6,6);
        // Fire breath
        if (elite) {
            for(int i=0;i<5;i++) {
                g.setColor(new Color(255,100+i*20,0, 200-i*35));
                g.fillOval(cx+22+i*12, cy-65+i*3, 20-i*2, 14-i*2);
            }
        }
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,16)); g.drawString("★",cx-8,cy-110); }
    }

    private static void drawDemon(Graphics2D g, int w, int h, boolean elite) {
        int cx = w/2, bot = h-10;
        Color c = elite ? new Color(200,0,100) : new Color(180,0,30);
        if (elite) drawGlow(g, cx, bot-60, 65, new Color(255,0,80,80));
        // Body
        g.setColor(c); g.fillRoundRect(cx-25, bot-95, 50, 80, 12, 12);
        // Wings (bat-style)
        int[] wx1={cx-20,cx-75,cx-40,cx-20}; int[] wy1={bot-95,bot-140,bot-110,bot-80};
        int[] wx2={cx+20,cx+75,cx+40,cx+20}; int[] wy2={bot-95,bot-140,bot-110,bot-80};
        g.setColor(c.darker()); g.fillPolygon(wx1,wy1,4); g.fillPolygon(wx2,wy2,4);
        // Head
        g.setColor(c); g.fillOval(cx-24, bot-145, 48, 48);
        // Horns
        g.setColor(new Color(60,0,0)); g.fillPolygon(new int[]{cx-18,cx-28,cx-10},new int[]{bot-145,bot-175,bot-162},3);
        g.fillPolygon(new int[]{cx+18,cx+28,cx+10},new int[]{bot-145,bot-175,bot-162},3);
        // Eyes (glowing)
        g.setColor(new Color(255,200,0)); g.fillOval(cx-14,bot-136,14,14); g.fillOval(cx+0,bot-136,14,14);
        g.setColor(Color.RED); g.fillOval(cx-10,bot-133,6,6); g.fillOval(cx+4,bot-133,6,6);
        // Tail
        g.setColor(c); g.drawArc(cx+20,bot-40,30,50,90,200); // arc as tail hint
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,14)); g.drawString("★",cx-8,bot-185); }
    }

    private static void drawLich(Graphics2D g, int w, int h, boolean elite) {
        int cx = w/2, bot = h-10;
        if (elite) drawGlow(g, cx, bot-70, 70, new Color(100,0,200,90));
        // Robe
        g.setColor(new Color(20,0,40)); g.fillPolygon(new int[]{cx-30,cx+30,cx+20,cx-20},new int[]{bot-80,bot-80,bot,bot},4);
        // Arm bones
        g.setColor(new Color(210,200,190)); g.fillRect(cx-42,bot-110,10,50); g.fillRect(cx+32,bot-110,10,50);
        // Skull body
        g.setColor(new Color(200,190,180)); g.fillOval(cx-24,bot-148,48,48);
        // Eye sockets (purple glow)
        g.setColor(new Color(0,0,0,220)); g.fillOval(cx-16,bot-140,14,16); g.fillOval(cx+2,bot-140,14,16);
        g.setColor(elite ? new Color(255,0,255) : new Color(120,0,200)); g.fillOval(cx-13,bot-138,8,10); g.fillOval(cx+5,bot-138,8,10);
        // Crown
        g.setColor(new Color(180,130,0));
        for(int i=-1;i<=1;i++) g.fillRect(cx+i*14-3, bot-162, 6, 16);
        g.fillRect(cx-18,bot-152,36,8);
        // Staff
        g.setColor(new Color(60,40,80)); g.fillRect(cx+30,bot-160,6,160);
        g.setColor(new Color(120,0,200)); g.fillOval(cx+24,bot-172,18,18);
        drawGlow(g, cx+33, bot-163, 12, new Color(180,0,255,120));
        if (elite) { g.setColor(new Color(255,200,0)); g.setFont(new Font("SansSerif",Font.BOLD,14)); g.drawString("★",cx-8,bot-168); }
    }

    private static void drawBoss(Graphics2D g, int w, int h) {
        int cx = w/2, cy = h/2;
        drawGlow(g, cx, cy, 90, new Color(255,100,0,100));
        drawGlow(g, cx, cy, 60, new Color(255,50,0,80));
        // Main body (armored demon lord)
        g.setColor(new Color(60,0,0)); g.fillOval(cx-45, cy-20, 90, 80);
        g.setColor(new Color(120,10,10)); g.fillOval(cx-38, cy-60, 76, 80);
        // Huge wings
        int[] wx1={cx-30,cx-w/2+5,cx-50,cx-20}; int[] wy1={cy-30,cy-80,cy+10,cy+10};
        int[] wx2={cx+30,cx+w/2-5,cx+50,cx+20}; int[] wy2={cy-30,cy-80,cy+10,cy+10};
        g.setColor(new Color(80,0,0)); g.fillPolygon(wx1,wy1,4); g.fillPolygon(wx2,wy2,4);
        // Crown of horns
        g.setColor(new Color(40,20,0));
        int[] hx={cx-30,cx-16,cx,cx+16,cx+30};
        int[] hy={cy-85,cy-110,cy-100,cy-110,cy-85};
        for(int i=0;i<5;i++) g.fillPolygon(new int[]{hx[i]-6,hx[i]+6,hx[i]},new int[]{cy-60,cy-60,hy[i]},3);
        // Face
        g.setColor(new Color(180,20,0)); g.fillOval(cx-28,cy-82,56,52);
        // Glowing eyes (3 pairs)
        g.setColor(new Color(255,200,0));
        g.fillOval(cx-20,cy-72,16,16); g.fillOval(cx+4,cy-72,16,16);
        g.setColor(Color.WHITE); g.fillOval(cx-14,cy-68,6,6); g.fillOval(cx+10,cy-68,6,6);
        // Aura particles
        g.setColor(new Color(255,80,0,150));
        for(int i=0;i<8;i++) {
            double angle = i * Math.PI/4;
            int px = cx+(int)(85*Math.cos(angle)), py = cy+(int)(80*Math.sin(angle));
            g.fillOval(px-5,py-5,10,10);
        }
    }

    // ─────────────────────────────────────────────────
    //  CHARACTER DRAWING
    // ─────────────────────────────────────────────────

    private static BufferedImage drawCharacter(String key, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setup(img);
        switch (key) {
            case "WARRIOR":  drawWarrior(g, w, h); break;
            case "MAGE":     drawMage(g, w, h); break;
            case "ARCHER":   drawArcher(g, w, h); break;
            case "PALADIN":  drawPaladin(g, w, h); break;
            case "ASSASSIN": drawAssassin(g, w, h); break;
            default:         drawWarrior(g, w, h);
        }
        g.dispose();
        return img;
    }

    private static void drawWarrior(Graphics2D g, int w, int h) {
        int cx = w/2, bot = h-8;
        // Outer aura
        drawGlow(g, cx, bot-90, 80, new Color(100,140,255,55));
        drawGlow(g, cx, bot-90, 50, new Color(180,200,255,40));

        // ── BOOTS (rounded greaves) ──
        g.setColor(new Color(50,52,72));
        g.fillRoundRect(cx-19,bot-52,15,52,6,6); g.fillRoundRect(cx+4,bot-52,15,52,6,6);
        g.setColor(new Color(80,70,50));
        g.fillRoundRect(cx-21,bot-20,17,20,5,5); g.fillRoundRect(cx+4,bot-20,17,20,5,5);
        g.setColor(new Color(110,100,70));
        g.fillRect(cx-21,bot-22,17,3); g.fillRect(cx+4,bot-22,17,3);

        // ── LOWER LEGS (shin guards) ──
        g.setColor(new Color(90,95,120));
        g.fillRoundRect(cx-18,bot-52,14,34,5,5); g.fillRoundRect(cx+4,bot-52,14,34,5,5);
        g.setColor(new Color(130,138,165));
        g.fillRoundRect(cx-17,bot-50,6,26,3,3); g.fillRoundRect(cx+11,bot-50,6,26,3,3);

        // ── THIGHS & BELT ──
        g.setColor(new Color(80,84,110));
        g.fillRoundRect(cx-20,bot-85,18,36,6,6); g.fillRoundRect(cx+2,bot-85,18,36,6,6);
        g.setColor(new Color(70,55,35));
        g.fillRoundRect(cx-24,bot-90,48,10,4,4);
        g.setColor(new Color(120,95,50));
        g.fillRect(cx-3,bot-90,6,10);

        // ── CHEST PLATE (heavy, layered) ──
        g.setColor(new Color(88,95,125));
        g.fillRoundRect(cx-26,bot-135,52,55,10,10);
        g.setColor(new Color(115,122,155));
        g.fillRoundRect(cx-22,bot-133,44,22,7,7);
        g.setColor(new Color(140,148,180));
        g.fillRoundRect(cx-16,bot-131,32,14,5,5);
        // Chest center ridge
        g.setColor(new Color(160,168,200));
        g.fillRect(cx-2,bot-133,4,53);
        // Rivet details
        g.setColor(new Color(200,195,170));
        int[] rv = {bot-128,bot-115,bot-102}; for(int ry:rv){g.fillOval(cx-18,ry,5,5);g.fillOval(cx+13,ry,5,5);}

        // ── PAULDRONS (shoulder guards) ──
        g.setColor(new Color(88,95,125));
        g.fillOval(cx-42,bot-140,28,22); g.fillOval(cx+14,bot-140,28,22);
        g.setColor(new Color(130,138,165));
        g.fillOval(cx-40,bot-138,24,18); g.fillOval(cx+16,bot-138,24,18);
        // Pauldron ridges
        g.setColor(new Color(160,165,195));
        g.fillRect(cx-38,bot-134,20,4); g.fillRect(cx+18,bot-134,20,4);

        // ── ARMS (vambraces) ──
        g.setColor(new Color(90,96,125));
        g.fillRoundRect(cx-44,bot-130,18,50,8,8); g.fillRoundRect(cx+26,bot-130,18,50,8,8);
        g.setColor(new Color(120,126,155));
        g.fillRoundRect(cx-42,bot-128,14,18,5,5); g.fillRoundRect(cx+28,bot-128,14,18,5,5);
        // Gauntlets
        g.setColor(new Color(75,80,105));
        g.fillRoundRect(cx-44,bot-85,18,22,6,6); g.fillRoundRect(cx+26,bot-85,18,22,6,6);

        // ── SHIELD (heater-style, left) ──
        int[] shx={cx-70,cx-44,cx-56}; int[] shy={bot-138,bot-138,bot-92};
        g.setColor(new Color(160,40,40)); g.fillPolygon(shx,shy,3);
        g.setColor(new Color(200,55,55)); g.fillPolygon(new int[]{cx-68,cx-46,cx-56},new int[]{bot-136,bot-136,bot-96},3);
        // Shield emblem (cross)
        g.setColor(new Color(230,210,150));
        g.fillRect(cx-59,bot-128,6,22); g.fillRect(cx-65,bot-120,18,6);
        // Shield border
        g.setColor(new Color(130,110,50));
        g.setStroke(new BasicStroke(2)); g.drawPolygon(shx,shy,3); g.setStroke(new BasicStroke(1));

        // ── SWORD (right, raised) ──
        // Handle
        g.setColor(new Color(90,65,35)); g.fillRoundRect(cx+42,bot-115,7,45,3,3);
        // Grip wrap
        g.setColor(new Color(60,40,20));
        for(int gy=bot-112;gy>bot-75;gy-=6) g.fillRect(cx+42,gy,7,3);
        // Cross-guard
        g.setColor(new Color(160,140,60)); g.fillRoundRect(cx+32,bot-120,28,9,4,4);
        // Blade
        g.setColor(new Color(195,200,220));
        g.fillPolygon(new int[]{cx+43,cx+50,cx+47},new int[]{bot-120,bot-120,bot-185},3);
        // Blade edge shine
        g.setColor(new Color(230,235,255,200));
        g.fillPolygon(new int[]{cx+43,cx+46,cx+47},new int[]{bot-120,bot-120,bot-182},3);
        // Blade glow
        drawGlow(g, cx+46, bot-155, 10, new Color(180,200,255,80));

        // ── HELM (full great-helm) ──
        g.setColor(new Color(88,95,125));
        g.fillOval(cx-22,bot-172,44,44);
        // Face plate
        g.setColor(new Color(75,80,108));
        g.fillRoundRect(cx-18,bot-162,36,30,5,5);
        // Cheek guards
        g.setColor(new Color(82,88,115));
        g.fillRoundRect(cx-20,bot-158,10,22,4,4); g.fillRoundRect(cx+10,bot-158,10,22,4,4);
        // Visor slits (T-shape)
        g.setColor(new Color(255,200,80,220));
        g.fillRect(cx-14,bot-155,28,4); // horizontal
        g.fillRect(cx-2,bot-160,4,12);  // vertical
        // Crest on top
        g.setColor(new Color(180,30,30));
        g.fillPolygon(new int[]{cx-8,cx+8,cx+5,cx-5},new int[]{bot-172,bot-172,bot-192,bot-192},4);
        g.setColor(new Color(220,50,50));
        g.fillRect(cx-4,bot-195,8,6);
        // Helm rivets
        g.setColor(new Color(190,185,160));
        g.fillOval(cx-19,bot-170,5,5); g.fillOval(cx+14,bot-170,5,5);
    }

    private static void drawMage(Graphics2D g, int w, int h) {
        int cx = w/2, bot = h-8;
        drawGlow(g, cx, bot-90, 85, new Color(80,60,220,60));
        drawGlow(g, cx, bot-90, 45, new Color(140,100,255,50));

        // ── BOOTS ──
        g.setColor(new Color(35,20,65));
        g.fillRoundRect(cx-15,bot-50,12,50,5,5); g.fillRoundRect(cx+3,bot-50,12,50,5,5);
        g.setColor(new Color(55,35,90));
        g.fillRoundRect(cx-17,bot-22,14,22,5,5); g.fillRoundRect(cx+3,bot-22,14,22,5,5);
        // Gold boot trim
        g.setColor(new Color(180,140,40)); g.fillRect(cx-17,bot-24,14,3); g.fillRect(cx+3,bot-24,14,3);

        // ── ROBE (layered) ──
        // Outer robe
        g.setColor(new Color(45,22,100));
        g.fillPolygon(new int[]{cx-26,cx+26,cx+35,cx-35},new int[]{bot-98,bot-98,bot,bot},4);
        // Inner robe highlight
        g.setColor(new Color(65,38,135));
        g.fillPolygon(new int[]{cx-20,cx+20,cx+26,cx-26},new int[]{bot-98,bot-98,bot-25,bot-25},4);
        // Robe center panel
        g.setColor(new Color(80,48,160));
        g.fillPolygon(new int[]{cx-8,cx+8,cx+10,cx-10},new int[]{bot-98,bot-98,bot-10,bot-10},4);
        // Gold hem decorations
        g.setColor(new Color(190,155,50));
        for(int rx=-28;rx<=28;rx+=8) g.fillOval(cx+rx-2,bot-6,4,4);

        // ── UPPER BODY ──
        g.setColor(new Color(55,30,115));
        g.fillRoundRect(cx-22,bot-135,44,45,8,8);
        // Sash/belt
        g.setColor(new Color(160,110,30));
        g.fillRoundRect(cx-24,bot-100,48,12,4,4);
        g.setColor(new Color(200,160,60));
        g.fillRoundRect(cx-4,bot-100,8,12,3,3); // buckle

        // ── SLEEVES (wide, billowing) ──
        g.setColor(new Color(45,22,100));
        g.fillRoundRect(cx-46,bot-130,24,52,10,10);
        g.fillRoundRect(cx+22,bot-130,24,52,10,10);
        // Sleeve cuffs
        g.setColor(new Color(190,155,50));
        g.fillRoundRect(cx-46,bot-84,24,8,4,4); g.fillRoundRect(cx+22,bot-84,24,8,4,4);

        // ── MAGIC STAFF (ornate) ──
        // Staff shaft
        g.setColor(new Color(80,55,25)); g.fillRect(cx+40,bot-170,6,170);
        // Shaft bands
        g.setColor(new Color(160,130,40));
        for(int sy=bot-40;sy>bot-160;sy-=25) g.fillRect(cx+38,sy,10,4);
        // Orb housing
        g.setColor(new Color(50,35,80)); g.fillOval(cx+30,bot-185,28,28);
        g.setColor(new Color(70,50,110)); g.fillOval(cx+32,bot-183,24,24);
        // Glowing orb core
        drawGlow(g, cx+44, bot-171, 22, new Color(100,150,255,180));
        g.setColor(new Color(140,180,255)); g.fillOval(cx+35,bot-180,18,18);
        g.setColor(new Color(200,220,255)); g.fillOval(cx+38,bot-177,10,10);
        g.setColor(new Color(255,255,255,200)); g.fillOval(cx+40,bot-175,5,5);
        // Prongs around orb
        g.setColor(new Color(180,150,50));
        g.fillPolygon(new int[]{cx+40,cx+44,cx+48},new int[]{bot-185,bot-198,bot-185},3);
        g.fillPolygon(new int[]{cx+30,cx+26,cx+30},new int[]{bot-175,bot-171,bot-167},3);

        // ── MAGICAL FLOATING RUNES ──
        g.setFont(new Font("SansSerif",Font.BOLD,10));
        Color[] runeC={new Color(100,150,255,160),new Color(200,100,255,140),new Color(80,200,255,150)};
        String[] runes={"✦","◈","⬡"};
        int[][] rpos={{cx-40,bot-110},{cx+48,bot-130},{cx-35,bot-60}};
        for(int i=0;i<3;i++){g.setColor(runeC[i]);drawGlow(g,rpos[i][0],rpos[i][1],8,runeC[i]);g.drawString(runes[i],rpos[i][0]-5,rpos[i][1]+4);}

        // ── FACE ──
        g.setColor(new Color(200,175,145)); g.fillOval(cx-17,bot-165,34,34);
        g.setColor(new Color(220,195,165)); g.fillOval(cx-14,bot-162,28,26);
        // Eyes (glowing purple)
        g.setColor(new Color(140,80,255)); g.fillOval(cx-10,bot-156,10,10); g.fillOval(cx+0,bot-156,10,10);
        g.setColor(new Color(200,160,255)); g.fillOval(cx-8,bot-154,5,5); g.fillOval(cx+2,bot-154,5,5);
        // Beard (optional)
        g.setColor(new Color(160,140,120)); g.fillRoundRect(cx-8,bot-143,16,8,4,4);

        // ── POINTED HAT (elaborate) ──
        g.setColor(new Color(38,18,85));
        g.fillPolygon(new int[]{cx,cx-26,cx+26},new int[]{bot-210,bot-138,bot-138},3);
        g.setColor(new Color(55,28,110));
        g.fillPolygon(new int[]{cx,cx-22,cx+22},new int[]{bot-207,bot-140,bot-140},3);
        // Hat brim
        g.setColor(new Color(45,22,95)); g.fillOval(cx-30,bot-148,60,16);
        g.setColor(new Color(65,38,130)); g.fillOval(cx-27,bot-147,54,12);
        // Hat band
        g.setColor(new Color(180,140,40)); g.fillRect(cx-26,bot-148,52,5);
        // Star on hat
        g.setColor(new Color(200,180,255,200)); g.setFont(new Font("SansSerif",Font.BOLD,14));
        g.drawString("★",cx-7,bot-175);
    }

    private static void drawArcher(Graphics2D g, int w, int h) {
        int cx = w/2, bot = h-8;
        drawGlow(g, cx, bot-85, 70, new Color(50,180,60,55));
        drawGlow(g, cx, bot-85, 38, new Color(100,220,80,40));

        // ── BOOTS ──
        g.setColor(new Color(40,28,15));
        g.fillRoundRect(cx-15,bot-52,12,52,5,5); g.fillRoundRect(cx+3,bot-52,12,52,5,5);
        g.setColor(new Color(60,42,22));
        g.fillRoundRect(cx-17,bot-24,14,24,5,5); g.fillRoundRect(cx+3,bot-24,14,24,5,5);
        // Boot buckles
        g.setColor(new Color(160,130,50)); g.fillRect(cx-17,bot-30,5,4); g.fillRect(cx+12,bot-30,5,4);

        // ── LEGS (leather + greaves) ──
        g.setColor(new Color(65,85,40));
        g.fillRect(cx-14,bot-52,11,32); g.fillRect(cx+3,bot-52,11,32);
        // Knee pads
        g.setColor(new Color(90,65,35)); g.fillOval(cx-16,bot-62,14,14); g.fillOval(cx+2,bot-62,14,14);
        g.setColor(new Color(115,85,45)); g.fillOval(cx-14,bot-60,10,10); g.fillOval(cx+4,bot-60,10,10);

        // ── LOWER BODY / TUNIC ──
        g.setColor(new Color(80,105,50));
        g.fillPolygon(new int[]{cx-22,cx+22,cx+18,cx-18},new int[]{bot-95,bot-95,bot-60,bot-60},4);
        g.setColor(new Color(100,130,65));
        g.fillRect(cx-18,bot-93,36,26);

        // ── TORSO (leather jerkin) ──
        g.setColor(new Color(90,65,35));
        g.fillRoundRect(cx-21,bot-135,42,45,7,7);
        g.setColor(new Color(115,85,50));
        g.fillRoundRect(cx-17,bot-133,34,18,5,5);
        // Jerkin stitching
        g.setColor(new Color(70,50,25));
        for(int ly=bot-125;ly>bot-95;ly-=7) {g.drawLine(cx-17,ly,cx-14,ly); g.drawLine(cx+14,ly,cx+17,ly);}
        // Quiver strap across chest
        g.setColor(new Color(65,45,20)); g.fillRect(cx-18,bot-133,5,45);
        g.setColor(new Color(95,70,35)); g.fillRect(cx-19,bot-131,7,3);

        // ── QUIVER (back-right) ──
        g.setColor(new Color(80,55,25)); g.fillRoundRect(cx+28,bot-120,16,45,4,4);
        g.setColor(new Color(100,70,35)); g.fillRoundRect(cx+28,bot-122,16,8,3,3);
        // Arrow tails in quiver
        g.setColor(new Color(200,170,100));
        for(int ax=cx+31;ax<cx+42;ax+=4) g.fillRect(ax,bot-140,2,20);
        g.setColor(new Color(80,160,80)); g.fillRect(cx+31,bot-145,2,7);g.fillRect(cx+35,bot-143,2,7);g.fillRect(cx+39,bot-144,2,7);

        // ── CLOAK (partial) ──
        g.setColor(new Color(50,70,35));
        g.fillPolygon(new int[]{cx+18,cx+36,cx+30},new int[]{bot-130,bot-80,bot-60},3);
        g.setColor(new Color(65,88,45));
        g.fillPolygon(new int[]{cx+18,cx+34,cx+28},new int[]{bot-128,bot-82,bot-62},3);

        // ── ARMS ──
        // Right arm (bow arm, extended)
        g.setColor(new Color(90,65,35)); g.fillRoundRect(cx-44,bot-125,18,48,7,7);
        // Left arm (draw arm)
        g.setColor(new Color(90,65,35)); g.fillRoundRect(cx+22,bot-122,18,40,7,7);
        // Arm bracers
        g.setColor(new Color(65,45,20)); g.fillRoundRect(cx-45,bot-90,20,16,4,4);
        g.setColor(new Color(65,45,20)); g.fillRoundRect(cx+21,bot-88,20,14,4,4);
        g.setColor(new Color(130,105,50)); g.fillRect(cx-44,bot-92,18,3); g.fillRect(cx+22,bot-90,18,3);

        // ── LONGBOW (elaborate) ──
        g.setColor(new Color(90,55,18));
        g.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g.drawArc(cx-60,bot-140,18,115,-80,160);
        // Bow limb detail
        g.setColor(new Color(130,85,30));
        g.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g.drawArc(cx-58,bot-136,14,106,-70,140);
        // Bowstring
        g.setColor(new Color(230,210,160));
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(cx-50,bot-132,cx-50,bot-30);
        // Arrow nocked
        g.setColor(new Color(120,80,30)); g.fillRect(cx-35,bot-82,40,3);
        g.setColor(new Color(180,180,200)); g.fillPolygon(new int[]{cx+5,cx+17,cx+5},new int[]{bot-86,bot-81,bot-76},3);
        g.setColor(new Color(80,150,60)); g.fillRect(cx-36,bot-87,5,6); // fletching 1
        g.setColor(new Color(60,120,50)); g.fillRect(cx-36,bot-80,5,5); // fletching 2
        g.setStroke(new BasicStroke(1));

        // ── HOOD + FACE ──
        g.setColor(new Color(50,70,30)); g.fillOval(cx-18,bot-168,36,36);
        g.fillPolygon(new int[]{cx,cx-24,cx+24},new int[]{bot-200,bot-148,bot-148},3);
        g.setColor(new Color(65,88,40)); g.fillOval(cx-16,bot-165,32,30);
        // Hood shadow
        g.setColor(new Color(30,42,18,120)); g.fillOval(cx-14,bot-160,28,20);
        // Face
        g.setColor(new Color(200,170,140)); g.fillOval(cx-12,bot-158,24,24);
        g.setColor(new Color(215,185,155)); g.fillOval(cx-10,bot-156,20,20);
        // Eyes (keen green)
        g.setColor(new Color(60,200,80)); g.fillOval(cx-8,bot-150,7,7); g.fillOval(cx+1,bot-150,7,7);
        g.setColor(new Color(0,80,20)); g.fillOval(cx-6,bot-149,4,4); g.fillOval(cx+3,bot-149,4,4);
        // Nose
        g.setColor(new Color(180,150,120)); g.fillOval(cx-2,bot-145,4,4);
    }

    private static void drawPaladin(Graphics2D g, int w, int h) {
        int cx = w/2, bot = h-8;
        drawGlow(g, cx, bot-95, 95, new Color(255,220,80,70));
        drawGlow(g, cx, bot-95, 55, new Color(255,240,150,50));

        // ── DIVINE AURA PARTICLES ──
        g.setColor(new Color(255,240,120,80));
        int[] ax={cx-45,cx+45,cx-55,cx+55,cx-30,cx+30};
        int[] ay={bot-140,bot-120,bot-80,bot-90,bot-40,bot-50};
        for(int i=0;i<6;i++){drawGlow(g,ax[i],ay[i],6,new Color(255,240,100,120));g.fillOval(ax[i]-3,ay[i]-3,6,6);}

        // ── GOLD SABATONS (boots) ──
        g.setColor(new Color(100,80,20));
        g.fillRoundRect(cx-22,bot-55,17,55,6,6); g.fillRoundRect(cx+5,bot-55,17,55,6,6);
        g.setColor(new Color(160,130,35));
        g.fillRoundRect(cx-22,bot-24,17,24,5,5); g.fillRoundRect(cx+5,bot-24,17,24,5,5);
        g.setColor(new Color(210,180,60));
        g.fillRect(cx-22,bot-26,17,3); g.fillRect(cx+5,bot-26,17,3);
        // Sabaton toe cap
        g.setColor(new Color(180,150,45));
        g.fillRoundRect(cx-23,bot-10,19,10,4,4); g.fillRoundRect(cx+4,bot-10,19,10,4,4);

        // ── GOLD GREAVES ──
        g.setColor(new Color(120,95,25));
        g.fillRoundRect(cx-21,bot-55,16,35,5,5); g.fillRoundRect(cx+5,bot-55,16,35,5,5);
        g.setColor(new Color(175,145,40));
        g.fillRoundRect(cx-20,bot-53,14,28,4,4); g.fillRoundRect(cx+6,bot-53,14,28,4,4);
        g.setColor(new Color(210,180,60));
        g.fillRect(cx-20,bot-55,14,3); g.fillRect(cx+6,bot-55,14,3);

        // ── TASSETS (hip plate) ──
        g.setColor(new Color(130,105,30));
        g.fillRoundRect(cx-28,bot-95,20,36,6,6); g.fillRoundRect(cx+8,bot-95,20,36,6,6);
        g.setColor(new Color(175,148,45));
        g.fillRoundRect(cx-27,bot-93,18,30,5,5); g.fillRoundRect(cx+9,bot-93,18,30,5,5);

        // ── CHEST PLATE (ornate gold) ──
        g.setColor(new Color(120,95,25));
        g.fillRoundRect(cx-28,bot-148,56,60,10,10);
        g.setColor(new Color(160,130,35));
        g.fillRoundRect(cx-24,bot-145,48,26,7,7);
        g.setColor(new Color(200,170,55));
        g.fillRoundRect(cx-18,bot-143,36,18,5,5);
        // Chest center line
        g.setColor(new Color(220,190,65)); g.fillRect(cx-2,bot-148,4,60);
        // Holy cross emblem
        g.setColor(new Color(255,245,160));
        g.fillRect(cx-2,bot-140,4,24); g.fillRect(cx-10,bot-130,20,4);
        // Cross glow
        drawGlow(g, cx, bot-128, 14, new Color(255,240,100,140));
        // Rivet border
        g.setColor(new Color(220,190,65));
        int[] px2={cx-24,cx-24,cx+20,cx+20}; int[] py2={bot-145,bot-100,bot-145,bot-100};
        for(int i=0;i<4;i++) g.fillOval(px2[i],py2[i],5,5);

        // ── PAULDRONS (massive shoulder plates) ──
        g.setColor(new Color(120,95,25)); g.fillOval(cx-50,bot-155,34,26); g.fillOval(cx+16,bot-155,34,26);
        g.setColor(new Color(170,140,40)); g.fillOval(cx-48,bot-153,30,22); g.fillOval(cx+18,bot-153,30,22);
        g.setColor(new Color(210,180,60)); g.fillOval(cx-45,bot-150,24,16); g.fillOval(cx+21,bot-150,24,16);
        // Shoulder studs
        g.setColor(new Color(240,210,80)); g.fillOval(cx-43,bot-153,6,6); g.fillOval(cx+37,bot-153,6,6);

        // ── ARMS (articulated plate) ──
        g.setColor(new Color(120,95,25));
        g.fillRoundRect(cx-50,bot-148,20,58,8,8); g.fillRoundRect(cx+30,bot-148,20,58,8,8);
        g.setColor(new Color(165,135,38));
        g.fillRoundRect(cx-48,bot-146,16,20,5,5); g.fillRoundRect(cx+32,bot-146,16,20,5,5);
        // Elbow cop
        g.setColor(new Color(190,160,50)); g.fillOval(cx-50,bot-120,20,16); g.fillOval(cx+30,bot-120,20,16);
        // Gauntlets
        g.setColor(new Color(100,78,20));
        g.fillRoundRect(cx-50,bot-98,20,24,5,5); g.fillRoundRect(cx+30,bot-98,20,24,5,5);
        g.setColor(new Color(150,120,35));
        g.fillRect(cx-50,bot-100,20,3); g.fillRect(cx+30,bot-100,20,3);

        // ── TOWER SHIELD ──
        g.setColor(new Color(130,105,30));
        g.fillRoundRect(cx-82,bot-155,36,68,8,8);
        g.setColor(new Color(170,138,40));
        g.fillRoundRect(cx-80,bot-153,32,64,6,6);
        g.setColor(new Color(200,165,52));
        g.fillRoundRect(cx-78,bot-150,28,58,5,5);
        // Shield cross
        g.setColor(new Color(255,245,160));
        g.fillRect(cx-66,bot-148,4,54); g.fillRect(cx-80,bot-126,28,4);
        drawGlow(g, cx-64, bot-120, 12, new Color(255,240,100,120));
        // Shield boss (center knob)
        g.setColor(new Color(220,190,60)); g.fillOval(cx-69,bot-125,10,10);
        drawGlow(g, cx-64, bot-120, 8, new Color(255,240,80,160));

        // ── HOLY WARHAMMER ──
        g.setColor(new Color(90,70,20)); g.fillRoundRect(cx+48,bot-158,8,100,3,3);
        // Grip wrap
        g.setColor(new Color(60,45,12));
        for(int gy=bot-155;gy>bot-70;gy-=7) g.fillRect(cx+47,gy,10,4);
        // Pommel
        g.setColor(new Color(190,160,50)); g.fillOval(cx+45,bot-72,14,14);
        drawGlow(g,cx+52,bot-65,8,new Color(255,230,80,140));
        // Hammer head (massive)
        g.setColor(new Color(160,135,40)); g.fillRoundRect(cx+36,bot-178,32,24,6,6);
        g.setColor(new Color(200,170,55)); g.fillRoundRect(cx+38,bot-176,28,20,5,5);
        // Hammer face engraving
        g.setColor(new Color(230,200,70));
        g.fillRect(cx+50,bot-173,2,14); g.fillRect(cx+45,bot-168,12,2);
        drawGlow(g, cx+52, bot-166, 10, new Color(255,240,100,150));

        // ── GREAT HELM ──
        g.setColor(new Color(120,96,25)); g.fillOval(cx-26,bot-186,52,52);
        g.setColor(new Color(165,135,38)); g.fillOval(cx-23,bot-183,46,46);
        // Face plate
        g.setColor(new Color(100,78,18)); g.fillRoundRect(cx-20,bot-178,40,34,5,5);
        g.setColor(new Color(130,105,28)); g.fillRoundRect(cx-18,bot-176,36,30,4,4);
        // Visor T-slot
        g.setColor(new Color(255,225,100,230));
        g.fillRect(cx-16,bot-168,32,5);
        g.fillRect(cx-3,bot-175,6,14);
        // Plume
        g.setColor(new Color(180,30,30));
        g.fillRoundRect(cx-5,bot-198,10,16,4,4);
        g.setColor(new Color(220,45,45));
        g.fillRoundRect(cx-4,bot-202,8,6,3,3);
        drawGlow(g,cx,bot-194,8,new Color(255,80,60,100));
        // Crown ridge
        g.setColor(new Color(200,170,55)); g.fillRect(cx-23,bot-186,46,5);
        // Cheek guards
        g.setColor(new Color(110,88,22)); g.fillRoundRect(cx-26,bot-178,10,26,4,4); g.fillRoundRect(cx+16,bot-178,10,26,4,4);
    }

    private static void drawAssassin(Graphics2D g, int w, int h) {
        int cx = w/2, bot = h-8;
        drawGlow(g, cx, bot-90, 80, new Color(120,0,180,55));
        drawGlow(g, cx, bot-90, 42, new Color(180,0,255,40));

        // ── SHADOW WISPS ──
        g.setColor(new Color(80,0,120,60));
        int[] swx={cx-50,cx+50,cx-60,cx+60,cx-40,cx+40};
        int[] swy={bot-130,bot-110,bot-70,bot-85,bot-30,bot-45};
        for(int i=0;i<6;i++){drawGlow(g,swx[i],swy[i],8,new Color(100,0,160,100));g.fillOval(swx[i]-3,swy[i]-3,6,6);}

        // ── SHADOW BOOTS ──
        g.setColor(new Color(12,10,22));
        g.fillRoundRect(cx-15,bot-56,12,56,5,5); g.fillRoundRect(cx+3,bot-56,12,56,5,5);
        g.setColor(new Color(22,15,38));
        g.fillRoundRect(cx-17,bot-26,14,26,5,5); g.fillRoundRect(cx+3,bot-26,14,26,5,5);
        // Boot buckle (purple gem)
        g.setColor(new Color(140,0,200)); g.fillOval(cx-16,bot-33,5,5); g.fillOval(cx+11,bot-33,5,5);
        drawGlow(g,cx-14,bot-31,4,new Color(180,0,255,150));
        drawGlow(g,cx+13,bot-31,4,new Color(180,0,255,150));

        // ── LEGS (dark leather) ──
        g.setColor(new Color(18,14,32));
        g.fillRect(cx-14,bot-56,11,30); g.fillRect(cx+3,bot-56,11,30);
        // Leg straps
        g.setColor(new Color(40,25,60)); g.fillRect(cx-16,bot-68,13,4); g.fillRect(cx+3,bot-68,13,4);
        // Throwing knife holster
        g.setColor(new Color(35,22,55)); g.fillRoundRect(cx+5,bot-72,10,18,3,3);
        g.setColor(new Color(170,175,190)); g.fillRect(cx+7,bot-80,3,12);
        g.setColor(new Color(60,50,75)); g.fillRect(cx+6,bot-71,5,3);

        // ── DARK BODY SUIT ──
        g.setColor(new Color(15,10,28));
        g.fillRoundRect(cx-22,bot-118,44,60,7,7);
        g.setColor(new Color(25,18,42));
        g.fillRoundRect(cx-18,bot-116,36,20,5,5);
        // Suit texture (diagonal lines)
        g.setColor(new Color(35,25,55,80));
        g.setStroke(new BasicStroke(1));
        for(int dl=-30;dl<30;dl+=6) g.drawLine(cx+dl,bot-118,cx+dl+20,bot-62);
        g.setStroke(new BasicStroke(1));

        // ── ASSASSIN CLOAK (asymmetric) ──
        g.setColor(new Color(18,12,32));
        g.fillPolygon(new int[]{cx-22,cx+22,cx+28,cx+8,cx-22},new int[]{bot-110,bot-110,bot-50,bot,bot},5);
        g.setColor(new Color(28,18,48,180));
        g.fillPolygon(new int[]{cx-20,cx+20,cx+24,cx+6,cx-20},new int[]{bot-108,bot-108,bot-52,bot-4,bot-4},5);
        // Cloak inner (dark purple)
        g.setColor(new Color(45,15,70,120));
        g.fillPolygon(new int[]{cx-18,cx+18,cx+20,cx+5,cx-18},new int[]{bot-106,bot-106,bot-55,bot-8,bot-8},5);

        // ── ARMS (shadow-wrapped) ──
        g.setColor(new Color(15,10,28));
        g.fillRoundRect(cx-42,bot-115,20,52,8,8); g.fillRoundRect(cx+22,bot-115,20,52,8,8);
        // Arm wraps
        g.setColor(new Color(30,15,50));
        for(int aw=0;aw<5;aw++){g.fillRect(cx-42,bot-108+aw*8,20,4);g.fillRect(cx+22,bot-108+aw*8,20,4);}
        // Gloves
        g.setColor(new Color(20,12,35)); g.fillRoundRect(cx-44,bot-72,22,20,5,5); g.fillRoundRect(cx+22,bot-72,22,20,5,5);

        // ── LEFT DAGGER (long, thin) ──
        // Handle
        g.setColor(new Color(35,22,55)); g.fillRoundRect(cx-58,bot-136,5,28,2,2);
        g.setColor(new Color(100,0,160,200));
        for(int dw=0;dw<4;dw++) g.fillRect(cx-58,bot-132+dw*6,5,3);
        // Guard
        g.setColor(new Color(80,65,90)); g.fillRoundRect(cx-62,bot-110,14,6,3,3);
        // Blade (long, elegant)
        g.setColor(new Color(185,190,210)); g.fillPolygon(new int[]{cx-57,cx-54,cx-56},new int[]{bot-110,bot-110,bot-165},3);
        g.setColor(new Color(220,225,245)); g.fillPolygon(new int[]{cx-57,cx-55,cx-56},new int[]{bot-110,bot-110,bot-163},3);
        // Purple edge glow
        drawGlow(g, cx-56, bot-138, 8, new Color(160,0,220,120));
        g.setColor(new Color(180,0,255,180)); g.fillRect(cx-57,bot-160,2,50);

        // ── RIGHT DAGGER (shorter, reversed grip) ──
        g.setColor(new Color(35,22,55)); g.fillRoundRect(cx+52,bot-125,5,24,2,2);
        g.setColor(new Color(80,65,90)); g.fillRoundRect(cx+48,bot-103,14,6,3,3);
        g.setColor(new Color(185,190,210)); g.fillPolygon(new int[]{cx+53,cx+56,cx+55},new int[]{bot-103,bot-103,bot-152},3);
        g.setColor(new Color(220,225,245)); g.fillPolygon(new int[]{cx+53,cx+55,cx+55},new int[]{bot-103,bot-103,bot-150},3);
        drawGlow(g, cx+55, bot-128, 8, new Color(160,0,220,120));
        g.setColor(new Color(180,0,255,180)); g.fillRect(cx+55,bot-148,2,46);

        // ── FACE / SHADOW MASK ──
        // Hood
        g.setColor(new Color(15,8,28)); g.fillOval(cx-20,bot-170,40,40);
        g.fillPolygon(new int[]{cx,cx-25,cx+25},new int[]{bot-200,bot-155,bot-155},3);
        g.setColor(new Color(25,14,42)); g.fillOval(cx-17,bot-167,34,34);
        // Face shadow
        g.setColor(new Color(8,4,18,180)); g.fillOval(cx-15,bot-162,30,28);
        // Only eyes visible
        g.setColor(new Color(180,0,255)); g.fillOval(cx-10,bot-153,9,8); g.fillOval(cx+1,bot-153,9,8);
        g.setColor(new Color(220,100,255)); g.fillOval(cx-8,bot-152,5,5); g.fillOval(cx+3,bot-152,5,5);
        drawGlow(g,cx-5,bot-149,6,new Color(180,0,255,160)); drawGlow(g,cx+5,bot-149,6,new Color(180,0,255,160));
        // Mask seam line
        g.setColor(new Color(40,25,62,150)); g.fillRect(cx-1,bot-162,2,28);

        // ── POISON VIALS (belt) ──
        g.setColor(new Color(40,25,60)); g.fillRoundRect(cx-25,bot-94,8,14,3,3);
        g.setColor(new Color(80,200,30,180)); g.fillRoundRect(cx-24,bot-92,6,10,2,2);
        drawGlow(g,cx-21,bot-88,5,new Color(100,255,50,120));
    }

    // ─────────────────────────────────────────────────
    //  DUNGEON BACKGROUNDS
    // ─────────────────────────────────────────────────

    private static BufferedImage drawDungeonBg(String type, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setup(img);
        switch (type) {
            case "cave":   drawCaveBg(g, w, h); break;
            case "castle": drawCastleBg(g, w, h); break;
            case "forest": drawForestBg(g, w, h); break;
            case "abyss":  drawAbyssBg(g, w, h); break;
            default:       drawCaveBg(g, w, h);
        }
        g.dispose();
        return img;
    }

    private static void drawCaveBg(Graphics2D g, int w, int h) {
        // Background gradient
        GradientPaint gp = new GradientPaint(0,0,new Color(15,10,25), w,h,new Color(40,20,50));
        g.setPaint(gp); g.fillRect(0,0,w,h);
        // Stalactites
        g.setColor(new Color(40,35,55));
        for(int i=0;i<8;i++) { int x=30+i*w/8; g.fillPolygon(new int[]{x-12,x+12,x},new int[]{0,0,20+i%3*15},3); }
        // Floor
        g.setColor(new Color(30,25,45)); g.fillRect(0,h-40,w,40);
        g.setColor(new Color(50,42,65)); g.fillRect(0,h-42,w,4);
        // Torches
        for(int tx : new int[]{w/4, 3*w/4}) {
            g.setColor(new Color(80,60,30)); g.fillRect(tx-3,h-100,6,30);
            drawTorchFlame(g, tx, h-100);
        }
        // Glowing cracks
        g.setColor(new Color(100,60,200,60));
        g.setStroke(new BasicStroke(1.5f)); g.drawLine(50,h-40,80,h-80); g.drawLine(w-60,h-40,w-90,h-90);
        g.setStroke(new BasicStroke(1));
    }

    private static void drawCastleBg(Graphics2D g, int w, int h) {
        GradientPaint gp = new GradientPaint(0,0,new Color(10,10,30), w,h,new Color(25,10,45));
        g.setPaint(gp); g.fillRect(0,0,w,h);
        // Stone wall tiles
        g.setColor(new Color(35,30,50));
        for(int r=0;r<6;r++) for(int c=0;c<10;c++) {
            int ox = (r%2==0)?0:w/10/2;
            g.fillRect(ox+c*w/10+1, r*(h/6)+1, w/10-2, h/6-2);
        }
        g.setColor(new Color(45,40,65));
        for(int r=0;r<6;r++) for(int c=0;c<10;c++) {
            int ox = (r%2==0)?0:w/10/2;
            g.drawRect(ox+c*w/10+1, r*(h/6)+1, w/10-2, h/6-2);
        }
        // Floor
        g.setColor(new Color(25,20,40)); g.fillRect(0,h-35,w,35);
        g.setColor(new Color(60,50,80)); g.fillRect(0,h-37,w,3);
        // Torch sconces
        for(int tx : new int[]{w/6, 5*w/6}) {
            g.setColor(new Color(60,50,30)); g.fillRect(tx-2,h-90,4,25);
            g.setColor(new Color(80,70,40)); g.fillRect(tx-6,h-95,12,8);
            drawTorchFlame(g, tx, h-90);
        }
    }

    private static void drawForestBg(Graphics2D g, int w, int h) {
        GradientPaint gp = new GradientPaint(0,0,new Color(5,15,10), w,h,new Color(10,30,20));
        g.setPaint(gp); g.fillRect(0,0,w,h);
        // Trees
        for(int i=0;i<6;i++) {
            int tx = i*(w/5)-20;
            g.setColor(new Color(40,25,10)); g.fillRect(tx+15,h-120,12,120);
            g.setColor(new Color(15,60,20)); g.fillOval(tx-8,h-200,60,100);
            g.setColor(new Color(20,80,30)); g.fillOval(tx,h-220,50,90);
        }
        // Ground
        g.setColor(new Color(20,50,15)); g.fillRect(0,h-30,w,30);
        g.setColor(new Color(30,70,20)); g.fillRect(0,h-32,w,4);
        // Fireflies
        g.setColor(new Color(200,255,100,180));
        for(int i=0;i<12;i++) g.fillOval(30+i*70+(i%3)*20, 50+i*30+(i%4)*15, 4, 4);
    }

    private static void drawAbyssBg(Graphics2D g, int w, int h) {
        GradientPaint gp = new GradientPaint(0,0,new Color(0,0,10), w,h,new Color(20,0,30));
        g.setPaint(gp); g.fillRect(0,0,w,h);
        // Void cracks
        g.setColor(new Color(80,0,120,100));
        g.setStroke(new BasicStroke(2));
        for(int i=0;i<5;i++) g.drawLine(w/2,h/2, (i*w/4)%w, (i*h/3)%h);
        g.setStroke(new BasicStroke(1));
        // Purple runes
        g.setColor(new Color(150,0,200,80));
        for(int i=0;i<8;i++) { int rx=20+i*w/8, ry=h/2+i%3*40-40; g.drawOval(rx,ry,20,20); g.drawLine(rx+10,ry,rx+10,ry+20); }
        // Floor
        g.setColor(new Color(10,0,20)); g.fillRect(0,h-30,w,30);
        g.setColor(new Color(80,0,120,80)); g.fillRect(0,h-32,w,4);
        // Stars/void particles
        g.setColor(new Color(180,100,255,120));
        for(int i=0;i<30;i++) g.fillOval(i*w/30+5, (i*37)%h, 3, 3);
    }

    private static void drawTorchFlame(Graphics2D g, int tx, int ty) {
        g.setColor(new Color(255,60,0,180)); g.fillOval(tx-6,ty-18,12,20);
        g.setColor(new Color(255,140,0,160)); g.fillOval(tx-4,ty-14,8,14);
        g.setColor(new Color(255,220,80,200)); g.fillOval(tx-2,ty-10,4,8);
        // Light halo
        g.setColor(new Color(255,150,50,30)); g.fillOval(tx-20,ty-25,40,35);
    }

    // ─────────────────────────────────────────────────
    //  HIT EFFECTS
    // ─────────────────────────────────────────────────

    private static BufferedImage drawHitYellow(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setup(img);
        int cx = w/2, cy = h/2;
        // Burst lines
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 8; i++) {
            double a = i * Math.PI / 4;
            g.setColor(new Color(255, 200+i*6, 0, 220));
            g.drawLine(cx, cy, cx+(int)(40*Math.cos(a)), cy+(int)(40*Math.sin(a)));
        }
        g.setStroke(new BasicStroke(1));
        // Core flash
        g.setColor(new Color(255, 240, 100, 230)); g.fillOval(cx-20, cy-20, 40, 40);
        g.setColor(new Color(255, 255, 255, 180)); g.fillOval(cx-10, cy-10, 20, 20);
        g.dispose();
        return img;
    }

    private static BufferedImage drawHitRed(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setup(img);
        int cx = w/2, cy = h/2;
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 6; i++) {
            double a = i * Math.PI / 3 + 0.2;
            g.setColor(new Color(220, 30+i*10, 30, 200));
            g.drawLine(cx, cy, cx+(int)(35*Math.cos(a)), cy+(int)(35*Math.sin(a)));
        }
        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(255, 50, 50, 200)); g.fillOval(cx-18, cy-18, 36, 36);
        g.setColor(new Color(255, 120, 120, 150)); g.fillOval(cx-8, cy-8, 16, 16);
        g.dispose();
        return img;
    }

    // ─────────────────────────────────────────────────
    //  DUNGEON SELECTION CARD
    // ─────────────────────────────────────────────────

    private static BufferedImage drawDungeonCard(String name, String emoji, String desc1, String desc2,
                                                  Color accent, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setup(img);
        // Card background
        GradientPaint gp = new GradientPaint(0,0, accent.darker().darker(), w,h, accent.darker());
        g.setPaint(gp); g.fillRoundRect(0,0,w,h,18,18);
        // Border
        g.setColor(accent); g.setStroke(new BasicStroke(2));
        g.drawRoundRect(1,1,w-3,h-3,18,18);
        g.setStroke(new BasicStroke(1));
        // Title
        g.setColor(new Color(255,230,100)); g.setFont(new Font("SansSerif",Font.BOLD,18));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(name, (w-fm.stringWidth(name))/2, 38);
        // Divider
        g.setColor(accent); g.fillRect(20, 46, w-40, 2);
        // Desc
        g.setColor(new Color(220,210,255)); g.setFont(new Font("SansSerif",Font.PLAIN,12));
        fm = g.getFontMetrics();
        g.drawString(desc1, (w-fm.stringWidth(desc1))/2, 68);
        g.drawString(desc2, (w-fm.stringWidth(desc2))/2, 86);
        g.dispose();
        return img;
    }

    // ─────────────────────────────────────────────────
    //  UTILITY
    // ─────────────────────────────────────────────────

    private static Graphics2D setup(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g;
    }

    private static void drawGlow(Graphics2D g, int cx, int cy, int r, Color c) {
        for (int i = r; i > 0; i -= 4) {
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, c.getAlpha() * i / r)));
            g.fillOval(cx-i, cy-i, i*2, i*2);
        }
    }

    /** Returns the monster key string for a given dungeon tier/elite flag */
    public static String monsterKey(int tier, boolean elite) {
        String[] keys = {"slime","goblin","bat","skeleton","orc","troll","dragon","demon","lich","boss"};
        String base = keys[Math.min(tier, keys.length-1)];
        return elite ? base+"_e" : base;
    }
}