import femto.Game;
import femto.State;
import femto.font.FontC64;

public class Main extends State {
    
    
    public static void main(String[] args){
        Game.run(FontC64.font(), new TitleScene());
    }
    
    void init(){
    }
    
    // TODO: Refactor into more manageable methods.
    void update(){
    }

}
