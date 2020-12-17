import femto.Game;
import femto.State;
import stage.SummaryStage;
import stage.MiniBossStage;
import stage.WormBossStage;
import stage.GrabbyMcStage;
import stage.ForkBombStage;

/**
 * Manages the switching of zones and sectors using the Globals ZONE and SECTOR
 * When appropriate will send the player to the boss scenes
 * Otherwise sends them to the correct NormalSector
 */
class SectorZoneManager {
    static State getNextState(){

        // There are no sectors passed 8, send to title.
        if(Globals.SECTOR > 8) return new SummaryStage();
        
        // Sector 4 is always mini boss stage.
        if(Globals.SECTOR == 4) return new MiniBossStage();
        if(Globals.SECTOR == 8){
            System.out.print("[I] Initiate Mega Boss Stage: ");
            switch(Globals.ZONE){
                case 1: 
                    System.out.println("WormBoss");
                    return new WormBossStage(); 
                    break;
                case 2: 
                    System.out.println("ForkBomb");
                    return new ForkBombStage(); 
                    break;
                case 3: 
                    System.out.println("GrabbyMc");
                    return new GrabbyMcStage(); 
                    break;
            }
        }
        
        // There are no sectors beyond 4 on ZONE 0
        if(Globals.ZONE == 0 && Globals.SECTOR > 4) return new SummaryStage();
        
        // Default return NormalSector. VirusManager will be initialized with the Globals.SECTOR variable.
        return new NormalSector();
    }
}