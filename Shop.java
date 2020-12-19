import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

import managers.SectorZoneManager;

import sprites.Loot;

class Shop extends State {
    HiRes16Color screen;
    int currency = 0, rate = 1, refresh = 50, shield = 100, charge = 1, damage = 2, select = 0;
    float magnet = 0.0f;
    Loot loot;
    void init() {
        if(Globals.shield != shield) shield = Globals.shield;
        if(Globals.saveManager.currency != currency) currency = Globals.saveManager.currency;
        if(Globals.saveManager.rate != rate) rate = Globals.saveManager.rate;
        if(Globals.saveManager.refresh != refresh) refresh = Globals.saveManager.refresh;
        if(Globals.saveManager.magnet != magnet) magnet = Globals.saveManager.magnet;
        if(Globals.saveManager.charge != charge) charge = Globals.saveManager.charge;
        if(Globals.saveManager.damage != damage) damage = Globals.saveManager.damage;
        screen = Globals.screen;
        loot = new Loot();
        loot.play();
    }
    
    //TODO: Balance the price to benefit ratio for upgrades
    void update() {
        screen.clear(3);
        
        if(Button.A.justPressed()){
            switch(select){
                case 0:
                    if(rate < 10 && currency >= 5){
                        currency -= 5;
                        if(rate > 10) rate = 10;
                        else rate++;
                    }
                break;    
                case 1:
                    if(refresh > 5 && currency >= 10){
                        currency -= 10;
                        refresh-=5;
                        if(refresh < 5) refresh = 5;
                    }
                break;
                case 2:
                    if(shield < 100 && currency >= 15){
                        currency -= 15;
                        if(shield + 10 > 100) shield = 100;
                        else{
                            shield+=10;
                        }
                    }
                break;
                case 3:
                    if(charge < 100 && currency >= 20){
                        currency -= 20;
                        if(charge + 5 <= 100) charge+=5;
                    }
                break;
                case 4:
                    if(magnet < 1.2f && currency >= 25){
                        currency -= 25;
                        if((magnet + 0.1f) > 1.0f) magnet = 1.02f;
                        else{
                            magnet+=0.1f;
                        }
                    }
                break;
                case 5:
                    if(damage < 20 && currency >= 30){
                        currency -= 30;
                        damage++;
                    }
                break;
                case 6:
                    Game.changeState(SectorZoneManager.getNextState());
                break;
            }
        }
        
        if(Button.Up.justPressed() && select > 0) select--;
        if(Button.Down.justPressed() && select < 6) select++;

        loot.draw(screen, 0, 0);
        screen.setTextPosition(8, 0);
        screen.print("x"+currency);
        screen.setTextPosition(0, 16);
        screen.setTextColor(0);
        screen.print(
            "  5 - Rate:    " + rate
        + "\n 10 - Refresh: " + refresh
        + "\n 15 - Shield:  " + shield
        + "\n 20 - Charge:  " + charge 
        + "\n 25 - Magnet:  " + (int)(magnet*100)
       + "%\n 30 - Damage:  " + damage
        + "\n Continue");
        
        screen.setTextPosition(0, 16+select*9);
        screen.print(">");
        
        if(Button.C.justPressed()){
            Game.changeState(SectorZoneManager.getNextState());
        }
        
        screen.flush();
    }
    
    /// We purposely do *not* save the cookie until after the summary stage. 
    void shutdown() {
        Globals.shield = shield;
        Globals.saveManager.currency = currency;
        Globals.saveManager.rate = rate;
        Globals.saveManager.refresh = refresh;
        Globals.saveManager.magnet = magnet;
        Globals.saveManager.charge = charge;
        Globals.saveManager.damage = damage;
        screen = null;
    }
}