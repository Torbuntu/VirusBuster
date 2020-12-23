import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import femto.mode.HiRes16Color;

import managers.BotManager;
import managers.BlastManager;
import managers.WormBossManager;
import stage.GameOverStage;
import stage.SummaryStage;

import sprites.MegaFragment;

class WormBossStage extends State {
    HiRes16Color screen;
    BotManager botManager;
    BlastManager blastManager;
    WormBossManager wormManager;
    MegaFragment frag;
    boolean collected = false;
    
    void init(){
        screen = Globals.screen;
        botManager = EntityManager.botManager;
        blastManager = EntityManager.blastManager;
        blastManager.reset();
        wormManager = new WormBossManager();
        frag = new MegaFragment();
        frag.complete();
        if(Globals.endless)collected = true;
        System.out.println("[I] - Worm Boss initialized");
    }
    
    void update(){
        screen.clear(3);
        Globals.drawGrid();
        
        if(Globals.shield <= 0){
            if(Globals.endless) Game.changeState(new SummaryStage());
            else Game.changeState(new GameOverStage());
        }
        
        if(wormManager.cleared()){
            if(collected){
                Globals.drawCleared(true);
            } else {
                if(Globals.boundingBox(botManager.getX(), botManager.getY(), 16, 98, 76, 24)) collected = true;
                frag.setMirrored(true);
                frag.draw(screen, 98, 76);
            }
        }  
        
        // Update
        botManager.updateBotMovement(blastManager.charge);
        wormManager.update(blastManager, botManager);
        
        blastManager.update(botManager.getX()+2, botManager.getY()+6, botManager.dir);
        
        botManager.render(screen);
        wormManager.render(screen);
        
        Globals.drawHud((int)(wormManager.getCurrentHealth() * 78 / wormManager.getTotalHealth()));
        
        blastManager.render(screen);

        screen.flush();
    }
    
    void shutdown(){
        //botManager = null;
        //blastManager = null;
        wormManager = null;
        screen = null;
        if(Globals.endless) {
            Globals.endlessSaveManager.currency += 25;
            Globals.shield += 25;
            if(Globals.shield > 100) Globals.shield = 100;
        }
    }
}