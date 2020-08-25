import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.palette.Colodore;
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
    
    public static HiRes16Color screen = new HiRes16Color(Colodore.palette(), Dragon.font());
    
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
    
    void update(){
        screen.clear(4);
        screen.setTextPosition(10, 10);
        screen.print("Kills: "+kills);
        
        //walls, tmp
        screen.fillRect(0, 0, 8, 180, 2);
        screen.fillRect(212, 0, 8, 180, 2);
        screen.fillRect(0, 0, 240, 8, 2);
        screen.fillRect(0, 168, 240, 8, 2);
        
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
        screen.flush();
    }
    
    
    void shutdown(){
        screen = null;
    }
}
