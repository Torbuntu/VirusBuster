import entities.Debris;

// TODO: The debris spawn needs to be more clever in avoiding placing spawns on the same tile
class DebrisManager {
    private Debris[] debris;
    
    public DebrisManager() {
        debris = new Debris[]{
            new Debris(0),
            new Debris(0),
            // new Debris(0),
            new Debris(2),
            new Debris(0),
            new Debris(1)
        };
        System.out.println("[I] - Debris initialized");
    }
    
    public void resetDebris(){
        for(Debris d : debris){
            d.reset();
        }
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
    
    void render(){
        for(Debris d : debris){
            d.draw();
        }
    }
    
    int getSpawnX(int p){
        for(Debris d : debris){
            if(d.type == p){
                return d.getX();
            }
        }
        return 0;
    }
    
    int getSpawnY(int p){
        for(Debris d : debris){
            if(d.type == p){
                return d.getY();
            }
        }
        return 0;
    }
    
    int getSpawnX(){
        for(Debris d : debris){
            if(d.type == 1){
                return d.getX();
            }
        }
        return 0;
    }
    
    int getSpawnY(){
        for(Debris d : debris){
            if(d.type == 1){
                return d.getY();
            }
        }
        return 0;
    }
    
    Debris[] getDebris(){
        return debris;
    }
}


