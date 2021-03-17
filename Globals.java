import femto.Game;
import femto.input.Button;
import femto.mode.HiRes16Color;
import femto.palette.UltimaViSharpX68000;
import femto.font.FontC64;

import audio.Hurt;

import sprites.Loot;
import sprites.Magnet;

import managers.SaveManager;
import managers.SectorZoneManager;
import managers.EndlessSaveManager;


/**
 * Globals contains all of the globally used variables as well as 
 * a couple of useful helper methods.
 * 
 */
class Globals {
    //TODO: I find it incredibly annoying to have the Bot's "hurt" variable here.
    static final Hurt pain = new Hurt(2);
    static final SaveManager saveManager = new SaveManager();
    // Initialize only in endless mode
    static EndlessSaveManager endlessSaveManager;
    static final HiRes16Color screen = new HiRes16Color(UltimaViSharpX68000.palette(), FontC64.font());

    static final Loot loot = new Loot();
    static final Magnet mag = new Magnet();

    static int ZONE = 0, SECTOR = 0, t = 100;
    static int score = 0, kills = 0;
    static int shield = 100, hurt = 0, hit = 0, shots = 0;
    static boolean createItemDrop = false;
    static float itemX = 0, itemY = 0;
    
    static final String PRESS_C_TRANSPORT = "[C] - Transport";
    
    static boolean endlessUnlocked, endless;

    static void initEndlessMode(){
        ZONE = 0;
        endlessSaveManager = new EndlessSaveManager();
        endless = true;
        shield = 100;
        score = 0;
        endlessSaveManager.highScore = 0;
        endlessSaveManager.rate = 1;
        endlessSaveManager.refresh = 50;
        endlessSaveManager.currency = 0;
        endlessSaveManager.magnet = 0;
        endlessSaveManager.charge = 1;
        endlessSaveManager.damage = 2;
        endlessSaveManager.saveCookie();
    }

    /**
    * Takes two square objects and checks if they intersect, given x,y and size
    * The boxing shape is always a square
    */
    public static boolean boundingBox(float x1, float y1, float s1, float x2, float y2, float s2){
        return (x1 < x2 + s2 && x1 + s1 > x2 && y1 < y2 + s2 && y1 + s1 > y2);
    }

    public static boolean checkHitBot(float vx, float vy, int vSize, float bx, float by, int bSize){
        if(boundingBox(vx, vy, vSize, bx, by, bSize)){
            if(hurt == 0){
                pain.play();
                hurt = 50;
                return true;
            }
        }
        return false;
    }

    /**
     * When an enemy is destroyed, this is called to create an item drop and to update variables
     */ 
    public static void updateKills(float x, float y){
        kills++;
        score += 10;
        itemX = x;
        itemY = y;
        createItemDrop = true;
    }

    public static void reset(){
        kills = 0;
        hit = 0;
        shots = 0;
        score = 0;
        SECTOR = 0;
    }
    
    public static void newGame(){
        saveManager.started = true;
        endlessUnlocked = false;
        saveManager.firstZoneClear = false;
        saveManager.secondZoneClear = false;
        saveManager.thirdZoneClear = false;
        saveManager.fourthZoneClear = false;
        saveManager.rate = 1;
        saveManager.refresh = 50;
        saveManager.currency = 0;
        saveManager.magnet = 0.0f;
        saveManager.charge = 1;
        saveManager.damage = 2;
        saveManager.saveCookie();
        System.out.println("New game");
    }

    /**
     * When a ZONE is cleared, we calculate the accuracy to display on the results view.
     * This will also be used to determine acheivments
     */ 
    public static int getAccuracy() {
        if(hit == 0) return 0;
        if(shots == 0) return 0;
        return Math.abs(hit * 100 / shots);
    }

    /**
     * drawHud displays the top progress bar indicators for shield/threats/boss health 
     * as well as displaying the Score, currency and current ZONE:SECTOR.
     * 
     */
    static void drawHud(int threatWidth){
        // Fill rects for the top and bottom sections
        screen.fillRect(0, 0, 220, 16, 3);
        screen.fillRect(0, 162, 140, 16, 3);

        // Bot Shield
        screen.drawRect(6, 0, 80, 6, 0);
        screen.fillRect(8, 2, 78, 4, 2);

        // bot shield
        screen.fillRect(8, 2, (int)(shield * 78 / 100), 4, 15);

        // Blast charge box
        screen.drawRect(6, 10, 70, 4, 0);
        screen.fillRect(8, 12, 68, 2, 2);

        // The blast charge fill is rendered in BlastManager:L85
        // NormalSector draws the sector total.
        // Boss Blast charge box
        if(SECTOR != 8){
            screen.drawRect(144, 10, 70, 4, 0);
            screen.fillRect(146, 12, 68, 2, 2);
        }

        //Threats or Boss Shield
        screen.drawRect(134, 0, 80, 6, 0);
        screen.fillRect(136, 2, 78, 4, 2);

        // threats health
        screen.fillRect(214-threatWidth, 2, threatWidth, 4, 8);

        // Zone : SECTOR
        screen.setTextPosition(98, 3);
        screen.setTextColor(0);
        screen.print(ZONE + ":" + SECTOR);

        // Mini Fragments (currency)
        if(SECTOR != 4 && SECTOR != 8){
            loot.play();
            loot.draw(screen, 6, 164);
            // Currency drawn on NormalSector
            // screen.setTextPosition(16, 164);
            // screen.print("x"+saveManager.currency);

            // Magnet
            if(endless){
                if(endlessSaveManager.magnet > 0.0f){
                    mag.idle();
                    mag.draw(screen, 120, 164);
                    screen.setTextPosition(130, 164);
                    screen.print((int)(endlessSaveManager.magnet*100) + "%");
                }
            }else{
                if(saveManager.magnet > 0.0f){
                    mag.idle();
                    mag.draw(screen, 120, 164);
                    screen.setTextPosition(130, 164);
                    screen.print((int)(saveManager.magnet*100) + "%");
                }
            }
        }
    }

    static void drawGrid(){
        screen.drawRect(6, 17, 208, 144, 12, true);
    }

    static void drawCleared(boolean boss){
        screen.setTextColor(12);

        screen.fillRect(0, 160, 220, 20, 3);
        screen.setTextPosition(0, 167);
        screen.print(PRESS_C_TRANSPORT);
        if(t < 50){
            int x = screen.textWidth(PRESS_C_TRANSPORT);
            if(t < 4) screen.drawRect(x, 166, 5, 9, 12);
            else screen.fillRect(x, 166, 5, 9, 12);
        }
        t--;
        if(t == 0){
            t = 100;
        }

        if(Button.C.justPressed()){
            SECTOR++;
            if(endless && boss){
                score += 500 + (ZONE * 50);
            }
            Game.changeState(new Shop());
        }
    }

}