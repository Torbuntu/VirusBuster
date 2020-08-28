import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.palette.UltimaViSharpX68000;
import femto.font.TIC80;
import femto.font.Dragon;

//import sprites.
import sprites.Blast;
import sprites.Bot;
import sprites.Frag;
import sprites.Virus;

import TitleScene;

import ItemDropManager;
import BlastManager;
import VirusManager;

public class Main extends State {
    public static void main(String[] args){
        Game.run(Dragon.font(), new TitleScene());
    }
    
    public static HiRes16Color screen = new HiRes16Color(UltimaViSharpX68000.palette(), Dragon.font());
    
    Bot bot;
    BlastManager blastManager;
    VirusManager virusManager;
    ItemDropManager itemDropManager;
    
    float sx = 0, sy = 0;
    int dir = 1;
    boolean attack = false;
    
    public boolean movingRooms = false;
    public int transitionCount = 250;
    public static boolean incoming = true;
    public static int roomNumber = 0;
    public static int score = 0;
    // public static int roomThreshold = 25;
    public static int roomThreats = 3;
    public static int kills = 0;
    public static void updateKills(){
        roomThreats--;
        kills++;
        score += 10;
    }
    
    void init(){
        bot = new Bot();
        bot.x = screen.width()/2;
        bot.y = screen.height()/2;
        
        itemDropManager = new ItemDropManager();
        blastManager = new BlastManager();
        virusManager = new VirusManager();
    }
    
    
    void drawHud(){
        //Score
        screen.fillRect(2, 2, 86, 12, 2);//background grey
        screen.drawVLine(0, 0, 14, 0);//first stroke down
        screen.drawHLine(0, 0, 100, 0);//top line
        screen.drawHLine(0, 14, 86, 0);//bottom line
        screen.fillTriangle(87, 2, 98, 2, 87, 14, 2);//right edge
        screen.drawLine(86, 14, 100, 0, 0);//finish the box
        
        
        //Room number and threats remaining
        screen.setTextPosition(3, screen.height()-12);
        screen.setTextColor(0);
        screen.print("Sector: " + roomNumber);
        if(roomThreats > 0){
            screen.print(" Threat Level: " + roomThreats);
        }else{
            screen.print(" Threats cleared!");
        }
        screen.print(" Score: "+score);
    }
    
    void drawBotVisor(){
        if(dir == 0){
            screen.drawHLine((int)bot.x+2, (int)bot.y+4, 2, 8);
        }
        if(dir == 2){
            screen.drawHLine((int)bot.x+12, (int)bot.y+4, 2, 8);
        }
        if(dir == 3){
            screen.drawHLine((int)bot.x+6, (int)bot.y+4, 4, 8);
        }
    }
    
    void drawGrid(){
        // draw grid
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 9; j++){
                Main.screen.drawRect(6+i*16, 16+j*16, 16, 16, 12);
            }
        }
    }
    
    void update(){
        screen.clear(3);

        
        
        //START move player
        sx = 0;
        sy = 0;
        if(Button.Down.isPressed() && bot.y+1 < screen.height()-32){
            bot.walkVert();    
            sy = 1;
            if(!attack){
                dir = 3;
            }
        }
        if( Button.Up.isPressed() && bot.y-1 > 16){
            bot.walkVert();
            sy = -1;
            if(!attack){
                dir = 1;
            }
        }
        if(sy == 0){
            if(Button.Right.isPressed() && bot.x+1 < screen.width()-20){
                bot.setMirrored( true );
                bot.walkHori();
                sx = 1;
                if(!attack){
                    dir = 2;
                }
            }
            if(Button.Left.isPressed() && bot.x-1 > 5){
                bot.setMirrored( false );
                bot.walkHori();
                sx = -1;
                if(!attack){
                    dir = 0;
                }
            }
        }
        
        if(sx == 0 && sy == 0) {
            if(attack) bot.shoot();
            else bot.idle();
        }
        if(Button.A.isPressed()) attack = true;
        else attack = false;
        
        if(Button.B.isPressed()){
            //sword attack?
        }
        bot.x += sx;
        bot.y += sy;
        //END move player
        
        if(Math.random(0, 30) == 10){
            itemDropManager.newDrop(Math.random(32, 200), Math.random(64, 150));
        }
        if(!movingRooms){
            drawGrid();
        }
        //START Move Blast
        blastManager.update(attack, bot.x+8, bot.y+6, dir);
        

        
        if(incoming){
            //Draw to screen
            itemDropManager.updateAndRender(bot.x, bot.y);
            blastManager.render();
            bot.draw(screen);
            drawBotVisor();
            virusManager.update(bot.x, bot.y);
            virusManager.checkBlastHits(blastManager);
            virusManager.render();
            drawHud();
        } else if(roomThreats == 0){
            screen.fillTriangle(screen.width()-6, screen.height()/2, screen.width()-24, screen.height()/2-8, screen.width()-24, screen.height()/2+8, 0);
            itemDropManager.updateAndRender(bot.x, bot.y);
            blastManager.render();
            bot.draw(screen);
            drawBotVisor();
            screen.setTextPosition(screen.width()/2-30, screen.height()/2);
            screen.setTextColor(0);
            screen.print("Sector Cleared");
            if(bot.x > screen.width()-30){
                bot.x = 6;
                bot.y = screen.height()/2;
                roomThreats = 25;
                virusManager.resetAll();
                roomNumber++;
                transitionCount = 250;
                movingRooms = true;
            }
            drawHud();
        }else if(movingRooms){
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
                    movingRooms = false;
                }
            }
        }else{
            itemDropManager.updateAndRender(bot.x, bot.y);
            blastManager.render();
            bot.draw(screen);
            drawBotVisor();
            screen.drawCircle(screen.width()/2, screen.height()/2, 16, 7);
            float bx = bot.x+8-screen.width()/2;
            float by = bot.y+8-screen.height()/2;
            float r = 8;
            if(Math.abs((bx) * (bx) + (by) * (by)) < (r) * (r)){
                incoming = true;
            }
            drawHud();
        }
        if(roomThreats == 0) incoming = false;

        
        
        screen.flush();
    }
    
    
    void shutdown(){
        screen = null;
    }
}
