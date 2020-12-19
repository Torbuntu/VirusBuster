import femto.Game;
import femto.State;
import femto.input.Button;
import femto.mode.HiRes16Color;

import audio.Explode;

import sprites.Virus;
import sprites.MegaFragment;

import stage.TitleStage;

class IntroCutStage extends State {
    
    HiRes16Color screen;
    Virus virus;
    MegaFragment megaFrag;
    Explode explode;
    
    int x, y, dir = 0;
    
    void init(){
        explode = new Explode(1);
        screen = Globals.screen;
        virus = new Virus();
        virus.walk();
        x = -16;
        y = 40;
        
        megaFrag = new MegaFragment();
    }
    
    void update(){
        if(Button.C.justPressed()) Game.changeState(new TitleStage());
        screen.clear(3);
        
        switch(dir){
            case 0:
                if(x == 90) explode.play();
                if(x < 220) x+=2;
                else {
                    dir++;
                    y = -16;
                    x = 118;
                }
            break;
            case 1:
                if(y == 40) explode.play();
                if(y < 180) y+=2;
                else {
                    dir++;
                    y = 60;
                    x = 230;
                }
            break;
            case 2:
                if(x == 90) explode.play();
                if(x > -16) x-=2;
                else{
                    dir++;
                    x = 118;
                    y = 180;
                }
            break;
            case 3:
                if(y == 40) explode.play();
                if(y > -16) y-=2;
                else{
                    dir++;
                }
            break;
            case 4:
                screen.fillRect(10, 100, 200, 50, 1);
                screen.setTextPosition(15, 105);
                screen.setTextColor(3);
                screen.print("Viruses have stolen\n  the Mega Fragments!\n  Initiate Virus Buster\n  Program!");
            break;
        }
        
        
        //first frag
        megaFrag.setFlipped(false);
        megaFrag.setMirrored(false);
        if(dir > 0 || dir == 0 && x > 90){
            megaFrag.corrupt();
        }else megaFrag.complete();
        megaFrag.draw(screen, 86, 32);
        
        //second
        megaFrag.setMirrored(true);
        if(dir > 1 || dir == 1 && y > 40){
            megaFrag.corrupt();
        }else megaFrag.complete();
        megaFrag.draw(screen, 110, 32);
        
        //third
        megaFrag.setMirrored(false);
        megaFrag.setFlipped(true);
        if(dir > 2 || dir == 2 && x < 90){
            megaFrag.corrupt();
        }else megaFrag.complete();
        megaFrag.draw(screen, 86, 56);
        
        //last
        megaFrag.setFlipped(true);
        megaFrag.setMirrored(true);
        if(dir > 3 || dir == 3 && y < 40){
            megaFrag.corrupt();
        }else megaFrag.complete();
        megaFrag.draw(screen, 110, 56);
        
        virus.draw(screen, x, y);
        
        
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
    }
}