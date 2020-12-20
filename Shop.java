import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

import audio.Coin;
import managers.SectorZoneManager;

import sprites.Loot;

class Shop extends State {
    
    HiRes16Color screen;
    Coin coin = new Coin(0);
    Loot loot;
    
    int currency = 0, rate = 1, refresh = 50, shield = 100, charge = 1, damage = 2, select = 0;
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
                        coin.play();
                        if(rate > 10) rate = 10;
                        else rate++;
                    }
                break;    
                case 1:
                    if(refresh > 5 && currency >= 10){
                        currency -= 10;
                        coin.play();
                        if(refresh - 5 < 5) refresh = 5;
                        else refresh-=5;
                    }
                break;
                case 2:
                    if(shield < 100 && currency >= 15){
                        currency -= 15;
                        coin.play();
                        if(shield + 10 > 100) shield = 100;
                        else shield+=10;
                    }
                break;
                case 3:
                    if(charge < 100 && currency >= 20){
                        currency -= 20;
                        if(charge + 5 > 100) charge=100;
                        else charge+=5;
                    }
                break;
                case 4:
                    if(magnet < 1.0f && currency >= 25){
                        currency -= 25;
                        coin.play();
                        if((magnet + 0.1f) > 1.0f) magnet = 1.02f;
                        else magnet+=0.1f;
                    }
                break;
                case 5:
                    if(damage < 20 && currency >= 30){
                        currency -= 30;
                        coin.play();
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
        
        screen.setTextColor(0);
        screen.setTextPosition(36, 0);
        screen.print("Program Upgrades");

        loot.draw(screen, 15, 42);
        screen.setTextPosition(25, 42);
        screen.print("x"+currency);
        
        // Containers for upgrades
        for(int i = 0; i < 6; i++){
            screen.drawRect(144, 61+i*9, 52, 6, 1);
            screen.fillRect(146, 63+i*9, 50, 4, 2, true);
        }
        
        screen.setTextPosition(16, 60);
        screen.print("  5 - Rate:");
        screen.fillRect(146, 63, ((rate-1)*50/9), 4, (rate == 10 ? 11 : 15), true);
        
        screen.setTextPosition(16, 60+9);
        screen.print(" 10 - Refresh:");
        screen.fillRect(146, 72, (Math.abs(refresh-50)*50/45), 4, (refresh == 5 ? 11 : 15), true);
        
        screen.setTextPosition(16, 60+18);
        screen.print(" 15 - Shield:");
        screen.fillRect(146, 63+18, (shield*50/100), 4, (shield == 100 ? 11 : 15), true);
        
        screen.setTextPosition(16, 60+27);
        screen.print(" 20 - Charge:");
        screen.fillRect(146, 63+27, (charge*50/100), 4, (charge == 100 ? 11 : 15), true);
        
        screen.setTextPosition(16, 60+36);
        screen.print(" 25 - Magnet:");
        screen.fillRect(146, 63+36, ((int)(magnet*100)*50/100), 4, ((int)(magnet*100) >= 100 ? 11 : 15), true);
        
        screen.setTextPosition(16, 60+45);
        screen.print(" 30 - Damage:");
        screen.fillRect(146, 63+45, ((damage-2)*50/18), 4, (damage == 20 ? 11 : 15), true);
        
        screen.setTextPosition(16, 60+54);
        screen.print(" Continue");
        
        screen.setTextPosition(16, 60+select*9);
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