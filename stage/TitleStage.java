import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import femto.mode.HiRes16Color;

// import stage.GrabbyMcStage;

public class TitleStage extends State {
    
    HiRes16Color screen;
    int select=0, count=0;
    
    void init(){
        screen = Globals.screen;
        select = 0;
        count = 0;
        Mixer.init(8000);
    }
    
    void update(){
        
        if(Button.B.justPressed()){
            Globals.ZONE = select;
            
            // Begin at the first sector (0)
            Globals.SECTOR = 0;
            
            // going in full health, reset hit/shot ratio
            Globals.shield = 100;
            Globals.hit = 0;
            Globals.shots = 0;
            // Game.changeState(new MiniBossStage());
            // Game.changeState(new WormBossStage());
            // Game.changeState(new GrabbyMcStage());
            // Game.changeState(new ForkBombStage());
            Game.changeState(new NormalSector());
        }
        
        screen.clear(3);
        
        screen.setTextPosition(10, 10);
        screen.setTextColor(0);
        screen.print(
            "Press B to begin Demo." +
            "\nSelect Zone to Begin"
            );
        
        
        if(Button.Right.justPressed() && select < 3){
            select++;
        }
        if(Button.Left.justPressed() && select > 0){
            select--;
        }
        
        count++;
        for(int i = 0; i < 4; i++){
            if(select == i && count > 10){
                screen.drawRect((11+i*50), 44, 17, 17, 8);
            }else{
                screen.drawRect((12+i*50), 45, 16, 16, 0);
            }
            switch(i){
                case 0:
                    if(Globals.saveManager.firstZoneClear){
                        screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 1:
                    if(Globals.saveManager.secondZoneClear){
                        screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 2:
                    if(Globals.saveManager.thirdZoneClear){
                        screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 3:
                    if(Globals.saveManager.fourthZoneClear){
                        screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
            }
        }
        screen.fillRect(10, 70, 200, 50, 8+select);
        screen.setTextPosition(15, 75);
        screen.setTextColor(3);
        screen.print("About zone " + select);
        
        if(count > 20) count = 0;
        
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
    }
}