import femto.Game;
import femto.State;
import femto.input.Button;

class Shop extends State {
    void init() {
        
    }
    
    void update() {
        Main.screen.clear(3);
        if(Button.A.justPressed() && Globals.rate < 10){
            if(Globals.currency >= 2){
                Globals.currency -= 2;
                if(Globals.rate > 10) Globals.rate = 10;
                else Globals.rate++;
            }
        }
        if(Button.B.justPressed() && Globals.refresh > 5){
            if(Globals.currency >= 2){
                Globals.currency -= 2;
                Globals.refresh-=5;
                if(Globals.refresh < 5) Globals.refresh = 5;
            }
        }
        if(Button.Up.justPressed() && Globals.shield < 100){
            if(Globals.currency >= 10){
                Globals.currency -= 10;
                if(Globals.shield + 10 > 100)Globals.shield = 100;
                else{
                    Globals.shield+=10;
                }
            }
        }


        Main.screen.setTextPosition(0, 16);
        Main.screen.setTextColor(0);
        Main.screen.print(
          "$2  - [A]  Rate: " + Globals.rate
        + "\n$2  - [B]  Refresh: " + Globals.refresh
        + "\n$10 - [Up] Shield: " + Globals.shield
        + "\n\n$$" + Globals.currency);

        if(Button.C.justPressed()){
            /*
            if(Globals.SECTOR == 4){
                switch(Globals.ZONE){
                    case 0:
                        bossManager.init(1);
                        break;
                    case 1:
                        bossManager.init(2);
                        break;
                    case 2:
                        bossManager.init(3);
                        break;
                    case 3:
                        bossManager.init(4);
                        break;
                }
            }else if (Globals.SECTOR == 8){
                wormManager.reset();
            }else{
              */
                Game.changeState(SectorZoneManager.getNextState());
                // virusManager.initWave(SECTOR, debrisManager.getSpawnX(), debrisManager.getSpawnY());
                // virusManager.resetAll();
            //}
            // itemDropManager.clear();
            // Globals.ROOM_STATUS = 3;
        }
        
        Main.screen.flush();
    }
    
    void shutdown() {
        
    }
}