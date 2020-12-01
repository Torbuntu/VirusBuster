import audio.Shoot;
import sprites.Blast;

class BlastObject {
    float sx = 0, sy = 0;
    boolean draw = false;
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
    
    void render(){
        if(draw){
            blast.draw(Main.screen);
        }
    }
    
    boolean active(){
        return draw;
    }
    
    void hit(){
        draw = false;
    }
    
    void setDir(float x, float y){
        shoot.play();
        sx = x;
        sy = y;
    }
    
    void setX(float x){
        blast.x = x;
    }
    
    void setY(float y){
        blast.y = y;
    }
    
    float getX(){
        return blast.x;
    }
    
    float getY(){
        return blast.y;
    }
}