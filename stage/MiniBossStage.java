import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import femto.mode.HiRes16Color;

import stage.GameOverStage;
import stage.SummaryStage;

import managers.BotManager;
import managers.BlastManager;
import entities.MiniBoss;

import sprites.MegaFragment;

class MiniBossStage extends State {
    
    HiRes16Color screen;
    
    //only used in ZONE 0 where miniboss is actually the megaboss
    MegaFragment frag;
    boolean collected = false;
    int fragmentTimer = 150, incoming = 150;
    
    BotManager botManager;
    BlastManager blastManager;
    MiniBoss bossManager;
    
    void init(){
        screen = Globals.screen;
        
        bossManager = new MiniBoss();
        botManager = EntityManager.botManager;
        blastManager = EntityManager.blastManager;
        blastManager.reset();
        
        if(Globals.ZONE == 0 && !Globals.endless){
            frag = new MegaFragment();
            frag.complete();
        }
        
        System.out.println("[I] - MiniBoss initialized");
    }
    
    void update(){
        screen.clear(3);
        Globals.drawGrid();
        
        // Update
        botManager.updateBotMovement(blastManager.charge);
        bossManager.update(blastManager, botManager.getX(), botManager.getY());
        
        if(Globals.shield <= 0){
            if(Globals.endless) Game.changeState(new SummaryStage());
            else Game.changeState(new GameOverStage());
        }
        
        if(bossManager.cleared()){
            // CLEARED!
            if(Globals.ZONE == 0 && !Globals.endless){
                if(incoming > 0){
                    incoming--;
                    screen.fillRect(80, 150-incoming-2, 70, 12, 3);
                    screen.setTextPosition(82, 150-incoming);
                    screen.setTextColor(11);
                    screen.print("<CLEAR>");
                }else{
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
            } else Globals.drawCleared(false);
        }
        
        // START update Blast
        blastManager.update(botManager.getX()+2, botManager.getY()+6, botManager.dir);
        
        // Render
        botManager.render(screen);
        
        
        Globals.drawHud((int)(bossManager.health * 78 / bossManager.maxHealth));
        bossManager.render(screen);
        blastManager.render(screen);
        
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
        //botManager = null;
        //blastManager = null;
        bossManager = null;
        if(Globals.endless) {
            Globals.endlessSaveManager.currency += 15;
            Globals.shield += 25;
            if(Globals.shield > 100) Globals.shield = 100;
        }
    }
}