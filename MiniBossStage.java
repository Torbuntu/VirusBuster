import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import femto.mode.HiRes16Color;

import managers.BotManager;
import managers.BlastManager;
import managers.MiniBossManager;

class MiniBossStage extends State {
    
    HiRes16Color screen;
    
    BotManager botManager;
    BlastManager blastManager;
    MiniBossManager bossManager;
    
    void init(){
        screen = Globals.screen;
        
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
        screen.clear(3);
        Globals.drawGrid();
        
        // Update
        botManager.updateBotMovement();
        bossManager.update(blastManager, (botManager.getX()+8), (botManager.getY()+8));
        
        if(bossManager.cleared()){
            // CLEARED!
            Globals.drawCleared(true);
        }
        
        // START update Blast
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        // Render
        botManager.render(screen);
        bossManager.render();
        
        Globals.drawHud((int)(bossManager.getCurrentHealth() * 78 / bossManager.getTotalHealth()));
        
        blastManager.render(screen);
        
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
        botManager = null;
        blastManager = null;
        bossManager = null;
    }
}