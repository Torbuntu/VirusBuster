import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import femto.mode.HiRes16Color;

import managers.BotManager;
import managers.BlastManager;
import managers.MiniBossManager;

import sprites.MegaFragment;

class MiniBossStage extends State {
    
    HiRes16Color screen;
    
    //only used in ZONE 0 where miniboss is actually the megaboss
    MegaFragment frag;
    boolean collected = false;
    
    BotManager botManager;
    BlastManager blastManager;
    MiniBossManager bossManager;
    
    void init(){
        screen = Globals.screen;
        
        bossManager = new MiniBossManager();
        botManager = new BotManager();
        blastManager = new BlastManager();
        
        switch(Globals.ZONE){
            case 0: 
                bossManager.init(1); 
                frag = new MegaFragment();
                frag.complete();
                break;
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
        bossManager.update(blastManager, botManager.getX(), botManager.getY());
        
        if(bossManager.cleared()){
            // CLEARED!
            //TODO: Wait until MegaFragment is collected if ZONE == 0
            if(Globals.ZONE == 0){
                if(collected){
                    Globals.drawCleared(true);
                } else {
                    if(Globals.boundingBox(botManager.getX(), botManager.getY(), 16, 98, 76, 24)) collected = true;
                    frag.draw(screen, 98, 76);
                    
                }
            } else Globals.drawCleared(true);
        }
        
        // START update Blast
        blastManager.update(botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        // Render
        botManager.render(screen);
        bossManager.render(screen);
        
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