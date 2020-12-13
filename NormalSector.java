import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

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
    
    int transitionTime = 0;
    
    void init(){
        screen = Globals.screen;
        
        botManager = new BotManager();
        blastManager = new BlastManager();
        debrisManager = new DebrisManager();
        virusManager = new VirusManager();
        itemDropManager = new ItemDropManager();
    
        debrisManager.resetDebris();
        virusManager.initWave(Globals.SECTOR, debrisManager);
        
        Mixer.init(8000);
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
            Globals.saveManager.currency++;
        }
        if(Globals.shield <= 0){
            //TODO: GameOver Stage
            Game.changeState(new GameOverStage());
        }
        
        botManager.updateBotMovement(debrisManager, true);
        botManager.render(screen);
        
        if(transition())return;
        
        debrisManager.render(screen);
        virusManager.update(botManager.getX(), botManager.getY(), debrisManager, blastManager);

        if(virusManager.getThreats() == 0){
            Globals.drawCleared(false);
        }
        virusManager.render(screen);

        //START update Blast
        blastManager.update(botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        Globals.drawHud((int)(virusManager.getThreats() * 78 / virusManager.getTotalThreats()));
        
        //Need to draw the blast manager after the hud or else some items don't render 
        blastManager.render(screen);
        
        screen.flush();
    }
    
    
    public void shutdown(){
        screen = null;
        virusManager = null;
        debrisManager = null;
        botManager = null;
        blastManager = null;
        itemDropManager = null;
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