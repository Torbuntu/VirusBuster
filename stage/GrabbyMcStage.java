import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;
import femto.mode.HiRes16Color;

import audio.Mega;

import managers.BotManager;
import managers.BlastManager;
import managers.GrabbyManager;
import stage.GameOverStage;
import stage.SummaryStage;
import sprites.MegaFragment;

class GrabbyMcStage extends State {
    HiRes16Color screen;
    BotManager botManager;
    BlastManager blastManager;
    GrabbyManager grabbyManager;
    MegaFragment frag;
    Mega pickup;
    boolean collected = false;
    int fragmentTimer = 150, incoming = 150;
    void init(){
        pickup = new Mega(3);
        screen = Globals.screen;
        botManager = EntityManager.botManager;
        blastManager = EntityManager.blastManager;
        blastManager.reset();
        grabbyManager = new GrabbyManager();
        grabbyManager.init();
        
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
        Globals.drawHud((int)(grabbyManager.getCurrentHealth() * 78 / grabbyManager.getTotalHealth()));
        
        if(grabbyManager.cleared()){
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
                        screen.fillRect(0, 150-fragmentTimer-2, 220, 10, 3);
                        screen.setTextPosition(1, 150-fragmentTimer);
                        screen.setTextColor(11);
                        screen.print("Mega Fragment Recovered!");
                    }else Globals.drawCleared(true);
                } else {
                    if(Globals.boundingBox(botManager.getX(), botManager.getY(), 16, 98, 76, 24)) collected = true;
                    frag.setMirrored(true);
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
        grabbyManager.update(blastManager, botManager);
        
        blastManager.update(botManager.getX()+2, botManager.getY()+6, botManager.dir);
        
        // Render
        botManager.render(screen);
        grabbyManager.render(screen);
        
        blastManager.render(screen);
        screen.flush();
    }
    
    void shutdown(){
        pickup = null;
        screen = null;
        //botManager = null;
        //blastManager = null;
        grabbyManager = null;
        if(Globals.endless) {
            Globals.endlessSaveManager.currency += 25;
            Globals.shield += 25;
            if(Globals.shield > 100) Globals.shield = 100;
        }
    }
}