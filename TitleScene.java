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
            Main.setZone(select);
            Game.changeState(new Main());
        }
        
        Main.screen.clear(3);
        
        Main.screen.setTextPosition(10, 10);
        Main.screen.setTextColor(0);
        Main.screen.print("Press B to begin Demo.");
        
        Main.screen.setTextPosition(10, 32);
        Main.screen.print("Select Zone to Begin");
        
        
        if(Button.Right.justPressed() && select < 3){
            select++;
        }
        if(Button.Left.justPressed() && select > 0){
            select--;
        }
        
        count++;
        for(int i = 0; i < 4; i++){
            if(select == i && count > 10){
                Main.screen.drawRect((11+i*50), 44, 17, 17, 8);
            }else{
                Main.screen.drawRect((12+i*50), 45, 16, 16, 0);
            }
            switch(i){
                case 0:
                    if(Main.saveManager.firstZoneClear){
                        Main.screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 1:
                    if(Main.saveManager.secondZoneClear){
                        Main.screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 2:
                    if(Main.saveManager.thirdZoneClear){
                        Main.screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
                case 3:
                    if(Main.saveManager.fourthZoneClear){
                        Main.screen.fillCircle(20+i*50, 52, 6, 11);
                    }
                    break;
            }
        }
        Main.screen.fillRect(10, 70, 200, 50, 8+select);
        Main.screen.setTextPosition(15, 75);
        Main.screen.setTextColor(3);
        Main.screen.print("About zone " + select);
        
        if(count > 20) count = 0;
        
        Main.screen.flush();
    }
}