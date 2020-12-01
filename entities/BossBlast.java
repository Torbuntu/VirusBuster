class BossBlast {
    float x, y, dx, dy;
    boolean active = false;
    
    void init(float dx, float dy, float x, float y){
        this.dx = dx;
        this.dy = dy;
        this.x = x;
        this.y = y;
        active = true;
    }
    
    void update(float ex, float ey){
        if(!active)return;

        if(active && Globals.circle(x, y, ex, ey, 8, 4)){
            //player hit!
            active = false;
            Globals.shield -= 15;
        }
        
        x += dx;
        y += dy;
        if(x > 220 || x < 0 || y < 0 || y > 170)active = false;
    }
    
    //TODO: Make a blast sprite?
    void render(){
        if(active)
            Main.screen.drawCircle(x, y, 8, 8, false);
    }
    
    boolean isActive(){
        return active;
    }
    
}