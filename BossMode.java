import sprites.SmallBoss;
import managers.BlastManager;

public class BossMode{
    BossBlast[] blasts;
    int bossType = 0, hurt = 0;
    float sx = 0, sy = 0;//speed variables
    SmallBoss virus;
    int health, maxHealth;
    int shooting = 0, shootReady = 250;
    
    
    boolean alive = true;
    
    public BossMode(int t){
        virus = new SmallBoss();
        
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
                    new BossBlast()
                };
                break;
        }
        
    }
    
    void update(float bx, float by){
        if(alive){
            shootReady--;
            if(shootReady==0){
                checkActiveBlasts();
            }
            shooting--;
            if(shooting > 0){
                virus.bite();
                return;
            }
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
            if(bx >= (virus.x - 50) && bx <= (virus.x + 50) && by >= (virus.y - 50) && by <= (virus.y + 50) ){
                if(bx >= (virus.x - 50) && bx <= virus.x){
                    virus.setMirrored(true);
                }
                if(bx <= (virus.x + 50) && bx >= virus.x){
                    virus.setMirrored(false);
                }
                if(hurt==0)
                    virus.bite();
            }else{
                if(hurt==0)
                    virus.walk();
            }
            virus.x += sx;
            virus.y += sy;
            if(hurt > 0){
                hurt--;
                if(hurt % 5 == 0){
                    virus.setMirrored(true);
                }else{
                    virus.setMirrored(false);
                }
            }
            for(BossBlast b : blasts){
                if(b.isActive()) b.update(bx, by);
            }
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
            int barWidth = (int)(health * virus.width() / maxHealth);
            Main.screen.drawHLine((int)virus.x, (int)virus.y-4, barWidth, 8);
            virus.draw(Main.screen);
            
            for(BossBlast b : blasts){
                if(b.isActive()) b.render();
            }
        }else{
            
        }
    }
    
    void hit(int damage){
        health -= damage;
        if(hurt==0){
            int hurtType = Math.random(0, 4);
            switch(hurtType){
                case 0:
                    virus.hurtA();
                    break;
                case 1:
                    virus.hurtB();
                    break;
                case 2:
                    virus.hurtC();
                    break;
                case 3:
                    virus.hurtD();
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
        float bx = x-(ex+8);
        float by = y-(ey+8);
        float r = 8+4;
        if(active && Math.abs((bx) * (bx) + (by) * (by)) < (r) * (r)){
            //player hit!
            active = false;
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