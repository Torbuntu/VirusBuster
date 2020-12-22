import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;
import femto.mode.HiRes16Color;
import femto.font.FontC64;

import managers.SaveManager;
import stage.MenuStage;

public class Main extends State {
    
    public static void main(String[] args){
        if(Globals.saveManager.refresh == 0) Globals.saveManager.refresh = 50;
        if(Globals.saveManager.rate == 0) Globals.saveManager.rate = 1;
        if(Globals.saveManager.charge == 0) Globals.saveManager.charge = 1;
        if(Globals.saveManager.damage == 0) Globals.saveManager.damage = 2;
        Mixer.init(8000);
        Globals.endlessUnlocked = (Globals.saveManager.firstZoneClear && Globals.saveManager.secondZoneClear && Globals.saveManager.thirdZoneClear && Globals.saveManager.fourthZoneClear);
        EntityManager.initializeGlobalEntities();
        Game.run(FontC64.font(), new TitleScreen());
    }
    
    void init(){
    }
    
    void update(){
    }
    
    void shutdown(){
    }
}