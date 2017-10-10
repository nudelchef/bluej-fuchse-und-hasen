/**
 * Objekte dieser Klasse repräsentieren 
 * Locationen in einem rechteckigen Feld.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2008.03.30
 */
public class Location
{
    // Zeilen- und Spaltenlocation.
    private int x;
    private int y;

    /**
     * Repräsentiere eine Zeile und eine Spalte.
     * @param zeile die Zeile.
     * @param spalte die Spalte.
     */
    public Location(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Prüfung auf Datengleichheit.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Location) {
            Location andereLocation = (Location) obj;
            return zeile == andereLocation.gibZeile()
                && spalte == andereLocation.gibSpalte();
        }
        else {
            return false;
        }
    }
    
    /**
     * Liefere einen String in der Form 'Zeile,Spalte'
     * @return eine Stringdarstellung dieser Location.
     */
    public String toString()
    {
        return x + "," + y;
    }
    
    /**
     * @return Die Zeile.
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * @return Die Spalte.
     */
    public int getY()
    {
        return y;
    }
}
