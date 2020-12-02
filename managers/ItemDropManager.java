import audio.Coin;
import entities.Item;

public class ItemDropManager {
    
    Coin coin = new Coin(0);
    
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
                if(Globals.circle(i.loot.x+4, i.loot.y+4, x, y, 6, 8)){
                    i.reset();
                    coin.play();
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
