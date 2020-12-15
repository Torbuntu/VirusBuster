import entities.Debris;
import femto.mode.HiRes16Color;
// TODO: The debris spawn needs to be more clever in avoiding placing spawns on the same tile
// TODO: If spawners are on the same X or Y, and ZONE is 1, 2, or 3, enable blasts or zappers
class DebrisManager {
    private Debris[] debris;
    
    public DebrisManager() {
        debris = new Debris[]{
            new Debris(0, 0),
            new Debris(0, 1),
            new Debris(0, 2),
            new Debris(2, 3),
            new Debris(0, 4),
            new Debris(1, 5),
            new Debris(0, 6),
            new Debris(0, 7),
            new Debris(0, 8)
        };
        System.out.println("[I] - Debris initialized");
    }

    boolean checkCollides(float fx, float fy, float fw, float fh){
        for(Debris d : debris){
            if(d.collide(fx, fy, fw, fh)) return true;
        }
        return false;
    }
    boolean checkVirusCollides(float fx, float fy, float fw, float fh){
        for(Debris d : debris){
            if(d.type != 0)return false;
            if(d.collide(fx, fy, fw, fh)) return true;
        }
        return false;
    }
    
    void render(HiRes16Color screen){
        for(Debris d : debris){
            d.render(screen);
        }
    }
    
    int getSpawnX(int p){
        for(Debris d : debris){
            if(d.type == p){
                return d.x;
            }
        }
        return 0;
    }
    
    int getSpawnY(int p){
        for(Debris d : debris){
            if(d.type == p){
                return d.y;
            }
        }
        return 0;
    }
    
    int getSpawnX(){
        for(Debris d : debris){
            if(d.type == 1){
                return d.x;
            }
        }
        return 0;
    }
    
    int getSpawnY(){
        for(Debris d : debris){
            if(d.type == 1){
                return d.y;
            }
        }
        return 0;
    }
    
    Debris[] getDebris(){
        return debris;
    }
}


