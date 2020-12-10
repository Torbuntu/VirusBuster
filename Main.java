import femto.Game;
import femto.State;
import femto.font.FontC64;

public class Main extends State {
    
    public static void main(String[] args){
        if(Globals.saveManager.refresh == 0) Globals.saveManager.refresh = 50;
        if(Globals.saveManager.rate == 0) Globals.saveManager.rate = 1;
        Game.run(FontC64.font(), new TitleScene());
    }
    
    void init(){
        
    }
    
    void update(){
    }

}
