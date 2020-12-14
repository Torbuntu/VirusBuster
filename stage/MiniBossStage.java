import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import femto.mode.HiRes16Color;

import stage.GameOverStage;
import managers.BotManager;
import managers.BlastManager;
import entities.MiniBoss;

import sprites.MegaFragment;

class MiniBossStage extends State {
    
    HiRes16Color screen;
    
    //only used in ZONE 0 where miniboss is actually the megaboss
    MegaFragment frag;
    boolean collected = false;
    
    BotManager botManager;
    BlastManager blastManager;
    MiniBoss bossManager;
    
    void init(){
        screen = Globals.screen;
        
        bossManager = new MiniBoss();
        botManager = new BotManager();
        blastManager = new BlastManager();
        
        if(Globals.ZONE == 0){
            frag = new MegaFragment();
            frag.complete();
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
        
        if(Globals.shield <= 0){
            Game.changeState(new GameOverStage());
        }
        
        if(bossManager.cleared()){
            // CLEARED!
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
        
        
        Globals.drawHud((int)(bossManager.health * 78 / bossManager.maxHealth));
        bossManager.render(screen);
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