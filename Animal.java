
abstract class Animal
{ 
   
    // Das Alter dieses Fuchses.
    public int alter;
    // Ist dieser Fuchs noch lebendig?
    public boolean lebendig;
    // Die Position dieses Fuchses
    public Position position;
    // Das belegte Feld
    public Feld feld;
    
    public Animal(boolean zufaelligesAlter, Feld feld, Position position)
    {
        alter = 0;
        lebendig = true;
        this.feld = feld;
        setzePosition(position);
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
     * Liefere die Position des Fuches.
     * @return die Position des Fuches.
     */
    public Position gibPosition()
    {
        return position;
    }

    /**
     * Setze den Fuchs auf die gegebene im aktuellen Feld.
     * @param neuePosition die neue Position dieses Fuchses.
     */
    public void setzePosition(Position neuePosition)
    {
        if(position != null) {
            feld.raeumen(position);
        }
        position = neuePosition;
        feld.platziere(this, neuePosition);
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
        if(position != null) {
            feld.raeumen(position);
            position = null;
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
