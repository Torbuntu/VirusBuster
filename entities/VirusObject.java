import sprites.Virus;
import sprites.Frag;

class VirusObject{
    Virus virus;
    Frag frag;//dead virus
    boolean alive = true;
    float sx = 0, sy = 0;
    int animationTime = 50;
    int hitTime = 0;
    
    public int aroundX = 0, aroundY = 0;

    int type;
    int baseHealth = 2;
    int health = 2;
    
    VirusObject(int x, int y, int t){
        type = t;
        virus = new Virus();
        virus.walk();
        
        reset(x, y);
        
        frag = new Frag();
        frag.die();
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
        sx = 0;
        sy = 0;
        
        // Calculate the absolute distances between the x/y coordinates. Virus moves closer by whichever is further.
        float dx = Math.abs(virus.x - bx);
        float dy = Math.abs(virus.y - by);
        
        if(dx > dy){
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
        
        //check if close
        if(bx >= (virus.x - 32) && bx <= (virus.x + 32) && by >= (virus.y - 32) && by <= (virus.y + 32) ){
            if(bx >= (virus.x - 32) && bx <= virus.x){
                virus.setMirrored(true);
            }
            if(bx <= (virus.x + 32) && bx >= virus.x){
                virus.setMirrored(false);
            }
            if(Main.circle(virus.x+8, virus.y+8, bx+8, by+8, 8, 6)){
                Main.shield--;
            }
            
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
    
    void render(){
        if(alive){
            virus.draw(Main.screen);
            //debug circle
            // Main.screen.drawCircle(virus.x+8, virus.y+8, 8, 10, false);
        }else{
            if(animationTime != 0){
                frag.draw(Main.screen);
                animationTime--;
            }
        }
    }
    
    float getX(){
        if(alive){
            return virus.x;
        }else{
            return frag.x;
        }
    }
    
    float getY(){
        if(alive){
            return virus.y;
        }else{
            return frag.y;
        }
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
    
    int getType(){
        return type;
    }
    
    int getHealth(){
        return health;
    }
    
    boolean isAlive(){
        return alive;
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
        frag.x = virus.x;
        frag.y = virus.y;
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
        health = baseHealth;
    }
}

