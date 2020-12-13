public class SaveManager extends femto.Cookie {
    SaveManager() {
        super();
        begin("VBUST");
    }
    boolean firstZoneClear;
    boolean secondZoneClear;
    boolean thirdZoneClear;
    boolean fourthZoneClear;
    
    // highScore only used in Endless mode
    int highScore;
    int rate;
    int refresh;
    int currency;
    float magnet;
}