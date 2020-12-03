import entities.BlastObject;

public class BlastManager {
    
    // refresh -30 how quickly blasts refresh
    // rate    -3 how many blasts are active at a time
    int cooldown = 0;
    BlastObject[] blasts;
    
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
            cooldown = Globals.refresh;
            for(int i = 0; i < Globals.rate; i++){
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
                    Globals.shots++;
                    return;
                }
            }
        }
        
        for(int i = 0; i < Globals.rate; i++){
            blasts[i].update();
        }
    }
    
    public boolean hitEnemy(float ex, float ey, float er){
        for(BlastObject b : blasts){
            if(b.active() && Globals.boundingBox(b.getX()+1, b.getY()+1,6, ex, ey, er)){
                b.hit();
                Globals.hit++;
                return true;
            }
        }
        return false;
    }
    
    void render(){
        int cooldownWidth = (int)(cooldown * 78 / Globals.refresh);
        Globals.screen.drawHLine(6, 13, cooldownWidth, 8);
        for(int i = 0; i < Globals.rate; i++){
            blasts[i].render();
        }
    }

}

