import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

import stage.GameOverStage;

import managers.VirusManager;
import managers.DebrisManager;
import managers.BotManager;
import managers.BlastManager;
import managers.ItemDropManager;

class NormalSector extends State {
    
    HiRes16Color screen;
    
    BotManager botManager;
    BlastManager blastManager;
    DebrisManager debrisManager;
    VirusManager virusManager;
    ItemDropManager itemDropManager;
    
    int transitionTime = 0, currency = 0;
    
    void init(){
        screen = Globals.screen;
        currency = Globals.saveManager.currency;
        
        botManager = EntityManager.botManager;
        botManager.setPos(110, 88);
        blastManager = EntityManager.blastManager;
        debrisManager = EntityManager.debrisManager;
        virusManager = EntityManager.virusManager;
        itemDropManager = EntityManager.itemDropManager;
    
        debrisManager.reset();
        blastManager.reset();
        virusManager.initWave(Globals.SECTOR, debrisManager);
    }
    
    void update(){
        screen.clear(3);

        if(Globals.createItemDrop){
            Globals.createItemDrop = false;
            itemDropManager.newDrop(Globals.itemX, Globals.itemY);
        }

        Globals.drawGrid();
        itemDropManager.updateAndRender(screen);
        if(itemDropManager.checkCollect(botManager.getX(), botManager.getY(), Globals.saveManager.magnet)){
            currency++;
        }
        if(Globals.shield <= 0){
            Game.changeState(new GameOverStage());
        }
        
        botManager.updateBotMovement(debrisManager, blastManager.charge);
        botManager.render(screen);
        
        if(transition())return;
        
        debrisManager.render(screen);
        virusManager.update(botManager.getX(), botManager.getY(), debrisManager, blastManager);

        if(virusManager.getThreats() == 0){
            Globals.drawCleared(false);
        }
        virusManager.render(screen);

        //START update Blast
        blastManager.update(botManager.getX()+8, botManager.getY()+6, botManager.dir);
        
        Globals.drawHud((int)(virusManager.getThreats() * 78 / virusManager.getTotalThreats()));
        screen.setTextPosition(16, 164);
        screen.print("x"+currency);
        
        //Need to draw the blast manager after the hud or else some items don't render 
        blastManager.render(screen);
        
        screen.flush();
    }
    
    
    public void shutdown(){
        screen = null;
        /*
        virusManager = null;
        debrisManager = null;
        botManager = null;
        blastManager = null;
        itemDropManager = null;*/
        botManager.setPos(110, 88);
        Globals.saveManager.currency = currency;
    }
    
    
    boolean transition(){
        if(transitionTime < virusManager.getThreats()){
            Globals.drawHud((int)(transitionTime * 78 / virusManager.getTotalThreats()));
            transitionTime++;
            screen.flush();
            return true;
        }
        return false;
    }
}