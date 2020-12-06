import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import managers.BotManager;
import managers.BlastManager;
import managers.GrabbyManager;

class GrabbyMcStage extends State {
    
    BotManager botManager;
    BlastManager blastManager;
    GrabbyManager grabbyManager;
    
    void init(){
        botManager = new BotManager();
        blastManager = new BlastManager();
        grabbyManager = new GrabbyManager();
        grabbyManager.init();
    }
    
    void update(){
        Globals.screen.clear(3);
        Globals.drawGrid();
        
        if(grabbyManager.cleared()){
            Globals.drawCleared(true);
        }
        
        // Update
        botManager.updateBotMovement();
        grabbyManager.update(blastManager, botManager.getX(), botManager.getY());
        
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        // Render
        botManager.render();
        grabbyManager.render();
        
        Globals.drawHud((int)(grabbyManager.getCurrentHealth() * 78 / grabbyManager.getTotalHealth()));
        blastManager.render();
        Globals.screen.flush();
    }
    
    void shutdown(){
        botManager = null;
        blastManager = null;
        grabbyManager = null;
    }
}