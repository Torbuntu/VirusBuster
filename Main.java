import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;
import femto.mode.HiRes16Color;
import femto.font.FontC64;

import managers.SaveManager;
import stage.TitleStage;
import stage.TutorialStage;
import stage.IntroCutStage;

public class Main extends State {
    HiRes16Color screen;
    boolean start = false, confirm = false;
    public static void main(String[] args){
        if(Globals.saveManager.refresh == 0) Globals.saveManager.refresh = 50;
        if(Globals.saveManager.rate == 0) Globals.saveManager.rate = 1;
        if(Globals.saveManager.charge == 0) Globals.saveManager.charge = 1;
        if(Globals.saveManager.damage == 0) Globals.saveManager.damage = 2;
        Mixer.init(8000);
        EntityManager.initializeGlobalEntities();
        Game.run(FontC64.font(), new Main());
    }
    
    void init(){
        start = Globals.saveManager.started;
        screen = Globals.screen;
        screen.setTextColor(0);
        Globals.initTitle();
    }
    
    void update(){
        screen.clear(3);
        Globals.drawTitle();
        screen.setTextPosition(0,140);
        
        if(confirm){
            screen.println("Initiate Training\nProgram?\n");
            screen.println("[A] Yes - [B] No");
            if(Button.A.justPressed()){
                Game.changeState(new TutorialStage());
            }
            if(Button.B.justPressed()){
                Game.changeState(new IntroCutStage());
            }
        }else{
            screen.println("[A] - New Game");
            if(start){
                if(Button.C.justPressed())Game.changeState(new TitleStage());
                screen.println("[C] - Continue");
            }
            if(Button.A.justPressed()) {
                //TODO: If old save data exists, purge it.
                confirm = true;
                Globals.newGame();
            }
        }
        screen.flush();
    }
    
    void shutdown(){
        Globals.destroyTitle();
        screen = null;
    }
}