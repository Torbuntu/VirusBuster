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
import managers.BossManager;
import managers.WormBossManager;



public class Main extends State {
    public static void main(String[] args){
        Game.run(FontC64.font(), new TitleScene());
    }
    public static final SaveManager saveManager = new SaveManager();
    public static HiRes16Color screen = new HiRes16Color(UltimaViSharpX68000.palette(), FontC64.font());
    
    // Bot bot;
    BotManager botManager;
    BlastManager blastManager;
    VirusManager virusManager;
    ItemDropManager itemDropManager;
    BossManager bossManager;
    WormBossManager wormManager;
    
    public static final String PRESS_C_TRANSPORT = "Press C to transport";
    public static final String SECTOR_CLEAR = "Sector Cleared";
    
    public static int ROOM_STATUS = 0; 
    public static int ZONE = 0;
    public static void setZone(int z){
        ZONE = z;
    }
    
    int currency = 0, viby = 0, threatWidth = 0;
    boolean movingRooms = false;
    public static boolean createItemDrop = false;
    public static float itemX = 0, itemY = 0;
    public static int kills = 0, score = 0, sector = 0, shield = 100;
    public static void updateKills(float x, float y){
        kills++;
        score += 10;
        itemX = x;
        itemY = y;
        createItemDrop = true;
    }
    
    // helper methods
    public static boolean checkCollides(float x1, float y1, float x2, float y2, float r1, float r2){
        float vx = x1 - x2;
        float vy = y1 - y2;
        float vr = r1 + r2;
        return Math.abs((vx) * (vx) + (vy) * (vy)) < (vr) * (vr);
    } 
    
    void init(){
        botManager = new BotManager();
        itemDropManager = new ItemDropManager();
        blastManager = new BlastManager();
        virusManager = new VirusManager();
        bossManager = new BossManager();
        wormManager = new WormBossManager();
        virusManager.initWave(0);

        shield = 100;

        Mixer.init(8000);
    }
    
