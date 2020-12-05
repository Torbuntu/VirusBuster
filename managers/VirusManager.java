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
    int total;
    int max;
    Explode explode;
    
    int spawnX, spawnY, spawnClear = 60;
    
    int incoming = 150;
    boolean toggle = false;
    
    public VirusManager(int x, int y){
        this.spawnX = x;
        this.spawnY = y;
        explode = new Explode(1);
        viruses = new VirusObject[]{
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0),
            new VirusObject(spawnX, spawnY, 1),
            new VirusObject(spawnX, spawnY, 0)
        };
        System.out.println("[I] - Viruses initialized");
    }
    
    public void update(float bx, float by, DebrisManager debris, BlastManager blastManager){
        checkAvailable();
        if(incoming > 0){
            incoming--;
            return;
        }
        spawnClear--;
        toggle = !toggle;
        for(int i = 0; i < spawned; i++){
            if(toggle && viruses[i].type == 1){
                continue;
            }
            if(viruses[i].alive){
                if(blastManager.hitEnemy(viruses[i].getX(), viruses[i].getY(), 16.0f)){
                    viruses[i].hit(1);
                    if(!viruses[i].alive){
                        explode.play();
                        total--;
                        Globals.updateKills(viruses[i].virus.x, viruses[i].virus.y);
                    }
                }
            
                viruses[i].update(bx, by);
                checkVirusesCollide(i);
                
                for(Debris d : debris.getDebris()){
                    if(d.getType() == 0){
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
        if(spawnClear == 0){
            spawnClear = 45;
            if(spawned < waves[currentWave]){
                spawn(debris);
            } 
        }
    }
    
    void spawn(DebrisManager debrisManager){
        spawned++;
        int p = Math.random(1,3);
        viruses[spawned].reset(debrisManager.getSpawnX(p), debrisManager.getSpawnY(p));
    }
    
    void checkVirusesCollide(int i){
        for(int x = 0; x < spawned; x++){
            if(x != i && viruses[x].alive){
                if(Globals.boundingBox(viruses[i].getX(), viruses[i].getY(), 8, viruses[x].getX(), viruses[x].getY(), 8)){
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
            Globals.screen.fillRect(Globals.screen.width()/2-50, 150-incoming-2, 94, 12, 3);
            Globals.screen.setTextPosition(Globals.screen.width()/2-48, 150-incoming);
            Globals.screen.setTextColor(8);
            Globals.screen.print("<Incoming>");
            return;
        }
        for(int i = 0; i < spawned; i++){
            viruses[i].render();
        }
    }
    
    public void checkAvailable(){
        for(int i = 0; i < spawned; i++){
            if(viruses[i].alive){
                return;
            }
        }
        if(spawned < waves[currentWave]){
            return;
        }
        if(total > 0){
            currentWave++;
            for(int i = 0; i < waves[currentWave]; i++){
                int r = Math.random(0, 2);
                viruses[i].reset(r, spawnX, spawnY);
            }
            spawned = 1;
            spawnClear = 45;
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
        switch(Globals.ZONE){
            case 0:
                zoneZero(sector);
                break;
            default:
                switch(sector){
                    case 0:
                        waves = new int[]{2, 3, 5};
                        total = 10;
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
    }
    
    void zoneZero(int sector){
        switch(sector){
            case 0:
                waves = new int[]{22, 3, 5};
                total = 30;
                break;
            case 1:
                waves = new int[]{3, 5, 5, 7};
                total = 20;
                break;
            case 2:
                waves = new int[]{5, 5, 8, 10};
                total = 28;
                break;
            case 3:
                waves = new int[]{5, 5, 10, 15};
                total = 35;
                break;
        }
    }
}
