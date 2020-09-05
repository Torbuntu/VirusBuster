import managers.BlastManager;
import managers.BossMode;

public class BossManager {
    int activeBosses = 1;
    BossMode[] bosses = new BossMode[]{
        new BossMode(),
        new BossMode(),
        new BossMode(),
        new BossMode()
    };
    

    public void init(int number, int[] types){
        activeBosses = number;
        for(int i = 0; i < activeBosses; i++){
            bosses[i].init(types[i]);
        }
    }
    
    public void update(BlastManager blastManager, float x, float y){
        for(int i = 0; i < activeBosses; i++){
            bosses[i].update(blastManager, x, y);
            for(int x = 0; x < activeBosses; x++){
                if(x != i){
                    if(Main.checkCollides(bosses[i].getX()+16, bosses[i].getY()+16, bosses[x].getX()+16, bosses[x].getY()+16, 14, 14)){
                        if(bosses[i].getX() < bosses[x].getX()) {
                            bosses[i].setSpeedX(-2.0f);
                            bosses[x].incX(2.0f);
                        }else{
                            bosses[i].setSpeedX(2.0f);
                            bosses[x].incX(-2.0f);
                        }
                        if(bosses[i].getY() < bosses[x].getY()) {
                            bosses[i].setSpeedY(-2.0f);
                            bosses[x].incY(2.0f);
                        }else{
                            bosses[i].setSpeedY(2.0f);
                            bosses[x].incY(-2.0f);
                        }
                    }
                }
            }
        }
    }
    
    public void render(){
        for(int i = 0; i < activeBosses; i++){
            bosses[i].render();
        }
    }
    
    public int getTotalHealth(){
        int total = 0;
        for(int i = 0; i < activeBosses; i++){
            total += bosses[i].getMaxHealth();
        }
        return total;
    }
    
    public int getCurrentHealth(){
        int total = 0;
        for(int i = 0; i < activeBosses; i++){
            total += bosses[i].getHealth();
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