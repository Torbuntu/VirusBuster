import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

public class TitleScene extends State {
    int select=0, count=0;
    
    void init(){
        select = 0;
        count = 0;
        Mixer.init(8000);
    }
    
    void update(){
        
        if(Button.B.justPressed()){
            Globals.ZONE = select;
            Globals.SECTOR = 0; // TODO: revert this to start at 0
            Globals.shield = 100; //going in full health
            Game.changeState(new GrabbyMcStage());
            // Game.changeState(new NormalSector());
        }
        
        Globals.screen.clear(3);
        
        Globals.screen.setTextPosition(10, 10);
        Globals.screen.setTextColor(0);
        Globals.screen.print("Press B to begin Demo.");
        
        Globals.screen.setTextPosition(10, 32);
        Globals.screen.print("Select Zone to Begin");
        
        
        if(Button.Right.justPressed() && select < 3){
            select++;
        }
        if(Button.Left.justPressed() && select > 0){
            select--;
        }
        
        count++;
        for(int i = 0; i < 4; i++){
            if(select == i && count > 10){
                Globals.screen.drawRect((11+i*50), 44, 17, 17, 8);
            }else{
                Globals.screen.drawRect((12+i*50), 45, 16, 16, 0);
            }
            switch(i){
                case 0:
                    if(Globals.saveManager.firstZoneClear){
                        Globals.screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 1:
                    if(Globals.saveManager.secondZoneClear){
                        Globals.screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 2:
                    if(Globals.saveManager.thirdZoneClear){
                        Globals.screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 3:
                    if(Globals.saveManager.fourthZoneClear){
                        Globals.screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
            }
        }
        Globals.screen.fillRect(10, 70, 200, 50, 8+select);
        Globals.screen.setTextPosition(15, 75);
        Globals.screen.setTextColor(3);
        Globals.screen.print("About zone " + select);
        
        if(count > 20) count = 0;
        
        Globals.screen.flush();
    }
}