import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class B�r extends Animal
{
    // Eigenschaften aller F�chse (statische Datenfelder)
    
    // Das Alter, in dem ein Fuchs geb�rf�hig wird.
    private static final int GEBAER_ALTER = 100;
    // Das H�chstalter eines Fuchses.
    private static final int MAX_ALTER = 300;
    // Die Wahrscheinlichkeit, mit der ein Fuchs Nachwuchs geb�rt.
    private static final double GEBAER_WAHRSCHEINLICHKEIT = 0.35;
    // Die maximale Gr��e eines Wurfes (Anzahl der Jungen).
    private static final int MAX_WURFGROESSE = 5;
    // Der N�hrwert eines einzelnen Hasen. Letztendlich ist
    // dies die Anzahl der Schritte, die ein Fuchs bis zur
    //n�chsten Mahlzeit laufen kann.
    private static final int F�CHSE_NAEHRWERT = 7;
    // Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    // Der Futter-Level, der durch das Fressen von Hasen erh�ht wird.
    private int futterLevel;
    public B�r(boolean zufaelligesAlter, Feld feld, Location location)
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
     * Ein Fuchs kann geb�ren, wenn er das geb�rf�hige
     * Alter erreicht hat.
     */
    public boolean kannGebaeren()
    {
        return alter >= GEBAER_ALTER;
    }    
        
    /**
     * Pr�fe, ob dieser Fuchs in diesem Schritt geb�ren kann.
     * Neugeborene kommen in freie Nachbarlocationen.
     * @param neueFuechse Liste, in die neugeborene F�chse eingetragen werden.
     */
    private void gebaereNachwuchs(List<B�r> neueB�ren)
    {
        // Neugeborene kommen in freie Nachbarlocationen.
        // Freie Nachbarlocationen abfragen.
        List<Location> frei = feld.freieNachbarlocationen(location);
        int geburten = traechtig();
        for(int b = 0; b < geburten && frei.size() > 0; b++) 
        {
            Location pos = frei.remove(0);
            B�r jung = new B�r(false, feld, pos);
            neueB�ren.add(jung);
        }
    }    
       
    /**
     * Erzeuge eine Zahl f�r die Wurfgroesse, wenn der Fuchs
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
     * Dabei kann er Nachwuchs geb�ren, vor Hunger sterben oder
     * an Altersschw�che.
     * @param neueFuechse Liste, in die neue F�chse eingef�gt werden.
     */
    public void jage(List<B�r> neueB�ren)
    {
        if(lebendig) {
            gebaereNachwuchs(neueB�ren);
            // In die Richtung bewegen, in der Futter gefunden wurde.
            Location neueLocation = findeNahrung(location);
            if(neueLocation == null) {  
                // kein Futter - zuf�llig bewegen
                neueLocation = feld.freieNachbarlocation(location);
            }
            // Ist Bewegung m�glich?
            if(neueLocation != null) {
                setzeLocation(neueLocation);
            }
            else {
                // �berpopulation
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
                    futterLevel = F�CHSE_NAEHRWERT;
                    return pos;
                }
            }
        }
        return null;
    }
    
}
