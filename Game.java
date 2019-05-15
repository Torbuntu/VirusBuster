import femto.mode.HiRes16Color;
import femto.input.Button;
import femto.palette.Colodore;
import femto.font.Tiny;
import femto.font.Tic80;
import femto.font.Dragon;
import Background;

public class Game {

    public static void main(String[] args){
        //START Create
        HiRes16Color screen = new HiRes16Color(Dragon.data());
        Colodore.applyTo( screen );
        Background bg = new Background();
        
        boolean attack = false;
        boolean dead = false;
        Bot bot = new Bot();
        Blast blast = new Blast();
        
        Virus[] virusBank = {new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus(), new Virus()};
        Frag[] fragBank = {new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag(), new Frag()};
        boolean[] deadFrags = {false, false, false, false, false, false, false, false, false, false};
        int[] deathAnim = {50,50,50,50,50,50,50,50,50,50};
        
        int activeEnemies = 1;
        
        int kills = 0;
        
        bot.x = 2;
        bot.y = 2;
        blast.x = 0;
        blast.y = 0;
        int blastFacing = 16;
        blast.setMirrored( true );
        
        virusBank[0].x = 102;
        virusBank[0].y = 102;
        
        float sx = 0, sy = 0, bsx=1, vsx = 0, vsy = 0;
        int counter = 0;
        
        virusBank[0].walk();
        //END Create
        
        //Main game loop
        while(true){
            
            System.out.println(screen.fps());
            screen.clear( 0x44 );
            bg.draw(screen, 0.0f, 0.0f);
            screen.setTextPosition(10, 160);
            screen.print("Kills: "+kills);
            
            //START move player
            sx = 0;
            sy = 0;
            if(Button.Down.isPressed()){
                bot.walkVert();    
                sy = 1;
            }
            if( Button.Up.isPressed()){
                bot.walkVert();
                sy = -1;
            }
            if(sy == 0){
                if(Button.Right.isPressed()){
                    bot.setMirrored( true );
                    
                    if(!attack){
                       blastFacing = 16; 
                       bsx = 1;
                       blast.setMirrored( true );
                    } 
                    bot.walkHori();
                    sx = 1;
                    
                }
                if(Button.Left.isPressed()){
                    bot.setMirrored( false );
                    
                    if(!attack) {
                        blastFacing = -16;
                        bsx = -1;
                        blast.setMirrored( false );
                    }
                    bot.walkHori();
                    sx = -1;
                    
                }
            }
            
            if(sx == 0 && sy == 0) bot.idle();
            if(Button.A.isPressed() && !attack){
                bot.shoot();
                attack = true;
                blast.y = bot.y+3;
                blast.x = bot.x+blastFacing;
                sx = 0;
                sy = 0;
                blast.fire();
            }
            bot.x += sx;
            bot.y += sy;
            //END move player
            
            //START Move Blast
            if(attack){
                blast.x += bsx;
            }
            if(blast.x > 220 || blast.x < 0){
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
                    if(bot.x > 110){
                        virusBank[i].x = 230;
                    }else{
                        virusBank[i].x = 0;
                    }
                    if(bot.y > 68){
                        virusBank[i].y = 180;
                    }else{
                        virusBank[i].y = 0;
                    }
                    
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
}
