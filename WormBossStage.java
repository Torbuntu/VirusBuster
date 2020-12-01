import managers.WormBossManager;

class WormBossTage extends State {
    WormBossManager wormManager;
    void init(){
        wormManager = new WormBossManager();
    }
    void update(){
        //TODO Bot and Blast management. 
        
        wormManager.update(blastManager, botManager.getX(), botManager.getY());
        if(wormManager.bodyCollidesWithBot(botManager.getX(), botManager.getY())
        || wormManager.headCollidesWithBot(botManager.getX(), botManager.getY())){
            // Move bot Y
            if(botManager.getY() + 32 < screen.height()-45) botManager.setY(botManager.getY()+32);
            else botManager.setY(botManager.getY()-32);
            // Move bot X
            if(botManager.getX() + 32 < screen.width()-45) botManager.setX(botManager.getX()+32);
            else botManager.setX(botManager.getX()-32);
            // Subtract shielding
            shield-=10;
        }
        
        wormManager.render();
        if(wormManager.cleared()){
            ROOM_STATUS = 1;
        }  
        
    }
    void shutdown(){
        wormManager = null;
    }
}