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
            // CLEARED!
            Globals.screen.setTextPosition(Globals.screen.width()/2-58, Globals.screen.height()/2);
            Globals.screen.setTextColor(0);
            Globals.screen.print(Globals.SECTOR_CLEAR);
            
            Globals.screen.setTextPosition(26, Globals.screen.height()/2+16);
            Globals.screen.print(Globals.PRESS_C_TRANSPORT);
            if(Button.C.justPressed()){
                Globals.SECTOR++;
                Game.changeState(SectorZoneManager.getNextState());
            }
            Globals.ROOM_STATUS = 1;
        }
        
        // Update
        botManager.updateBotMovement();
        forkBombManager.update();
        
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