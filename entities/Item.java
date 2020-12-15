import sprites.Loot;
import femto.mode.HiRes16Color;
class Item {
    int type;
    boolean available = false;
    int life = 350;
    Loot loot = new Loot();
    
    void init(int type, float x, float y){
        this.type = type;
        available = true;
        loot.play();
        loot.x = x;
        loot.y = y;
    }
    
    void render(HiRes16Color screen){
        if(life < 75){
            if(life%2==0){
                loot.draw(screen);
            }
        }else{
            loot.draw(screen);
        }
    }
    
    void reset(){
        loot.x = 0;
        loot.y = 0;
        available = false;
        life = 350;
    }
    
    void update(){
        life--;
        if(life <= 0){
            available = false;
            life = 350;
        }
    }
    
}