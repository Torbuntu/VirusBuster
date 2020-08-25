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
    
    public static int kills = 0;
    public static void updateKills(){
        kills++;
    }
    
    void init(){
        bot = new Bot();
        bot.x = screen.width()/2;
        bot.y = screen.height()/2;
        
        blastManager = new BlastManager();
        virusManager = new VirusManager();
    }
    
    
    void drawHud(){
        screen.drawRect(0, 0, 47, 14, 0);
        screen.fillRect(2, 2, 45, 12, 2);
        screen.setTextPosition(3, 3);
        screen.print("Kills: "+kills);
    }
    
    void update(){
        screen.clear(3);
        
        //walls, tmp
        screen.fillRect(0, 0, 8, 180, 3);
        screen.fillRect(212, 0, 8, 180, 3);
        screen.fillRect(0, 0, 240, 8, 3);
        screen.fillRect(0, 168, 240, 8, 3);
        
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 10; j++){
                Main.screen.drawRect(6+i*16, 6+j*16, 16, 16, 12);
            }
        }
        
        
        //START move player
        sx = 0;
        sy = 0;
        if(Button.Down.isPressed()){
            bot.walkVert();    
            sy = 1;
            dir = 3;
        }
        if( Button.Up.isPressed()){
            bot.walkVert();
            sy = -1;
            dir = 1;
        }
        if(sy == 0){
            if(Button.Right.isPressed()){
                bot.setMirrored( true );
                bot.walkHori();
                sx = 1;
                dir = 2;
            }
            if(Button.Left.isPressed()){
                bot.setMirrored( false );
                bot.walkHori();
                sx = -1;
                dir = 0;
            }
        }
        
        if(sx == 0 && sy == 0) bot.idle();
        if(Button.A.isPressed()){
            bot.shoot();
            sx = 0;
            sy = 0;
        }
        if(Button.B.isPressed()){
            //sword attack
        }
        bot.x += sx;
        bot.y += sy;
        //END move player
        
        //START Move Blast
        blastManager.update(Button.A.isPressed(), bot.x, bot.y, dir);
        virusManager.update(bot.x, bot.y);
        virusManager.checkBlastHits(blastManager);
        
        //Draw to screen
        bot.draw(screen);
        virusManager.render();
        blastManager.render();
        
        // if(kills >= 15 ){
        //     activeEnemies=10;
        // }
        // else if(kills >= 10 ){
        //     activeEnemies=5;
        // }
        // else if(kills >= 5 ){
        //     activeEnemies=2;
        // }
        
        
        drawHud();
        screen.flush();
    }
    
    
    void shutdown(){
        screen = null;
    }
}
