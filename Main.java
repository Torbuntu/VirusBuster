import femto.Game;
import femto.State;
import femto.font.FontC64;

import managers.SaveManager;
import stage.TitleStage;

public class Main extends State {
    
    public static void main(String[] args){
        if(Globals.saveManager.refresh == 0) Globals.saveManager.refresh = 50;
        if(Globals.saveManager.rate == 0) Globals.saveManager.rate = 1;
        if(Globals.saveManager.charge == 0) Globals.saveManager.charge = 100;
        if(Globals.saveManager.damage == 0) Globals.saveManager.damage = 2;
        Game.run(FontC64.font(), new TitleStage());
    }
    
    void init(){
        
    }
    
    void update(){
    }

}
