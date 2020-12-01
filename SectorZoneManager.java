import femto.Game;
import femto.State;

/**
 * Manages the switching of zones and sectors using the Globals ZONE and SECTOR
 * When appropriate will send the player to the boss scenes
 * Otherwise sends them to the correct NormalSector
 */
class SectorZoneManager {
    static State getNextState(){
        switch(Globals.ZONE){
            case 0:
                if(Globals.SECTOR == 4){
                    //send to miniboss scene
                    return new MiniBossStage();
                }
                if(Globals.SECTOR > 4){
                    return new TitleScene();
                }
                return new NormalSector();
                break;
            case 1:
                
                break;
            case 2:
                
                break;
            case 3:
                
                break;
        }
    }
}