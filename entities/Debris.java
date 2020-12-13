import sprites.Chip;
import sprites.Spawn;
import femto.mode.HiRes16Color;
class Debris {
    
    int x, y, w, h;
    
    /*
    * Type is used to determine if the debris is a corruption tile (spawn point for viruses) 
    * or a circuit chip of some sort (which blocks player and viruses from moving freely)
    */
    int type;
    
    Chip chip;
    Spawn spawn;
    
    Debris(int t){
        type = t;
        if(type != 0){
            spawn = new Spawn();
            spawn.idle();
        }else{
            chip = new Chip();
        }
        x = getCoordX();
        y = getCoordY();
        w = 16;
        h = 16;
    }
    
    int getX(){
        return x;
    }
    
    int getY(){
        return y;
    }
    
    void reset(){
        x = getCoordX();
        y = getCoordY();
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
        int coord = 16+(id*16);
        return coord;
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