import managers.BlastManager;
//import sprites.Grabby;
import sprites.GrabbyHand;

class GrabbyManager {
    
    //Grabby grabby;
    GrabbyHand hand;
    
    float leftX, rightX, leftY, rightY, leftMove=1.2, rightMove=-1.5;
    int shooting = 0;
    
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
        if(leftY < 16 || leftY > 140)leftMove = -leftMove;
        if(rightY < 16 || rightY > 140)rightMove = -rightMove;
        
        if(shooting == 0){
            leftY += leftMove;
            rightY += rightMove;
        }else{
            if(leftY > 16){
                leftY-=1.0f;
                rightY-=1.0f;
            }
            shooting--;
            if(shooting == 0) {
                leftY += 2.0f;
                rightY += 3.0f;
            }
        }
        
        if((int)leftY == (int)rightY && shooting == 0){
            shooting = 100;
            rightY = leftY;
        }
    }
    
    void render(){
        if(shooting > 0){
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
    
}