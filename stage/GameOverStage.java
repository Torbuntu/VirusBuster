import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;
import stage.MenuStage;

class GameOverStage extends State {
    HiRes16Color screen;
    void init(){
        screen = Globals.screen;
    }
    void update(){
        screen.clear(3);
        if(Button.C.justPressed()){
            Game.changeState(new MenuStage());
        }
        
        screen.setTextPosition(110-40, 0);
        screen.setTextColor(0);
        screen.println("Zone Failed");
        
        screen.fillRect(10, 30, 200, 50, 1);
        screen.setTextPosition(15, 35);
        screen.setTextColor(3);
        screen.print(
            "Virus Buster Program\n  "+
            "Reinitializing...\n  "+
            "Program Recovered\n  "+
            "Power at 50%");
        
        screen.flush();
    }
    void shutdown(){
        Globals.shield = 50; //start at 50 as a penalty for losing a bot
        screen = null;
    }
}