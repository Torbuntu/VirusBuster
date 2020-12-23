import femto.mode.HiRes16Color;

import sprites.BossBlast;

class Spike{
    BossBlast blast;
    boolean active = false;
    float x, y;
    int sx, sy, move;
    
    Spike(){
        blast = new BossBlast();
        blast.fire();
    }
    
    void update(float bx, float by, boolean dash){
        if(Globals.boundingBox(x, y, 16, bx, by, 16)){
            if(!dash) {
                Globals.shield -= 15;
                Globals.hurt = 10;
                Globals.pain.play();
            }
            active = false;
        }
        if(move > 0){
            move--;
            x+=sx;
            y+=sy;
            if(x < 16){
                x = 16;
                y+=1;
            }
            if(y > 150)y = 150;
        }
    }
    void render(HiRes16Color screen){
        blast.draw(screen, x, y);
        //screen.drawCircle(x, y, 6, 8, true);
    }
}