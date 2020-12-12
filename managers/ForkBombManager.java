import managers.BlastManager;
import audio.Explode;
import sprites.ForkBomb;

class ForkBombManager {
    ForkBomb forkBomb;
    Explode explode;
    
    int health, total, dying, hurt, charge = 500, retreat = 0, sliding = 400, index = 0, shoot = 15;
    float sx, sy, height;
    Spike[] spikes;
    
    void init() {
        explode = new Explode(1);
        forkBomb = new ForkBomb();
        forkBomb.bite();
        forkBomb.x = 92;
        forkBomb.y = -32;
        
        spikes = new Spike[] {
            new Spike(),
            new Spike(),
            new Spike(),
            new Spike(),
            new Spike(),
            new Spike(),
            new Spike(),
            new Spike(),
            new Spike()
        };
        
        dying = 100;
        total = 10;
        health = total;
        hurt = 0;
        
        height = 100;
        
        sx = 1;
        
        System.out.println("[I] - Fork Bomb initialized");
        
    }
    
    /**
     * Start: Move side to side (noticed by shadow)
     * Stops to drop down. Then moves side to side for X iterations
     * When charge is ready, stops to spread spikes.
     * Flies back up off the screen.
     * goto:start
     * 
     */ 
    void update(BlastManager blastManager, float bx, float by, boolean dash) {
        if(health == 0)return;
        int damage = blastManager.hitEnemy(forkBomb.x+8, forkBomb.y+17, 14);
        if(hurt == 0 && damage > 0 ){
            health-=damage;
            if(health <= 0){
                explode.play();
                forkBomb.die();
            }
            if(hurt == 0)hurt = 35;
        }
        
        sy = 0;
        // move side side, randomly adjusting the height variable
        if(sliding > 0){
            sliding--;
            if(forkBomb.x < 16 || forkBomb.x > 172) sx = -sx;
            height += Math.random(-1, 2);
            if(height < 32) height = 32;
            if(height > 110) height = 110;
        }
        
        // Stops sliding, drops down to the height variable.
        if(sliding == 0 && forkBomb.y < height){
            sy = 2;
        } else {
            forkBomb.x += sx;
        }
        
        // Slide and charge
        if(sliding == 0 && retreat == 0 && forkBomb.y >= height){
            if(charge > 0){
                charge--;
                // slide while charging
                if(forkBomb.x < 16 || forkBomb.x > 172) sx = -sx;
            }else{
                //don't move while shooting
                sx = 0;
                if(shoot > 0) shoot--;
                else {
                    System.out.println("[I] - Fork Bomb pew pew");
                    fire();
                    shoot = 15;
                }
                if(index >= spikes.length) {
                    index = 0;
                    charge = 500;
                    retreat = 150;
                }
            }
        }
        
        // retreat
        if(retreat > 0){
            retreat--;
            sy = -1;
            if(retreat == 0){
                sx = 1;
                sliding = 400;
            }
        }
        
        // update spikes
        for(Spike s : spikes){
            if(s.active) s.update(bx, by, dash);
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
            if(forkBomb.getCurrentFrame()==forkBomb.getEndFrame()) forkBomb.dead();
        }else{
            forkBomb.bite();
        }
        for(Spike s : spikes){
            if(s.active)s.render();
        }
        
        forkBomb.draw(Globals.screen);
        //shadow
        if(forkBomb.y < height) Globals.screen.drawCircle(forkBomb.x+16, height+16, 16, 8, true);
    }
    
    boolean allActive(){
        for(Spike s : spikes){
            if(!s.active)return false;
        }
        return true;
    }
    
    void fire(){
        System.out.println("[I] - Index: " + index);
        spikes[index].x = forkBomb.x+16;
        spikes[index].y = forkBomb.y+16;
        int sx = Math.random(-1, 2);
        int sy = Math.random(-1, 2);
        if(sx == 0) sx = 1;
        if(sy == 0) sy = 0;
        spikes[index].sx = sx;
        spikes[index].sy = sy;
        spikes[index].active = true;
        spikes[index].move = 20;
        if(index < spikes.length)index++;
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

class Spike{
    boolean active = false;
    float x, y;
    int sx, sy, move;
    
    void update(float bx, float by, boolean dash){
        if(Globals.boundingBox(x, y, 6, bx, by, 16)){
            if(!dash) Globals.shield -= 5;
            active = false;
        }
        if(move > 0){
            move--;
            x+=sx;
            y+=sy;
        }
    }
    void render(){
        Globals.screen.drawCircle(x, y, 6, 8, true);
    }
}