import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.palette.UltimaViSharpX68000;
import femto.font.FontC64;
import femto.sound.Mixer;

import TitleScene;

import managers.BotManager;
import managers.ItemDropManager;
import managers.BlastManager;
import managers.VirusManager;
import managers.MiniBossManager;
import managers.WormBossManager;
import managers.DebrisManager;

public class Main extends State {
    public static void main(String[] args){
        Game.run(FontC64.font(), new TitleScene());
    }
    public static final SaveManager saveManager = new SaveManager();
    public static HiRes16Color screen = new HiRes16Color(UltimaViSharpX68000.palette(), FontC64.font());

    static BotManager botManager = new BotManager();
    static BlastManager blastManager = new BlastManager();
    
    static MiniBossManager bossManager = new MiniBossManager();
    static DebrisManager debrisManager = new DebrisManager();
    static ItemDropManager itemDropManager = new ItemDropManager();
    static WormBossManager wormManager = new WormBossManager();
    static VirusManager virusManager = new VirusManager(debrisManager.getSpawnX(), debrisManager.getSpawnY());
    
    public final String PRESS_C_TRANSPORT = "Press C to transport";
    public final String SECTOR_CLEAR = "Sector Cleared";
    
    public static int SECTOR = 0;
    public static int ROOM_STATUS = 0; 
    public static int ZONE = 0;
    public static void setZone(int z){
        //Always start  sector at 0
        SECTOR = 0;
        ZONE = z;
    }
    
    int currency = 0, viby = 0, threatWidth = 0;
    public static boolean createItemDrop = false;
    public static float itemX = 0, itemY = 0;
    public static int kills = 0, score = 0, shield = 100;
    public static void updateKills(float x, float y){
        kills++;
        score += 10;
        itemX = x;
        itemY = y;
        createItemDrop = true;
    }
    
    void reset(){
        currency = 0;
        viby = 0;
        threatWidth = 0;
        createItemDrop = false;
        itemX = 0;
        itemY = 0;
        kills = 0;
        score = 0;
        SECTOR = 0;
        shield = 100;
        ROOM_STATUS = 0;
    }
    
    // helper methods

    //Deprecated(potentially slow)
    public static boolean circle(float x1, float y1, float x2, float y2, float r1, float r2){
        int vx = (int)(x1 - x2);
        int vy = (int)(y1 - y2);
        int vr = (int)(r1 + r2);
        return ((vx) * (vx) + (vy) * (vy)) < (vr) * (vr);
    }
    
    /**
    * Takes two square objects and checks if they intersect, given x,y and size
    */
    public static boolean boundingBox(float x1, float y1, float s1, float x2, float y2, float s2){
        return (x1 < x2 + s2 && x1 + s1 > x2 && y1 < y2 + s2 && y1 + s1 > y2);
    }
    
    void init(){
        debrisManager.resetDebris();
        virusManager.initWave(0, debrisManager.getSpawnX(), debrisManager.getSpawnY());
        virusManager.resetAll();
        shield = 100;
        Mixer.init(8000);
    }
    
    /**
     * drawHud displays the top progress bar indicators for shield/threats/boss health 
     * as well as displaying the Score, currency and current ZONE:SECTOR.
     * 
     */
    void drawHud(){
        // Fill rects for the top and bottom sections
        screen.fillRect(0, 0, screen.width(), 16, 3);
        screen.fillRect(0, screen.height()-14, screen.width(), 16, 3);
        
        //Bot Shield
        screen.drawRect(6, 0, 80, 10, 0);
        screen.fillRect(8, 2, 78, 8, 2);//background grey
        
        // bot shield
        int shieldWidth = (int)(shield * 78 / 100);
        screen.fillRect(8, 2, shieldWidth, 8, 15);
        
        
        //Threats or Boss Shield
        screen.drawRect(134, 0, 80, 10, 0);
        screen.fillRect(136, 2, 78, 8, 2);
        
        // threats or boss health
        switch(SECTOR){
            case 4:
                threatWidth = (int)(bossManager.getCurrentHealth() * 78 / bossManager.getTotalHealth());
                screen.fillRect(214-threatWidth, 2, threatWidth, 8, 8);
                break;
            case 8:
                threatWidth = (int)(wormManager.getCurrentHealth() * 78 / wormManager.getTotalHealth());
                screen.fillRect(214-threatWidth, 2, threatWidth, 8, 8);
                break;
            default:
                threatWidth = (int)(virusManager.getThreats() * 78 / virusManager.getTotalThreats());
                screen.fillRect(214-threatWidth, 2, threatWidth, 8, 8);
                break;
        }

        //Zone : SECTOR 
        screen.setTextPosition(98, 3);
        screen.setTextColor(0);
        screen.print(ZONE + ":" + SECTOR);
        
        //Score and Currency 
        screen.setTextPosition(3, screen.height()-12);
        screen.print("Score: "+score);
        
        screen.setTextPosition(140, screen.height()-12);
        screen.print("$$: " + currency);
    }

