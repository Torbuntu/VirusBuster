import managers.BlastManager;
import entities.MiniBoss;
import femto.mode.HiRes16Color;

public class MiniBossManager {
    int activeBosses = 1;
    MiniBoss boss;
    
    MiniBossManager(){
        boss = new MiniBoss();
    }

    
    public void update(BlastManager blastManager, float bx, float by){
        boss.update(blastManager, bx, by);
    }
    
    public void render(HiRes16Color screen){
        boss.render(screen);
    }
    
    public int getTotalHealth(){
        return boss.maxHealth;
    }
    
    public int getCurrentHealth(){
        return boss.health;
    }
    
    public boolean cleared(){
        return !boss.getAlive();
    }
    
}