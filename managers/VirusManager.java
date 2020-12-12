import femto.mode.HiRes16Color;

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
    
    int spawnClear = 60;
    
    int incoming = 150;
    boolean toggle = false;
    
    public VirusManager(){
        explode = new Explode(1);
        viruses = new VirusObject[]{
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0),
            new VirusObject(1),
            new VirusObject(0)
        };
        System.out.println("[I] - Viruses initialized");
    }
    
    public void update(float bx, float by, DebrisManager debris, BlastManager blastManager){
        checkAvailable(debris);
        if(incoming > 0){
            incoming--;
            return;
        }
        spawnClear--;
        toggle = !toggle;
        for(int i = 0; i < spawned; i++){
            // Skip every other update for perfs
            if(toggle && viruses[i].type == 1) continue;
            
            if(viruses[i].alive){
                int damage = blastManager.hitEnemy(viruses[i].virus.x, viruses[i].virus.y, 16.0f);
                if(damage > 0){
                    viruses[i].hit(damage);
                    if(!viruses[i].alive){
                        explode.play();
                        total--;
                        Globals.updateKills(viruses[i].virus.x, viruses[i].virus.y);
                        continue;
                    }
                }
            
                viruses[i].update(bx, by);
                checkVirusesCollide(i);
                
                for(Debris d : debris.getDebris()){
                    if(d.type == 0){
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
    
    public void render(HiRes16Color screen){
        if(incoming > 0){
            screen.fillRect(60, 150-incoming-2, 94, 12, 3);
            screen.setTextPosition(62, 150-incoming);
            screen.setTextColor(8);
            screen.print("<Incoming>");
            return;
        }
        for(int i = 0; i < spawned; i++){
            viruses[i].render();
        }
    }
    
    public void checkAvailable(DebrisManager debrisManager){
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
                int p = Math.random(1,3);
                viruses[i].reset(r, debrisManager.getSpawnX(p), debrisManager.getSpawnY(p));
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

    //TODO: add Sector info
    public void initWave(int sector, DebrisManager debrisManager){
        currentWave = 0;
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
        for(int i = 0; i < waves[currentWave]; i++){
            int r = Math.random(0, 2);
            int p = Math.random(1,3);
            viruses[i].reset(r, debrisManager.getSpawnX(p), debrisManager.getSpawnY(p));
        }
    }
    
    void zoneZero(int sector){
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
