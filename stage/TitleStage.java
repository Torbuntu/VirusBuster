import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import femto.mode.HiRes16Color;

// import stage.GrabbyMcStage;
// import stage.WormBossStage;
// import stage.ForkBombStage;
// import stage.MiniBossStage;

import sprites.MegaFragment;

public class TitleStage extends State {
    
    HiRes16Color screen;
    int select=0, count=0;
    MegaFragment megaFrag;
    
    void init(){
        megaFrag = new MegaFragment();
        megaFrag.corrupt();
        
        screen = Globals.screen;
        select = 0;
        count = 0;
        Mixer.init(8000);
    }
    
    void update(){
        
        if(Button.B.justPressed()){
            Globals.ZONE = select;
            
            // Begin at the first sector (0)
            Globals.SECTOR = 0;
            
            // going in full health, reset hit/shot ratio
            Globals.shield = 100;
            Globals.hit = 0;
            Globals.shots = 0;
            // Game.changeState(new MiniBossStage());
            // Game.changeState(new WormBossStage());
            // Game.changeState(new GrabbyMcStage());
            // Game.changeState(new ForkBombStage());
            Game.changeState(new NormalSector());
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
        megaFrag.draw(screen, 86+24, 32);
        
        //third
        megaFrag.setMirrored(false);
        megaFrag.setFlipped(true);
        if(Globals.saveManager.thirdZoneClear){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 86, 32+24);
        
        //last
        megaFrag.setFlipped(true);
        megaFrag.setMirrored(true);
        if(Globals.saveManager.fourthZoneClear){
            megaFrag.complete();
        }else megaFrag.corrupt();
        megaFrag.draw(screen, 86+24, 32+24);
                

        switch(select){
            case 0:
                screen.drawRect(86, 32, 24, 24, 11);
                break;
            case 1:
                screen.drawRect(86+24, 32, 24, 24, 11);
                break;
            case 2:
                screen.drawRect(86, 32+24, 24, 24, 11);
                break;
            case 3:
                screen.drawRect(86+24, 32+24, 24, 24, 11);
                break;
        }
        screen.fillRect(10, 100, 200, 50, 8+select);
        screen.setTextPosition(15, 105);
        screen.setTextColor(3);
        screen.print("About zone " + select);
        
        if(count > 20) count = 0;
        
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
    }
}