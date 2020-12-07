import managers.BlastManager;
//import sprites.Grabby;
import sprites.GrabbyHand;

class GrabbyManager {
    
    //Grabby grabby;
    GrabbyHand hand;
    
    boolean ready = false;
    
    float leftX, rightX, leftY, rightY;
    int shooting = 0, dying = 100, leftMove=1, rightMove=-2, bounce = 0, meet = 100;
    
    void init(){
        leftX = 0;
        leftY = 16;
        rightX = Globals.screen.width()-16;
        rightY = Globals.screen.height()-40;
        
        hand = new GrabbyHand();
        hand.move();
        //grabby = new Grabby();
    }

    void update(BlastManager blastManager, float bx, float by){
        
        if(shooting == 0){
            if(leftY < 16 || leftY > 140){
                leftMove = -leftMove;
                bounce++;   
            }
            if(rightY < 16 || rightY > 140){
                rightMove = -rightMove;
                bounce++;   
            }
            leftY += leftMove;
            rightY += rightMove;
        }else{
            if(ready){
                if(leftY > 16){
                    leftY-=1.0f;
                    rightY-=1.0f;
                }
                shooting--;
                if(shooting == 0) {
                    rightY += 3.0f;
                }
                if(leftMove > 1){
                    rightMove = 2;
                    leftMove = -1;
                }else{
                    leftMove = 2;
                    rightMove = -1;
                }
            }else{
                System.out.println("Left: " + leftY + ", Right: " + rightY + ", Meet: " + meet);
                if(leftY == meet && rightY == meet){
                    ready = true;
                }else{
                    
                    if(leftY > meet)leftY -= 1;
                    else leftY += 1;
                    
                    if(rightY > meet) rightY -= 1;
                    else rightY += 1;
                }
                
            }
        }
        
        if(bounce > 14 && shooting == 0){
            ready = false;
            meet = Math.random(100, 140);
            bounce = 0;
            shooting = 100;
            rightY = leftY;
        }
    }
    
    void render(){
        if(shooting > 0 && ready){
            Globals.screen.drawHLine(13, (int)(leftY+16), 204, 8);
        }
        //grabby.draw(Globals.screen);
        hand.setMirrored(false);
        hand.setFlipped(false);
        hand.draw(Globals.screen, leftX, leftY);
        hand.setFlipped(true);
        hand.draw(Globals.screen, leftX, leftY+16);
        
        hand.setMirrored(true);
        hand.setFlipped(false);
        hand.draw(Globals.screen, rightX, rightY);
        hand.setFlipped(true);
        hand.draw(Globals.screen, rightX, rightY+16);
        
        
    }
    
    int getCurrentHealth(){
        return 100;
    }
    
    int getTotalHealth(){
        return 100;
    }
    
    boolean cleared(){
        return dying == 0;
    }
    
}