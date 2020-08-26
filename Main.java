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
    
    float sx = 0, sy = 0;
    int dir = 1;
    boolean attack = false;
    
    public static int roomNumber = 0;
    public static int score = 0;
    public static int roomThreshold = 25;
    public static int roomThreats = 25;
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
        screen.setTextPosition(3, 3);
        screen.setTextColor(3);
        screen.print("Score: "+score);
        
        //Threat Level
        screen.drawHLine(3, screen.height()-3, roomThreats*2, 7);
        
        //Room number
        screen.setTextPosition(3, screen.height()-12);
        screen.setTextColor(0);
        screen.print(roomNumber);
    }
    
    void update(){
        screen.clear(3);

        // draw grid
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 9; j++){
                Main.screen.drawRect(6+i*16, 16+j*16, 16, 16, 12);
            }
        }
        
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
        
        //START Move Blast
        blastManager.update(attack, bot.x+8, bot.y+6, dir);
        
        
        //Draw to screen
        blastManager.render();
        bot.draw(screen);
        if(dir == 3){
            screen.drawHLine((int)bot.x+6, (int)bot.y+4, 4, 8);
        }
        
        if(roomThreats == 0){
            screen.fillTriangle(screen.width()-6, screen.height()/2, screen.width()-24, screen.height()/2-8, screen.width()-24, screen.height()/2+8, 0);
            
            screen.setTextPosition(screen.width()/2-30, screen.height()/2);
            screen.setTextColor(0);
            screen.print("Sector Cleared");
            if(bot.x > screen.width()-30){
                bot.x = 6;
                bot.y = screen.height()/2;
                roomThreats = 25;
                virusManager.resetAll();
                roomNumber++;
            }
        }else{
            virusManager.update(bot.x, bot.y);
            virusManager.checkBlastHits(blastManager);
            virusManager.render();
        }
        
        drawHud();
        screen.flush();
    }
    
    
    void shutdown(){
        screen = null;
    }
}
