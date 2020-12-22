import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;
import stage.MenuStage;

class GameOverStage extends State {
    HiRes16Color screen;
    
    int section = 0, t = 200, c = 100;
    
    void init(){
        screen = Globals.screen;
        screen.setTextColor(12);//We only use green here.
    }
    void update(){
        screen.clear(3);
        if(Button.C.justPressed()){
            section++;
            t = 200;
        }
        screen.setTextPosition(0,0);
        
        switch(section){
            case 0:
                screen.println("-- !ERROR! --");
                if(t < 125){
                    screen.println("Virus Buster Force Quit\n(signal 0, exit 42)");
                }
                if(t < 50){
                     screen.println("panic:\n Program corrupted!");
                     screen.println("Dump complete");
                }
                if( t < 20){
                    screen.println("-- !ZONE FAILED! --");
                }
                if(t == 0){
                    screen.setTextPosition(0, 167);
                    screen.println("P:// [C] - CONTINUE");
                    if(c < 50){
                        int x = screen.textWidth("P:// [C] - CONTINUE");
                        if(c < 4) screen.drawRect(x, 166, 5, 9, 12);
                        else screen.fillRect(x, 166, 5, 9, 12);
                    }
                    c--;
                    if(c == 0){
                        c = 100;
                    }
                }
                if(t > 0)t--;
                break;
            case 1:
                screen.println("P:// VBUST_RECOV.EXE");
                if(t < 125 && t > 50){
                    screen.println("Attempting to recover\n Virus Buster Program");
                }
                if(t <= 50){
                    screen.println("Reinitializing...");
                }
                if(t == 0){
                    screen.println("Program recovered\nPower restored: 50%");
                    
                    screen.setTextPosition(0, 167);
                    screen.println("P:// [C] - CONTINUE");
                    if(c < 50){
                        int x = screen.textWidth("P:// [C] - CONTINUE");
                        if(c < 4) screen.drawRect(x, 166, 5, 9, 12);
                        else screen.fillRect(x, 166, 5, 9, 12);
                    }
                    c--;
                    if(c == 0){
                        c = 100;
                    }
                }
                if(t>0)t--;
                break;
            default:
                Game.changeState(new MenuStage());
            break;
        }
        
        screen.flush();
    }
    void shutdown(){
        Globals.shield = 50; //start at 50 as a penalty for losing a bot
        screen = null;
    }
}