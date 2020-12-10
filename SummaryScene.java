import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;

/**
 * Summary scene displays the final after stage summary. 
 * This class also saves the player variables between Zones.
 */ 
class SummaryScene extends State {
    HiRes16Color screen;
    void init(){
        screen = Globals.screen;
    }
    void update(){
        screen.clear(3);
        if(Button.C.justPressed()){
            Game.changeState(new TitleScene());
        }
        
        screen.setTextPosition(52, 88);
        screen.setTextColor(0);
        screen.println("Summary");
        
        screen.println("Accuracy: " + Globals.getAccuracy());
        
        screen.flush();
    }
    void shutdown(){
        //TODO: SaveManager
        Globals.saveManager.saveCookie();
        screen = null;
    }
}