import managers.BlastManager;

import audio.Explode;

import sprites.WormBoss;
import sprites.WormBody;

public class WormBossManager {
    WormBoss sprite;
    Body[] body;
    Explode explode;
    int health = 25;
    int sx = 1, sy = 0, speed = 1, shoot = 150;
    boolean clear = false, angry = false;
    
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
        int stage = 4;
        if(angry){
            stage = 4;
        }
        switch(Math.random(0, stage)){
            case 0://going left
                sprite.hori();
                sprite.x = Main.screen.width();
                sx = -1;
                sprite.setMirrored(true);
                sprite.y = Math.random(16, Main.screen.height()-40);
                break;
            case 1://going right
                sprite.hori();
                sprite.x = -32;
                sx = 1;
                sprite.setMirrored(false);
                sprite.y = Math.random(16, Main.screen.height()-40);
                break;
                
            case 2://going Up
                sprite.vert();
                sprite.y = Main.screen.height();
                sy = -1;
                sprite.setFlipped(false);
                sprite.x = Math.random(8, Main.screen.width()-40);
                break;
            case 3://going down
                sprite.vert();
                sprite.y = -32;
                sy = 1;
                sprite.setFlipped(true);
                sprite.x = Math.random(8, Main.screen.width()-40);
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
        if(Main.checkCollides(sprite.x+18, sprite.y+18, bx+8, by+8, 14, 8)){
            return true;
        }
        return false;
    }
    boolean bodyCollidesWithBot(float bx, float by){
        if(bodyDestroyed()){
            return false;
        }
        for(Body b : body){
            if(b.alive && Main.checkCollides(b.body.x+9, b.body.y+9, bx+8, by+8, 7, 8)){
                return true;
            }
        }
        return false;
    }
    
    void update(BlastManager blastManager, float bx, float by){
        if(bodyDestroyed()){
            speed = 2;
            if(blastManager.hitEnemy(sprite.x+18, sprite.y+18, 16)){
                health--;
                if(health <= 0){
                    explode.play();
                    clear = true;
                }
            }
            
            shoot--;
            if(shoot <= 0){
                shoot = 100;
                blastX = sprite.x + 18;
                blastY = sprite.y + 18;
                if(bx > blastX){
                    bvx = 2;
                }else{
                    bvx = -2;
                }
                if(by > blastY){
                    bvy = 2;
                }else{
                    bvy = -2;
                }
            }
            blastX += bvx;
            blastY += bvy;
            if(Main.checkCollides(blastX, blastY, bx+8, by+8, 4, 8)){
                blastX = -10;
                blastY = -10;
                Main.shield -= 10;
            }
        }else{
           for(Body b : body){
                if(b.last && b.alive && blastManager.hitEnemy(b.body.x+9, b.body.y+9, 12.0f)){
                    b.hit();
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
        
        if(sprite.x > Main.screen.width()+140){
            setDir();
        }
        if(sprite.x < -172){
            setDir();
        }
        if(sprite.y > Main.screen.height()+140){
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
    
    void render(){
        if(bodyDestroyed()){
            Main.screen.drawCircle(blastX, blastY, 8, 8, false);
        }

        for(Body b : body){
            b.render();
        }
        sprite.draw(Main.screen);
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
    
    public boolean cleared(){
        return clear;
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
            if(body.x > Main.screen.width()-28){
                sx = Math.random(-4, -1);
                sy = Math.random(-3, 2);
            } 
            if(body.y < 16) {
                sy = Math.random(1, 4);
                sx = Math.random(-3, 2);
            }
            if(body.y > Main.screen.height()-28) {
                sy = Math.random(-4, -1);
                sx = Math.random(-3, 2);
            }
            body.x += sx;
            body.y += sy;
        }
        // body.x += sx;
    }
    
    void render(){
        if(alive || dying > 0)body.draw(Main.screen);
    }
    
    void hit(){
        hurt = 10;
        if(health == 6){
            sx = -2;
            sy = 2;
        }
        health--;
        if(health <= 0){
            dying = 20;
        }
    }
    
    int getHealth(){
        return health;
    }
    
}