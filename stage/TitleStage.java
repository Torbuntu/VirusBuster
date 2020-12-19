import femto.Game;
import femto.State;
import femto.input.Button;

import femto.mode.HiRes16Color;

// import stage.GrabbyMcStage;
// import stage.WormBossStage;
// import stage.ForkBombStage;
// import stage.MiniBossStage;

import sprites.MegaFragment;

public class TitleStage extends State {
    
    HiRes16Color screen;
    MegaFragment megaFrag;
    int select=0, count=0;
    String[] aboutText;
    
    void init(){
        megaFrag = new MegaFragment();
        megaFrag.corrupt();
        
        screen = Globals.screen;
        select = 0;
        count = 0;
        
        aboutText = new String[]{
            "Zone 0: The smallest\n  Zone with the lowest\n  corruption",
            "Zone 1: A Spreading\n  threat detected. It\n  trails corruption",
            "Zone 2: Be ready to\n  to Boost through the\n  corruption",
            "Zone 3: Lasers are \n  awesome, but don't\n  touch them!",
            "Endless: "
        };
    }
    
    void update(){
        
        if(Button.B.justPressed()){
            // TODO: create endless mode
            //if(select == 4) Game.changeState(new Endless());
            Globals.ZONE = select;
            Globals.reset();
            // Globals.saveManager.refresh = 10;
            // Globals.saveManager.charge = 10;
            // Globals.saveManager.rate = 8;
            // Game.changeState(new MiniBossStage());
            // Game.changeState(new WormBossStage());
            // Game.changeState(new GrabbyMcStage());
            // Game.changeState(new ForkBombStage());
            EntityManager.initializeNormalStage();
            Game.changeState(new NormalSector());
            return;
        }
        
        screen.clear(3);
        
        screen.setTextPosition(0, 10);
        screen.setTextColor(0);
        screen.print(
            "Press B to begin Demo." +
            "\nSelect Zone to Begin"
            );
        
        
        if(Button.Right.justPressed() && select < 3){
            select++;
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
        if(Globals.saveManager.firstZoneClear){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 86, 32);
        
        //second
        megaFrag.setMirrored(true);
        if(Globals.saveManager.secondZoneClear){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 110, 32);
        
        //third
        megaFrag.setMirrored(false);
        megaFrag.setFlipped(true);
        if(Globals.saveManager.thirdZoneClear){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 86, 56);
        
        //last
        megaFrag.setFlipped(true);
        megaFrag.setMirrored(true);
        if(Globals.saveManager.fourthZoneClear){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 110, 56);
                

        switch(select){
            case 0:
                screen.drawRect(86, 32, 24, 24, 11);
                break;
            case 1:
                screen.drawRect(110, 32, 24, 24, 11);
                break;
            case 2:
                screen.drawRect(86, 56, 24, 24, 11);
                break;
            case 3:
                screen.drawRect(110, 56, 24, 24, 11);
                break;
        }
        screen.fillRect(10, 100, 200, 50, 8+select);
        screen.setTextPosition(15, 105);
        screen.setTextColor(3);
        screen.print(aboutText[select]);
        
        if(count > 20) count = 0;
        
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
    }
}