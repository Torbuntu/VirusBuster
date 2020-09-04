import sprites.Virus;
import sprites.Frag;

import managers.BlastManager;
import Math;

public class VirusManager{
    VirusObject[] viruses;
    int[] waves;
    int currentWave;
    int active;
    int total;
    int max;
    
    int incoming = 150;
    
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
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
            new VirusObject(),
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
        checkAvailable();
        if(incoming > 0){
            incoming--;
            return;
        }
        for(int i = 0; i < waves[currentWave]; i++){
            viruses[i].update(bx, by);
            for(int x = 0; x < waves[currentWave]; x++){
                if(x != i && viruses[x].isAlive()){
                    if(Main.checkCollides(viruses[i].getX()+8, viruses[i].getY()+8, viruses[x].getX()+8, viruses[x].getY()+8, 8, 6)){
                        if(Math.random(0, 2) == 1){
                            viruses[i].setSpeedX(-1.0f);
                            viruses[x].virus.x += 1.0f;
                        }else{
                            viruses[i].setSpeedY(-1.0f);
                            viruses[x].virus.y += 1.0f;
                        }
                    }
                }
            }
            
            viruses[i].updateMovement();
        }
    }
    
    public void render(){
        if(incoming > 0){
            Main.screen.setTextPosition(Main.screen.width()/2-16, Main.screen.height()/2);
            Main.screen.setTextColor(8);
            Main.screen.print("<Incoming>");
            return;
        }
        for(int i = 0; i < waves[currentWave]; i++){
            viruses[i].render();
        }
    }
    
    public void checkBlastHits(BlastManager blastManager){
        if(incoming <= 0){
            for(int i = 0; i < waves[currentWave]; i++){
                if(viruses[i].isAlive() &&  blastManager.hitEnemy(viruses[i].getX()+8, viruses[i].getY()+8, 6.0f)){
                    viruses[i].hit(1);
                    if(!viruses[i].isAlive()){
                        total--;
                        active--;
                        Main.updateKills(viruses[i].frag.x, viruses[i].frag.y);
                    }
                }
            }
        }
    }

    public void checkAvailable(){
        for(int i = 0; i < waves[currentWave]; i++){
            if(viruses[i].isAlive()){
                return;
            }
        }
        if(total > 0){
            currentWave++;
            active = waves[currentWave];
            for(int i = 0; i < waves[currentWave]; i++){
                int r = Math.random(0, 2);
                viruses[i].reset(r);
            }
            incoming = 150;
        }
    }
    
    public int getThreats(){
        return total;
    }
    public int getTotalThreats(){
        return max;
    }
    
    public void resetAll(){
        for(VirusObject v : viruses){
            v.reset();
        }
    }
    
    //TODO: add Sector info
    public void initWave(int sector){
        currentWave = 0;
        switch(sector){
            case 0:
                waves = new int[]{3, 5, 5, 7};
                total = 20;
                break;
            case 1:
                waves = new int[]{3, 5, 5, 7};
                total = 20;
                break;
            case 2:
                waves = new int[]{3, 5, 5, 7};
                total = 20;
                break;
            case 7:
                waves = new int[]{5, 8, 12, 16};
                total = 41;
                break;
            default:
                waves = new int[]{3, 5, 5, 7};
                total = 20;
                break;
        }
        max = total;
        active = waves[currentWave];
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

    void reset(int t){
        type = t;
        reset();
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

