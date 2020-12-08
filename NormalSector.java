import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import managers.VirusManager;
import managers.DebrisManager;
import managers.BotManager;
import managers.BlastManager;
import managers.ItemDropManager;

class NormalSector extends State {
    
    BotManager botManager;
    BlastManager blastManager;
    DebrisManager debrisManager;
    VirusManager virusManager;
    ItemDropManager itemDropManager;
    
    void init(){
        botManager = new BotManager();
        blastManager = new BlastManager();
        debrisManager = new DebrisManager();
        virusManager = new VirusManager(debrisManager.getSpawnX(), debrisManager.getSpawnY());
        itemDropManager = new ItemDropManager();
    
        debrisManager.resetDebris();
        virusManager.initWave(Globals.SECTOR, debrisManager.getSpawnX(), debrisManager.getSpawnY());
        virusManager.resetAll();
        
        Mixer.init(8000);
    }
    
    void update(){
        Globals.screen.clear(3);

        if(Globals.createItemDrop){
            Globals.createItemDrop = false;
            itemDropManager.newDrop(Globals.itemX, Globals.itemY);
        }

        Globals.drawGrid();
        itemDropManager.updateAndRender();
        if(itemDropManager.checkCollect(botManager.getX(), botManager.getY())){
            Globals.currency++;
        }
        if(Globals.shield <= 0){
            Globals.ROOM_STATUS = 5;//GAME OVER
        }
        
        botManager.updateBotMovement(debrisManager, true);
        botManager.render();
        
        debrisManager.render();
        virusManager.update(botManager.getX(), botManager.getY(), debrisManager, blastManager);

        if(virusManager.getThreats() == 0){
            Globals.drawCleared(false);
        }
        virusManager.render();

        //START update Blast
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        Globals.drawHud((int)(virusManager.getThreats() * 78 / virusManager.getTotalThreats()));
        
        //Need to draw the blast manager after the hud or else some items don't render 
        blastManager.render();
        
        Globals.screen.flush();
    }
    
    
    public void shutdown(){
        virusManager = null;
        debrisManager = null;
        botManager = null;
        blastManager = null;
        itemDropManager = null;
    }
}