import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import managers.BotManager;
import managers.BlastManager;
import managers.ForkBombManager;


class ForkBombStage extends State {
    
    BotManager botManager;
    BlastManager blastManager;
    ForkBombManager forkBombManager;
    
    void init(){
        botManager = new BotManager();
        blastManager = new BlastManager();
        forkBombManager = new ForkBombManager();
        forkBombManager.init();
    }
    
    void update(){
        Globals.screen.clear(3);
        Globals.drawGrid();
        
        if(forkBombManager.cleared()){
            Globals.drawCleared(true);
        }
        
        // Update
        botManager.updateBotMovement();
        forkBombManager.update(blastManager, botManager.getX(), botManager.getY(), botManager.speed > 1);
        
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        // Render
        botManager.render();
        forkBombManager.render();
        
        Globals.drawHud((int)(forkBombManager.getCurrentHealth() * 78 / forkBombManager.getTotalHealth()));
        blastManager.render();
        Globals.screen.flush();
    }
    
    void shutdown(){
        botManager = null;
        blastManager = null;
        forkBombManager = null;
    }
    
}