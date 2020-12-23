import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

import managers.BotManager;
import managers.BlastManager;
import managers.ForkBombManager;
import stage.GameOverStage;
import stage.SummaryStage;

class ForkBombStage extends State {
    HiRes16Color screen;
    BotManager botManager;
    BlastManager blastManager;
    ForkBombManager forkBombManager;
    
    void init(){
        screen = Globals.screen;
        botManager = EntityManager.botManager;
        blastManager = EntityManager.blastManager;
        blastManager.reset();
        forkBombManager = new ForkBombManager();
    }
    
    void update(){
        screen.clear(3);
        Globals.drawGrid();
        
        if(forkBombManager.cleared()){
            Globals.drawCleared(true);
        }
        if(Globals.shield <= 0){
            if(Globals.endless) Game.changeState(new SummaryStage());
            else Game.changeState(new GameOverStage());
        }
        
        // Update
        botManager.updateBotMovement(blastManager.charge);
        forkBombManager.update(blastManager, botManager.getX(), botManager.getY(), botManager.speed > 1);
        
        blastManager.update(botManager.getX()+2, botManager.getY()+6, botManager.dir);
        
        // Render
        botManager.render(screen);
        forkBombManager.render(screen);
        
        Globals.drawHud((int)(forkBombManager.getCurrentHealth() * 78 / forkBombManager.getTotalHealth()));
        blastManager.render(screen);
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
        //botManager = null;
        //blastManager = null;
        forkBombManager = null;
        if(Globals.endless) {
            Globals.endlessSaveManager.currency += 25;
            Globals.shield += 25;
            if(Globals.shield > 100) Globals.shield = 100;
        }
    }
    
}