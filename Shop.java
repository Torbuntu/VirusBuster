import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

class Shop extends State {
    HiRes16Color screen;
    int currency = 0, rate = 1, refresh = 50;
    void init() {
        if(Globals.saveManager.currency != currency) currency = Globals.saveManager.currency;
        if(Globals.saveManager.rate != rate) rate = Globals.saveManager.rate;
        if(Globals.saveManager.refresh != refresh) refresh = Globals.saveManager.refresh;
        screen = Globals.screen;
    }
    
    //TODO: Balance the price to benefit ratio for upgrades
    void update() {
        screen.clear(3);
        if(Button.A.justPressed() && rate < 10){
            if(currency >= 2){
                currency -= 2;
                if(rate > 10) rate = 10;
                else rate++;
            }
        }
        if(Button.B.justPressed() && refresh > 5){
            if(currency >= 2){
                currency -= 2;
                refresh-=5;
                if(refresh < 5) refresh = 5;
            }
        }
        if(Button.Up.justPressed() && Globals.shield < 100){
            if(currency >= 10){
                currency -= 10;
                if(Globals.shield + 10 > 100)Globals.shield = 100;
                else{
                    Globals.shield+=10;
                }
            }
        }
        //Debug
        if(Button.Right.justPressed())Globals.SECTOR++;

        screen.setTextPosition(0, 16);
        screen.setTextColor(0);
        screen.print(
          "$2  - [A]  Rate: " + rate
        + "\n$2  - [B]  Refresh: " + refresh
        + "\n$10 - [Up] Shield: " + Globals.shield
        + "\n\n$$" + currency);

        if(Button.C.justPressed()){
            Game.changeState(SectorZoneManager.getNextState());
        }
        
        screen.flush();
    }
    
    void shutdown() {
        Globals.saveManager.currency = currency;
        Globals.saveManager.rate = rate;
        Globals.saveManager.refresh = refresh;
        screen = null;
    }
}