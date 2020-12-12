import managers.BlastManager;
import managers.BotManager;

import audio.Explode;

import sprites.WormBoss;
import sprites.WormBody;

public class WormBossManager {
    WormBoss sprite;
    Body[] body;
    Explode explode;
    int health = 25;
    int sx = 1, sy = 0, shoot = 150, dying = 150;
    boolean angry = false, alive = true;
    float speed = 1;
    float blastX = -10, blastY = -10, bvx = 0, bvy = 0;
    
    WormBossManager(){
        explode = new Explode(1);
        sprite = new WormBoss();
        sprite.x = -32;
        sprite.y = 120;
        sprite.hori();
        
        // We initialize at id 1 so as to begin just after the head.
        body = new Body[]{
            new Body(1, false),
            new Body(2, false),
            new Body(3, false),
            new Body(4, false),
            new Body(5, false),
            new Body(6, false),
            new Body(7, false),
            new Body(8, true),
        };
    }
    
    private void setDir(){
        sx = 0;
        sy = 0;

        switch(Math.random(0, 4)){
            case 0://going left
                sprite.hori();
                sprite.x = 220;//Globals.screen.width();
                sx = -1;
                sprite.setMirrored(true);
                sprite.y = Math.random(16, 136);
                break;
            case 1://going right
                sprite.hori();
                sprite.x = -32;
                sx = 1;
                sprite.setMirrored(false);
                sprite.y = Math.random(16, 136);
                break;
                
            case 2://going Up
                sprite.vert();
                sprite.y = 176;
                sy = -1;
                sprite.setFlipped(false);
                sprite.x = Math.random(8, 180);
                break;
            case 3://going down
                sprite.vert();
                sprite.y = -32;
                sy = 1;
                sprite.setFlipped(true);
                sprite.x = Math.random(8, 180);
                break;
        }
    }

    boolean bodyDestroyed(){
        for(Body b : body){
            if(b.alive){
                return false;
            }
        }
        angry = true;
        return true;
    }
    
    boolean headCollidesWithBot(float bx, float by){
        if(Globals.boundingBox(sprite.x, sprite.y, 28, bx, by, 16)){
            return true;
        }
        return false;
    }
    
    void moveHurtBot(BotManager botManager){
         // Move bot Y
        if(botManager.getY() + 20 < 131) botManager.setY(botManager.getY()+20);
        else botManager.setY(botManager.getY()-20);
        // Move bot X
        if(botManager.getX() + 20 < 175) botManager.setX(botManager.getX()+20);
        else botManager.setX(botManager.getX()-20);
        
        Globals.shield -= 10;
        Globals.hurt = 50;
    }

    void update(BlastManager blastManager, BotManager botManager){
        if(!alive)return;
        if(bodyDestroyed()){
            speed = 1.4;
            int damage = blastManager.hitEnemy(sprite.x, sprite.y, 32);
            if(damage > 0){
                health-=damage;
                if(health <= 0){
                    explode.play();
                    alive = false;
                }
            }
            
            shoot--;
            if(shoot <= 0){
                shoot = 100;
                blastX = sprite.x + 18;
                blastY = sprite.y + 18;
                if(botManager.getX() > blastX){
                    bvx = 2;
                }else{
                    bvx = -2;
                }
                if(botManager.getY() > blastY){
                    bvy = 2;
                }else{
                    bvy = -2;
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
           for(Body b : body){
                if(b.alive && Globals.hurt == 0 && Globals.boundingBox(b.body.x+2, b.body.y+2, 14, botManager.getX(), botManager.getY(), 16)){
                    moveHurtBot(botManager);
                }
                int damage = blastManager.hitEnemy(b.body.x, b.body.y, 18.0f);
                if(damage > 0){
                    if(b.last && b.alive){
                        b.hit(damage);
                        if(b.health == 0){
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
        if(Globals.hurt == 0 && Globals.boundingBox(sprite.x, sprite.y, 28, botManager.getX(), botManager.getY(), 16)){
            moveHurtBot(botManager);
        }
        if(health > 0){
            if(sprite.x > 360){
                setDir();
            }
            if(sprite.x < -172){
                setDir();
            }
            if(sprite.y > 316){
                setDir();
            }
            if(sprite.y < -172){
                setDir();
            }
            sprite.y += sy * speed;
            sprite.x += sx * speed;
            
            for(Body b : body){
                b.update(sprite.x, sprite.y, sx, sy);
            }
        }
    }
    
    void render(){
        // Don't render if dead.
        if(dying == 0)return;
        if(bodyDestroyed()){
            Globals.screen.drawCircle(blastX, blastY, 8, 8, false);
        }

        for(Body b : body){
            b.render();
        }
        if(!alive && dying > 0){
            sprite.setFlipped(false);
            sprite.setMirrored(false);
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
            sprite.die();
        }
        sprite.draw(Globals.screen);
    }
    
    int getCurrentHealth(){
        int total = health;
        for(Body b : body){
            total += b.getHealth();
        }
        return total;
    }
    
    int getTotalHealth(){
        return 145;//health + body * 15
    }
    
    // ensure we draw the full enemy death animation
    public boolean cleared(){
        return dying == 0; 
    }
}

class Body {
    WormBody body;
    int sx = 1, sy = 0, id, health = 15, hurt = 0, dying = 0;
    boolean last, alive = true;
    
    Body(int id, boolean last){
        this.id = id;
        this.last = last;
        body = new WormBody();
        body.run();
        body.y = 122;
        body.x = id * -18;
    }
    
    void update(float x, float y, int dirX, int dirY){
        if(!alive) return;
        if(dying > 0){
            dying--;
            body.die();
            if(dying == 0){
                alive = false;
                // remove the body from the field.
                body.x = -16;
                body.y = -16;
            }
            return;
        }
        if(hurt > 0){
            hurt--;
            body.hurt();
        }else if(last){
            body.last();
        }else{
            body.run();
        }
        if(health > 5){
            if(dirX > 0){
                body.x = x + id*18*-dirX;
            }else if(dirX < 0){
                body.x = x + 16 + id*18*-dirX;
            }
            if(dirY == 0){
                body.y = y+7;
            }
            
            if(dirY > 0){
                body.y = y + id*18*-dirY;
            }else if(dirY < 0){
                body.y = y + 16 + id*18*-dirY;
            }
            
            if(dirX == 0){
                body.x = x + 7;
            }
            
        }else{
            if(body.x < 0) {
                sx = Math.random(1, 4);
                sy = Math.random(-3, 2);
            }
            if(body.x > 192){
                sx = Math.random(-4, -1);
                sy = Math.random(-3, 2);
            } 
            if(body.y < 16) {
                sy = Math.random(1, 4);
                sx = Math.random(-3, 2);
            }
            if(body.y > 148) {
                sy = Math.random(-4, -1);
                sx = Math.random(-3, 2);
            }
            body.x += sx;
            body.y += sy;
        }
        // body.x += sx;
    }
    
    void render(){
        if(alive || dying > 0)body.draw(Globals.screen);
    }
    
    void hit(int damage){
        hurt = 10;
        if(health == 6){
            sx = -2;
            sy = 2;
        }
        health-=damage;
        if(health <= 0){
            dying = 20;
        }
    }
    
    int getHealth(){
        return health;
    }
    
}