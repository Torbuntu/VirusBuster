import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.palette.UltimaViSharpX68000;
import femto.font.Dragon;

//import sprites.
import sprites.Blast;
import sprites.Bot;
import sprites.Frag;
import sprites.Virus;

import TitleScene;

import managers.ItemDropManager;
import managers.BlastManager;
import managers.VirusManager;
import managers.BossManager;

class SaveManager extends femto.Cookie {
    SaveManager() {
        super();
        begin("VBUST");
    }
    boolean firstZoneClear;
    boolean secondZoneClear;
    boolean thirdZoneClear;
    boolean fourthZoneClear;
    boolean endlessUnlocked;
    
    int highScore;
}

public class Main extends State {
    public static void main(String[] args){
        Game.run(Dragon.font(), new TitleScene());
    }
    public static final SaveManager saveManager = new SaveManager();
    public static HiRes16Color screen = new HiRes16Color(UltimaViSharpX68000.palette(), Dragon.font());
    
    Bot bot;
    BlastManager blastManager;
    VirusManager virusManager;
    ItemDropManager itemDropManager;
    BossManager bossManager;
    
    //0 = enemies!, 1 = threats cleared, 2 = Traveling
    public static int ROOM_STATUS = 0; 
    public static int ZONE = 0;
    public static void setZone(int z){
        ZONE = z;
    }
    
    
    int speed = 1, dir = 1, currency = 0, transitionCount = 250;
    float sx = 0, sy = 0;
    boolean attack = false, movingRooms = false;
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
        bot = new Bot();
        bot.x = screen.width()/2;
        bot.y = screen.height()/2;
        
