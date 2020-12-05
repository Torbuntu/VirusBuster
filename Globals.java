import femto.mode.HiRes16Color;
import femto.palette.UltimaViSharpX68000;
import femto.font.FontC64;

/**
 * Globals contains all of the globally used variables as well as 
 * a couple of useful helper methods.
 * 
 */
class Globals {
    
    static final SaveManager saveManager = new SaveManager();
    
    static final HiRes16Color screen = new HiRes16Color(UltimaViSharpX68000.palette(), FontC64.font());
    
    static int ZONE = 0, SECTOR = 0, ROOM_STATUS = 0;
    static int score = 0, currency = 0, kills = 0;
    //rate:1, refresh:50
    static int shield = 100, rate = 10, refresh = 5, hit = 0, shots = 0;
    static boolean createItemDrop = false;
    static float itemX = 0, itemY = 0;
        
    static final String PRESS_C_TRANSPORT = "Press C to transport";
    static final String SECTOR_CLEAR = "Sector Cleared";
    
    //Deprecated(potentially slow)
    public static boolean circle(float x1, float y1, float x2, float y2, float r1, float r2){
        int vx = (int)(x1 - x2);
        int vy = (int)(y1 - y2);
        int vr = (int)(r1 + r2);
        return ((vx) * (vx) + (vy) * (vy)) < (vr) * (vr);
    }
    
    /**
    * Takes two square objects and checks if they intersect, given x,y and size
    * The boxing shape is always a square
    */
    public static boolean boundingBox(float x1, float y1, float s1, float x2, float y2, float s2){
        return (x1 < x2 + s2 && x1 + s1 > x2 && y1 < y2 + s2 && y1 + s1 > y2);
    }
    
    /**
     * When an enemy is destroyed, this is called to create an item drop and to update variables
     */ 
    public static void updateKills(float x, float y){
        kills++;
        score += 10;
        itemX = x;
        itemY = y;
        createItemDrop = true;
    }
    
    public static void reset(){
        kills = 0;
        score = 0;
        shield = 100;
        ROOM_STATUS = 0;
        SECTOR = 0;
        ZONE = 0;
    }
    
    /**
     * When a ZONE is cleared, we calculate the accuracy to display on the results view.
     * This will also be used to determine acheivments
     */ 
    public int getAccuracy() {
        if(hit == 0)return 0;
        if(shots == 0) return 0;
        return Math.abs(hit * 100 / shots);
    }
    
    /**
     * drawHud displays the top progress bar indicators for shield/threats/boss health 
     * as well as displaying the Score, currency and current ZONE:SECTOR.
     * 
     */
    static void drawHud(int threatWidth){
        // Fill rects for the top and bottom sections
        screen.fillRect(0, 0, screen.width(), 16, 3);
        screen.fillRect(0, screen.height()-14, screen.width(), 16, 3);
        
        //Bot Shield
        screen.drawRect(6, 0, 80, 10, 0);
        screen.fillRect(8, 2, 78, 8, 2);//background grey
        
        // bot shield
        screen.fillRect(8, 2, (int)(Globals.shield * 78 / 100), 8, 15);
        
        //Threats or Boss Shield
        screen.drawRect(134, 0, 80, 10, 0);
        screen.fillRect(136, 2, 78, 8, 2);
        
        // threats health
        screen.fillRect(214-threatWidth, 2, threatWidth, 8, 8);

        //Zone : SECTOR
        screen.setTextPosition(98, 3);
        screen.setTextColor(0);
        screen.print(Globals.ZONE + ":" + Globals.SECTOR);
        
        //Score and Currency 
        screen.setTextPosition(3, screen.height()-12);
        screen.print("Score: "+Globals.score);
        
        screen.setTextPosition(140, screen.height()-12);
        screen.print("$$: " + Globals.currency);
    }

    static void drawGrid(){
        screen.drawRect(6, 16, screen.width()-12, screen.height()-32, 12, true);
    }
    
}