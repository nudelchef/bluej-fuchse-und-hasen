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
    private int width, height;
    // Speicher für die Tiere
    private Animal[][] feld;

    /**
     * Erzeuge ein Feld mit den angegebenen Dimensionen.
     * @param width die Tiefe des Feldes.
     * @param height die Breite des Feldes.
     */
    public Feld(int width, int height)
    {
        this.width = width;
        this.height = height;
        feld = new Animal[width][height];
    }
    
    /**
     * Räume das Feld.
     */
    public void raeumen()
    {
        for(int zeile = 0; zeile < width; zeile++) {
            for(int spalte = 0; spalte < height; spalte++) {
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
    public void platziere(Animal tier, int zeile, int spalte)
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
    public void platziere(Animal tier, Location location)
    {
        feld[location.getX()][location.getY()] = tier;
    }
    
    /**
     * Liefere das Tier an der angegebenen Location, falls vorhanden.
     * @param location die gewünschte Location.
     * @return das Tier an der angegebenen Location oder null, wenn
     *         dort kein Tier eingetragen ist.
     */
    public Animal gibObjektAn(Location location)
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
    public Animal gibObjektAn(int zeile, int spalte)
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
            int x = location.getX();
            int y = location.getY();
            for(int xDiff = -1; xDiff <= 1; xDiff++) {
                int nextX = x + xDiff;
                
                for(int yDiff = -1; yDiff <= 1; yDiff++) {
                    int nextY = y + yDiff;
                    // Ungueltige Locationen und Ausgangslocation ausschliessen.
                    
                    if (nextX >= width){
                        nextX = 0;
                    }else if(nextX < 0)
                    {
                        nextX = width-1;
                    }
                    
                    if (nextY >= height){
                        nextY = 0;
                    }else if(nextY < 0)
                    {
                        nextY = height-1;
                    }
                        
                    locationen.add(new Location(nextX, nextY));
                    
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
        return width;
    }
    
    /**
     * Liefere die Breite dieses Feldes.
     * @return die Breite dieses Feldes.
     */
    public int gibBreite()
    {
        return height;
    }
}
