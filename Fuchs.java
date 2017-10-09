import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Ein simples Modell eines Fuchses.
 * F�chse altern, bewegen sich, fressen Hasen und sterben.
 * 
 * @author David J. Barnes und Michael K�lling
 * @version 2008.03.30
 */
public class Fuchs extends Animal
{
    // Eigenschaften aller F�chse (statische Datenfelder)
    
    // Das Alter, in dem ein Fuchs geb�rf�hig wird.
    private static final int GEBAER_ALTER = 10;
    // Das H�chstalter eines Fuchses.
    private static final int MAX_ALTER = 150;
    // Die Wahrscheinlichkeit, mit der ein Fuchs Nachwuchs geb�rt.
    private static final double GEBAER_WAHRSCHEINLICHKEIT = 0.35;
    // Die maximale Gr��e eines Wurfes (Anzahl der Jungen).
    private static final int MAX_WURFGROESSE = 5;
    // Der N�hrwert eines einzelnen Hasen. Letztendlich ist
    // dies die Anzahl der Schritte, die ein Fuchs bis zur
    //n�chsten Mahlzeit laufen kann.
    private static final int HASEN_NAEHRWERT = 7;
    // Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    // Individuelle Eigenschaften (Instanzfelder).

    // Der Futter-Level, der durch das Fressen von Hasen erh�ht wird.
    private int futterLevel;

    /**
     * Erzeuge einen Fuchs. Ein Fuchs wird entweder neu geboren
     * (Alter 0 Jahre und nicht hungrig) oder kann mit einem zuf�lligen Alter
     * und zuf�lligem Hungergef�hl erzeugt werden.
     * 
     * @param zufaelligesAlter falls true, hat der neue Fuchs ein 
     *        zuf�lliges Alter und einen zuf�lligen Futter-Level.
     * @param feld Das aktuelle belegte Feld
     * @param position Die Position im Feld
     */
    public Fuchs(boolean zufaelligesAlter, Feld feld, Position position)
    {
        super(zufaelligesAlter,feld,position);
        if(zufaelligesAlter) {
            alter = rand.nextInt(MAX_ALTER);
            futterLevel = rand.nextInt(HASEN_NAEHRWERT);
        }
        else {
            // Alter bleibt 0
            futterLevel = HASEN_NAEHRWERT;
        }
    }
    
    /**
     * Das ist was ein Fuchs die meiste Zeit tut: er jagt Hasen.
     * Dabei kann er Nachwuchs geb�ren, vor Hunger sterben oder
     * an Altersschw�che.
     * @param neueFuechse Liste, in die neue F�chse eingef�gt werden.
     */
    public void jage(List<Fuchs> neueFuechse)
    {
        if(lebendig) {
            gebaereNachwuchs(neueFuechse);
            // In die Richtung bewegen, in der Futter gefunden wurde.
            Position neuePosition = findeNahrung(position);
            if(neuePosition == null) {  
                // kein Futter - zuf�llig bewegen
                neuePosition = feld.freieNachbarposition(position);
            }
            // Ist Bewegung m�glich?
            if(neuePosition != null) {
                setzePosition(neuePosition);
            }
            else {
                // �berpopulation
                sterben();
            }
        }
    }
    
    
    public void update()
    {
        alterErhoehen();
        hungerVergroessern();       
    }
    
    /**
     * Erh�he das Alter dieses Fuchses. Dies kann zu seinem
     * Tod f�hren.
     */
    public void alterErhoehen()
    {
        alter++;
        if(alter > MAX_ALTER) {
            sterben();
        }
    }
    
    /**
     * Vergr��ere den Hunger dieses Fuchses. Dies kann zu seinem
     * Tode f�hren.
     */
    private void hungerVergroessern()
    {
        futterLevel--;
        if(futterLevel <= 0) {
            sterben();
        }
    }
    
    /**
     * Suche nach Nahrung (Hasen) in den Nachbarpositionen.
     * Es wird nur der erste lebendige Hase gefressen.
     * @param position die Position, an der sich der Fuchs befindet.
     * @return die Position mit Nahrung, oder null, wenn keine vorhanden.
     */
    private Position findeNahrung(Position position)
    {
        List<Position> nachbarPositionen = 
                               feld.nachbarpositionen(position);
        Iterator<Position> iter = nachbarPositionen.iterator();
        while(iter.hasNext()) {
            Position pos = iter.next();
            Object tier = feld.gibObjektAn(pos);
            if(tier instanceof Hase) {
                Hase hase = (Hase) tier;
                if(hase.istLebendig()) { 
                    hase.sterben();
                    futterLevel = HASEN_NAEHRWERT;
                    return pos;
                }
            }
        }
        return null;
    }
        
    /**
     * Pr�fe, ob dieser Fuchs in diesem Schritt geb�ren kann.
     * Neugeborene kommen in freie Nachbarpositionen.
     * @param neueFuechse Liste, in die neugeborene F�chse eingetragen werden.
     */
    private void gebaereNachwuchs(List<Fuchs> neueFuechse)
    {
        // Neugeborene kommen in freie Nachbarpositionen.
        // Freie Nachbarpositionen abfragen.
        List<Position> frei = feld.freieNachbarpositionen(position);
        int geburten = traechtig();
        for(int b = 0; b < geburten && frei.size() > 0; b++) {
            Position pos = frei.remove(0);
            Fuchs jung = new Fuchs(false, feld, pos);
            neueFuechse.add(jung);
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
     * Ein Fuchs kann geb�ren, wenn er das geb�rf�hige
     * Alter erreicht hat.
     */
    public boolean kannGebaeren()
    {
        return alter >= GEBAER_ALTER;
    }
    
}
