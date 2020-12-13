import managers.BlastManager;
import entities.MiniBoss;
import femto.mode.HiRes16Color;

public class MiniBossManager {
    int activeBosses = 1;
    MiniBoss[] bosses = new MiniBoss[]{
        new MiniBoss(),
        new MiniBoss(),
        new MiniBoss(),
        new MiniBoss()
    };
    

    public void init(int number){
        activeBosses = number;
        for(int i = 0; i < activeBosses; i++){
            bosses[i].init();
        }
    }
    
    public void update(BlastManager blastManager, float bx, float by){
        for(int i = 0; i < activeBosses; i++){
            bosses[i].update(blastManager, bx, by);
            for(int x = 0; x < activeBosses; x++){
                if(x != i){
                    if(Globals.boundingBox(bosses[i].virus.x, bosses[i].virus.y, 28, bosses[x].virus.x, bosses[x].virus.y, 28)){
                        if(bosses[i].virus.x < bosses[x].virus.x) {
                            bosses[i].sx = (-2.0f);
                            bosses[x].virus.x += (2.0f);
                        }else{
                            bosses[i].sx = (2.0f);
                            bosses[x].virus.x += (-2.0f);
                        }
                        if(bosses[i].virus.y < bosses[x].virus.y)  {
                            bosses[i].sy = (-2.0f);
                            bosses[x].virus.y += (2.0f);
                        }else{
                            bosses[i].sy = (2.0f);
                            bosses[x].virus.y += (-2.0f);
                        }
                    }
                }
            }
        }
    }
    
    public void render(HiRes16Color screen){
        for(int i = 0; i < activeBosses; i++){
            bosses[i].render(screen);
        }
    }
    
    public int getTotalHealth(){
        int total = 0;
        for(int i = 0; i < activeBosses; i++){
            total += bosses[i].maxHealth;
        }
        return total;
    }
    
    public int getCurrentHealth(){
        int total = 0;
        for(int i = 0; i < activeBosses; i++){
            total += bosses[i].health;
        }
        return total;
    }
    
    public boolean cleared(){
        for(int i = 0; i < activeBosses; i++){
            if(bosses[i].getAlive()){
                return false; // not clear
            }
        }
        return true; // CLEARED!
    }
    
}