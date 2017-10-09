import java.util.List;
import java.util.Random;

public class Bär extends Animal
{
    // Eigenschaften aller Füchse (statische Datenfelder)
    
    // Das Alter, in dem ein Fuchs gebärfähig wird.
    private static final int GEBAER_ALTER = 100;
    // Das Höchstalter eines Fuchses.
    private static final int MAX_ALTER = 300;
    // Die Wahrscheinlichkeit, mit der ein Fuchs Nachwuchs gebärt.
    // /private static final double GEBAER_WAHRSCHEINLICHKEIT = 0.35;
    // Die maximale Größe eines Wurfes (Anzahl der Jungen).
    // /private static final int MAX_WURFGROESSE = 5;
    // Der Nährwert eines einzelnen Hasen. Letztendlich ist
    // dies die Anzahl der Schritte, die ein Fuchs bis zur
    //nächsten Mahlzeit laufen kann.
    // /private static final int HASEN_NAEHRWERT = 7;
    // Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    public Bär(boolean zufaelligesAlter, Feld feld, Position position)
    {
        super(zufaelligesAlter,feld,position);
        if(zufaelligesAlter) 
        {
            alter = rand.nextInt(MAX_ALTER);
        }
    }
    
    
    public void update()
    {
        alterErhoehen();        
    }
    
    public void alterErhoehen()
    {   
        alter++;
        if(alter > MAX_ALTER) 
        {
            sterben();
        }
    }
    
    /**
     * Ein Fuchs kann gebären, wenn er das gebärfähige
     * Alter erreicht hat.
     */
    public boolean kannGebaeren()
    {
        return alter >= GEBAER_ALTER;
    }
    
}
