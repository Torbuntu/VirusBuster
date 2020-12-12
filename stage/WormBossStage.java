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
            Globals.drawCleared(true);
        }  
        
        // Update
        botManager.updateBotMovement();
        wormManager.update(blastManager, botManager.getX(), botManager.getY());
        if(wormManager.bodyCollidesWithBot(botManager.getX(), botManager.getY())
        || wormManager.headCollidesWithBot(botManager.getX(), botManager.getY())){
            // Move bot Y
            if(botManager.getY() + 32 < 131) botManager.setY(botManager.getY()+32);
            else botManager.setY(botManager.getY()-32);
            // Move bot X
            if(botManager.getX() + 32 < 175) botManager.setX(botManager.getX()+32);
            else botManager.setX(botManager.getX()-32);
            // Subtract shielding
            Globals.shield-=10;
        }
        
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