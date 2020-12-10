import managers.BlastManager;
import managers.BotManager;
//import sprites.Grabby;
import sprites.GrabbyHand;
import femto.mode.HiRes16Color;

class GrabbyManager {
    
    //Grabby grabby;
    GrabbyHand hand;
    
    boolean ready = false;
    
    int leftX, rightX, leftY, rightY;
    int shooting = 0, dying = 100, leftMove=1, rightMove=-2, bounce = 0, meet = 100;
    int health = 20, headX = 32, headS = 1, hitHead = 0;
    int leftHealth = 5, hitLeft = 0;
    int rightHealth = 5, hitRight = 0;
    
    void init(){
        leftX = 0;
        leftY = 16;
        rightX = 204; // 220-16;
        rightY = 136; // 176-40;
        
        hand = new GrabbyHand();
        hand.move();
        //grabby = new Grabby();
    }

    void update(BlastManager blastManager, BotManager bot){
        
        // Blast manager
        
        // hit left hand
        if(leftHealth > 0 && hitLeft == 0 && blastManager.hitEnemy(leftX, leftY+12, 10)){
            leftHealth--;
            hitLeft = 35;
        }
        if(rightHealth > 0 && hitRight == 0 && blastManager.hitEnemy(rightX+8, rightY+12, 10)){
            rightHealth--;
            hitRight = 35;
        }
        
        if(rightHealth == 0 && leftHealth == 0 && hitHead == 0 && blastManager.hitEnemy(headX+6, 8, 20 )){
            health--;
            hitHead = 20;
        }

        if(shooting == 0){
            if(headX < 16 || headX > 204) headS = -headS;
            
            if(hitLeft == 0 && leftHealth > 0) {
                if(leftY < 16 || leftY > 140){
                    leftMove = -leftMove;
                    bounce++;   
                }
                leftY += leftMove;
            } else hitLeft--;
            
            if(hitRight == 0 && rightHealth > 0) {
                if(rightY < 16 || rightY > 140){
                    rightMove = -rightMove;
                    bounce++;   
                }
                rightY += rightMove;
            } else hitRight--;
            
            if(hitHead == 0) headX += headS;
            else hitHead--;
        }else{
            if(ready){
                if(leftY > 20){
                    leftY-=1;
                    rightY-=1;
                }
                shooting--;
                //TODO: Check if hitting bot
                if(Globals.hurt == 0 && (int)(leftY+12) > bot.getY() && (int)(leftY+18) < bot.getY()+12){
                    Globals.shield-=15;
                    Globals.hurt = 30;
                    bot.head.y = leftY - 12;
                }
                
                // Shooting is over at 0
                if(shooting == 0) {
                    rightY += 3;
                    if(leftMove > 1){
                        rightMove = 2;
                        leftMove = -1;
                    }else{
                        leftMove = 2;
                        rightMove = -1;
                    }
                }
            }else{
                if(leftY == meet && rightY == meet){
                    ready = true;
                }else{
                    if(leftY != meet){
                        if(leftY > meet)leftY -= 1;
                        else leftY += 1;
                    }
                    if(rightY != meet){
                        if(rightY > meet) rightY -= 1;
                        else rightY += 1;
                    }
                }
            }
        }
        
        if(bounce > 14 && shooting == 0){
            ready = false;
            bounce = 0;
            shooting = 100;
            meet = Math.random(100, 140);
        }
    }
    
    void render(){
        HiRes16Color screen = Globals.screen;
        if(shooting > 0 && ready){
            screen.drawHLine(13, (int)(leftY+16), 204, 8);
        }
        //grabby.draw(Globals.screen);
        hand.setMirrored(false);
        hand.setFlipped(false);
        hand.draw(screen, leftX, leftY);
        hand.setFlipped(true);
        hand.draw(screen, leftX, leftY+16);
        
        hand.setMirrored(true);
        hand.setFlipped(false);
        hand.draw(screen, rightX, rightY);
        hand.setFlipped(true);
        hand.draw(screen, rightX, rightY+16);
        
        // draw head
        screen.drawRect(headX, 8, 32, 20, 8);
    }
    
    int getCurrentHealth(){
        return health + leftHealth + rightHealth;
    }
    
    int getTotalHealth(){
        return 30; // 20 + 5 + 5
    }
    
    boolean cleared(){
        return dying == 0;
    }
    
}