import sprites.SmallBoss;
import managers.BlastManager;
import audio.Explode;

public class BossMode{
    Explode explode;
    BossBlast[] blasts;
    int hurt = 0;
    float sx = 0, sy = 0;//speed variables
    SmallBoss virus;
    int health, maxHealth;
    int shooting = 0, shootReady = 250, dying = 150, berserk = 0;
    boolean damaged = false;
    
    boolean alive = true;
    
    public void init(){
        explode = new Explode(1);
        virus = new SmallBoss();
        int dir = Math.random(0, 4);
        if(dir == 0){
            virus.x = 0;
            virus.y = 80;
        }
        if(dir == 1){
            virus.x = 100;
            virus.y = 0;
        }
        if(dir == 2){
            virus.x = 220;
            virus.y = 80;
        }
        if(dir == 3){
            virus.x = 100;
            virus.y = 160;
        }
 
        health = 50;
        maxHealth = health;
        shootReady = 250;
        blasts = new BossBlast[]{
            new BossBlast(),
            new BossBlast(),
            new BossBlast(),
            new BossBlast(),
            new BossBlast(),
            new BossBlast(),
            new BossBlast(),
            new BossBlast()
        };

    }
    
    void update(BlastManager blastManager, float bx, float by){
        if(berserk > 0){
            berserk--;
            if(virus.x+32 > Main.screen.width()){
                sx = -2.5f;
            }
            if(virus.x < 0){
                sx = 2.5f;
            }
            if(virus.y+32 > Main.screen.height()){
                sy = -2.5f;
            }
            if(virus.y < 0){
                sy = 2.5f;
            }
            virus.x += sx;
            virus.y += sy;
            return;
        }
        if(alive){
            for(BossBlast b : blasts){
                if(b.isActive()) b.update(bx, by);
            }
            
            shootReady--;
            if(shootReady==0){
                checkActiveBlasts();
            }
            shooting--;
            if(shooting > 0){
                virus.bite();
                return;
            }
            
            moveBoss(bx, by);
            
            checkBlastHits(blastManager);
            
            if(hurt > 0){
                hurt--;
                if(hurt % 5 == 0){
                    virus.setMirrored(true);
                }else{
                    virus.setMirrored(false);
                }
            } else {
                virus.x += sx;
                virus.y += sy;
            }
            if(getHealth() < getMaxHealth()/2){
                if(!damaged){
                    damaged = true;
                    berserk = 500;
                    sx = 2.5f;
                    sy = 2.5f;
                }
                virus.x += sx;
                virus.y += sy;
            }
        }
    }
    
    void moveBoss(float bx, float by){
        //START Move Virus
        sx = 0;
        sy = 0;
        
        // Calculate the absolute distances between the x/y coordinates. Virus moves closer by whichever is further.
        float dx = Math.abs(virus.x - bx);
        float dy = Math.abs(virus.y - by);
        
        if(dx > dy){
            if(virus.x < bx){
                sx = 0.5f;
            }
            if(virus.x > bx){
                sx = -0.5f;
            }
        }else{
            if(virus.y > by){
                sy = -0.5f;
            }
            if(virus.y < by){
                sy = 0.5f;
            }
        }
        
        //check if close
        if(bx >= (virus.x - 50) && bx <= (virus.x + 80) && by >= (virus.y - 50) && by <= (virus.y + 80) ){
            if(bx >= (virus.x - 50) && bx <= virus.x){
                virus.setMirrored(true);
            }
            if(bx <= (virus.x + 80) && bx >= virus.x){
                virus.setMirrored(false);
            }
            if(hurt==0)
                virus.bite();
        }else{
            if(hurt==0)
                virus.walk();
        }
    }
    
    public void checkBlastHits(BlastManager blastManager){
        if(alive && blastManager.hitEnemy(virus.x+16, virus.y+16, 14.0f)){
            hit(1);
        }
    }
    
    void render(){
        if(berserk > 0){
            virus.bite();
            virus.draw(Main.screen);
            return;
        }
        if(alive){
            virus.draw(Main.screen);
            
            for(BossBlast b : blasts){
                if(b.isActive()) b.render();
            }
        }else if(dying > 0){
            switch(dying){
                case 150:
                    explode.play();
                    break;
                case 100:
                    explode.play();
                    break;
                case 50:
                    explode.play();
                    break;
                case 1:
                    explode.play();
                    break;
            }
            dying--;
            virus.die();
            virus.draw(Main.screen);
        }
    }
    
    void hit(int damage){
        health -= damage;
        if(hurt==0){
            int hurtType = Math.random(0, 4);
            switch(hurtType){
                case 0:
                    hurtA();
                    break;
                case 1:
                    hurtB();
                    break;
                case 2:
                    hurtC();
                    break;
                case 3:
                    hurtD();
                    break;
            }
        }
        hurt = 25;
        if(health <= 0){
            alive = false;
        }
    }
    
    void checkActiveBlasts(){
        for(BossBlast b : blasts){
            if(b.isActive()){
                return;
            }
        }
        shooting = 25;
        shootReady = 250;

        blasts[0].init(-2, 0, virus.x+16, virus.y+16);
        blasts[1].init(2, 0, virus.x+16, virus.y+16);
        blasts[2].init(0, 2, virus.x+16, virus.y+16);
        blasts[3].init(0, -2, virus.x+16, virus.y+16);
    }
    
    public float getX(){
        return virus.x;
    }
    
    public float getY(){
        return virus.y;
    }
    
    public void incX(float x){
        virus.x += x;
    }
    public void incY(float y){
        virus.y += y;
    }
    
    public void setSpeedX(float x){
        sx = x;
    }
    public void setSpeedY(float y){
        sy = y;
    }
    
    public int getMaxHealth(){
        return maxHealth;
    }
    
    public int getHealth(){
        return health;
    }
    
    public boolean getAlive(){
        return dying > 0; // ensure we draw the full enemy death animation
    }
    
    
    void bite(){}
    void walk(){}
    void die(){}
    void hurtA(){
        virus.hurtA();
    }
    void hurtB(){
        virus.hurtB();
    }
    void hurtC(){
        virus.hurtC();
    }
    void hurtD(){
        virus.hurtD();
    }
}

class BossBlast{
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

        if(active && Main.circle(x, y, ex, ey, 8, 4)){
            //player hit!
            active = false;
            Main.shield -= 5;
        }
        
        x += dx;
        y += dy;
        if(x > 220 || x < 0 || y < 0 || y > 170)active = false;
    }
    
    void render(){
        if(active)
            Main.screen.drawCircle(x, y, 8, 8, false);
    }
    
    boolean isActive(){
        return active;
    }
    
}