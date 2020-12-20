import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;
import stage.TitleStage;

import sprites.Loot;

/**
 * Summary scene displays the final after stage summary. 
 * This class also saves the player variables between Zones.
 */ 
class SummaryStage extends State {
    HiRes16Color screen;
    Loot loot;
    int accuracy, c, highScore;
    void init(){
        screen = Globals.screen;
        screen.setTextColor(0);
        accuracy = Globals.getAccuracy();
        
        if(Globals.endless) c = Globals.saveManager.currency;
        else c = Globals.endlessSaveManager.currency;
        
        if(accuracy >= 50 && accuracy < 75){
            c += (int)(c*1.5);
        }else if(accuracy >= 75 && accuracy < 90){
            c += c*2;
        }else if(accuracy >= 90){
            c += c*3;
        }
        loot = new Loot();
        loot.play();
    }
    void update(){
        screen.clear(3);
        if(Button.C.justPressed()){
            Game.changeState(new TitleStage());
        }
        
        screen.setTextPosition(0, 0);
        screen.println("Summary:");
        screen.println("Accuracy: " + accuracy);
        if(accuracy >= 50 && accuracy < 75){
            screen.println("Bonus x1.5");
        }else if(accuracy >= 75 && accuracy < 90){
            screen.println("Bonus x2.0");
        }else if(accuracy >= 90){
            screen.println("Bonus x3.0");
        }
        screen.println(" x"+c);
        loot.draw(screen, 0, 26);
        screen.flush();
    }
    void shutdown(){
        if(!Globals.endless){
            switch(Globals.ZONE){
                case 0: Globals.saveManager.firstZoneClear = true; break;
                case 1: Globals.saveManager.secondZoneClear = true; break;
                case 2: Globals.saveManager.thirdZoneClear = true; break;
                case 3: Globals.saveManager.fourthZoneClear = true; break;
            }
            
            Globals.saveManager.currency = c;
            Globals.saveManager.saveCookie();
        }else{
            //TODO: Save high score and display score
            Globals.endlessSaveManager.saveCookie();
        }
        
        screen = null;
    }
}