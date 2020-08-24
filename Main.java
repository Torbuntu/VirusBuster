import femto.mode.HiRes16Color;
import femto.Game;
import femto.State;
import femto.input.Button;
import femto.palette.Colodore;
import femto.font.TIC80;
import femto.font.Dragon;

import sprites.Background;
import sprites.Blast;
import sprites.Bot;
import sprites.Frag;
import sprites.Virus;

public class Main extends State {
    
    HiRes16Color screen = new HiRes16Color(Colodore.palette(), Dragon.font());
    
    Background bg;
    
    boolean attack = false;
    boolean dead = false;
    Bot bot;
    Blast blast;
    
    Virus[] virusBank;
    Frag[] fragBank = {new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag()};
    boolean[] deadFrags = {false, false, false, false, false, false, false, false, false, false};
    int[] deathAnim = {50,50,50,50,50,50,50,50,50,50};
    
    int activeEnemies = 1;
    
    int kills = 0;

    int blastFacing = 16;
    int blastNextFace = 16;
    int blastNextX = 0;
    int blastNextY = 0;
    boolean flipBlastVert = false;
    boolean flipBlastHori = false;
    
    float sx = 0, sy = 0, bsx=1, bsy = 0, vsx = 0, vsy = 0;
    int counter = 0;

    public static void main(String[] args){
        Game.run(Dragon.font(), new Main());
    }
    
    void init(){
        bg = new Background();
        
        virusBank = new Virus[]{new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus()};
            
        virusBank[0].x = 102;
        virusBank[0].y = 102;
    
        virusBank[0].walk();
    
        bot = new Bot();
        blast = new Blast();
        bot.x = 2;
        bot.y = 2;
        blast.x = 0;
        blast.y = 0;
        blast.setMirrored( true );
    }
    
    void shutdown(){
        screen = null;
    }
    
    void update(){
       // System.out.println(screen.fps());
        screen.clear(0);
        bg.draw(screen, 0.0f, 0.0f);
        screen.setTextPosition(10, 160);
        screen.print("Kills: "+kills);
        
        //START move player
        sx = 0;
        sy = 0;
        if(Button.Down.isPressed()){
            bot.walkVert();    
            sy = 1;
            blastNextFace = 16;
            blastNextY = 1;
            blastNextX = 0;
            flipBlastVert = true;
            
        }
        if( Button.Up.isPressed()){
            bot.walkVert();
            sy = -1;
            blastNextFace = -16;
            blastNextY = -1;
            blastNextX = 0;
            flipBlastVert = false;
            
        }
        if(sy == 0){
            if(Button.Right.isPressed()){
                bot.setMirrored( true );
                blastNextFace = 16;
                blastNextX = 1;
                blastNextY = 0;
                flipBlastHori = true;
                flipBlastVert = false;
                bot.walkHori();
                sx = 1;
                
            }
            if(Button.Left.isPressed()){
                bot.setMirrored( false );
                blastNextFace = -16;
                blastNextX = -1;
                blastNextY = 0;
                flipBlastHori = false;
                flipBlastVert = false;
                bot.walkHori();
                sx = -1;
            }
        }
        
        if(sx == 0 && sy == 0) bot.idle();
        if(Button.A.isPressed() && !attack){
            blastFacing = blastNextFace;
            bot.shoot();
            attack = true;
            bsx = blastNextX;
            bsy = blastNextY;
            blast.setFlipped(flipBlastVert);
            blast.setMirrored(flipBlastHori);
            if(bsy != 0){
                blast.y = bot.y + blastFacing;
                blast.x = bot.x;
                blast.fireVert();
            }else{
                blast.y = bot.y+3;
                blast.x = bot.x+blastFacing;
                blast.fireHori();
            }
            sx = 0;
            sy = 0;
        }
        bot.x += sx;
        bot.y += sy;
        //END move player
        
        //START Move Blast
        if(attack){
            blast.x += bsx;
            blast.y += bsy;
        }
        if(blast.x >= screen.width() || blast.x <= 0){
            blast.x = 0;
            blast.y = 0;
            attack = false;
        }
        if(blast.y > screen.height() || blast.y <= 0){
            blast.y = 0;
            blast.x = 0;
            attack = false;
        }
        //END Move Blast
        
        
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
            if(blast.x >= virusBank[i].x-8 && blast.x <= virusBank[i].x+8 && blast.y >= virusBank[i].y-8 && blast.y <= virusBank[i].y+8 && attack){
                fragBank[i].die();
                fragBank[i].x = virusBank[i].x;
                fragBank[i].y = virusBank[i].y;
                virusBank[i].x = -16;
                virusBank[i].y = -16;
                deadFrags[i] = true;
                attack = false;
                blast.x = 0;
                blast.y = 0;
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
        
        if(attack){
            blast.draw(screen);
        }
        
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