    void drawGrid(){
        screen.drawRect(6, 16, screen.width()-12, screen.height()-32, 12, true);
    }

    // TODO: Refactor into more manageable methods.
    void update(){
        screen.clear(3);

        if(createItemDrop){
            createItemDrop = false;
            itemDropManager.newDrop(itemX, itemY);
        }

        switch(ROOM_STATUS){
            case 0:// threats incoming
                //Draw to screen
                drawGrid();
                itemDropManager.updateAndRender();
                if(itemDropManager.checkCollect(botManager.getX()+8, botManager.getY()+8)){
                    currency++;
                }
                if(shield <= 0){
                    ROOM_STATUS = 5;//GAME OVER
                }
                
                botManager.render();
                
                updateZone();
                
                drawHud();
                break;
            case 1:// cleared
                drawGrid();
                itemDropManager.updateAndRender();
                if(itemDropManager.checkCollect(botManager.getX()+7, botManager.getY()+7)){
                    currency++;
                }
                // Would be awkward to suddenly have all the debris disappear.
                if(SECTOR != 4 && SECTOR != 8){
                    debrisManager.render();
                }
                
                if(Button.C.justPressed()){
                    debrisManager.resetDebris();
                    if(ZONE == 0 && SECTOR == 4){
                        saveManager.firstZoneClear = true;
                        saveManager.saveCookie();
                        ROOM_STATUS = 4;
                        break;
                    }
                    if(SECTOR == 8){
                        switch(ZONE){
                            case 1: saveManager.secondZoneClear = true; break;
                            case 2: saveManager.thirdZoneClear = true; break;
                            case 3: saveManager.fourthZoneClear = true; break;
                        }
                        saveManager.saveCookie();
                        ROOM_STATUS = 4;
                        break;
                    }
                    botManager.setPos(6, screen.height()/2);
                    SECTOR++;
                    ROOM_STATUS = 2;
                }
                
                screen.setTextPosition(screen.width()/2-58, screen.height()/2);
                screen.setTextColor(0);
                screen.print(SECTOR_CLEAR);
                
                screen.setTextPosition(26, screen.height()/2+16);
                screen.print(PRESS_C_TRANSPORT);
                botManager.render();
                
                drawHud();
                break;
            case 2://traveling
                viby = Math.random(-2, 3);
                screen.fillCircle(screen.width()/2-8+viby, screen.height()/2+viby, 5, 10);
                
                if(Button.A.justPressed() && blastManager.getRate() < 10){
                    if(currency >= 2){
                        currency -= 2;
                        blastManager.incRate();
                    }
                }
                if(Button.B.justPressed() && blastManager.getRefresh() > 0){
                    if(currency >= 2){
                        currency -= 2;
                        blastManager.incRefresh();
                    }
                }
                if(Button.Up.justPressed() && shield < 100){
                    if(currency >= 10){
                        currency -= 10;
                        if(shield + 10 > 100)shield = 100;
                        else{
                            shield+=10;
                        }
                    }
                }

                screen.setTextPosition(0, 16);
                screen.setTextColor(0);
                screen.println("$2  - [A]  Rate: " + blastManager.getRate());
                screen.println("$2  - [B]  Refresh: " + blastManager.getRefresh());
                screen.println("$10 - [Up] Shield: " + shield);
                screen.println("\n$$" + currency);

                if(Button.C.justPressed()){
                    if(SECTOR == 4){
                        switch(ZONE){
                            case 0:
                                bossManager.init(1);
                                break;
                            case 1:
                                bossManager.init(2);
                                break;
                            case 2:
                                bossManager.init(3);
                                break;
                            case 3:
                                bossManager.init(4);
                                break;
                        }
                    }else if (SECTOR == 8){
                        wormManager.reset();
                    }else{
                        virusManager.initWave(SECTOR, debrisManager.getSpawnX(), debrisManager.getSpawnY());
                        virusManager.resetAll();
                    }
                    itemDropManager.clear();
                    ROOM_STATUS = 3;
                }
                
                break;
            case 3://Prep
                drawGrid();
                botManager.render();
                if(SECTOR != 8 && SECTOR != 4){
                    debrisManager.render();
                }
                
                screen.setTextPosition(screen.width()/2-58, screen.height()/2);
                screen.setTextColor(0);
                screen.print("C to begin");
                if(Button.C.justPressed()){
                    ROOM_STATUS = 0;
                }
                
                drawHud();
                break;
            case 4://Summary!
                screen.setTextPosition(0, 10);
                screen.println("This zone is clear!");
                screen.println("Accuracy: " + blastManager.getAccuracy());
                screen.println("Score: " + score);
                screen.println("[C] To title screen");
                if(Button.C.justPressed()){
                    reset();
                    Game.changeState(new TitleScene());
                }
                break;
            case 5://GAME OVER
                screen.setTextPosition(0, 10);
                screen.setTextColor(8);
                screen.println("Your Bot was destroyed");
                screen.println("\n[C] To title screen");
                if(Button.C.justPressed()){
                    reset();
                    Game.changeState(new TitleScene());
                }
                
                break;
        }
        
        screen.flush();
    }
    
