import sprites.Virus;
import sprites.Frag;
import audio.Explode;

import entities.Debris;

import managers.BlastManager;
import managers.DebrisManager;
import Math;

public class VirusManager{
    VirusObject[] viruses;
    int[] waves;
    int currentWave;
    int active;
    int total;
    int max;
    Explode explode;
    
    int spawnX, spawnY;
    
    int incoming = 150;
    
    public VirusManager(int x, int y){
        this.spawnX = x;
        this.spawnY = y;
        explode = new Explode(1);
        viruses = new VirusObject[]{
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY),
            new VirusObject(spawnX, spawnY)
        };
    }
    
    public void update(float bx, float by, DebrisManager debris){
        checkAvailable();
        if(incoming > 0){
            incoming--;
            return;
        }
        for(int i = 0; i < waves[currentWave]; i++){
            viruses[i].update(bx, by);
            if(viruses[i].isAlive()){
                for(int x = 0; x < waves[currentWave]; x++){
                    if(x != i && viruses[x].isAlive()){
                        if(Main.checkCollides(viruses[i].getX()+8, viruses[i].getY()+8, viruses[x].getX()+8, viruses[x].getY()+8, 8, 6)){
                            if(viruses[i].getX() < viruses[x].getX()){
                                viruses[i].setSpeedX(-2.0f);
                            }else{
                                viruses[i].setSpeedX(2.0f);
                            }
                            if(viruses[i].getY() < viruses[x].getY()){
                                viruses[i].setSpeedY(-2.0f);
                            }else{
                                viruses[i].setSpeedY(2.0f);
                            }
                        }
                    }
                }
                
                for(Debris d : debris.getDebris()){
                    if(d.getType() != 1){
                        if(d.collide(viruses[i].getX(), viruses[i].getY(), 16, 16)){
                            if(viruses[i].getX()+6 > d.getX() && viruses[i].getX()+10 < d.getX()+16){
                                viruses[i].setSpeedY(0);
                                if(viruses[i].getX() < bx){
                                    viruses[i].aroundX = 16;
                                }else{
                                    viruses[i].aroundX = -16;
                                }
                            }else{
                                viruses[i].setSpeedX(0);
                                if(viruses[i].getY() < by){
                                    viruses[i].aroundY = 16;
                                }else{
                                    viruses[i].aroundY = -16;
                                }
                            }
                        }
                    }
                }
                viruses[i].updateMovement();
            }
            
        }
    }
    
    public void render(){
        if(incoming > 0){
            Main.screen.fillRect(Main.screen.width()/2-50, 150-incoming-2, 94, 12, 3);
            Main.screen.setTextPosition(Main.screen.width()/2-48, 150-incoming);
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
                        explode.play();
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
                viruses[i].reset(r, spawnX, spawnY);
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
            v.reset(spawnX, spawnY);
        }
    }
    
    //TODO: add Sector info
    public void initWave(int sector, int x, int y){
        currentWave = 0;
        this.spawnX = x;
        this.spawnY = y;
        switch(Main.ZONE){
            default:
                switch(sector){
                    case 0:
                        waves = new int[]{16, 5, 7};
                        total = 15;
                        break;
                    case 1:
                        waves = new int[]{3, 5, 5, 7};
                        total = 20;
                        break;
                    case 2:
                        waves = new int[]{3, 5, 5, 7, 10};
                        total = 30;
                        break;
                    case 3:
                        waves = new int[]{3, 5, 5, 7, 10};
                        total = 30;
                        break;
                    case 5:
                        waves = new int[]{3, 5, 5, 7, 10};
                        total = 30;
                        break;
                    case 6:
                        waves = new int[]{3, 5, 5, 7, 10};
                        total = 30;
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

