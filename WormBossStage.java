import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;

import managers.BotManager;
import managers.BlastManager;
import managers.WormBossManager;

class WormBossStage extends State {
    
    BotManager botManager;
    BlastManager blastManager;
    WormBossManager wormManager;
    
    void init(){
        botManager = new BotManager();
        blastManager = new BlastManager();
        wormManager = new WormBossManager();
        
        Mixer.init(8000);
        System.out.println("[I] - Worm Boss initialized");
    }
    void update(){
        Main.screen.clear(3);
        
        Globals.drawGrid();
        
        if(wormManager.cleared()){
            // CLEARED!
            Main.screen.setTextPosition(Main.screen.width()/2-58, Main.screen.height()/2);
            Main.screen.setTextColor(0);
            Main.screen.print(Globals.SECTOR_CLEAR);
            
            Main.screen.setTextPosition(26, Main.screen.height()/2+16);
            Main.screen.print(Globals.PRESS_C_TRANSPORT);
            if(Button.C.justPressed()){
                Globals.SECTOR++;
                Game.changeState(SectorZoneManager.getNextState());
            }
            Globals.ROOM_STATUS = 1;
        }  
        
        // Update
        botManager.updateBotMovement();
        wormManager.update(blastManager, botManager.getX(), botManager.getY());
        if(wormManager.bodyCollidesWithBot(botManager.getX(), botManager.getY())
        || wormManager.headCollidesWithBot(botManager.getX(), botManager.getY())){
            // Move bot Y
            if(botManager.getY() + 32 < Main.screen.height()-45) botManager.setY(botManager.getY()+32);
            else botManager.setY(botManager.getY()-32);
            // Move bot X
            if(botManager.getX() + 32 < Main.screen.width()-45) botManager.setX(botManager.getX()+32);
            else botManager.setX(botManager.getX()-32);
            // Subtract shielding
            Globals.shield-=10;
        }
        
        blastManager.update(botManager.getAttacking(), botManager.getX()+8, botManager.getY()+6, botManager.getDir());
        
        botManager.render();
        wormManager.render();
        
        Globals.drawHud((int)(wormManager.getCurrentHealth() * 78 / wormManager.getTotalHealth()));
        
        blastManager.render();

        Main.screen.flush();
    }
    
    void shutdown(){
        botManager = null;
        blastManager = null;
        wormManager = null;
    }
}