        itemDropManager = new ItemDropManager();
        blastManager = new BlastManager();
        virusManager = new VirusManager();
        bossManager = new BossManager();
        virusManager.initWave(0);
    }
    
    /**
     * updateBotMovement controls the logic for bot movement and animation type
     * When dashing, player shield is lower and thus they take more damage.
     * Unable to shoot while dashing.
     * 
     */
    void updateBotMovement(){
        //START move player
        sx = 0;
        sy = 0;
        speed = 1;
        if(Button.B.isPressed()){
            speed = 2;
        }
        if(Button.Down.isPressed() && bot.y+1 < screen.height()-32){
            if(speed==2){
                bot.dashVert();
            }else{
                bot.walkVert(); 
            }
               
            sy = speed;
            if(!attack){
                dir = 3;
            }
        }
        if( Button.Up.isPressed() && bot.y-1 > 16){
            if(speed==2){
                bot.dashVert();
            }else{
                bot.walkVert(); 
            }
            sy = -speed;
            if(!attack){
                dir = 1;
            }
        }
        if(sy == 0){
            if(Button.Right.isPressed() && bot.x+1 < screen.width()-20){
                bot.setMirrored( true );
                if(speed==2){
                    bot.dashHori();
                }else{
                    bot.walkHori();
                }
                sx = speed;
                if(!attack){
                    dir = 2;
                }
            }
            if(Button.Left.isPressed() && bot.x-1 > 5){
                bot.setMirrored( false );
                if(speed==2){
                    bot.dashHori();
                }else{
                    bot.walkHori();
                }
                sx = -speed;
                if(!attack){
                    dir = 0;
                }
            }
        }
        
        if(sx == 0 && sy == 0) {
            if(attack) bot.shoot();
            else if(speed!=2) bot.idle();
        }
        if(Button.A.isPressed()) attack = true;
        else attack = false;
        if(Button.B.isPressed()) attack = false;
        
        bot.x += sx;
        bot.y += sy;
        //END move player
    }
    
    /**
     * drawHud displays the top progress bar indicators for shield/threats/boss health 
     * as well as displaying the Score, currency and current ZONE:sector.
     * 
     */
    void drawHud(){
        //Bot Shield
        screen.fillRect(2, 2, 86, 12, 2);//background grey
        screen.drawVLine(0, 0, 14, 0);//first stroke down
        screen.drawHLine(0, 0, 100, 0);//top line
        screen.drawHLine(0, 14, 86, 0);//bottom line
        screen.fillTriangle(87, 2, 98, 2, 87, 14, 2);//right edge
        
        // bot shield
        int shieldWidth = (int)(shield * 95 / 100);
        screen.fillRect(2, 3, shieldWidth, 8, 15);
        
        screen.fillTriangle(87, 14, 101, 0, 100, 14, 3);
        screen.drawLine(86, 14, 100, 0, 0);//finish the box
        
        
        //Threats or Boss Shield
        screen.fillRect(134, 2, 84, 12, 2);
        screen.drawVLine(219, 0, 14, 0);//first stroke down
        screen.drawHLine(120, 0, 100, 0);//top line
        screen.drawHLine(134, 14, 86, 0);//bottom line
        screen.fillTriangle(122, 2, 134, 2, 136, 14, 2);//right edge
        
        // threats or boss health
        if(sector == 4){
            int threatWidth = (int)(bossManager.getCurrentHealth() * 95 / bossManager.getTotalHealth());
            screen.fillRect(218-threatWidth, 3, threatWidth, 8, 8);
        }else{
            int threatWidth = (int)(virusManager.getThreats() * 95 / virusManager.getTotalThreats());
            screen.fillRect(218-threatWidth, 3, threatWidth, 8, 8);
        }
        
        screen.fillTriangle(121, 0, 135, 14, 121, 14, 3);
        screen.drawLine(120, 0, 134, 14, 0);//finish the box
        
        // black middle box behind the ZONE:sector indicator 
        screen.fillRect(100, 0, 19, 16, 3);
        
        
        //Zone : Sector 
        screen.setTextPosition(106, 3);
        screen.setTextColor(0);
        screen.print(ZONE + ":" + sector);
        
        //Score and Currency 
        screen.setTextPosition(3, screen.height()-12);
        screen.print("Score: "+score);
        
        screen.setTextPosition(110, screen.height()-12);
        screen.print("Currency: " + currency);
        
        screen.print("be: " + bossManager.getCurrentHealth());
    }
    
    void drawBotVisor(){
        // dashing and actually moving
        if(speed==2){
            if(dir == 0){//l
                screen.drawHLine((int)bot.x+14, (int)bot.y+9, Math.random(2, 5), 10);
            }
            if(dir == 1){//u
                screen.drawVLine((int)bot.x+4, (int)bot.y+16, Math.random(2, 5), 10);
                screen.drawVLine((int)bot.x+9, (int)bot.y+16, Math.random(2, 5), 10);
            }
            if(dir == 2){//r
                screen.drawHLine((int)bot.x, (int)bot.y+9, -Math.random(2, 5), 10);
            }
            if(dir == 3){//d
                screen.drawVLine((int)bot.x+4, (int)bot.y, -Math.random(2, 5), 10);
                screen.drawVLine((int)bot.x+9, (int)bot.y, -Math.random(2, 5), 10);
                
                //eyes
                screen.fillRect((int)bot.x+3, (int)bot.y+5, 2, 2, 8);
                screen.fillRect((int)bot.x+9, (int)bot.y+5, 2, 2, 8);
            }
        }
        if (speed != 2){
            if(dir == 0){
                screen.drawHLine((int)bot.x+2, (int)bot.y+4, 2, 8);
            }
            if(dir == 2){
                screen.drawHLine((int)bot.x+10, (int)bot.y+4, 2, 8);
            }
            if(dir == 3){
                screen.drawHLine((int)bot.x+3, (int)bot.y+4, 2, 8);
                screen.drawHLine((int)bot.x+9, (int)bot.y+4, 2, 8);
            }
        }
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
        
        if(Button.C.justPressed()){
            //TODO, go to currency
            Game.changeState(new TitleScene());
        }
        
        if(createItemDrop){
            createItemDrop = false;
            itemDropManager.newDrop(itemX, itemY);
        }
        
        updateBotMovement();
   
        //START Move Blast
        blastManager.update(attack, bot.x+7, bot.y+6, dir);
        blastManager.render();
        
        switch(ROOM_STATUS){
            case 0:// threats incoming
                //Draw to screen
                drawGrid();
                itemDropManager.updateAndRender();
                if(itemDropManager.checkCollect(bot.x+7, bot.y+8)){
                    currency++;
                }
                
                bot.setRecolor(kills);
                bot.draw(screen);
                drawBotVisor();
                
                if(sector == 4){
                    bossManager.update(blastManager, (bot.x+7), (bot.y+8));
                    bossManager.render();
                    if(bossManager.cleared()){
                        ROOM_STATUS = 1; // CLEARED!
                    }
                }else{
                    virusManager.update(bot.x, bot.y);
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
                if(itemDropManager.checkCollect(bot.x+7, bot.y+7)){
                    currency++;
                }
                
                bot.draw(screen);
                drawBotVisor();
                
                screen.drawCircle(screen.width()-64, 64, 16, 12);
                
                if(Math.abs((bot.x+7-(screen.width()-64)) * (bot.x+7-(screen.width()-64)) + (bot.y+8-64) * (bot.y+8-64)) < (7) * (8)){
                    bot.x = 6;
                    bot.y = screen.height()/2;
                    virusManager.resetAll();
                    sector++;
                    transitionCount = 250;
                    ROOM_STATUS = 2;
                }
                
                screen.setTextPosition(screen.width()/2-30, screen.height()/2);
                screen.setTextColor(0);
                screen.print("Sector Cleared");
                drawHud();
                break;
            case 2://traveling
                if(transitionCount > 0){
                    transitionCount--;
    
                    bot.draw(screen, screen.width()/2 - 8, screen.height()/2 - 8);
                    drawBotVisor();
                    screen.drawCircle(screen.width()/2, screen.height()/2, transitionCount/3, 10);
                    
                }else{
                    if(bot.y < screen.height()/2+16){
                        bot.walkVert();
                        bot.y = bot.y + 1;
                        bot.draw(screen);
                        drawBotVisor();
                    }else{
                        
                        if(sector == 4){
                            switch(ZONE){
                                case 0:
                                    bossManager.init(1, new int[]{0});
                                    break;
                                case 1:
                                    bossManager.init(2, new int[]{0, 0});
                                    break;
                                case 2:
                                    bossManager.init(3, new int[]{0, 0, 0});
                                    break;
                                case 3:
                                    bossManager.init(4, new int[]{0, 0, 0, 0});
                                    break;
                            }
                            
                        }else{
                            virusManager.initWave(sector);
                        }
                        ROOM_STATUS = 3;
                    }
                }
                break;
            case 3://Prep
                drawGrid();
                bot.draw(screen);
                drawBotVisor();
                screen.drawCircle(screen.width()/2, screen.height()/2, 16, 7);
                float bx = bot.x+8-screen.width()/2;
                float by = bot.y+8-screen.height()/2;
                float r = 8;
                if(Math.abs((bx) * (bx) + (by) * (by)) < (r) * (r)){
                    ROOM_STATUS = 0;
                }
                drawHud();
                break;
        }
        
        screen.flush();
    }
}
