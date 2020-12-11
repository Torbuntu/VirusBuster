import entities.BlastObject;
import entities.VirusObject;
import femto.input.Button;
import femto.mode.HiRes16Color;

public class BlastManager {
    
    // refresh: 50 how quickly blasts refresh
    // rate:    1  how many blasts are active at a time
    int cooldown = 0, refresh, rate, charge;
    BlastObject[] blasts;
    float swordX = 0, swordY = 0;
    
    public BlastManager(){
        blasts = new BlastObject[]{
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject(),
            new BlastObject()
        };
        rate = Globals.saveManager.rate;
        refresh = Globals.saveManager.refresh;
        
        System.out.println("[I] - Blasts initialized");
    }
    
    void update(float x, float y, int dir){
        
        if(cooldown > 0){
            cooldown--;
        }
        for(int i = 0; i < rate; i++){
            blasts[i].update();
        }
        
        if(Button.B.isPressed()) return;//No shooting while
        if(Button.A.isPressed() && cooldown == 0){
            cooldown = refresh;
            for(int i = 0; i < rate; i++){
                if(!blasts[i].draw){
                    switch(dir){
                        case 0: blasts[i].init(-2.0f, 0.0f, x, y, charge == 50);break;//left
                        case 1: blasts[i].init(0.0f, -2.0f, x, y, charge == 50);break;//up
                        case 2: blasts[i].init(2.0f, 0.0f, x, y, charge == 50);break;//right
                        case 3: blasts[i].init(0.0f, 2.0f, x, y, charge == 50);break;//down
                    }
                    Globals.shots++;
                    break;
                }
            }
            // always set charge to 0
            charge = 0;
        }
        if(charge < 50) charge++;
    }
    
    public boolean hitEnemy(float ex, float ey, float er){
        for(BlastObject b : blasts){
            if(b.draw && Globals.boundingBox(b.getX()+1, b.getY()+1,6, ex, ey, er)){
                b.hit();
                Globals.hit++;
                return true;
            }
        }
        return false;
    }
    
    public boolean hitVirus(VirusObject virus){
        for(BlastObject b : blasts){
            if(b.draw && Globals.boundingBox(b.getX()+1, b.getY()+1,6, virus.getX(), virus.getY(), 16.0f)){
                virus.hit(b.charged ? 2 : 1);
                b.hit();
                Globals.hit++;
                return true;
            }
        }
        return false;
    }
    
    void render(HiRes16Color screen){
        screen.drawHLine(6, 13, (int)(cooldown * 78 / refresh), 8);
        screen.drawHLine(6, 15, (int)(charge * 78 / 50), 11);
        for(int i = 0; i < rate; i++){
            blasts[i].render(screen);
        }
    }

}

