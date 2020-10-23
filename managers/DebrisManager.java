import BotManager;
import VirusManager;

class DebrisManager {
    private Debris[] debris;
    
    public DebrisManager() {
        debris = new Debris[]{
            new Debris(),
            new Debris(),
//            new Debris(),
//            new Debris(),
//            new Debris(),
            new Debris()
        };
    }
    
    boolean checkCollides(float fx, float fy, float fw, float fh){
        for(Debris d : debris){
            if(d.collide(fx, fy, fw, fh)) return true;
        }
        return false;
    }
    
    void render(){
        for(Debris d : debris){
            d.draw();
        }
    }
}

class Debris {
    int x, y, w, h;
    Debris(){
        x = getCoord();
        y = getCoord();
        w = 16;
        h = 16;
    }
    
    private int getCoord(){
        int coord = Math.random(30, 200);
        while(coord % 8 != 0){
            coord = Math.random(30, 200);
        }
        return coord;
    }
    
    boolean collide(float fx, float fy, float fw, float fh){
        return (x < fx + fw && x + w > fx && y < fy + fh && y + h > fy);
    }
    
    void draw(){
        Main.screen.fillRect(x, y, w, h, 11);
    }
}
