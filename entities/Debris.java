class Debris {
    int x, y, w, h;
    
    /*
    * Type is used to determine if the debris is a corruption tile (spawn point for viruses) 
    * or a circuit chip of some sort (which blocks player and viruses from moving freely)
    */
    int type;
    
    Debris(int t){
        type = t;
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
    
    int getType(){
        return type;
    }
    
    void reset(){
        x = getCoordX();
        y = getCoordY();
    }
    
    private int getCoordX(){
        int id = Math.random(0, 13);
        while(id == 6 || id == 7){
            id = Math.random(0, 13);
        }
        int coord = 6+(id*16);
        return coord;
    }
    
    private int getCoordY(){
        int id = Math.random(0, 9);
        while(id == 4 || id == 5){
            id = Math.random(0, 9);
        }
        int coord = 16+(id*16);
        return coord;
    }
    
    boolean collide(float fx, float fy, float fw, float fh){
        return (x < fx + fw && x + w > fx && y < fy + fh && y + h > fy);
    }
    
    void draw(){
        if(type != 0){
            Main.screen.fillRect(x, y, w, h, 7);
        }else{
            Main.screen.fillRect(x, y, w, h, 11);
        }
    }
}