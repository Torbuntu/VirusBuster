import sprites.Virus;
import sprites.Frag;

class VirusObject{
    Virus virus;
    Frag frag;//dead virus
    boolean alive = true;
    float sx = 0, sy = 0;
    int animationTime = 50;
    
    public int aroundX = 0, aroundY = 0;
    
    //0 = normal, 1 = large
    int type = 0;
    int baseHealth = 2;
    int health = 2;
    
    VirusObject(int x, int y){
        virus = new Virus();
        virus.walk();
        
        reset(x, y);
        
        frag = new Frag();
        frag.die();
    }

    void update(float bx, float by){
        if(alive){
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
                if(Main.checkCollides(virus.x+8, virus.y+8, bx+8, by+8, 8, 6)){
                    Main.shield--;
                }
                
                virus.bite();
            }else{
                virus.walk();
            }
        }
    }
    
    void updateMovement(){
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
    
    void hit(int damage){
        health = health - damage;
        if(health <= 0){
            kill();
        }
    }
    
    int getHealth(){
        return health;
    }
    
    void kill(){
        animationTime = 50;
        alive = false;
        frag.x = virus.x;
        frag.y = virus.y;
    }
    
    boolean isAlive(){
        return alive;
    }

    void reset(int t, int x, int y){
        type = t;
        reset(x, y);
    }
    
    void reset(int x, int y){
        virus.x = x;
        virus.y = y;
        alive = true;
        health = baseHealth;
    }
}

