import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class Bär extends Animal
{
    // Eigenschaften aller Füchse (statische Datenfelder)
    
    // Das Alter, in dem ein Fuchs gebärfähig wird.
    private static final int GEBAER_ALTER = 100;
    // Das Höchstalter eines Fuchses.
    private static final int MAX_ALTER = 300;
    // Die Wahrscheinlichkeit, mit der ein Fuchs Nachwuchs gebärt.
    private static final double GEBAER_WAHRSCHEINLICHKEIT = 0.35;
    // Die maximale Größe eines Wurfes (Anzahl der Jungen).
    private static final int MAX_WURFGROESSE = 5;
    // Der Nährwert eines einzelnen Hasen. Letztendlich ist
    // dies die Anzahl der Schritte, die ein Fuchs bis zur
    //nächsten Mahlzeit laufen kann.
    private static final int FÜCHSE_NAEHRWERT = 7;
    // Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    // Der Futter-Level, der durch das Fressen von Hasen erhöht wird.
    private int futterLevel;
    public Bär(boolean zufaelligesAlter, Feld feld, Location location)
    {
        super(zufaelligesAlter,feld,location);
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
        
    /**
     * Prüfe, ob dieser Fuchs in diesem Schritt gebären kann.
     * Neugeborene kommen in freie Nachbarlocationen.
     * @param neueFuechse Liste, in die neugeborene Füchse eingetragen werden.
     */
    private void gebaereNachwuchs(List<Bär> neueBären)
    {
        // Neugeborene kommen in freie Nachbarlocationen.
        // Freie Nachbarlocationen abfragen.
        List<Location> frei = feld.freieNachbarlocationen(location);
        int geburten = traechtig();
        for(int b = 0; b < geburten && frei.size() > 0; b++) 
        {
            Location pos = frei.remove(0);
            Bär jung = new Bär(false, feld, pos);
            neueBären.add(jung);
        }
    }    
       
    /**
     * Erzeuge eine Zahl für die Wurfgroesse, wenn der Fuchs
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
     * Das ist was ein Fuchs die meiste Zeit tut: er jagt Hasen.
     * Dabei kann er Nachwuchs gebären, vor Hunger sterben oder
     * an Altersschwäche.
     * @param neueFuechse Liste, in die neue Füchse eingefügt werden.
     */
    public void jage(List<Bär> neueBären)
    {
        if(lebendig) {
            gebaereNachwuchs(neueBären);
            // In die Richtung bewegen, in der Futter gefunden wurde.
            Location neueLocation = findeNahrung(location);
            if(neueLocation == null) {  
                // kein Futter - zufällig bewegen
                neueLocation = feld.freieNachbarlocation(location);
            }
            // Ist Bewegung möglich?
            if(neueLocation != null) {
                setzeLocation(neueLocation);
            }
            else {
                // Überpopulation
                sterben();
            }
        }
    }
       
    /**
     * Suche nach Nahrung (Hasen) in den Nachbarlocationen.
     * Es wird nur der erste lebendige Hase gefressen.
     * @param location die Location, an der sich der Fuchs befindet.
     * @return die Location mit Nahrung, oder null, wenn keine vorhanden.
     */
    private Location findeNahrung(Location location)
    {
        List<Location> nachbarLocationen = 
                               feld.nachbarlocationen(location);
        Iterator<Location> iter = nachbarLocationen.iterator();
        while(iter.hasNext()) {
            Location pos = iter.next();
            Object tier = feld.gibObjektAn(pos);
            if(tier instanceof Fuchs) {
                Fuchs fuchs = (Fuchs) tier;
                if(fuchs.istLebendig()) { 
                    fuchs.sterben();
                    futterLevel = FÜCHSE_NAEHRWERT;
                    return pos;
                }
            }
        }
        return null;
    }
    
}
