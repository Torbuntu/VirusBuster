import sprites.Loot;

public class ItemDropManager {
    Item[] items = new Item[]{
        new Item(),
        new Item(),
        new Item(),
        new Item(),
        new Item(),
        new Item()
    };
    
    void newDrop(float x, float y){
        for(Item i : items){
            if(!i.getAvailable()){
                i.init(0, x, y);
                return;
            }
        }
    }
       
    void updateAndRender(){
        for(Item i : items){
            if(i.getAvailable()){
               i.update();
               i.render();
            }
        }
    }   
    
    
    public boolean checkCollect(float x, float y){
        for(Item i : items){
            if(i.getAvailable()){
                if(Main.checkCollides(i.loot.x+4, i.loot.y+4, x, y, 6, 8)){
                    i.reset();
                    return true;
                }
            }
        }
        return false;
    }
    
    public void clear(){
        for(Item i : items){
            i.available = false;
        }
    }
       
}

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
                loot.draw(Main.screen);
            }
        }else{
            loot.draw(Main.screen);
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