import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;

// import stage.GrabbyMcStage;
// import stage.WormBossStage;
// import stage.ForkBombStage;
// import stage.MiniBossStage;

import sprites.MegaFragment;
import sprites.Chip;

public class MenuStage extends State {
    
    HiRes16Color screen;
    MegaFragment megaFrag;
    Chip chip;
    int select=0, count=0;
    String[] aboutText;
    boolean first, second, third, fourth, endless;
    
    void init(){
        endless = Globals.endlessUnlocked;
        first = Globals.saveManager.firstZoneClear;
        second = Globals.saveManager.secondZoneClear;
        third = Globals.saveManager.thirdZoneClear;
        fourth = Globals.saveManager.fourthZoneClear;
        
        megaFrag = new MegaFragment();
        megaFrag.corrupt();
        
        chip = new Chip();
        chip.idle();
        
        screen = Globals.screen;
        select = 0;
        count = 0;
        
        aboutText = new String[]{
            "Zone 0: The smallest\n  Zone with the lowest\n  corruption",
            "Zone 1: A Spreading\n  threat detected. It\n  trails corruption",
            "Zone 2: Be ready to\n  to Boost through the\n  corruption",
            "Zone 3: Lasers are \n  awesome, but don't\n  touch them!",
            "Endless: Battle for\n  glory against the \n  endless waves of\n  viruses!"
        };
    }
    
    void update(){
        
        if(Button.B.justPressed()){
            // TODO: create endless mode
            if(select == 4) {
                Globals.ZONE = 0;
                Globals.initEndlessMode();   
            } else Globals.ZONE = select;
            
            Globals.reset();
            EntityManager.initializeNormalStage();
            Game.changeState(new NormalSector());
            return;
        }
        
        screen.clear(3);
        screen.setTextColor(endless ? 0:3);
        screen.drawRect(8, 8, 202, 38, 1);
        screen.fillRect(10, 10, 200, 36, endless ? 13 : 8);
        screen.setTextPosition(15, 15);
        
        screen.print(
            "Virus Buster Program" +
            "\n  Initialized" +
            "\n  Select Zone to Begin" 
            );
        
        
        if(Button.Right.justPressed() ){
            if((select == 3) && endless) select = 4;
            else if (select < 3) select++;
        }
        if(Button.Left.justPressed() && select > 0){
            select--;
        }
        if(Button.Down.justPressed()){
            if(select == 1) select = 3;
            if(select == 0) select = 2;
        }
        if(Button.Up.justPressed()){
            if(select == 2) select = 0;
            if(select == 3) select = 1;
        }
        
        //first frag
        megaFrag.setFlipped(false);
        megaFrag.setMirrored(false);
        if(first){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 86, 32+24);
        
        //second
        megaFrag.setMirrored(true);
        if(second){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 110, 32+24);
        
        //third
        megaFrag.setMirrored(false);
        megaFrag.setFlipped(true);
        if(third){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 86, 56+24);
        
        //last
        megaFrag.setFlipped(true);
        megaFrag.setMirrored(true);
        if(fourth){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 110, 56+24);
        
        // draw endless zone icon
        if(endless) {
            screen.drawHLine(134, 60+24, 30, 10);
            // screen.fillRect(160, 40+24, 24, 24, 15);
            chip.draw(screen, 164, 71);
        }

        switch(select){
            case 0:
                screen.drawRect(86, 32+24, 24, 24, 11);
                break;
            case 1:
                screen.drawRect(110, 32+24, 24, 24, 11);
                break;
            case 2:
                screen.drawRect(86, 56+24, 24, 24, 11);
                break;
            case 3:
                screen.drawRect(110, 56+24, 24, 24, 11);
                break;
            case 4:
                screen.drawRect(160, 71-4, 24, 24, 11);
                break;
        }
        
        
        screen.drawRect(8, 98+16, 202, 48, 1);
        screen.fillRect(10, 100+16, 200, 46, endless ? 13 : 8);
        screen.setTextPosition(15, 105+16);
        screen.print(aboutText[select]);
        
        if(count > 20) count = 0;
        
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
    }
}