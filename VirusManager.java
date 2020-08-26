import sprites.Virus;
import sprites.Frag;

import BlastManager;
import Math;

public class VirusManager{
    VirusObject[] viruses;
    
    int active = 3;
    boolean canMove = false;
    
    public VirusManager(){
        viruses = new VirusObject[]{
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject()
        };
    }
    
    public void update(float bx, float by){
        for(int i = 0; i < active; i++){
            viruses[i].update(bx, by);
            
            for(int x = 0; x < active; x++){
                if(x != i && viruses[x].isAlive()){
                    if(viruses[i].checkCircleCollide(viruses[x].getX(), viruses[x].getY(), 8)){
                        if(Math.random(0, 2) == 1){
                            viruses[i].setSpeedX(-1.0f);
                            viruses[x].virus.x += 1.0f;
                        }else{
                            viruses[i].setSpeedY(-1.0f);
                            viruses[x].virus.y -= 1.0f;
                        }
                    }
                }
            }
            
            viruses[i].updateMovement();
            checkAvailable();
        }
    }
    
    public void render(){
        for(int i = 0; i < active; i++){
            viruses[i].render();
        }
    }
    
    public void checkBlastHits(BlastManager blastManager){
        for(int i = 0; i < active; i++){
            if(viruses[i].isAlive() &&  blastManager.hitEnemy(viruses[i].getX()+8, viruses[i].getY()+8, 6.0f)){
                viruses[i].hit(1);
                if(!viruses[i].isAlive()){
                    Main.updateKills();
                }
            }
        }
    }
    
    public void setActive(int inc){
        active = inc;
    }
    
    public void checkAvailable(){
        boolean cleared = true;
        for(int i = 0; i < active; i++){
            if(viruses[i].isAlive()){
                cleared = false;
                return;
            }
        }
        if(cleared && Main.roomThreats > 0){
            for(int i = 0; i < 10; i++){
                if(i < Main.roomThreats){
                    viruses[i].reset();
                }
            }
        }
    }
    
    public void resetAll(){
        for(VirusObject v : viruses){
            v.reset();
        }
    }
}

class VirusObject{
    Virus virus;
    Frag frag;//dead virus
    
    boolean alive = true;
    
    float sx = 0, sy = 0;
    
    int animationTime = 50;
    
    //0 = normal, 1 = large
    int type = 0;
    int baseHealth = 2;
    int health = 2;
    
    VirusObject(){
        virus = new Virus();
        virus.walk();
        
        reset();
        
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
                
                virus.bite();
            }else{
                virus.walk();
            }
        }
    }
    
    void updateMovement(){
        virus.x += sx;
        virus.y += sy;
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
            }else{
             //   reset();
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
    
    
    void setSpeedX(float s){
        sx = s;
    }
    void setSpeedY(float s){
        sy = s;
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
    
    boolean checkCircleCollide(float x2, float y2, float r2){ 
        float vx = virus.x+8 - x2;
        float vy = virus.y+8 - y2;
        float vr = 8 + r2;
        return Math.abs((vx) * (vx) + (vy) * (vy)) < (vr) * (vr);
    }
    
    void reset(){
        int dir = Math.random(0, 4);
        if(dir == 0){
            virus.x = 0;
            virus.y = 80;
        }
        if(dir == 1){
            virus.x = 100;
            virus.y = 0;
        }
        if(dir == 2){
            virus.x = 220;
            virus.y = 80;
        }
        if(dir == 3){
            virus.x = 100;
            virus.y = 160;
        }
        alive = true;
        health = baseHealth;
    }
}