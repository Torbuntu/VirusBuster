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
        y = getCoordY();
        w = 16;
        h = 16;
    }
    
    void reset(){
        x = getCoordX();
        y = getCoordY();
    }
    
    void reset(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    private int getCoordX(){
        int id = Math.random(1, 12);
        while(id == 6 || id == 7){
            id = Math.random(0, 13);
        }
        int coord = 6+(id*16);
        return coord;
    }
    
    private int getCoordY(){
        int id = Math.random(1, 8);
        while(id == 4 || id == 5){
            id = Math.random(0, 9);
        }
        int coord = 17+(id*16);
        return coord;
    }
    
    boolean collide(int x, int y){
        return this.x == x && this.y == y;
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