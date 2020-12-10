import entities.BlastObject;
import femto.mode.HiRes16Color;

public class BlastManager {
    
    // refresh -30 how quickly blasts refresh
    // rate    -3 how many blasts are active at a time
    int cooldown = 0, refresh, rate;
    BlastObject[] blasts;
    float swordX = 0, swordY = 0;
    boolean sword = false;
    
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
        rate = Globals.saveManager.rate;
        refresh = Globals.saveManager.refresh;
        
        System.out.println("[I] - Blasts initialized");
    }
    
    void update(boolean attack, float x, float y, int dir){
        if(cooldown > 0){
            cooldown--;
        }
        if(attack && cooldown == 0){
            cooldown = refresh;
            if(sword){
                switch(dir){
                    case 0:// Left
                        swordX = x-16;
                        swordY = y;
                        break;
                    case 1:// Up
                        break;
                    case 2://Right
                        swordX = x+8;
                        swordY = y;
                        break;
                    case 3:// Down
                        break;
                }
            }else{
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
                        Globals.shots++;
                        return;
                    }
                }
            }
        }
        
        for(int i = 0; i < rate; i++){
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
    
    void render(HiRes16Color screen){
        screen.drawHLine(6, 13, (int)(cooldown * 78 / refresh), 8);
        if(sword){
            Globals.screen.drawLine(swordX, swordY+8, swordX-4, swordY-4, 6, true);
        }else{
            for(int i = 0; i < rate; i++){
                blasts[i].render(screen);
            }
        }
    }

}

