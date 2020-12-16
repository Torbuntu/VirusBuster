import entities.BlastObject;
import entities.VirusObject;
import femto.input.Button;
import femto.mode.HiRes16Color;

import sprites.Blast;

public class BlastManager {
    
    Blast blast;
    // refresh: 50 how quickly blasts refresh
    // rate:    1  how many blasts are active at a time
    int cooldown = 0, refresh, rate, charge, chargeSize;
    BlastObject[] blasts;
    float swordX = 0, swordY = 0;
    
    public BlastManager(){
        blast = new Blast();
        blast.charge();
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
        chargeSize = Globals.saveManager.charge;
        
        System.out.println("[I] - Blasts initialized");
    }
    
    void tutorial(int ra, int re){
        rate = ra;
        refresh = re;
    }
    
    void update(float x, float y, int dir){
        
        if(cooldown > 0){
            cooldown--;
        }
        for(int i = 0; i < rate; i++){
            blasts[i].update();
        }
        
        if(Button.B.isPressed() && charge > 0){
            if(Button.Left.isPressed()) charge--;
            if(Button.Right.isPressed()) charge--;
            if(Button.Down.isPressed()) charge--;
            if(Button.Up.isPressed()) charge--;
            return;//No shooting while
        } 
        if(Button.A.isPressed() && cooldown == 0){
            cooldown = refresh;
            for(int i = 0; i < rate; i++){
                if(!blasts[i].draw){
                    switch(dir){
                        case 0: blasts[i].init(-2.0f, 0.0f, x, y, charge == chargeSize);break;//left
                        case 1: blasts[i].init(0.0f, -2.0f, x, y, charge == chargeSize);break;//up
                        case 2: blasts[i].init(2.0f, 0.0f, x, y, charge == chargeSize);break;//right
                        case 3: blasts[i].init(0.0f, 2.0f, x, y, charge == chargeSize);break;//down
                    }
                    Globals.shots++;
                    break;
                }
            }
            // always set charge to 0
            charge = 0;
        }
        if(charge < chargeSize) charge++;
    }
    
    public int hitEnemy(float ex, float ey, float er){
        for(BlastObject b : blasts){
            if(b.draw && Globals.boundingBox(b.getX()+1, b.getY()+1,6, ex, ey, er)){
                b.hit();
                Globals.hit++;
                return (b.charged ? Globals.saveManager.damage : 1);
            }
        }
        return 0;
    }
    
    void render(HiRes16Color screen){
        if(charge == chargeSize)blast.draw(screen, 78, 8);
        // screen.drawVLine(4, 160, -(int)(charge * 140 / 50), 11);
        // screen.drawVLine(2, 160, -(int)(cooldown * 140 / refresh), 8);
        // screen.drawHLine(6, 12, (int)(cooldown * 70 / refresh), 8);
        // screen.drawHLine(6, 13, (int)(charge * 70 / 50), 11);
        screen.fillRect(8, 12, (int)(charge * 68 / chargeSize), 2, 11);
        screen.drawHLine(8, 8, (int)(cooldown * 70 / refresh), 8);
        
        for(int i = 0; i < rate; i++){
            blasts[i].render(screen);
        }
    }

}

