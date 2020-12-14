import sprites.Virus;
import femto.mode.HiRes16Color;

class VirusObject{
    Virus virus;
    boolean alive = true;
    float sx = 0, sy = 0;
    int animationTime = 50;
    int hitTime = 0, updateTime = 0;
    
    public int aroundX = 0, aroundY = 0;

    int type;
    int baseHealth = 2;
    int health = 2;
    
    VirusObject(int t){
        type = t;
        virus = new Virus();
        virus.walk();
    }

    void update(float bx, float by){
        if(hitTime > 0){
            hitTime--;
            virus.hit();
            return;
        }
        if(!alive){
            return;
        }
        //START Move Virus
        if(updateTime == 0){
            updateTime = 30;
            sx = 0;
            sy = 0;
            
            // Calculate the absolute distances between the x/y coordinates. Virus moves closer by whichever is further.
            if(Math.abs(virus.x - bx) > Math.abs(virus.y - by)){
                if(virus.x < bx){
                    sx = 0.5f;
                }
                if(virus.x > bx){
                    sx = -0.5f;
                }
            }else{
                if(virus.y > by){
                    sy = -0.5f;
                }
                if(virus.y < by){
                    sy = 0.5f;
                }
            }
        }else updateTime--;
        
        
        //check if close
        if(bx >= (virus.x - 32) && bx <= (virus.x + 32) && by >= (virus.y - 32) && by <= (virus.y + 32) ){
            if(bx >= (virus.x - 32) && bx <= virus.x){
                virus.setMirrored(true);
            }
            if(bx <= (virus.x + 32) && bx >= virus.x){
                virus.setMirrored(false);
            }
            if(Globals.checkHitBot(virus.x+2, virus.y+2, 12, bx+2, by+2, 8))Globals.shield-=2;
            
            virus.bite();
        }else{
            virus.walk();
        }
    }
    
    void updateMovement(){
        if(hitTime == 0 && alive){
            if(aroundX > 0){
                aroundX--;
                virus.x += 1;
            }else if(aroundX < 0){
                aroundX++;
                virus.x += -1;
            }else{
                virus.x += sx;
            }
            if(aroundY > 0){
                aroundY--;
                virus.y += 1;
            }else if(aroundY < 0){
                aroundY++;
                virus.y += -1;
            }else{
                virus.y += sy;
            }
        }
    }
    
    void render(HiRes16Color screen){
        if(alive){
            virus.draw(screen);
        }else{
            if(animationTime != 0){
                virus.die();
                virus.draw(screen);
                animationTime--;
            }
            // else we don't render anything
        }
    }
    
    float getX(){
        return virus.x;
    }
    
    float getY(){
        return virus.y;
    }
    
    float getWidth(){
        return virus.width();
    }
    
    float getHeight(){
        return virus.height();
    }
    
    void setSpeedX(float s){
        sx = s;
    }
    void setSpeedY(float s){
        sy = s;
    }
    
    float getSpeedX(){
        return sx;
    }
    
    float getSpeedY(){
        return sy;
    }

    void hit(int damage){
        if(hitTime == 0 && alive){
            hitTime = 10;
        }
        health = health - damage;
        if(health <= 0){
            kill();
        }
    }
    
    void kill(){
        animationTime = 50;
        alive = false;
    }
    
    void reset(int t, int x, int y){
        type = t;
        reset(x, y);
    }
    
    void reset(int x, int y){
        virus.x = x;
        virus.y = y;
        alive = true;
        hitTime = 0;
        health = baseHealth+Globals.ZONE;
    }
}

