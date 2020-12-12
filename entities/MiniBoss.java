import sprites.SmallBoss;
import managers.BlastManager;
import audio.Explode;
import entities.BossBlast;

public class MiniBoss{
    Explode explode;
    BossBlast[] blasts;
    SmallBoss virus;
        
    int hurt = 0;
    float dx, dy, sx = 0, sy = 0;//speed variables

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
            if(virus.x+32 > Globals.screen.width()){
                sx = -2.5f;
            }
            if(virus.x < 0){
                sx = 2.5f;
            }
            if(virus.y+32 > Globals.screen.height()){
                sy = -2.5f;
            }
            if(virus.y < 0){
                sy = 2.5f;
            }
            virus.x += sx;
            virus.y += sy;
            if(Globals.checkHitBot(virus.x+4, virus.y+4, 24, bx+1, by+1, 14))Globals.shield-=20;
            return;
        }
        if(alive){
            for(BossBlast b : blasts){
                if(b.active) b.update(bx, by);
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
            
            moveBoss(bx+8, by+8);
            
            checkBlastHits(blastManager);
            
            if(hurt > 0){
                hurt--;
                if(hurt % 5 == 0){
                    virus.setMirrored(true);
                }else{
                    virus.setMirrored(false);
                }
            } 
            if(health < maxHealth/2){
                if(!damaged){
                    damaged = true;
                    berserk = 500;
                    sx = 2.5f;
                    sy = 2.5f;
                }
            }
            virus.x += sx;
            virus.y += sy;
            if(Globals.checkHitBot(virus.x+4, virus.y+4, 24, bx+1, by+1, 14))Globals.shield-=10;
        }
    }
    
    void moveBoss(float bx, float by){
        //START Move Virus
        sx = 0;
        sy = 0;
        
        // Calculate the absolute distances between the x/y coordinates. Virus moves closer by whichever is further.
        dx = Math.abs(virus.x - bx);
        dy = Math.abs(virus.y - by);
        
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
            if(hurt==0) virus.bite();
        }else{
            if(hurt==0) virus.walk();
        }
    }
    
    public void checkBlastHits(BlastManager blastManager){
        //Globals.screen.drawRect(virus.x+2, virus.y+2, 28, 28, 8, true);
        if(alive){
            int damage = blastManager.hitEnemy(virus.x+2, virus.y+2, 28.0f);
            if(damage > 0) hit(damage);
        }
    }
    
    void render(){
        if(berserk > 0){
            virus.bite();
            virus.draw(Globals.screen);
            return;
        }
        if(alive){
            virus.draw(Globals.screen);
            
            for(BossBlast b : blasts){
                if(b.active) b.render();
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
            virus.draw(Globals.screen);
        }
    }
    
    void hit(int damage){
        health -= damage;
        if(hurt==0){
            switch(Math.random(0, 4)){
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
            if(b.active)return;
        }
        shooting = 25;
        shootReady = 250;

        blasts[0].init(-2, 0, virus.x+16, virus.y+16);
        blasts[1].init(2, 0, virus.x+16, virus.y+16);
        blasts[2].init(0, 2, virus.x+16, virus.y+16);
        blasts[3].init(0, -2, virus.x+16, virus.y+16);
    }

    // ensure we draw the full enemy death animation
    public boolean getAlive(){
        return dying > 0; 
    }
}
