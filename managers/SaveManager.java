public class SaveManager extends femto.Cookie {
    SaveManager() {
        super();
        begin("VBUST");
    }
    boolean firstZoneClear;
    boolean secondZoneClear;
    boolean thirdZoneClear;
    boolean fourthZoneClear;
    boolean started;
    // highScore only used in Endless mode
    
    int rate;
    int refresh;
    int currency;
    float magnet;
    int charge;
    int damage;
}