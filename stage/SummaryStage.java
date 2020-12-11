import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;
import stage.TitleStage;

/**
 * Summary scene displays the final after stage summary. 
 * This class also saves the player variables between Zones.
 */ 
class SummaryStage extends State {
    HiRes16Color screen;
    void init(){
        screen = Globals.screen;
    }
    void update(){
        screen.clear(3);
        if(Button.C.justPressed()){
            Game.changeState(new TitleStage());
        }
        
        screen.setTextPosition(0, 88);
        screen.setTextColor(0);
        screen.println("Summary:");
        
        screen.println("Accuracy: " + Globals.getAccuracy());
        
        screen.flush();
    }
    void shutdown(){
        switch(Globals.ZONE){
            case 0: Globals.saveManager.firstZoneClear = true; break;
            case 1: Globals.saveManager.secondZoneClear = true; break;
            case 2: Globals.saveManager.thirdZoneClear = true; break;
            case 3: Globals.saveManager.fourthZoneClear = true; break;
        }
        Globals.saveManager.saveCookie();
        screen = null;
    }
}