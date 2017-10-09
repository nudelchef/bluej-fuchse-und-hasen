import java.util.List;
import java.util.Random;

public class Bär extends Animal
{
    // Das Höchstalter eines BÄren.
    private static final int MAX_ALTER = 100;
    // Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    public Bär(boolean zufaelligesAlter, Feld feld, Position position)
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
