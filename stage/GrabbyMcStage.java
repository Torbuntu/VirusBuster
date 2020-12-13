import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import managers.BotManager;
import managers.BlastManager;
import managers.GrabbyManager;

import femto.mode.HiRes16Color;

class GrabbyMcStage extends State {
    HiRes16Color screen;
    BotManager botManager;
    BlastManager blastManager;
    GrabbyManager grabbyManager;
    
    void init(){
        screen = Globals.screen;
        botManager = new BotManager();
        blastManager = new BlastManager();
        grabbyManager = new GrabbyManager();
        grabbyManager.init();
        
        Mixer.init(8000);
    }
    
    void update(){
        screen.clear(3);
        Globals.drawGrid();
        
        if(grabbyManager.cleared()){
            Globals.drawCleared(true);
        }
        
        // Update
        botManager.updateBotMovement();
        grabbyManager.update(blastManager, botManager);
        
        blastManager.update(botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        // Render
        botManager.render(screen);
        grabbyManager.render(screen);
        
        Globals.drawHud((int)(grabbyManager.getCurrentHealth() * 78 / grabbyManager.getTotalHealth()));
        blastManager.render(screen);
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
        botManager = null;
        blastManager = null;
        grabbyManager = null;
    }
}