import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import managers.BotManager;
import managers.BlastManager;
import managers.ForkBombManager;

import femto.mode.HiRes16Color;

class ForkBombStage extends State {
    HiRes16Color screen;
    BotManager botManager;
    BlastManager blastManager;
    ForkBombManager forkBombManager;
    
    void init(){
        screen = Globals.screen;
        botManager = new BotManager();
        blastManager = new BlastManager();
        forkBombManager = new ForkBombManager();
        forkBombManager.init();
    }
    
    void update(){
        screen.clear(3);
        Globals.drawGrid();
        
        if(forkBombManager.cleared()){
            Globals.drawCleared(true);
        }
        
        // Update
        botManager.updateBotMovement();
        forkBombManager.update(blastManager, botManager.getX(), botManager.getY(), botManager.speed > 1);
        
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        // Render
        botManager.render(screen);
        forkBombManager.render();
        
        Globals.drawHud((int)(forkBombManager.getCurrentHealth() * 78 / forkBombManager.getTotalHealth()));
        blastManager.render(screen);
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
        botManager = null;
        blastManager = null;
        forkBombManager = null;
    }
    
}