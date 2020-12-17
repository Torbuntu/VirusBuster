import sprites.Chip;
import sprites.Spawn;
import femto.mode.HiRes16Color;

class Debris {
    
    Chip chip;
    Spawn spawn;
    
    int x, y, w, h, id;
    
    /*
    * Type is used to determine if the debris is a corruption tile (spawn point for viruses) 
    * or a circuit chip of some sort (which blocks player and viruses from moving freely)
    */
    int type;
    
    Debris(int t, int id){
        this.id = id;
        type = t;
        if(type != 0){
            spawn = new Spawn();
            spawn.idle();
        }else{
            chip = new Chip();
            chip.idle();
        }
        x = getCoordX();
        y = 17+(id*16);
        w = 16;
        h = 16;
    }
    
    void reset(int x){
        this.x = x;
    }
    
    private int getCoordX(){
        int index = Math.random(0, 13);
        if(id == 4 || id == 5){
            if(index == 6 || index == 7)index = 5;
        }
        return 6+(index*16);;
    }
    
    boolean collide(float fx, float fy, float fw, float fh){
        //Main.screen.drawRect(fx, fy, fw, fh, 9, true);
        return (x < fx + fw && x + w > fx && y < fy + fh && y + h > fy);
    }
    
    void render(HiRes16Color screen){
        if(type != 0){
            spawn.draw(screen, x, y);
        }else{
            chip.draw(screen, x, y);
        }
    }
}