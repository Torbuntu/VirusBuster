import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

import managers.SectorZoneManager;

class Shop extends State {
    HiRes16Color screen;
    int currency = 0, rate = 1, refresh = 50, shield = 100;
    float magnet = 0.0f;
    void init() {
        if(Globals.shield != shield) shield = Globals.shield;
        if(Globals.saveManager.currency != currency) currency = Globals.saveManager.currency;
        if(Globals.saveManager.rate != rate) rate = Globals.saveManager.rate;
        if(Globals.saveManager.refresh != refresh) refresh = Globals.saveManager.refresh;
        if(Globals.saveManager.magnet != magnet) magnet = Globals.saveManager.magnet;
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
        if(Button.Up.justPressed() && shield < 100){
            if(currency >= 10){
                currency -= 10;
                if(shield + 10 > 100) shield = 100;
                else{
                    shield+=10;
                }
            }
        }
        if(Button.Down.justPressed() && magnet < 2.0f){
            if(currency >= 25){
                currency -= 25;
                if((magnet + 0.02f) > 2.0f) magnet += 0.02f;
                else{
                    magnet+=0.02f;
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
        + "\n$10 - [Up] Shield: " + shield
        + "\n$25 - [Down] Magnet: " + magnet
        + "\n\n$$" + currency);

        if(Button.C.justPressed()){
            Game.changeState(SectorZoneManager.getNextState());
        }
        
        screen.flush();
    }
    
    void shutdown() {
        Globals.shield = shield;
        Globals.saveManager.currency = currency;
        Globals.saveManager.rate = rate;
        Globals.saveManager.refresh = refresh;
        Globals.saveManager.magnet = magnet;
        screen = null;
    }
}