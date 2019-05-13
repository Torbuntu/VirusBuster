import femto.mode.HiRes16Color;
import femto.input.Button;
import femto.palette.Colodore;
import femto.font.Tiny;
import femto.font.Tic80;
import femto.font.Dragon;

public class Game {

    public static void main(String[] args){
        HiRes16Color screen = new HiRes16Color(Dragon.data());
        Colodore.applyTo( screen );

        Bot bot = new Bot();
        
        bot.x = 10;
        bot.y = 10;
        
        float sx = 0, sy = 0;
        int counter = 0;
        
        while(true){

            screen.clear( 0x44 );
            screen.textX = 0;
            screen.textY = 0;
            screen.print(screen.fps());
            
            sx = 0;
            sy = 0;
            if(Button.Down.isPressed()){
                bot.walkVert();    
                sy = 0.5f;
            }
            if( Button.Up.isPressed()){
                bot.walkVert();
                sy = -0.5f;
            }
            if(sy == 0){
                if(Button.Right.isPressed()){
                    bot.setMirrored( true );
                    bot.walkHori();
                    sx = 0.5f;
                }
                if(Button.Left.isPressed()){
                    bot.setMirrored( false );
                    bot.walkHori();
                    sx = -0.5f;
                }
            }
            
            if(sx == 0 && sy == 0) bot.idle();
            if(Button.A.isPressed()){
                bot.shoot();
                sx = 0;
                sy = 0;
            }
            bot.x += sx;
            bot.y += sy;
            bot.draw(screen);
            screen.flush();
        }
    }
}
