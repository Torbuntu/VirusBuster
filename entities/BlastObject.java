import audio.Shoot;
import sprites.Blast;
import femto.mode.HiRes16Color;

class BlastObject {
    float sx = 0, sy = 0;
    boolean draw = false, charged = false;
    Blast blast;
    Shoot shoot;
    
    public BlastObject(){
        blast = new Blast();
        blast.fire();
        shoot = new Shoot(0);
    }
    
    void update(){
        if(sx != 0 && (blast.x < -8 || blast.x > 250) ){
            draw = false;
        }
        
        if(sy != 0 && (blast.y < -8 || blast.y > 170)){
            draw = false;
        }
        blast.x += sx;
        blast.y += sy;
    }
    
    void render(HiRes16Color screen){
        if(draw){
            //if(charged) blast.rotoscale(screen, 0, 1.4f);
            //else 
            blast.draw(screen);
        }
    }

    void hit(){
        draw = false;
    }
    
    void init(float dx, float dy, float cx, float cy, boolean charge){
        // Default charged to false
        charged = charge;
        if(charge){
            blast.charge();
        }else{
            blast.fire();
        }
        draw = true;
        shoot.play();
        blast.x = cx;
        blast.y = cy;
        sx = dx;
        sy = dy;
    }

    float getX(){
        return blast.x;
    }
    
    float getY(){
        return blast.y;
    }
}