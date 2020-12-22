import femto.mode.HiRes16Color;
import sprites.SmallBoss;
import managers.BlastManager;
import audio.Explode;
import entities.BossBlastObject;

import sprites.BlastCharge;

public class MiniBoss{
    Explode explode;
    BossBlastObject[] blasts;
    SmallBoss virus;
    BlastCharge charge;
        
    int hurt = 0;
    float dx, dy, sx = 0, sy = 0, speed = 0.5f;//speed variables

    int health, maxHealth;
    int shooting = 0, shootReady = 250, dying = 150, berserk = 0;
    boolean damaged = false;
    
    boolean alive = true;
    
    public MiniBoss(){
        explode = new Explode(1);
        virus = new SmallBoss();
        switch(Math.random(0, 4)){
            case 0:
                virus.x = 0;
                virus.y = 80;
            break;
            case 1:
                virus.x = 100;
                virus.y = 0;
            break;
            case 2:
                virus.x = 220;
                virus.y = 80;
            break;
            case 3:
                virus.x = 100;
                virus.y = 160;
            break;
        }
        
        if(Globals.ZONE == 0 && !Globals.endless){
            health = 25;
        }else health = 50 + Globals.ZONE*10;
        maxHealth = health;
        shootReady = 250;
        blasts = new BossBlastObject[]{
            new BossBlastObject(),
            new BossBlastObject(),
            new BossBlastObject(),
            new BossBlastObject(),
            new BossBlastObject(),
            new BossBlastObject(),
            new BossBlastObject(),
            new BossBlastObject()
        };
        charge = new BlastCharge();
        charge.charge();

    }
    
    void update(BlastManager blastManager, float bx, float by){
        if(berserk > 0){
            berserk--;
            if(virus.x+32 > 214){
                sx = -1.5f;
            }
            if(virus.x < 4){
                sx = 1.5f;
            }
            if(virus.y+32 > 160){
                sy = -1.5f;
            }
            if(virus.y < 16){
                sy = 1.5f;
            }
            virus.x += sx;
            virus.y += sy;
            if(Globals.checkHitBot(virus.x+4, virus.y+4, 24, bx+1, by+1, 12))Globals.shield-=20;
            return;
        }
        if(alive){
            for(BossBlastObject b : blasts){
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
                if(damaged){
                    if(sx > 0)sx = 0.2f;
                    else if(sx < 0) sx = -0.2f;
                    if(sy > 0) sy = 0.2f;
                    else if(sy < 0) sy = -0.2f;
                }else{
                    sx = 0;
                    sy = 0;
                }
            } 
            if(health < maxHealth/2){
                if(!damaged){
                    damaged = true;
                    berserk = 500;
                    sx = 2.5f;
                    sy = 2.5f;
                    speed = 0.75f;
                }
            }
            if(virus.x+32 > 214){
                sx = -1.5f;
            }
            if(virus.x < 4){
                sx = 1.5f;
            }
            if(virus.y+32 > 160){
                sy = -1.5f;
            }
            if(virus.y < 16){
                sy = 1.5f;
            }
            virus.x += sx;
            virus.y += sy;
            if(Globals.checkHitBot(virus.x+4, virus.y+4, 24, bx, by, 12))Globals.shield-=10;
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
                sx = speed;
            }
            if(virus.x > bx){
                sx = -speed;
            }
        }else{
            if(virus.y > by){
                sy = -speed;
            }
            if(virus.y < by){
                sy = speed;
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
        if(alive){
            int damage = blastManager.hitEnemy(virus.x+2, virus.y+2, 28.0f);
            if(damage > 0) hit(damage);
        }
    }
    
    void render(HiRes16Color screen){
        if(berserk > 0){
            virus.bite();
            virus.draw(screen);
            return;
        }
        if(alive){
            virus.draw(screen);
            
            for(BossBlastObject b : blasts){
                if(b.active) b.render(screen);
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
            virus.draw(screen);
        }
        
        
        int pos = Math.abs((int)((shootReady-250) * 68 / 250));
        screen.fillRect(214-pos, 12, pos, 2, 8);
        if(shooting > 0){
            charge.draw(screen, 134, 8);
        }
    }
    
    void hit(int damage){
        // If boss is damaged, take any and all damage from blast
        if(!damaged && hurt > 0)return;
        shootReady+=2;
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
        if(damaged)hurt = 50;
        else hurt = 25;
        if(health <= 0){
            alive = false;
        }
    }
    
    void checkActiveBlasts(){
        for(BossBlastObject b : blasts){
            if(b.active)return;
        }
        shooting = 40;
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
    
    public boolean cleared(){
        return dying == 0;
    }
}
