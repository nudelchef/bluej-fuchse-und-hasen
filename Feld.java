import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Ein rechteckiges Gitter von Feldlocationen.
 * Jede Location kann ein einzelnes Tier aufnehmen.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2008.03.30
 */
public class Feld
{
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    // Die Tiefe und die Breite des Feldes
    private int tiefe, breite;
    // Speicher für die Tiere
    private Object[][] feld;

    /**
     * Erzeuge ein Feld mit den angegebenen Dimensionen.
     * @param tiefe die Tiefe des Feldes.
     * @param breite die Breite des Feldes.
     */
    public Feld(int tiefe, int breite)
    {
        this.tiefe = tiefe;
        this.breite = breite;
        feld = new Object[tiefe][breite];
    }
    
    /**
     * Räume das Feld.
     */
    public void raeumen()
    {
        for(int zeile = 0; zeile < tiefe; zeile++) {
            for(int spalte = 0; spalte < breite; spalte++) {
                feld[zeile][spalte] = null;
            }
        }
    }
    
    /**
     * Räume die gegebene Location.
     * @param location die zu leerende Location
     */
    public void raeumen(Location location)
    {
        feld[location.getX()][location.getY()] = null;
    }
    
    /**
     * Platziere das gegebene Tier an der angegebenen Location.
     * Wenn an der Location bereits ein Tier eingetragen ist,
     * geht es verloren.
     * @param tier das Tier das platziert werden soll.
     * @param zeile die Zeilenkoordinate der Location.
     * @param spalte die Spaltenkoordinate der Location.
     */
    public void platziere(Object tier, int zeile, int spalte)
    {
        platziere(tier, new Location(zeile, spalte));
    }
    
    /**
     * Platziere das gegebene Tier an der angegebenen Location.
     * Wenn an der Location bereits ein Tier eingetragen ist,
     * geht es verloren.
     * @param tier das Tier, das platziert werden soll.
     * @param location die Location, an der das Tier platziert werden soll.
     */
    public void platziere(Object tier, Location location)
    {
        feld[location.getX()][location.getY()] = tier;
    }
    
    /**
     * Liefere das Tier an der angegebenen Location, falls vorhanden.
     * @param location die gewünschte Location.
     * @return das Tier an der angegebenen Location oder null, wenn
     *         dort kein Tier eingetragen ist.
     */
    public Object gibObjektAn(Location location)
    {
        return gibObjektAn(location.getX(), location.getY());
    }
    
    /**
     * Liefere das Tier an der angegebenen Location, falls vorhanden.
     * @param zeile die gewünschte Zeile.
     * @param spalte die gewünschte Spalte.
     * @return das Tier an der angegebenen Location oder null, wenn
     *         dort kein Tier eingetragen ist.
     */
    public Object gibObjektAn(int zeile, int spalte)
    {
        return feld[zeile][spalte];
    }
    
    /**
     * Wähle zufällig eine der Locationen, die an die gegebene Location
     * angrenzen, oder die gegebene Location selbst.
     * Die gelieferte Location liegt innerhalb der gültigen Grenzen
     * dieses Feldes.
     * @param location die Location, von der ein Nachbar zu wählen ist.
     * @return eine gültige Location innerhalb dieses Feldes. Das kann
     *         auch die gegebene Location selbst sein.
     */
    public Location zufaelligeNachbarlocation(Location location)
    {
        List<Location> nachbarn = nachbarlocationen(location);
        return nachbarn.get(0);
    }
    
    /**
     * Liefert eine gemischte Liste von freien Nachbarlocation.
     * @param location die Location, für die Nachbarlocationen
     *                 zu liefern ist.
     * @return eine Liste freier Nachbarlocationen.
     */
    public List<Location> freieNachbarlocationen(Location location)
    {
        List<Location> frei = new LinkedList<Location>();
        List<Location> nachbarn = nachbarlocationen(location);
        for(Location naechste : nachbarn) {
            if(gibObjektAn(naechste) == null) {
                frei.add(naechste);
            }
        }
        return frei;
    }
    
    /**
     * Versuche, eine freie Nachbarlocation zur gegebenen Location zu
     * finden. Wenn es keine gibt, liefere null.
     * Die gelieferte Location liegt innerhalb der Feldgrenzen.
     * @param location die Location, für die eine Nachbarlocation
     *                 zu liefern ist.
     * @return eine gültige Location innerhalb der Feldgrenzen. 
     */
    public Location freieNachbarlocation(Location location)
    {
        // Die verfügbaren freien Nachbarlocationen
        List<Location> frei = freieNachbarlocationen(location);
        if(frei.size() > 0) {
            return frei.get(0);
        } 
        else {
            return null;
        }
    }

    /**
     * Liefert eine gemischte Liste von Nachbarlocationen
     * zu der gegebenen Location. Diese Liste enthält nicht die gegebene 
     * Location selbst. Alle Locationen liegen innerhalb des Feldes.
     * @param location die Location, für die Nachbarlocationen zu liefern sind.
     * @return eine Liste der Nachbarlocationen zur gegebenen Location.
     */
    public List<Location> nachbarlocationen(Location location)
    {
        assert location != null : "Keine Location an nachbarpostionen uebergeben";
        // Die Liste der zurueckzuliefernden Locationen
        List<Location> locationen = new LinkedList<Location>();
        if(location != null) {
            int zeile = location.getX();
            int spalte = location.getY();
            for(int zDiff = -1; zDiff <= 1; zDiff++) {
                int naechsteZeile = zeile + zDiff;
                if(naechsteZeile >= 0 && naechsteZeile < tiefe) {
                    for(int sDiff = -1; sDiff <= 1; sDiff++) {
                        int naechsteSpalte = spalte + sDiff;
                        // Ungueltige Locationen und Ausgangslocation ausschliessen.
                        if(naechsteSpalte >= 0 && naechsteSpalte < breite && (zDiff != 0 || sDiff != 0)) {
                            locationen.add(new Location(naechsteZeile, naechsteSpalte));
                        }
                    }
                }
            }          
            // Mische die Liste. Verschiedene andere Methoden verlassen sich darauf, 
            // dass die Liste ungeordnet ist.
            Collections.shuffle(locationen, rand);
        }
        return locationen;
    }

    /**
     * Liefere die Tiefe dieses Feldes.
     * @return die Tiefe dieses Feldes.
     */
    public int gibTiefe()
    {
        return tiefe;
    }
    
    /**
     * Liefere die Breite dieses Feldes.
     * @return die Breite dieses Feldes.
     */
    public int gibBreite()
    {
        return breite;
    }
}
