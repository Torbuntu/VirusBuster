import entities.BlastObject;

public class BlastManager {
    
    // refresh -30 how quickly blasts refresh
    // rate    -3 how many blasts are active at a time
    int cooldown = 0, refresh = 30, rate = 3;
    BlastObject[] blasts;
    int shots = 0;
    int hit = 0;
    
    public BlastManager(){
        blasts = new BlastObject[]{
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject()
        };
        
        System.out.println("[I] - Blasts initialized");
    }
    
    void update(boolean attack, float x, float y, int dir){
        if(cooldown > 0){
            cooldown--;
        }
        if(attack && cooldown == 0){
            cooldown = refresh;
            for(int i = 0; i < rate; i++){
                if(!blasts[i].active()){
                    blasts[i].setX(x);
                    blasts[i].setY(y);
                    if(dir == 0){//left
                        blasts[i].setDir(-2.0f, 0.0f);
                    }
                    if(dir == 1){//up
                        blasts[i].setDir(0.0f, -2.0f);
                    }
                    if(dir == 2){//right
                        blasts[i].setDir(2.0f, 0.0f);
                    }
                    if(dir == 3){//down
                        blasts[i].setDir(0.0f, 2.0f);
                    }
                    blasts[i].draw = true;
                    shots++;
                    return;
                }
            }
        }
        
        for(int i = 0; i < rate; i++){
            blasts[i].update();
        }
    }
    
    public boolean hitEnemy(float ex, float ey, float er){
        for(BlastObject b : blasts){
            if(b.active() && Main.boundingBox(b.getX()+1, b.getY()+1,6, ex+1, ey+1, 14)){
                b.hit();
                hit++;
                return true;
            }
        }
        return false;
    }
    
    void render(){
        int cooldownWidth = (int)(cooldown * 78 / refresh);
        Main.screen.drawHLine(6, 13, cooldownWidth, 8);
        for(int i = 0; i < rate; i++){
            blasts[i].render();
        }
    }
    
    public void incRate(){
        if(rate > 10) rate = 10;
        else rate++;
    }
    public void decRate(){
        if(rate > 3) rate--;
    }
    public void incRefresh(){
        refresh-=5;
        if(refresh < 5) refresh = 5;
    }
    public void decRefresh(){
        refresh+=5;
    }
    public int getRefresh(){
        return refresh;
    }
    public int getRate(){
        return rate;
    }
    
    //TODO: This is always returning 0 for some reason. 
    public int getAccuracy(){
        if(hit == 0)return 0;
        if(shots == 0) return 0;
        return Math.abs(hit * 100 / shots);
    }
}

