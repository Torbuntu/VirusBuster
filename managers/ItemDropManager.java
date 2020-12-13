import audio.Coin;
import entities.Item;
import femto.mode.HiRes16Color;

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
            if(!i.available){
                i.init(0, x, y);
                return;
            }
        }
    }
       
    void updateAndRender(HiRes16Color screen){
        for(Item i : items){
            if(i.available){
               i.update();
               i.render(screen);
            }
        }
    }   
    
    
    public boolean checkCollect(float x, float y, float mag){
        for(Item i : items){
            if(i.available){
                if(Globals.boundingBox(i.loot.x, i.loot.y, 8, x, y, 16)){
                    i.reset();
                    coin.play();
                    return true;
                }else{
                    if(mag > 0.0f){
                        if(x > i.loot.x)i.loot.x += mag;
                        else i.loot.x -= mag;
                        if(y > i.loot.y) i.loot.y += mag;
                        else i.loot.y -= mag;
                    }
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
