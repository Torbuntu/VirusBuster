import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;
import femto.font.FontC64;

import managers.SaveManager;
import stage.TitleStage;
import stage.TutorialStage;

public class Main extends State {
    HiRes16Color screen;
    boolean start = false;
    public static void main(String[] args){
        if(Globals.saveManager.refresh == 0) Globals.saveManager.refresh = 50;
        if(Globals.saveManager.rate == 0) Globals.saveManager.rate = 1;
        if(Globals.saveManager.charge == 0) Globals.saveManager.charge = 100;
        if(Globals.saveManager.damage == 0) Globals.saveManager.damage = 2;
        
        Game.run(FontC64.font(), new Main());
    }
    
    void init(){
        start = Globals.saveManager.started;
        screen = Globals.screen;
        screen.setTextColor(0);
    }
    
    void update(){
        screen.clear(3);
        
        
        screen.setTextPosition(0,0);
        screen.println("Virus Buster");
        screen.println("[A] - New Game");
        if(start){
            if(Button.C.justPressed())Game.changeState(new TitleStage());
            screen.println("[C] - Continue");
        }
        if(Button.A.justPressed()) {
            //TODO: If old save data exists, purge it.
            Game.changeState(new TutorialStage());
        }
        screen.flush();
    }
    
    void shutdown(){
        if(!start) Globals.saveManager.started = true;
        screen = null;
    }

}
