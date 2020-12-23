import femto.Game;
import femto.State;
import femto.input.Button;
import femto.sound.Mixer;
import femto.mode.HiRes16Color;
import managers.BotManager;
import managers.BlastManager;
import stage.IntroCutStage;

class TutorialStage extends State{
    HiRes16Color screen;
    String[] messages;
    int stage;
    BotManager botManager;
    BlastManager blastManager;
    
    int charge = 100;
    void init(){
        botManager = EntityManager.botManager;
        blastManager = EntityManager.blastManager;
        messages = new String[]{
            "Press [C] at any time\nto progress",
            "Initiating tutorial\nprogram",
            "Bot loaded...",
            "Blaster installed...",
            "To begin, use the arrows\nto move the Bot",
            "Holding [B] will convert\nthe blaster to a booster",
            "Charged blast will not\ncharge when booster is\nactive",
            "Blaster is also not\navailable during boost",
            "[A] fires the blaster.\nIt will auto-charge",
            "A pulsing flash icon\nwill appear when a\ncharged blast is ready",
            "Your blaster will be\ntemporarily upgraded",
            "Holding [A] while moving\nwill strafe",
            "This ends the tutorial\nprogram.",
            "Good luck. Have fun.",
            "end"
        };
        
        screen = Globals.screen;
        screen.setTextColor(12);
    }
    
    void update(){
        screen.clear(3);
        
        switch(stage){
            case 0:
            case 1:
            case 2:
            case 3:
                screen.setTextPosition(0,0);
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                screen.setTextPosition(0,0);
                movement();
                break;
            case 8:
            case 9:
                movement();
                shootingAndCharge();
                screen.setTextPosition(0, 32);
                break;
            
            case 10:
            case 11:
                blastManager.tutorial(10, 5);
                movement();
                shootingAndCharge();
                screen.setTextPosition(0, 32);
                break;
            case 12:
            case 13:
                screen.setTextPosition(0,0);
                break;
            default:
                blastManager.reset();
                Game.changeState(new IntroCutStage());
            break;
        }
        
        screen.println("P:// TUT_PROG.EXE");
        screen.println(messages[stage]);
        if(Button.C.justPressed()){
            if(stage < 15){
                stage++;
            }
        }
                
        screen.flush();
    }
    
    void shutdown(){
        //blastManager = null;
        //botManager = null;
        screen = null;
        messages = null;
    }
    
    void movement(){
        botManager.updateBotMovement(charge);
        botManager.render(screen);
    }
    void shootingAndCharge(){
        blastManager.update(botManager.getX()+2, botManager.getY()+6, botManager.dir);
        Globals.drawHud(10);
        // Hud changes text to white for the ZONE:SECTOR rendering
        screen.setTextColor(12);
        blastManager.render(screen);
    }
    
}