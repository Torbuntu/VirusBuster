import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.palette.Colodore;
import femto.font.TIC80;
import femto.font.Dragon;

//import sprites.Background;
import sprites.Blast;
import sprites.Bot;
import sprites.Frag;
import sprites.Virus;

import BlastManager;

public class Main extends State {
    
    public static HiRes16Color screen = new HiRes16Color(Colodore.palette(), Dragon.font());
    
    boolean attack = false;
    boolean dead = false;
    Bot bot;
    BlastManager blastManager;
    
    Virus[] virusBank;
    Frag[] fragBank = {new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag()};
    boolean[] deadFrags = {false, false, false, false, false, false, false, false, false, false};
    int[] deathAnim = {50,50,50,50,50,50,50,50,50,50};
    
    int activeEnemies = 1;
    
    int kills = 0;

    float sx = 0, sy = 0, bsx=1, bsy = 0, vsx = 0, vsy = 0;
    int counter = 0, dir = 1;

    public static void main(String[] args){
        Game.run(Dragon.font(), new Main());
    }
    
    void init(){
        virusBank = new Virus[]{new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus()};
            
        virusBank[0].x = 102;
        virusBank[0].y = 102;
    
        virusBank[0].walk();
    
        bot = new Bot();
        bot.x = 2;
        bot.y = 2;
        
        blastManager = new BlastManager();
    }
    
    void shutdown(){
        screen = null;
    }
    
    void update(){
        screen.clear(4);
        screen.setTextPosition(10, 10);
        screen.print("Kills: "+kills);
        
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
        if(Button.A.isPressed() && !attack){
            bot.shoot();
            attack = true;
            sx = 0;
            sy = 0;
        }
        bot.x += sx;
        bot.y += sy;
        //END move player
        
        //START Move Blast
        blastManager.update(Button.A.isPressed(), bot.x, bot.y, dir);
        blastManager.render();
        
        for(int i = 0; i < activeEnemies; i++){
            //START Move Virus
            if(virusBank[i].x < bot.x){
                vsx = 0.5f;
            }
            if(virusBank[i].x > bot.x){
                vsx = -0.5f;
            }
            if(virusBank[i].y > bot.y){
                vsy = -0.5f;
            }
            if(virusBank[i].y < bot.y){
                vsy = 0.5f;
            }
            
            virusBank[i].x += vsx;
            virusBank[i].y += vsy;
            //END Move Virus
            //Bot close to virus
            if(bot.x >= (virusBank[i].x - 32) && bot.x <= (virusBank[i].x + 32) && bot.y >= (virusBank[i].y - 32) && bot.y <= (virusBank[i].y + 32) ){
                if(bot.x >= (virusBank[i].x - 32) && bot.x <= virusBank[i].x){
                    virusBank[i].setMirrored(true);
                }
                if(bot.x <= (virusBank[i].x + 32) && bot.x >= virusBank[i].x){
                    virusBank[i].setMirrored(false);
                }
                
                virusBank[i].bite();
            }else{
                virusBank[i].walk();
            }
            //END bot close to virus    
            
            //START Bolt hit enemies
            if(blastManager.hitEnemy(virusBank[i].x, virusBank[i].y)){
                fragBank[i].die();
                fragBank[i].x = virusBank[i].x;
                fragBank[i].y = virusBank[i].y;
                virusBank[i].x = -16;
                virusBank[i].y = -16;
                deadFrags[i] = true;
                attack = false;
                kills++;
            }
            //END Bolt Hit Enemies
            
            //START Enemy drawing
            if(deadFrags[i]){
                if(deathAnim[i] > 0){
                    fragBank[i].draw(screen);
                    deathAnim[i]--;
                }
            }else{
                virusBank[i].draw(screen);
            }
            
            if(deathAnim[i] == 0){
                deadFrags[i] = false;
                virusBank[i].walk();
                int r = Math.random(0,2);
				if(r== 1){
					virusBank[i].x = 0;
				}else{
					virusBank[i].x = 230;
				}

				virusBank[i].y = Math.random(0, 180);
                
                deathAnim[i] = 50;
            }
            //END Enemy drawing
        }
        
        //Draw to screen
        bot.draw(screen);
        
        if(kills >= 15 ){
            activeEnemies=10;
        }
        else if(kills >= 10 ){
            activeEnemies=5;
        }
        else if(kills >= 5 ){
            activeEnemies=2;
        }
        screen.flush();
    }
}
