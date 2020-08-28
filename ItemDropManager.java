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
            }
        }
    }
       
    void updateAndRender(float bx, float by){
        for(Item i : items){
            if(i.getAvailable()){
               i.update(bx, by);
               i.render();
            }
        }
    }   
       
}

class Item {
    int type;
    boolean available = false;
    int life = 100;
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
    
    void update(float bx, float by){
        life--;
        if(life <= 0){
            available = false;
            life = 100;
        }
    }
    
    boolean getAvailable(){
        return available; 
    }
}