import femto.mode.HiRes16Color;
import sprites.BossBlast;
class BossBlastObject {
    BossBlast blast = new BossBlast();
    float x, y, dx, dy;
    boolean active = false;
    
    void init(float dx, float dy, float x, float y){
        this.dx = dx;
        this.dy = dy;
        this.x = x;
        this.y = y;
        blast.fire();
        active = true;
    }
    
    void update(float ex, float ey){
        if(!active)return;

        if(Globals.checkHitBot(x, y, 16, ex, ey, 12)){
            //player hit!
            active = false;
            Globals.shield -= 15;
        }
        
        x += dx;
        y += dy;
        if(x > 220 || x < 0 || y < 16 || y > 170)active = false;
    }
    
    //TODO: Make a blast sprite?
    void render(HiRes16Color screen){
        if(active) blast.draw(screen, x, y);
    }

}