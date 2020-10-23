import sprites.Bot;
import femto.input.Button;

import managers.DebrisManager;

public class BotManager {
    Bot bot;
    float sx = 0, sy = 0;
    int speed = 1, dir = 1;
    boolean attack = false;
    
    public BotManager(){
        bot = new Bot();
        bot.x = Main.screen.width()/2;
        bot.y = Main.screen.height()/2;
    }
    
    public boolean getAttacking(){return attack;}
    public int getDir(){return dir;}
    public float getX(){return bot.x;}
    public float getY(){return bot.y;}
    public void setX(float x){bot.x = x;}
    public void setY(float y){bot.y = y;}
    public void setPos(float x, float y){
        bot.x = x;
        bot.y = y;
    }
    
    /**
     * updateBotMovement controls the logic for bot movement and animation type
     * When dashing, player shield is lower and thus they take more damage.
     * Unable to shoot while dashing.
     * 
     */
    void updateBotMovement(DebrisManager debrisManager){
        //START move player
        sx = 0;
        sy = 0;
        speed = 1;
        if(Button.B.isPressed()){
            speed = 2;
        }
        if(Button.Down.isPressed() && bot.y+1 < Main.screen.height()-32){
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
            if(Button.Right.isPressed() && bot.x+1 < Main.screen.width()-20){
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
        
        if(!debrisManager.checkCollides(bot.x+sx, bot.y+sy, bot.width(), bot.height() )){
            bot.x += sx;
            bot.y += sy;
        }
       
        //END move player
    }
    
    /**
     * Draws the robot's red eyes and the dashing sparks behind the jets
     * 
     */
    void drawBotVisor(){
        // dashing and actually moving
        if(speed==2){
            if(dir == 0){//l
                Main.screen.drawHLine((int)bot.x+14, (int)bot.y+9, Math.random(2, 5), 10);
            }
            if(dir == 1){//u
                Main.screen.drawVLine((int)bot.x+4, (int)bot.y+16, Math.random(2, 5), 10);
                Main.screen.drawVLine((int)bot.x+9, (int)bot.y+16, Math.random(2, 5), 10);
            }
            if(dir == 2){//r
                Main.screen.drawHLine((int)bot.x, (int)bot.y+9, -Math.random(2, 5), 10);
            }
            if(dir == 3){//d
                Main.screen.drawVLine((int)bot.x+4, (int)bot.y, -Math.random(2, 5), 10);
                Main.screen.drawVLine((int)bot.x+9, (int)bot.y, -Math.random(2, 5), 10);
                
                //eyes
                Main.screen.fillRect((int)bot.x+3, (int)bot.y+5, 2, 2, 8);
                Main.screen.fillRect((int)bot.x+9, (int)bot.y+5, 2, 2, 8);
            }
        }
        if (speed != 2){
            if(dir == 0){
                Main.screen.drawHLine((int)bot.x+2, (int)bot.y+4, 2, 8);
            }
            if(dir == 2){
                Main.screen.drawHLine((int)bot.x+10, (int)bot.y+4, 2, 8);
            }
            if(dir == 3){
                Main.screen.drawHLine((int)bot.x+3, (int)bot.y+4, 2, 8);
                Main.screen.drawHLine((int)bot.x+9, (int)bot.y+4, 2, 8);
            }
        }
    }
    
    public void render(){
        bot.draw(Main.screen);
        drawBotVisor();
    }
    
    public void render(float x, float y){
        bot.draw(Main.screen, x, y);
        drawBotVisor();
    }
    
}