
abstract class Animal
{ 
   
    // Das Alter dieses Fuchses.
    public int alter;
    // Ist dieser Fuchs noch lebendig?
    public boolean lebendig;
    // Die Location dieses Fuchses
    public Location location;
    // Das belegte Feld
    public Feld feld;
    
    public Animal(boolean zufaelligesAlter, Feld feld, Location location)
    {
        alter = 0;
        lebendig = true;
        this.feld = feld;
        setLocation(location);
    }
    
    
    /**
     * Pr�fe, ob dieser Fuchs noch lebendig ist.
     * @return true wenn dieser Fuchs noch lebt.
     */
    public boolean istLebendig()
    {
        return lebendig;
    }

    /**
     * Liefere die Location des Fuches.
     * @return die Location des Fuches.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Setze den Fuchs auf die gegebene im aktuellen Feld.
     * @param neueLocation die neue Location dieses Fuchses.
     */
    public void setLocation(Location newLocation)
    {
        if(location != null) {
            feld.raeumen(location);
        }
        location = newLocation;
        feld.platziere(this, newLocation);
    }

    /**
     * Erh�he das Alter dieses Fuchses. Dies kann zu seinem
     * Tod f�hren.
     */
    public abstract void alterErhoehen();
    
    /**
     * Anzeigen, dass der Fuchs nicht mehr laenger lebendig ist.
     * Fuchs aus dem Feld entfernen.
     */
    public void sterben()
    {
        lebendig = false;
        if(location != null) {
            feld.raeumen(location);
            location = null;
            feld = null;
        }
    }
    
    public abstract void update();
    
    /**
     * Ein Hase kann geb�ren, wenn er das geb�rf�hige
     * Alter erreicht hat.
     */
    public abstract boolean kannGebaeren();
}
