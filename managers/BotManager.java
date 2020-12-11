import sprites.Bot;
import sprites.BotHead;
import femto.input.Button;

import femto.mode.HiRes16Color;

import managers.DebrisManager;

public class BotManager {
    Bot bot;
    BotHead head;
    int sx = 0, sy = 0;
    int speed = 1, dir = 1, ghost = 0;
    boolean attack = false;
    
    public BotManager(){
        bot = new Bot();
        head = new BotHead();
        head.x = 110;
        head.y = 88;
        
        head.down();
        
        System.out.println("[I] - Bot initialized");
    }
    
    public boolean getAttacking(){return attack;}
    public int getDir(){return dir;}
    public float getX(){return head.x;}
    public float getY(){return head.y;}
    public void setX(float x){head.x = x;}
    public void setY(float y){head.y = y;}
    public void setPos(float x, float y){
        head.x = x;
        head.y = y;
    }
    
    void updateBotMovement(){
        //START move player
        sx = 0;
        sy = 0;
        speed = 1;
        if(Button.B.isPressed()){
            speed = 2;
        }
        if(Button.Down.isPressed() && head.y+1 < 144){
            if(speed==2){
                if(ghost==0)ghost=6;
                head.dashDown();
            }else{
                if(!attack){
                    head.down();
                }
                bot.walkVert(); 
            }
               
            sy = speed;
            if(!attack){
                dir = 3;
            }
        }
        if( Button.Up.isPressed() && head.y-1 > 16){
            if(speed==2){
                if(ghost==0)ghost=6;
                head.dashUp();
            }else{
                if(!attack){
                    head.up();
                }
                bot.walkVert(); 
            }
            sy = -speed;
            if(!attack){
                dir = 1;
            }
        }
        if(sy == 0){
            if(Button.Right.isPressed() && head.x+1 < 200){
                bot.setMirrored( true );
                if(speed==2){
                    if(ghost==0)ghost=6;
                    head.setMirrored( true );
                    head.dashHori();
                }else{
                    if(!attack){
                        head.setMirrored( false );
                        head.right();
                    }
                    bot.walkHori();
                }
                sx = speed;
                if(!attack){
                    dir = 2;
                }
            }
            if(Button.Left.isPressed() && head.x-1 > 5){
                bot.setMirrored( false );
                
                if(speed==2){
                    if(ghost==0)ghost=6;
                    head.setMirrored( false );
                    head.dashHori();
                }else{
                    if(!attack){
                        head.setMirrored( false );
                        head.left();
                    }
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
            else if(speed!=2){
                bot.idle();
                if(dir == 0)head.left();
                if(dir == 1)head.up();
                if(dir == 2)head.right();
                if(dir == 3)head.down();
            } 
        }
        if(Button.A.isPressed()) attack = true;
        else attack = false;
        if(Button.B.isPressed()) attack = false;
        
        if(speed==2 && ghost > 0)ghost--;
        if(Globals.hurt > 0)Globals.hurt--;
        
        head.x += sx;
        head.y += sy;
        //END move player
    }
    
    /**
     * updateBotMovement controls the logic for bot movement and animation type
     * When dashing, player shield is lower and thus they take more damage.
     * Unable to shoot while dashing.
     * 
     */
    void updateBotMovement(DebrisManager debrisManager, boolean checkDebris){
        //START move player

        updateBotMovement();
        if(checkDebris && debrisManager.checkCollides(head.x+2+sx, head.y+2+sy, 8, 12 )){
            head.x -= sx;
            head.y -= sy;
        }

        //END move player
    }

    
    public void render(HiRes16Color screen){
        if(Globals.hurt%2 != 0)return;
        
        head.draw(screen);
        if(speed != 2) bot.draw(screen, head.x, head.y+9);
        else{
            if(ghost > 2){ 
                switch(dir){
                    case 0:screen.drawRect(head.x-sx*8, head.y+2, 6, 6, 15, true);break;//left
                    case 2:screen.drawRect(head.x-sx*6, head.y+2, 6, 6, 15, true);break;//right
                    case 1:screen.drawRect(head.x+2, head.y-sy*6, 6, 6, 15, true);break;//up
                    case 3:screen.drawRect(head.x+2, head.y-sy*6, 6, 6, 15, true);break;//down
                }
            }
        }
    }

}