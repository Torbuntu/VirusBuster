import sprites.Loot;
class Item {
    int type;
    boolean available = false;
    int life = 200;
    Loot loot = new Loot();
    
    void init(int type, float x, float y){
        this.type = type;
        available = true;
        loot.play();
        loot.x = x;
        loot.y = y;
    }
    
    void render(){
        if(life < 25){
            if(life%2==0){
                loot.draw(Globals.screen);
            }
        }else{
            loot.draw(Globals.screen);
        }
    }
    
    void reset(){
        loot.x = 0;
        loot.y = 0;
        available = false;
        life = 200;
    }
    
    void update(){
        life--;
        if(life <= 0){
            available = false;
            life = 200;
        }
    }
    
    boolean getAvailable(){
        return available; 
    }
}