import femto.mode.HiRes16Color;

import managers.BlastManager;
import managers.BotManager;

import audio.Explode;

import sprites.WormBoss;
import sprites.WormBody;
import sprites.BossBlast;

import entities.WormBodyObject;

public class WormBossManager {
    BossBlast blast;
    WormBoss head;
    WormBodyObject[] body;
    Explode explode;
    int health = 25;
    int sx = 1, sy = 0, shoot = 150, dying = 150, turnX, turnY, dir;
    boolean angry = false, alive = true, met = false;
    float speed = 1;
    float blastX = -10, blastY = -10, bvx = 0, bvy = 0;
    
    WormBossManager(){
        explode = new Explode(1);
        head = new WormBoss();
        head.x = -32;
        head.y = 120;
        head.hori();
        
        dir = 1; // init going right
        
        turnX = Math.random(50, 140);
        turnY = 120;//same as start head.y
        
        blast = new BossBlast();
        blast.fire();
        
        // We initialize at id 1 so as to begin just after the head.
        body = new WormBodyObject[]{
            new WormBodyObject(1, false),
            new WormBodyObject(2, false),
            new WormBodyObject(3, false),
            new WormBodyObject(4, false),
            new WormBodyObject(5, false),
            new WormBodyObject(6, false),
            new WormBodyObject(7, false),
            new WormBodyObject(8, true),
        };
        for(WormBodyObject b : body){
            b.initDirection(dir, sx, sy, head.x, head.y, turnX, turnY);
        }
    }
    
    private void setDir(){
        sx = 0;
        sy = 0;
        met = false;//have not met the turn dir
        dir = Math.random(0, 4);
        switch(dir){
            case 0://going left
                head.hori();
                head.x = 220;
                sx = -1;
                head.setMirrored(true);
                head.y = Math.random(16, 136);
                turnY = (int)head.y;
                turnX = Math.random(50, 180);
                break;
            case 1://going right
                head.hori();
                head.x = -32;
                sx = 1;
                head.setMirrored(false);
                head.y = Math.random(16, 136);
                turnY = (int)head.y;
                turnX = Math.random(50, 180);
                break;
                
            case 2://going Up
                head.vert();
                head.y = 176;
                sy = -1;
                head.setFlipped(false);
                head.x = Math.random(8, 180);
                turnX = (int)head.x;
                turnY = Math.random(50, 136);
                break;
            case 3://going down
                head.vert();
                head.y = -32;
                sy = 1;
                head.setFlipped(true);
                head.x = Math.random(8, 180);
                turnX = (int)head.x;
                turnY = Math.random(50, 136);
                break;
        }
        for(WormBodyObject b : body){
            b.initDirection(dir, sx, sy, head.x, head.y, turnX, turnY);
        }
    }
    
    boolean bodyDestroyed(){
        for(WormBodyObject b : body){
            if(b.alive){
                return false;
            }
        }
        angry = true;
        return true;
    }
    
    boolean headCollidesWithBot(float bx, float by){
        return Globals.boundingBox(head.x, head.y, 28, bx, by, 16);
    }
    
    void moveHurtBot(BotManager botManager){
        //I did not like the knock back, so it is gone.
        Globals.shield -= 10;
        Globals.hurt = 50;
    }

    void update(BlastManager blastManager, BotManager botManager){
        if(!alive)return;
        if(bodyDestroyed()){
            speed = 1.4;
            int damage = blastManager.hitEnemy(head.x, head.y, 32);
            if(damage > 0){
                health-=damage;
                if(health <= 0){
                    explode.play();
                    alive = false;
                }
            }
            
            shoot--;
            if(shoot <= 0){
                shoot = 200;
                blastX = head.x + 18;
                blastY = head.y + 18;
                if(botManager.getX() > blastX){
                    bvx = 1;
                }else{
                    bvx = -1;
                }
                if(botManager.getY() > blastY){
                    bvy = 1;
                }else{
                    bvy = -1;
                }
            }
            blastX += bvx;
            blastY += bvy;
            if(Globals.boundingBox(blastX, blastY, 8, botManager.getX(), botManager.getY(), 16)){
                blastX = -10;
                blastY = -10;
                moveHurtBot(botManager);
            }
        }else{
           for(WormBodyObject b : body){
                if(b.alive && Globals.hurt == 0 && Globals.boundingBox(b.body.x+2, b.body.y+2, 14, botManager.getX(), botManager.getY(), 16)){
                    moveHurtBot(botManager);
                }
                int damage = blastManager.hitEnemy(b.body.x, b.body.y, 18.0f);
                if(damage > 0){
                    if(b.last && b.alive){
                        b.hit(damage);
                        // Health can go lower than 0 with charged blast
                        if(b.health <= 0){
                            b.last = false;
                            explode.play();
                            if(b.id > 1){
                                //id's are 1 indexed, so we need to subtract 2 in order to get the correct index based on the id offset
                                body[b.id-2].last = true;
                            }
                        }
                    }
                }
            } 
        }
        if(Globals.hurt == 0 && Globals.boundingBox(head.x, head.y, 28, botManager.getX(), botManager.getY(), 16)){
            moveHurtBot(botManager);
        }
        if(health > 0){
            if(head.x > 400 || head.x < -200 || head.y > 400 || head.y < -200){
                setDir();
                
            }
            head.y += sy * speed;
            head.x += sx * speed;
            
            if(!met) checkHeadTurn();
            
            for(WormBodyObject b : body){
                b.update(dir);
            }
        }
    }
    
    void checkHeadTurn(){
        // time to turn the head
        if((int)head.x == turnX && (int)head.y == turnY){
            met = true;
            setTurnDir();
        }
    }
    
    /**
     * Similar to setDir() except it does not reposition the x,y nor adjust the turnX,turnY 
     * 
     */
    private void setTurnDir(){
        sx = 0;
        sy = 0;
        if(dir == 0 || dir == 1) dir = Math.random(0, 2) == 1 ? 2 : 3;
        else dir = Math.random(0,2) == 1 ? 0 : 1;
        
        switch(dir){
            case 0://going left
                head.hori();
                sx = -1;
                head.setMirrored(true);
                break;
            case 1://going right
                head.hori();
                sx = 1;
                head.setMirrored(false);
                break;
                
            case 2://going Up
                head.vert();
                sy = -1;
                head.setFlipped(false);
                break;
            case 3://going down
                head.vert();
                sy = 1;
                head.setFlipped(true);
                break;
        }
    }
    
    void render(HiRes16Color screen){
        // Don't render if dead.
        if(dying == 0)return;
        if(bodyDestroyed()){
            blast.draw(screen, blastX, blastY);
        }

        for(WormBodyObject b : body){
            b.render(screen);
        }
        if(!alive && dying > 0){
            head.setFlipped(false);
            head.setMirrored(false);
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
            head.die();
        }
        head.draw(screen);
    }
    
    int getCurrentHealth(){
        int total = health;
        for(WormBodyObject b : body){
            total += b.getHealth();
        }
        return total;
    }
    
    int getTotalHealth(){
        return 145;//health + 8 * 15
    }
    
    // ensure we draw the full enemy death animation
    public boolean cleared(){
        return dying == 0; 
    }
}