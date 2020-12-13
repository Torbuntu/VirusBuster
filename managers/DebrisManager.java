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
            new Debris(0, 7)
        };
        System.out.println("[I] - Debris initialized");
    }
    
    public void resetDebris(){
        for(Debris d : debris){
            d.reset();
        }
        //Go through debris and spread if they overlap
        for(int i = 0; i < 8; i++){
            while(checkOverlap(debris[i].x, debris[i].y, debris[i].id)){
                debris[i].reset();
            }
        }
    }
    
    boolean checkOverlap(int x, int y, int id){
        for(Debris d : debris){
            if(d.id == id) return false;
            if(d.collide(x, y))return true;
        }
        return false;
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


