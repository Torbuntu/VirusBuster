
import audio.Explode;

import entities.Debris;
import entities.VirusObject;

import managers.BlastManager;
import managers.DebrisManager;
import Math;

public class VirusManager{
    VirusObject[] viruses;
    int[] waves;
    int currentWave;
    int spawned;
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
        int spawnClear = 0;
        for(int i = 0; i < spawned; i++){
        // for(int i = 0; i < waves[currentWave]; i++){
            viruses[i].update(bx, by);
            if(viruses[i].isAlive()){
                //
                checkVirusesCollide(i);
                
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
                    }else{
                        if(d.collide(viruses[i].getX(), viruses[i].getY(), 20, 20)){
                            spawnClear++;
                        }
                    }
                }
                viruses[i].updateMovement();
            }
        }
        if(spawnClear == 0){
            if(spawned < waves[currentWave]) spawned++;
        }
    }
    
    void checkVirusesCollide(int i){
        // for(int x = 0; x < waves[currentWave]; x++){
        for(int x = 0; x < spawned; x++){
            if(x != i && viruses[x].isAlive()){
                if(Main.boundingBox(viruses[i].getX(), viruses[i].getY(), 8, viruses[x].getX(), viruses[x].getY(), 8)){
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
    }
    
    public void render(){
        if(incoming > 0){
            Main.screen.fillRect(Main.screen.width()/2-50, 150-incoming-2, 94, 12, 3);
            Main.screen.setTextPosition(Main.screen.width()/2-48, 150-incoming);
            Main.screen.setTextColor(8);
            Main.screen.print("<Incoming>");
            return;
        }
        // for(int i = 0; i < waves[currentWave]; i++){
        for(int i = 0; i < spawned; i++){
            viruses[i].render();
        }
    }
    
    public void checkBlastHits(BlastManager blastManager){
        if(incoming <= 0){
            // for(int i = 0; i < waves[currentWave]; i++){
            for(int i = 0; i < spawned; i++){
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
        // for(int i = 0; i < waves[currentWave]; i++){
        for(int i = 0; i < spawned; i++){
            if(viruses[i].isAlive()){
                return;
            }
        }
        if(total > 0){
            currentWave++;
            active = waves[currentWave];
            // for(int i = 0; i < waves[currentWave]; i++){
            for(int i = 0; i < spawned; i++){
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
        spawned = 1;
        max = total;
        active = waves[currentWave];
    }
}
