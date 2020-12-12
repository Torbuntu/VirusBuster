import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import femto.mode.HiRes16Color;

import managers.BotManager;
import managers.BlastManager;
import managers.WormBossManager;

class WormBossStage extends State {
    HiRes16Color screen;
    BotManager botManager;
    BlastManager blastManager;
    WormBossManager wormManager;
    
    void init(){
        screen = Globals.screen;
        botManager = new BotManager();
        blastManager = new BlastManager();
        wormManager = new WormBossManager();
        
        Mixer.init(8000);
        System.out.println("[I] - Worm Boss initialized");
    }
    void update(){
        screen.clear(3);
        Globals.drawGrid();
        
        if(wormManager.cleared()){
            //TODO: Collect Mega Fragment logic
            Globals.drawCleared(true);
        }  
        
        // Update
        botManager.updateBotMovement();
        wormManager.update(blastManager, botManager);
        
        blastManager.update(botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        botManager.render(screen);
        wormManager.render();
        
        Globals.drawHud((int)(wormManager.getCurrentHealth() * 78 / wormManager.getTotalHealth()));
        
        blastManager.render(screen);

        screen.flush();
    }
    
    void shutdown(){
        botManager = null;
        blastManager = null;
        wormManager = null;
        screen = null;
    }
}