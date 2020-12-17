import managers.BlastManager;
import audio.Explode;
import sprites.ForkBomb;

import femto.mode.HiRes16Color;

// TODO: Need to work on a part 2 after health is halved
class ForkBombManager {
    ForkBomb forkBomb;
    Explode explode;
    
    int health, total, dying, hurt, charge = 350, retreat = 0, sliding = 400, index = 0, shoot = 15, height;
    float sx, sy;
    Spike[] spikes;
    
    ForkBombManager() {
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
        
        height = 70;
        
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
        if(health <= 0)return;
        int damage = blastManager.hitEnemy(forkBomb.x+9, forkBomb.y+18, 14);
        if(hurt == 0 && damage > 0 ){
            health-=damage;
            if(health <= 0){
                explode.play();
                forkBomb.die();
            }
            hurt = 35;
        }else{
            // We check if it hits the boss, but it won't hurt if it isn't in the face. pew pew
            blastManager.hitEnemy(forkBomb.x, forkBomb.y, 32);
        }
        if(hurt > 0)return;
         // update spikes
        for(Spike s : spikes){
            if(s.active) s.update(bx, by, dash);
        }
        
        sy = 0;
        if(forkBomb.x < 16 || forkBomb.x > 172) sx = -sx;
        // move side side, randomly adjusting the height variable
        if(sliding > 0){
            sliding--;
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
            }else{
                //don't move while shooting
                //sx = 0;
                if(shoot > 0) shoot--;
                else {
                    System.out.println("[I] - Fork Bomb pew pew");
                    fire();
                    shoot = 15;
                }
                if(index >= spikes.length) {
                    index = 0;
                    charge = 250;
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
                sliding = Math.random(300, 600);
                height = Math.random(40, 70);
            }
        }
        
        forkBomb.y += sy;
    }
    
    void render(HiRes16Color screen) {
        // Don't render if dead.
        if(dying == 0)return;
        if(health <= 0 && dying > 0){
            dying--;
            if(dying == 50) explode.play();
        }else if(hurt > 0){
            hurt--;
            forkBomb.hurt();
        }else{
            forkBomb.bite();
        }
        
        for(Spike s : spikes){
            if(s.active)s.render(screen);
        }
        
        forkBomb.draw(screen);
        //shadow
        if(forkBomb.y < height) screen.drawCircle(forkBomb.x+16, height+16, 16, 8, true);
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
        spikes[index].y = forkBomb.y+28;
        int sx = Math.random(-1, 2);
        int sy = Math.random(0, 2);//only move down
        if(sx == 0) sx = 1;
        spikes[index].sx = sx;
        spikes[index].sy = sy;
        spikes[index].active = true;
        spikes[index].move = Math.random(20, 32);
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
            if(x < 16){
                x = 16;
                y+=1;
            }
            if(y > 150)y = 150;
        }
    }
    void render(HiRes16Color screen){
        screen.drawCircle(x, y, 6, 8, true);
    }
}