import sprites.SmallBoss;
import managers.BlastManager;

public class BossMode{
    BossBlast[] blasts;
    int bossType = 0, hurt = 0;
    float sx = 0, sy = 0;//speed variables
    SmallBoss virus;
    int health, maxHealth;
    int shooting = 0, shootReady = 250, dying = 150;
    
    boolean alive = true;
    
    public void init(int t){
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
        bossType = t;
        switch(bossType){
            case 0:
            default:
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
                break;
        }
        
    }
    
    void update(BlastManager blastManager, float bx, float by){
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
            }else{
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
            if(!alive){
                switch(bossType){
                    case 0:
                        //spawn viruses
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    void render(){
        if(alive){
            switch(bossType){
                case 0:
                    virus.draw(Main.screen);
                    break;
            }
            
            for(BossBlast b : blasts){
                if(b.isActive()) b.render();
            }
        }else if(dying > 0){
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
        switch(bossType){
            case 0:
                blasts[0].init(-2, 0, virus.x+16, virus.y+16);
                blasts[1].init(2, 0, virus.x+16, virus.y+16);
                blasts[2].init(0, 2, virus.x+16, virus.y+16);
                blasts[3].init(0, -2, virus.x+16, virus.y+16);
                break;
            default:
            break;
        }
    }
    
    public float getX(){
        switch(bossType){
            case 0:
                return virus.x;
            default:
                return 0;
        }
    }
    
    public float getY(){
        switch(bossType){
            case 0:
                return virus.y;
            default:
                return 0;
        }
    }
    
    public void incX(float x){
        switch(bossType){
            case 0:
                virus.x += x;
                break;
        }
    }
    public void incY(float y){
        switch(bossType){
            case 0:
                virus.y += y;
                break;
        }
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
        switch(bossType){
            case 0:
                virus.hurtA();
                break;
            default:
            break;
        }
    }
    void hurtB(){
        switch(bossType){
            case 0:
                virus.hurtB();
                break;
            default:
            break;
        }
    }
    void hurtC(){
        switch(bossType){
            case 0:
                virus.hurtC();
                break;
            default:
            break;
        }
    }
    void hurtD(){
        switch(bossType){
            case 0:
                virus.hurtD();
                break;
            default:
            break;
        }
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

        if(active && Main.checkCollides(x, y, ex, ey, 8, 4)){
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