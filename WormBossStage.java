import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import managers.BotManager;
import managers.BlastManager;
import managers.WormBossManager;

class WormBossStage extends State {
    
    BotManager botManager;
    BlastManager blastManager;
    WormBossManager wormManager;
    
    void init(){
        botManager = new BotManager();
        blastManager = new BlastManager();
        wormManager = new WormBossManager();
        
        Mixer.init(8000);
        System.out.println("[I] - Worm Boss initialized");
    }
    void update(){
        Globals.screen.clear(3);
        
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
            if(botManager.getY() + 32 < Globals.screen.height()-45) botManager.setY(botManager.getY()+32);
            else botManager.setY(botManager.getY()-32);
            // Move bot X
            if(botManager.getX() + 32 < Globals.screen.width()-45) botManager.setX(botManager.getX()+32);
            else botManager.setX(botManager.getX()-32);
            // Subtract shielding
            Globals.shield-=10;
        }
        
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        botManager.render();
        wormManager.render();
        
        Globals.drawHud((int)(wormManager.getCurrentHealth() * 78 / wormManager.getTotalHealth()));
        
        blastManager.render();

        Globals.screen.flush();
    }
    
    void shutdown(){
        botManager = null;
        blastManager = null;
        wormManager = null;
    }
}