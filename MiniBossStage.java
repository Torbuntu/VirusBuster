import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import managers.BotManager;
import managers.BlastManager;
import managers.MiniBossManager;

class MiniBossStage extends State {
    
    BotManager botManager;
    BlastManager blastManager;
    MiniBossManager bossManager;
    
    void init(){
        bossManager = new MiniBossManager();
        botManager = new BotManager();
        blastManager = new BlastManager();
        
        switch(Globals.ZONE){
            case 0: bossManager.init(1); break;
            case 1: bossManager.init(2); break;
            case 2: bossManager.init(3); break;
            case 3: bossManager.init(4); break;
        }
        Mixer.init(8000);
        System.out.println("[I] - MiniBoss initialized");
    }
    void update(){
        Globals.screen.clear(3);
        Globals.drawGrid();
        
        // Update
        botManager.updateBotMovement();
        bossManager.update(blastManager, (botManager.getX()+8), (botManager.getY()+8));
        
        if(bossManager.cleared()){
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
        
        // START update Blast
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        // Render
        botManager.render();
        bossManager.render();
        
        Globals.drawHud((int)(bossManager.getCurrentHealth() * 78 / bossManager.getTotalHealth()));
        
        blastManager.render();
        
        Globals.screen.flush();
    }
    
    void shutdown(){
        botManager = null;
        blastManager = null;
        bossManager = null;
    }
}