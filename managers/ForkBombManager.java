import sprites.ForkBomb;

class ForkBombManager {
    ForkBomb forkBomb;
    
    int health, total;
    float sx, sy, height = 0;
    boolean clear = false;
    int[] spikes;
    
    void init() {
        total = 200;
        health = total;
        forkBomb = new ForkBomb();
        forkBomb.x = 92;
        forkBomb.y = -32;
        height = 100;
        forkBomb.bite();
        spikes = new int[] {0,0,0,0,0,0,0,0,0,0,0,0};
        System.out.println("[I] - Fork Bomb initialized");
        sx = 1;
    }
    
    void update() {
        
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
        forkBomb.draw(Globals.screen);
        //shadow
        if(forkBomb.y < height) Globals.screen.drawCircle(forkBomb.x+16, height+16, 16, 8, true);
    }
    
    
    boolean cleared(){
        return clear;
    }
    
    int getCurrentHealth(){
        return health;
    }
    
    int getTotalHealth() {
        return total;
    }
}