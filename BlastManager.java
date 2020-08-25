import sprites.Blast;

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
            cooldown = 20;
            for(int i = 0; i < blasts.length; i++){
                if(!blasts[i].active()){
                    blasts[i].setX(x);
                    blasts[i].setY(y);
                    if(dir == 0){//left
                        blasts[i].setDir(-1.0f, 0.0f);
                    }
                    if(dir == 1){//up
                        blasts[i].setDir(0.0f, -1.0f);
                    }
                    if(dir == 2){//right
                        blasts[i].setDir(1.0f, 0.0f);
                    }
                    if(dir == 3){//down
                        blasts[i].setDir(0.0f, 1.0f);
                    }
                    blasts[i].draw = true;
                    return;
                }
            }
        }
        
        for(int i = 0; i < blasts.length; i++){
            blasts[i].update();
        }
    }
    
    boolean hitEnemy(float ex, float ey){
        for(int i = 0; i < blasts.length; i++){
            if(blasts[i].active() && blasts[i].getX() >= ex-8 && blasts[i].getX() <= ex+8 && blasts[i].getY() >= ey-8 && blasts[i].getY() <= ey+8){
                blasts[i].hit();
                return true;
            }
        }
        return false;
    }
    
    void render(){
        for(int i = 0; i < blasts.length; i++){
            blasts[i].render();
        }
    }
    
    BlastObject[] getBlasts(){
        return blasts;
    }
    
}


class BlastObject {
    float sx = 0, sy = 0;
    boolean draw = false;
    Blast blast;
    
    public BlastObject(){
        blast = new Blast();
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