    /**
     * drawHud displays the top progress bar indicators for shield/threats/boss health 
     * as well as displaying the Score, currency and current ZONE:sector.
     * 
     */
    void drawHud(){
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
        switch(sector){
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

        //Zone : Sector 
        screen.setTextPosition(98, 3);
        screen.setTextColor(0);
        screen.print(ZONE + ":" + sector);
        
        //Score and Currency 
        screen.setTextPosition(3, screen.height()-12);
        screen.print("Score: "+score);
        
        screen.setTextPosition(140, screen.height()-12);
        screen.print("$$: " + currency);
        
        screen.setTextPosition(2, 12);
        // screen.println(blastManager.getAccuracy());
    }
    
    void drawGrid(){
        // draw grid
        if(ZONE % 2 == 1){
            drawHexGrid();
        }else{
            drawRectGrid();
        }
    }
    
    void drawRectGrid(){
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 9; j++){
                screen.drawRect(6+i*16, 16+j*16, 16, 16, 12);
            }
        }
    }
    
    void drawHexGrid(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 7; j++){
                int x = 16+i*20;
                int y = 18+j*20;
                screen.drawHLine(x,     y,    8,          12);//top
                screen.drawLine(x+8,    y,    x+14, y+6,  12);
                screen.drawVLine(x+14,  y+6,  8,          12);//right
                screen.drawLine(x+14,   y+14, x+8,  y+20, 12);
                screen.drawHLine(x,     y+20, 8,          12);//bottom
                screen.drawLine(x,      y+20, x-6,  y+14, 12);
                screen.drawVLine(x-6,   y+6,  8,          12);//left
                screen.drawLine(x-6,    y+6,  x,    y,    12);
            }
        }
    }
    
    // TODO: Refactor into more manageable methods.
    void update(){
        screen.clear(3);

        if(createItemDrop){
            createItemDrop = false;
            itemDropManager.newDrop(itemX, itemY);
        }
        
        // If not traveling
        if(ROOM_STATUS != 2){
            botManager.updateBotMovement();
       
            //START Move Blast
            blastManager.update(botManager.getAttacking(), botManager.getX()+7, botManager.getY()+6, botManager.getDir());
            blastManager.render();
        }
        switch(ROOM_STATUS){
            case 0:// threats incoming
                //Draw to screen
                drawGrid();
                itemDropManager.updateAndRender();
                if(itemDropManager.checkCollect(botManager.getX()+7, botManager.getY()+8)){
                    currency++;
                }
                if(shield <= 0){
                    Game.changeState(new TitleScene());
                }
                
                botManager.render();
                
                if(sector == 4){
                    bossManager.update(blastManager, (botManager.getX()+7), (botManager.getY()+8));
                    bossManager.render();
                    if(bossManager.cleared()){
                        ROOM_STATUS = 1; // CLEARED!
                    }
                }else if (sector == 8) {
                    wormManager.update(blastManager, botManager.getX(), botManager.getY());
                    
                    if(wormManager.bodyCollidesWithBot(botManager.getX(), botManager.getY())
                    || wormManager.headCollidesWithBot(botManager.getX(), botManager.getY())){
                        botManager.setY(botManager.getY()+32);
                        shield-=10;
                    }
                    
                    wormManager.render();
                    if(wormManager.cleared()){
                        ROOM_STATUS = 1;
                    }
                }else{
                    virusManager.update(botManager.getX(), botManager.getY());
                    virusManager.checkBlastHits(blastManager);
                    if(virusManager.getThreats() == 0){
                        ROOM_STATUS = 1; // CLEARED!
                    }
                    virusManager.render();
                }
                drawHud();
                break;
            case 1:// cleared
                drawGrid();
                itemDropManager.updateAndRender();
                if(itemDropManager.checkCollect(botManager.getX()+7, botManager.getY()+7)){
                    currency++;
                }
                
                screen.setTextPosition(screen.width()/2-58, screen.height()/2);
                screen.setTextColor(0);
                screen.print(SECTOR_CLEAR);
                screen.setTextPosition(26, screen.height()/2+16);
                screen.print(PRESS_C_TRANSPORT);
                
                botManager.render();
                
                if(Button.C.justPressed()){
                    if(sector == 8){
                        switch(ZONE){
                            case 0: saveManager.firstZoneClear = true; break;
                        }
                        saveManager.saveCookie();
                        ROOM_STATUS = 4;
                    }
                    botManager.setPos(6, screen.height()/2);
                    virusManager.resetAll();
                    sector++;
                    ROOM_STATUS = 2;
                }
                
                drawHud();
                break;
            case 2://traveling
                viby = Math.random(-2, 3);
                screen.fillCircle(screen.width()/2-8+viby, screen.height()/2+viby, 5, 10);
                
                if(Button.A.justPressed() && blastManager.getRate() < 10){
                    currency -= 2;
                    blastManager.incRate();
                }
                if(Button.B.justPressed() && blastManager.getRefresh() > 0){
                    currency -= 2;
                    blastManager.incRefresh();
                }
                if(Button.Up.justPressed() && shield < 100){
                    currency -= 10;
                    if(shield + 10 > 100)shield = 100;
                    else{
                        shield+=10;
                    }
                }

                screen.setTextPosition(2, 16);
                screen.setTextColor(0);
                screen.println("$2  - Rate: " + blastManager.getRate());
                screen.println("$2  - Refresh: " + blastManager.getRefresh());
                screen.println("$10 - Shield: " + shield);

                if(Button.C.justPressed()){
                    if(sector == 4){
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
                    }else if (sector == 8){
                        // wormManager.init();
                    }else{
                        virusManager.initWave(sector);
                    }
                    itemDropManager.clear();
                    ROOM_STATUS = 3;
                }
                
                break;
            case 3://Prep
                drawGrid();
                botManager.render();
                screen.drawCircle(screen.width()/2, screen.height()/2, 16, 7);
                if(checkCollides(botManager.getX()+8, botManager.getY()+8, screen.width()/2, screen.height()/2, 8, 8)){
                    ROOM_STATUS = 0;
                }
                drawHud();
                break;
            case 4://Summary!
                screen.setTextPosition(2, 10);
                screen.println("Score: " + score);
                screen.println("Press C to go to title screen");
                if(Button.C.justPressed()){
                    Game.changeState(new TitleScene());
                }
                break;
        }
        
        screen.flush();
    }
}
