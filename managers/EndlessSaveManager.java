class EndlessSaveManager extends femto.Cookie {
    EndlessSaveManager(){
        super();
        begin("VBEND");
    }
    
    // highScore only used in Endless mode
    int highScore;
    int ZONE;
    int SECTOR;
    int rate;
    int refresh;
    int currency;
    float magnet;
    int charge;
    int damage;
}