import femto.Game;
import femto.State;
import femto.input.Button;


import sprites.Bot;
import sprites.Virus;

public class TitleScene extends State {
    Virus virus, virus2;
    Bot bot;
    
    int sx = 0, sy = 0;
    void init(){
        bot = new Bot();
        
        bot.x = 32;
        bot.y = 32;
        
        virus = new Virus();
        virus2 = new Virus();
        
        virus.x = 100;
        virus.y = 100;
        
        virus2.x = 80;
        virus2.y = 80;
    }
    
    void update(){
        
        if(Button.C.justPressed()){
            Game.changeState(new Main());
        }
        
        Main.screen.clear(4);
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 10; j++){
                if(virus2.x/16 == i && virus2.y/16 == j){
                    Main.screen.fillRect(6+i*16, 6+j*16, 16, 16, 8);
                }else{
                    Main.screen.drawRect(6+i*16, 6+j*16, 16, 16, 8);
                }
            }
        }
        
        if(Button.Right.isPressed()){
            sx = 1;
        }
        if(Button.Left.isPressed()){
            sx = -1;
        }
        if(Button.Down.isPressed()){
            sy = 1;
        }
        if(Button.Up.isPressed()){
            sy = -1;
        }
        
        
        Main.screen.setTextPosition(10, 10);
        Main.screen.print("Press C to begin.");
        
        
        
        bot.draw(Main.screen);
        virus.draw(Main.screen);
        virus2.draw(Main.screen);
        
        
        Main.screen.flush();
    }
}