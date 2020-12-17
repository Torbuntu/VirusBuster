import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;
import femto.mode.HiRes16Color;

import managers.BotManager;
import managers.BlastManager;
import managers.ForkBombManager;
import stage.GameOverStage;


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
        
        Mixer.init(8000);
    }
    
    void update(){
        screen.clear(3);
        Globals.drawGrid();
        
        if(forkBombManager.cleared()){
            Globals.drawCleared(true);
        }
        if(Globals.shield <= 0){
            Game.changeState(new GameOverStage());
        }
        
        // Update
        botManager.updateBotMovement(blastManager.charge);
        forkBombManager.update(blastManager, botManager.getX(), botManager.getY(), botManager.speed > 1);
        
        blastManager.update(botManager.getX()+8, botManager.getY()+6, botManager.dir);
        
        // Render
        botManager.render(screen);
        forkBombManager.render(screen);
        
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