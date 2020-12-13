import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;
import stage.TitleStage;

class GameOverStage extends State {
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
        screen.println("Zone Failed");
        
        screen.println("Accuracy: " + Globals.getAccuracy());
        
        screen.flush();
    }
    void shutdown(){
        screen = null;
    }
}