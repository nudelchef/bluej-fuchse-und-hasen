import java.util.List;
import java.util.Random;

public class B�r extends Animal
{
    // Das H�chstalter eines B�ren.
    private static final int MAX_ALTER = 100;
    // Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    public B�r(boolean zufaelligesAlter, Feld feld, Position position)
    {
        super(zufaelligesAlter,feld,position);
        if(zufaelligesAlter) {
            alter = rand.nextInt(MAX_ALTER);
        }
    }
    
    
    public void alterErhoehen()
    {
        
    }
}
