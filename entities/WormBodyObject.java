import femto.mode.HiRes16Color;
import sprites.WormBody;

class WormBodyObject {
    WormBody body;
    int sx = 1, sy = 0, id, health = 15, hurt = 0, dying = 0, turnX, turnY;
    boolean last, alive = true;
    
    WormBodyObject(int id, boolean last){
        this.id = id;
        this.last = last;
        body = new WormBody();
        body.run();
        body.y = 122;
        body.x = id * -18;
        body.x-=14;
    }
    
    void initDirection(int dir, int sx, int sy, float x, float y, int turnX, int turnY){
        if(health <= 5)return;
        this.sx = sx;
        this.sy = sy;
        switch(dir){
            case 0: // left
                body.x = x+14+(id*18);
                body.y = y+7;
                this.turnX = turnX+7;
                this.turnY = turnY+7;
                break;
            case 1: // right
                body.x = x+(id*-18);
                body.y = y+7;
                this.turnX = turnX+7;
                this.turnY = turnY+7;
                break;
            case 2://up
                body.x = x+7;
                body.y = y+14+(id*18);
                this.turnX = turnX+7;
                this.turnY = turnY+7;
                break;
            case 3://down
                body.x = x+7;
                body.y = y+(id*-18);
                this.turnX = turnX+7;
                this.turnY = turnY+7;
                break;
        }
    }
    
    void update(int dir){
        if(!alive) return;
        if(dying > 0){
            dying--;
            body.die();
            if(dying == 0){
                alive = false;
                // remove the body from the field.
                body.x = -16;
                body.y = -16;
            }
            return;
        }
        if(hurt > 0){
            hurt--;
            body.hurt();
        }else if(last){
            body.last();
        }else{
            body.run();
        }
        if(health > 5){
            // met the turn variables, redirecting
            if((int)body.x == turnX && (int)body.y == turnY){
                sx = 0;
                sy = 0;
                switch(dir){
                    case 0: sx = -1; break;
                    case 1: sx = 1; break;
                    case 2: sy = -1; break;
                    case 3: sy = 1; break;
                }
            }
            
            body.x += sx;
            body.y += sy;
        }else{
            if(body.x < 0) {
                sx = Math.random(0, 2);
                sy = Math.random(-1, 2);
            }
            if(body.x > 192){
                sx = Math.random(-1, 1);
                sy = Math.random(-1, 2);
            } 
            if(body.y < 17) {
                sy = Math.random(0, 2);
                sx = Math.random(-1, 2);
            }
            if(body.y > 148) {
                sy = Math.random(-1, 1);
                sx = Math.random(-1, 2);
            }
            body.x += sx;
            body.y += sy;
        }
    }
    
    void render(HiRes16Color screen){
        if(alive || dying > 0)body.draw(screen);
    }
    
    void hit(int damage){
        hurt = 10;
        if(health == 6){
            sx = -2;
            sy = 2;
        }
        health-=damage;
        if(health <= 0){
            dying = 20;
        }
        if(health < 0)health = 0;
    }
    
    int getHealth(){
        return health;
    }
 
}