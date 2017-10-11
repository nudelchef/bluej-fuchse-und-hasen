import java.util.List;
import java.util.Random;

/**
 * Ein einfaches Modell eines Hasen.
 * Ein Hase altert, bewegt sich, geb�rt Nachwuchs und stirbt.
 * 
 * @author David J. Barnes und Michael K�lling
 * @version 2008.03.30
 */
public class Hase extends Animal
{
    // Eigenschaften aller Hasen (statische Datenfelder).

    // Das Alter, in dem ein Hase geb�rf�hig wird.
    private static final int GEBAER_ALTER = 5;
    // Das H�chstalter eines Hasen.
    private static final int MAX_ALTER = 40;
    // Die Wahrscheinlichkeit, mit der ein Hase Nachwuchs geb�rt.
    private static final double GEBAER_WAHRSCHEINLICHKEIT = 0.15;
    // Die maximale Gr��e eines Wurfes (Anzahl der Jungen)
    private static final int MAX_WURFGROESSE = 4;
	// Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    // Individuelle Eigenschaften eines Hasen (Instanzfelder).
    

    /**
     * Erzeuge einen neuen Hasen. Ein neuer Hase kann das Alter 0 
     *(neu geboren) oder ein zuf�lliges Alter haben.
     * @param zufaelligesAlter soll der Hase ein zuf�lliges Alter haben?
     * @param feld Das aktuelle belegte Feld
     * @param location Die Location im Feld
     */
    public Hase(boolean zufaelligesAlter, Feld feld, Location location)
    {
        super(zufaelligesAlter,feld,location);
        if(zufaelligesAlter) {
            alter = rand.nextInt(MAX_ALTER);
        }
    }
    
    /**
     * Das ist was ein Hase die meiste Zeit tut - er l�uft herum.
     * Manchmal geb�rt er Nachwuchs und irgendwann stirbt er
     * an Altersschw�che.
     * @param neueHasen Eine Liste, in die neue Hasen eingef�gt werden.
     */
    
    public void update()
    {
        alterErhoehen();        
    }
    
    public void laufe(List<Animal> animals)
    {
        if(lebendig) {
            gebaereNachwuchs(animals);
            // nur in das n�chste Feld setzen, wenn eine Location frei ist
            Location neueLocation = feld.freieNachbarlocation(location);
            if(neueLocation != null) {
                setLocation(neueLocation);
            }
            else {
                // �berpopulation 
                sterben();
            }
        }
    }

    /**
     * Erh�he das Alter. 
     * Dies kann zum Tod des Hasen f�hren.
     */
    public void alterErhoehen()
    {
        alter++;
        if(alter > MAX_ALTER) {
            sterben();
        }
    }
       
    /**
     * Pr�fe, ob dieser Hase in diesem Schritt geb�ren kann.
     * Neugeborene kommen in freie Nachbarlocationen.
     * @param neueHasen Liste, in die neugeborene Hasen eingetragen werden.
     */
    private void gebaereNachwuchs(List<Animal> animals)
    {
        // Neugeborene kommen in freie Nachbarlocationen.
        // Freie Nachbarlocationen abfragen.
        List<Location> frei = feld.freieNachbarlocationen(location);
        int geburten = traechtig();
        for(int b = 0; b < geburten && frei.size() > 0; b++) {
            Location pos = frei.remove(0);
            Hase jung = new Hase(false, feld, pos);
            animals.add(jung);
        }
    }

    /**
     * Erzeuge eine Zahl f�r die Wurfgroesse, wenn der Hase
     * gebaeren kann.
     * @return  Wurfgroesse (kann Null sein).
     */
    private int traechtig()
    {
        int wurfgroesse = 0;
        if(kannGebaeren() && rand.nextDouble() <= GEBAER_WAHRSCHEINLICHKEIT) {
            wurfgroesse = rand.nextInt(MAX_WURFGROESSE) + 1;
        }
        return wurfgroesse;
    }
    /**
     * Ein Hase kann geb�ren, wenn er das geb�rf�hige
     * Alter erreicht hat.
     */
    public boolean kannGebaeren()
    {
        return alter >= GEBAER_ALTER;
    }
}
