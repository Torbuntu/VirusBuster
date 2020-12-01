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
    
    int shieldWidth, threatWidth;

    void init(){
        shieldWidth = 0;
        threatWidth = 0;
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
        Main.screen.clear(3);
        
        if(Globals.createItemDrop){
            Globals.createItemDrop = false;
            itemDropManager.newDrop(Globals.itemX, Globals.itemY);
        }

        drawGrid();
        itemDropManager.updateAndRender();
        if(itemDropManager.checkCollect(botManager.getX()+8, botManager.getY()+8)){
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
            Globals.ROOM_STATUS = 1; // CLEARED!
            Main.screen.setTextPosition(Main.screen.width()/2-58, Main.screen.height()/2);
            Main.screen.setTextColor(0);
            Main.screen.print(Globals.SECTOR_CLEAR);
            
            Main.screen.setTextPosition(26, Main.screen.height()/2+16);
            Main.screen.print(Globals.PRESS_C_TRANSPORT);
            if(Button.C.justPressed()){
                Globals.SECTOR++;
                Game.changeState(new Shop());
            }
        }
        virusManager.render();

        //START update Blast
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        drawHud();
        
        //Need to draw the blast manager after the hud or else some items don't render 
        blastManager.render();
        
        Main.screen.flush();
    }
    
    
    /**
     * drawHud displays the top progress bar indicators for shield/threats/boss health 
     * as well as displaying the Score, currency and current ZONE:SECTOR.
     * 
     */
    void drawHud(){
        // Fill rects for the top and bottom sections
        Main.screen.fillRect(0, 0, Main.screen.width(), 16, 3);
        Main.screen.fillRect(0, Main.screen.height()-14, Main.screen.width(), 16, 3);
        
        //Bot Shield
        Main.screen.drawRect(6, 0, 80, 10, 0);
        Main.screen.fillRect(8, 2, 78, 8, 2);//background grey
        
        // bot shield
        shieldWidth = (int)(Globals.shield * 78 / 100);
        Main.screen.fillRect(8, 2, shieldWidth, 8, 15);
        
        //Threats or Boss Shield
        Main.screen.drawRect(134, 0, 80, 10, 0);
        Main.screen.fillRect(136, 2, 78, 8, 2);
        
        // threats health
        threatWidth = (int)(virusManager.getThreats() * 78 / virusManager.getTotalThreats());
        Main.screen.fillRect(214-threatWidth, 2, threatWidth, 8, 8);

        //Zone : SECTOR
        Main.screen.setTextPosition(98, 3);
        Main.screen.setTextColor(0);
        Main.screen.print(Globals.ZONE + ":" + Globals.SECTOR);
        
        //Score and Currency 
        Main.screen.setTextPosition(3, Main.screen.height()-12);
        Main.screen.print("Score: "+Globals.score);
        
        Main.screen.setTextPosition(140, Main.screen.height()-12);
        Main.screen.print("$$: " + Globals.currency);
    }

    void drawGrid(){
        Main.screen.drawRect(6, 16, Main.screen.width()-12, Main.screen.height()-32, 12, true);
    }
    
    public void shutdown(){
        virusManager = null;
        debrisManager = null;
        botManager = null;
        blastManager = null;
        itemDropManager = null;
    }
}