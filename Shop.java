import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

import managers.SectorZoneManager;

class Shop extends State {
    HiRes16Color screen;
    int currency = 0, rate = 1, refresh = 50, shield = 100, charge = 1, damage = 2;
    float magnet = 0.0f;
    void init() {
        if(Globals.shield != shield) shield = Globals.shield;
        if(Globals.saveManager.currency != currency) currency = Globals.saveManager.currency;
        if(Globals.saveManager.rate != rate) rate = Globals.saveManager.rate;
        if(Globals.saveManager.refresh != refresh) refresh = Globals.saveManager.refresh;
        if(Globals.saveManager.magnet != magnet) magnet = Globals.saveManager.magnet;
        if(Globals.saveManager.charge != charge) charge = Globals.saveManager.charge;
        if(Globals.saveManager.damage != damage) damage = Globals.saveManager.damage;
        screen = Globals.screen;
    }
    
    //TODO: Balance the price to benefit ratio for upgrades
    void update() {
        screen.clear(3);
        if(Button.A.justPressed() && rate < 10){
            if(currency >= 5){
                currency -= 5;
                if(rate > 10) rate = 10;
                else rate++;
            }
        }
        if(Button.B.justPressed() && refresh > 5){
            if(currency >= 10){
                currency -= 10;
                refresh-=5;
                if(refresh < 5) refresh = 5;
            }
        }
        if(Button.Up.justPressed() && shield < 100){
            if(currency >= 15){
                currency -= 15;
                if(shield + 10 > 100) shield = 100;
                else{
                    shield+=10;
                }
            }
        }
        if(Button.Left.justPressed() && charge > 10){
            if(currency >= 20){
                currency -= 20;
                if(charge - 5 < 10) charge = 10;
                else charge-=5;
            }
        }
        if(Button.Down.justPressed() && magnet < 2.0f){
            if(currency >= 25){
                currency -= 25;
                if((magnet + 0.1f) > 1.0f) magnet = 1.02f;
                else{
                    magnet+=0.10f;
                }
            }
        }
        
        if(Button.Right.justPressed() && damage < 20){
            if(currency >= 30){
                currency -= 30;
                damage++;
            }
        }

        screen.setTextPosition(0, 16);
        screen.setTextColor(0);
        screen.print(
            "$5  - [A] Rate: " + rate
        + "\n$10 - [B] Refresh: " + refresh
        + "\n$15 - [U] Shield: " + shield
        + "\n$20 - [L] Charge: " + charge 
        + "\n$25 - [D] Magnet: " + (int)(magnet*100)
        + "%\n$30 - [R] Damage: " + damage
        + "\n\n$$" + currency
        + "\n[C] - Continue");
        
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
        Globals.saveManager.charge = charge;
        Globals.saveManager.damage = damage;
        screen = null;
    }
}