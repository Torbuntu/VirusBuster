import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;
import stage.MenuStage;

import sprites.Loot;

/**
 * Summary scene displays the final post-stage summary. 
 * This class also saves the player variables between Zones.
 */ 
class SummaryStage extends State {
    HiRes16Color screen;
    Loot loot;
    int accuracy, c, highScore, score, bonusScore = 0, t = 100;
    int sector, zone;
    void init(){
        screen = Globals.screen;
        screen.setTextColor(12);
        accuracy = Globals.getAccuracy();
        
        if(!Globals.endless) {
            c = Globals.saveManager.currency;
            accuracy = 100;
            if(accuracy >= 50 && accuracy < 75){
                c += (int)(c*1.5);
            }else if(accuracy >= 75 && accuracy < 90){
                c += c*2;
            }else if(accuracy >= 90){
                c += c*3;
            }
        }else{
            score = Globals.score;
            highScore = Globals.endlessSaveManager.highScore;
            sector = Globals.SECTOR;
            zone = Globals.ZONE;
            if(accuracy >= 50 && accuracy < 75){
                bonusScore = 1000;
            }else if(accuracy >= 75 && accuracy < 90){
                bonusScore = 2000;
            }else if(accuracy >= 90 && accuracy < 99){
                bonusScore = 3000;
            }else if(accuracy > 99){//holy crap dude
                bonusScore = 6000;
            }
        }
        loot = new Loot();
        loot.play();
    }
    
    void update(){
        screen.clear(3);
        if(Button.C.justPressed()){
            Game.changeState(new MenuStage());
        }
        
        screen.drawRect(18, 16, 184, 140, 12);
        screen.setTextPosition(0, 0);
        screen.println("P:// SUMMARY.EXE");
        
        screen.setTextPosition(20, 18);
        
        screen.println(    "Accuracy:   " + accuracy);
        if(Globals.endless){
            //tmp
            screen.setTextPosition(110, 8);
            screen.println(Globals.ZONE + ":" + Globals.SECTOR);
            screen.setTextPosition(20, 18+9);
            screen.print("Bonus:      " + bonusScore);
            screen.setTextPosition(20, 18+18);
            screen.print("Score:      " + score);
            screen.setTextPosition(20, 18+27);
            screen.print("Total:      " + (bonusScore + score));
            
            
            if(score+bonusScore > highScore){
                screen.setTextPosition(20, 70);
                screen.print("!NEW HIGH SCORE!");
                screen.setTextPosition(20, 88);
                screen.print("-"+(score+bonusScore)+"-");
            }else{
                screen.setTextPosition(20, 18+36);
                screen.println("High Score: " + highScore);
            }
            if(zone > Globals.endlessSaveManager.ZONE){
                screen.setTextPosition(20, 88+18);
                screen.print("!NEW BEST ZONE!");
                screen.setTextPosition(20, 88+27);
                screen.print("-"+zone+":"+sector+"-");
            }else{
                screen.setTextPosition(20, 88+18);
                screen.print("-"+Globals.endlessSaveManager.ZONE+":"+Globals.endlessSaveManager.SECTOR+"-");
            }
        }else{
            screen.setTextPosition(20, 27);
            if(accuracy >= 50 && accuracy < 75){
                screen.println("Bonus:      x1.5");
            }else if(accuracy >= 75 && accuracy < 90){
                screen.println("Bonus:      x2.0");
            }else if(accuracy >= 90){
                screen.println("Bonus:      x3.0");
            }
            screen.setTextPosition(25, 36);
            screen.println(" x"+c);
            loot.draw(screen, 20, 36);
        }
        
        screen.setTextPosition(0, 167);
        screen.print("P:// [C] - CONTINUE");
        if(t < 50){
            int x = screen.textWidth("P:// [C] - CONTINUE");
            if(t < 4) screen.drawRect(x, 166, 5, 9, 12);
            else screen.fillRect(x, 166, 5, 9, 12);
        }
        t--;
        if(t == 0){
            t = 100;
        }
        
        screen.flush();
    }
    
    void shutdown(){
        if(!Globals.endless){
            switch(Globals.ZONE){
                case 0: Globals.saveManager.firstZoneClear = true; break;
                case 1: Globals.saveManager.secondZoneClear = true; break;
                case 2: Globals.saveManager.thirdZoneClear = true; break;
                case 3: Globals.saveManager.fourthZoneClear = true; break;
            }
            
            Globals.saveManager.currency = c;
            Globals.saveManager.saveCookie();
        }else{
            //TODO: Save high score and display score
            Globals.endless = false;
            if(bonusScore + score > Globals.endlessSaveManager.highScore) Globals.endlessSaveManager.highScore = (score + bonusScore);
            if(sector > Globals.endlessSaveManager.SECTOR) Globals.endlessSaveManager.SECTOR = sector;
            if(zone > Globals.endlessSaveManager.ZONE) Globals.endlessSaveManager.ZONE = zone;
            Globals.score = 0;
            Globals.shield = 100;
            // Refreshing shield on endless ending, because why not ;) 
            Globals.endlessSaveManager.saveCookie();
        }
        
        screen = null;
    }
}