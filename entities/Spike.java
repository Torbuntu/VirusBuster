import femto.mode.HiRes16Color;

class Spike{
    boolean active = false;
    float x, y;
    int sx, sy, move;
    
    void update(float bx, float by, boolean dash){
        if(Globals.boundingBox(x, y, 6, bx, by, 16)){
            if(!dash) {
                Globals.shield -= 15;
                Globals.hurt = 10;
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
        screen.drawCircle(x, y, 6, 8, true);
    }
}