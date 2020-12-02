import femto.Game;
import femto.State;

/**
 * Manages the switching of zones and sectors using the Globals ZONE and SECTOR
 * When appropriate will send the player to the boss scenes
 * Otherwise sends them to the correct NormalSector
 */
class SectorZoneManager {
    static State getNextState(){
        // Sector 4 is always mini boss stage.
        if(Globals.SECTOR == 4){
            //send to miniboss scene
            return new MiniBossStage();
        }
        
        switch(Globals.ZONE){
            case 0:
                if(Globals.SECTOR > 4){
                    return new TitleScene();
                }
                break;
            case 1:
                if(Globals.SECTOR == 8){
                    return new WormBossStage();
                }
                break;
            case 2:
                if(Globals.SECTOR == 8){
                    return new ForkBombStage();
                }
                break;
            case 3:
                if(Globals.SECTOR == 8){
                    return new GrabbyMcStage();
                }
                break;
            default:
                if(Globals.SECTOR > 8) return new TitleScene();
                break;
        }
        // Default return NormalSector. VirusManager will be initialized with the Globals.SECTOR variable.
        return new NormalSector();
    }
}