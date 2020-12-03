import managers.BlastManager;
import audio.Explode;
import sprites.ForkBomb;

class ForkBombManager {
    ForkBomb forkBomb;
    Explode explode;
    
    int health, total, dying, hurt;
    float sx, sy, height;
    int[] spikes;
    
    void init() {
        explode = new Explode(1);
        forkBomb = new ForkBomb();
        forkBomb.bite();
        forkBomb.x = 92;
        forkBomb.y = -32;
        
        spikes = new int[] {0,0,0,0,0,0,0,0,0,0,0,0};
        
        dying = 150;
        total = 10;
        health = total;
        hurt = 0;
        
        height = 100;
        
        sx = 1;
        
        System.out.println("[I] - Fork Bomb initialized");
        
    }
    
    void update(BlastManager blastManager) {
        if(health == 0)return;
        if(blastManager.hitEnemy(forkBomb.x, forkBomb.y, 32)){
            health--;
            if(health <= 0){
                explode.play();
                //alive = false;
            }
            if(hurt == 0)hurt = 25;
        }
        
        sy = 0;
        
        if(forkBomb.y < height){
            sy = 2;
        } else {
            if(forkBomb.x < 16 || forkBomb.x > 172) sx = -sx;
            forkBomb.x += sx;
        }
        
        forkBomb.y += sy;
    }
    
    void render() {
        // Don't render if dead.
        if(dying == 0)return;
        if(hurt > 0){
            hurt--;
            forkBomb.die();
        }else if(health == 0 && dying > 0){
            dying--;
            if(dying == 50) explode.play();
            forkBomb.die();
        }else{
            forkBomb.bite();
        }
        
        forkBomb.draw(Globals.screen);
        //shadow
        if(forkBomb.y < height) Globals.screen.drawCircle(forkBomb.x+16, height+16, 16, 8, true);
    }
    
    
    boolean cleared(){
        return dying == 0;
    }
    
    int getCurrentHealth(){
        return health;
    }
    
    int getTotalHealth() {
        return total;
    }
}