    void updateMiniBoss(){
        // We set false because there is no debris in the boss battles
        botManager.updateBotMovement(debrisManager, false);
        bossManager.update(blastManager, (botManager.getX()+8), (botManager.getY()+8));
        bossManager.render();
        if(bossManager.cleared()){
            // CLEARED!
            ROOM_STATUS = 1;
        }
    }
    
    void updateWormBoss(){
        wormManager.update(blastManager, botManager.getX(), botManager.getY());
        if(wormManager.bodyCollidesWithBot(botManager.getX(), botManager.getY())
        || wormManager.headCollidesWithBot(botManager.getX(), botManager.getY())){
            // Move bot Y
            if(botManager.getY() + 32 < screen.height()-45) botManager.setY(botManager.getY()+32);
            else botManager.setY(botManager.getY()-32);
            // Move bot X
            if(botManager.getX() + 32 < screen.width()-45) botManager.setX(botManager.getX()+32);
            else botManager.setX(botManager.getX()-32);
            // Subtract shielding
            shield-=10;
        }
        
        wormManager.render();
        if(wormManager.cleared()){
            ROOM_STATUS = 1;
        }  
    }
    
    /**
     *   This is the typical virus spawning wave shooter event. 
     *   Increasing waves of enemy viruses attack.
     *   When updating the bot movement, we pass `true` so it knows to check debris collisions.
     */
    void updateVirusRoom(){
        botManager.updateBotMovement(debrisManager, true);
        debrisManager.render();
        virusManager.update(botManager.getX(), botManager.getY(), debrisManager, blastManager);

        if(virusManager.getThreats() == 0){
            ROOM_STATUS = 1; // CLEARED!
        }
        virusManager.render();
    }
    
    /**
     * ZONE's have 9 sectors (0 through 8), except ZONE 0, which ends after 4
     * Sector 4 is another mini boss battle. 8 is the boss battle with The Worm
     */ 
    void updateZone(){
        switch(SECTOR){
            default:
                updateVirusRoom();
                break;
            case 4:
                updateMiniBoss();
                break;
            case 8:
                switch(ZONE){
                    case 1:
                        updateWormBoss();
                        break;
                    case 2:
                        //TODO: update Fork Bomb
                        break;
                    case 3:
                        //TODO: update grabby mc grab face
                        break;
                }
                botManager.updateBotMovement(debrisManager, false);
                break;
        }
        //START update Blast
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        blastManager.render();
    }
    
    void updateEndless(){}
}
