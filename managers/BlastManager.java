import sprites.Blast;

import Math;

public class BlastManager {
    
    int cooldown = 0;
    BlastObject[] blasts;
    
    public BlastManager(){
        blasts = new BlastObject[]{
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject()
        };
    }
    
    void update(boolean attack, float x, float y, int dir){
        if(cooldown > 0){
            cooldown--;
        }
        if(attack && cooldown == 0){
            cooldown = 10;
            for(BlastObject blast : blasts){
                if(!blast.active()){
                    blast.setX(x);
                    blast.setY(y);
                    if(dir == 0){//left
                        blast.setDir(-2.0f, 0.0f);
                    }
                    if(dir == 1){//up
                        blast.setDir(0.0f, -2.0f);
                    }
                    if(dir == 2){//right
                        blast.setDir(2.0f, 0.0f);
                    }
                    if(dir == 3){//down
                        blast.setDir(0.0f, 2.0f);
                    }
                    blast.draw = true;
                    return;
                }
            }
        }
        
        for(BlastObject b : blasts){
            b.update();
        }
    }
    
    public boolean hitEnemy(float ex, float ey, float er){
        for(BlastObject b : blasts){
            if(b.active() && Main.checkCollides(b.getX()+4, b.getY()+4, ex, ey, 4, er)){
                b.hit();
                return true;
            }
        }
        return false;
    }
    
    void render(){
        for(BlastObject b : blasts){
            b.render();
        }
    }
}


class BlastObject {
    float sx = 0, sy = 0;
    boolean draw = false;
    Blast blast;
    
    public BlastObject(){
        blast = new Blast();
        blast.fire();
    }
    
    void update(){
        if(sx != 0 && (blast.x < -8 || blast.x > 250) ){
            draw = false;
            return;
        }
        
        if(sy != 0 && (blast.y < -8 || blast.y > 170)){
            draw = false;
            return;
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