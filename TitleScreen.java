import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;
import femto.mode.HiRes16Color;
import femto.font.FontC64;

import audio.Shoot;
import managers.SaveManager;
import stage.MenuStage;
import stage.TutorialStage;
import stage.IntroCutStage;

import stage.SummaryStage;

import sprites.Blast;

class TitleScreen extends State {
    
    Shoot shoot;
    Blast blast;
    HiRes16Color screen;
    Title title;
    boolean start = false, confirm = false, confirmNew = false, shooting = false;
    int select = 0, t = 200, c = 100, x = 0;
    
    
    boolean drawingIntro(){
        if(t == 0)return false;
        screen.setTextPosition(0, 0);
        screen.setTextColor(12);
        screen.print("P:// ");
        if(t >= 180){
            x = screen.textWidth("P:// VIR");
            screen.print("VIR");
        }
        if(t >= 150 && t < 180){
            x = screen.textWidth("P:// VIRUS_");
            screen.print("VIRUS_");
        }
        if(t >= 100 && t < 150){
            x = screen.textWidth("P:// VIRUS_BUSTE");
            screen.print("VIRUS_BUSTE");
        }
        if(t < 100){
            x = screen.textWidth("P:// VIRUS_BUSTER.EXE");
            screen.print("VIRUS_BUSTER.EXE");
        }
        
        // Cursor
        if(c < 25){
            
            if(c < 4) screen.drawRect(x, 0, 5, 9, 12);
            else screen.fillRect(x, 0, 5, 9, 12);
        }
        c--;
        if(c == 0){
            c = 50;
        }
        
        
        t--;
        if(Button.C.justPressed())t = 0;
        return true;
    }
    
    void confirmNewUpdate(){
        screen.fillRect(110, 130, 65, 10, 3);
        screen.setTextPosition(10,131);
        screen.print("Initiate New\nProgram?\n");
        screen.setTextPosition(10,149);
        screen.print(" - Yes");
        screen.setTextPosition(10,158);
        screen.print(" - No");
    
        if(shooting){
            if(shootAnimation()){
                if(select == 0) {
                    confirmNew = false;
                    confirm = true;
                    System.out.println("New game started");
                    Globals.newGame();
                    System.out.println("New game initialized");
                } else  confirmNew = false;
                shooting = false;
            }
        }else{
            shootWait();
        }
    }
    
    boolean shootAnimation(){
        if(shooting){
            blast.x+=5;
            blast.charge();
            if(blast.x > 130){
                shooting = false;
                return true;
            }
            blast.draw(screen);
        }
        return false;
    }
    
    void shootWait(){
        if(Button.A.justPressed() || Button.C.justPressed()){
            shoot.play();
            shooting = true;
        }
        
        blast.fire();
        if(select == 0){
            blast.draw(screen, 4, 148);
        }else{
            blast.draw(screen, 4, 157);
        }
    }
    
    void init(){
        title = new Title();
        start = Globals.saveManager.started;
        screen = Globals.screen;
        screen.setTextColor(12);
        blast = new Blast();
        blast.fire();
        
        shoot = new Shoot(0);
        
        System.out.println("[I] - TitleScreen initialized");
    }
    
    void update(){
        screen.clear(3);
        
        if(drawingIntro()) {
            
            screen.flush();
            return;
        }
        
        
        title.draw(screen, 0,0);
        
        if(Button.Up.justPressed() && select == 1) select = 0;
        if(start && Button.Down.justPressed() && select == 0) select = 1;
        
        if(confirmNew){
            confirmNewUpdate();
        }else if(confirm){
            screen.fillRect(110, 130, 65, 10, 3);
            screen.setTextPosition(10,131);
            screen.print("Initiate Training\nProgram?\n");
            screen.setTextPosition(10,149);
            screen.print(" - Yes");
            screen.setTextPosition(10,158);
            screen.print(" - No");

            if(shooting){
                if(shootAnimation()){
                    System.out.println("New Game screen transition start");
                    if(select == 0) Game.changeState(new TutorialStage());
                    else  Game.changeState(new IntroCutStage());
                    shooting = false;
                }
            }else{
                shootWait();
            }
        }else{
            
            screen.setTextPosition(10,149);
            screen.print(" - New Game");
            if(start){
                screen.setTextPosition(10,158);
                screen.print(" - Continue");
            }
            
            if(shooting){
                if(shootAnimation()){
                    if(select == 0){
                        //new
                        confirmNew = true;
                        //Globals.newGame();
                    }else{
                        Game.changeState(new MenuStage());
                    }
                    shooting = false;
                }
            }else{
                shootWait();
            }
        }
        screen.flush();
    }
    
    void shutdown(){
        screen = null;
        title = null;
        blast = null;
        shoot = null;
    }
}