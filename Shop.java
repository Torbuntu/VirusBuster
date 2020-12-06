import femto.Game;
import femto.State;
import femto.input.Button;

class Shop extends State {
    void init() {
        
    }
    
    //TODO: Balance the price to benefit ratio for upgrades
    void update() {
        Globals.screen.clear(3);
        if(Button.A.justPressed() && Globals.rate < 10){
            if(Globals.currency >= 2){
                Globals.currency -= 2;
                if(Globals.rate > 10) Globals.rate = 10;
                else Globals.rate++;
            }
        }
        if(Button.B.justPressed() && Globals.refresh > 5){
            if(Globals.currency >= 2){
                Globals.currency -= 2;
                Globals.refresh-=5;
                if(Globals.refresh < 5) Globals.refresh = 5;
            }
        }
        if(Button.Up.justPressed() && Globals.shield < 100){
            if(Globals.currency >= 10){
                Globals.currency -= 10;
                if(Globals.shield + 10 > 100)Globals.shield = 100;
                else{
                    Globals.shield+=10;
                }
            }
        }

        Globals.screen.setTextPosition(0, 16);
        Globals.screen.setTextColor(0);
        Globals.screen.print(
          "$2  - [A]  Rate: " + Globals.rate
        + "\n$2  - [B]  Refresh: " + Globals.refresh
        + "\n$10 - [Up] Shield: " + Globals.shield
        + "\n\n$$" + Globals.currency);

        if(Button.C.justPressed()){
            Game.changeState(SectorZoneManager.getNextState());
        }
        
        Globals.screen.flush();
    }
    
    void shutdown() {
        
    }
}