import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

import managers.BotManager;
import managers.BlastManager;
import managers.ForkBombManager;
import stage.GameOverStage;
import stage.SummaryStage;

import sprites.MegaFragment;

class ForkBombStage extends State {
    HiRes16Color screen;
    BotManager botManager;
    BlastManager blastManager;
    ForkBombManager forkBombManager;
    
    MegaFragment frag;
    
    boolean collected = false;
    int incoming = 150, fragmentTimer = 150;
    
    
    void init(){
        screen = Globals.screen;
        botManager = EntityManager.botManager;
        blastManager = EntityManager.blastManager;
        blastManager.reset();
        forkBombManager = new ForkBombManager();
        
        frag = new MegaFragment();
        frag.complete();
        if(Globals.endless){
            collected = true;
            fragmentTimer  = 0;
        }
    }
    
    void update(){
        screen.clear(3);
        Globals.drawGrid();
        
        if(forkBombManager.cleared()){
            if(incoming > 0){
                incoming--;
                screen.fillRect(80, 150-incoming-2, 70, 12, 3);
                screen.setTextPosition(82, 150-incoming);
                screen.setTextColor(11);
                screen.print("<CLEAR>");
            } else{
                if(collected){
                    if(fragmentTimer > 0){
                        fragmentTimer--;
                        screen.fillRect(0, 150-incoming-2, 220, 10, 3);
                        screen.setTextPosition(1, 150-incoming);
                        screen.setTextColor(11);
                        screen.print("Mega Fragment Recovered!");
                    }else Globals.drawCleared(true);
                } else {
                    if(Globals.boundingBox(botManager.getX(), botManager.getY(), 16, 98, 76, 24)) collected = true;
                    frag.setMirrored(false);
                    frag.setFlipped(true);
                    frag.draw(screen, 98, 76);
                }
            }
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