import java.util.List;
import java.util.Random;

/**
 * Ein einfaches Modell eines Hasen.
 * Ein Hase altert, bewegt sich, gebärt Nachwuchs und stirbt.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2008.03.30
 */
public class Hase extends Animal
{
    // Eigenschaften aller Hasen (statische Datenfelder).

    // Das Alter, in dem ein Hase gebärfähig wird.
    private static final int GEBAER_ALTER = 5;
    // Das Höchstalter eines Hasen.
    private static final int MAX_ALTER = 40;
    // Die Wahrscheinlichkeit, mit der ein Hase Nachwuchs gebärt.
    private static final double GEBAER_WAHRSCHEINLICHKEIT = 0.15;
    // Die maximale Größe eines Wurfes (Anzahl der Jungen)
    private static final int MAX_WURFGROESSE = 4;
	// Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    // Individuelle Eigenschaften eines Hasen (Instanzfelder).
    

    /**
     * Erzeuge einen neuen Hasen. Ein neuer Hase kann das Alter 0 
     *(neu geboren) oder ein zufälliges Alter haben.
     * @param zufaelligesAlter soll der Hase ein zufälliges Alter haben?
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
     * Das ist was ein Hase die meiste Zeit tut - er läuft herum.
     * Manchmal gebärt er Nachwuchs und irgendwann stirbt er
     * an Altersschwäche.
     * @param neueHasen Eine Liste, in die neue Hasen eingefügt werden.
     */
    
    public void update()
    {
        alterErhoehen();        
    }
    
    public void laufe(List<Animal> animals)
    {
        if(lebendig) {
            gebaereNachwuchs(animals);
            // nur in das nächste Feld setzen, wenn eine Location frei ist
            Location neueLocation = feld.freieNachbarlocation(location);
            if(neueLocation != null) {
                setLocation(neueLocation);
            }
            else {
                // Überpopulation 
                sterben();
            }
        }
    }

    /**
     * Erhöhe das Alter. 
     * Dies kann zum Tod des Hasen führen.
     */
    public void alterErhoehen()
    {
        alter++;
        if(alter > MAX_ALTER) {
            sterben();
        }
    }
       
    /**
     * Prüfe, ob dieser Hase in diesem Schritt gebären kann.
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
     * Erzeuge eine Zahl für die Wurfgroesse, wenn der Hase
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
     * Ein Hase kann gebären, wenn er das gebärfähige
     * Alter erreicht hat.
     */
    public boolean kannGebaeren()
    {
        return alter >= GEBAER_ALTER;
    }
}
