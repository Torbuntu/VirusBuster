import managers.BotManager;
import managers.BlastManager;
import managers.DebrisManager;
import managers.VirusManager;
import managers.ItemDropManager;

/**
 * Used to manage Entities, such as global entities that are present across the game
 * and for clearing for space.
 */ 
class EntityManager {
    static boolean normalInitialized = false;
    static BotManager botManager;
    static BlastManager blastManager;
    static DebrisManager debrisManager;
    static VirusManager virusManager;
    static ItemDropManager itemDropManager;
    
    /// These are consistent in every stage 
    static void initializeGlobalEntities(){
        botManager = new BotManager();
        blastManager = new BlastManager();
    }
    
    /// Prepare for normal stages.
    static void initializeNormalStage(){
        if(!normalInitialized){
            normalInitialized = true;
            virusManager = new VirusManager();
            debrisManager = new DebrisManager();
            itemDropManager = new ItemDropManager();
        }
    }
    
    /// Called prior to a final boss stage to make some sort of space. 
    static void clearNormalStage(){
        virusManager = null;
        debrisManager = null;
        itemDropManager = null;
        normalInitialized = false;
    }
}