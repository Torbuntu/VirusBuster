import femto.Game;
import femto.State;

/**
 * Manages the switching of zones and sectors using the Globals ZONE and SECTOR
 * When appropriate will send the player to the boss scenes
 * Otherwise sends them to the correct NormalSector
 */
class SectorZoneManager {
    static State getNextState(){
        System.out.println("Sector: " + Globals.SECTOR);
        
        // Sector 4 is always mini boss stage.
        if(Globals.SECTOR == 4) return new MiniBossStage();
        // There are no sectors passed 8, send to title.
        if(Globals.SECTOR > 8) return new TitleScene();
        
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
        }
        
        // Default return NormalSector. VirusManager will be initialized with the Globals.SECTOR variable.
        return new NormalSector();
    }
}