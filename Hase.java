import java.util.List;
import java.awt.*;

/**
 * Ein einfaches Modell eines Hasen.
 * Ein Hase altert, bewegt sich, geb�rt Nachwuchs und stirbt.
 * 
 * @author David J. Barnes und Michael K�lling
 * @version 2008.03.30
 */
public class Hase extends Animal
{
    public Hase(boolean zufaelligesAlter, Feld feld, Location location)
    {
        super(zufaelligesAlter,feld,location);
        
        setMaxAlter(40);
        setGebaerAlter(5);
        setGebaerWahrscheinlichkeit(0.25);
        setMaxWurfgroesse(6);
        
        setNaehrwert(7);
        
        setColor(new Color(220,220,220));
        
        if(zufaelligesAlter) {
            alter = rand.nextInt(getMaxAlter());
        }
    }
    
    public void update(List<Animal> animals)
    {
        alterErhoehen();  
        move(animals);
    }
    
    public void gebaereNachwuchs(List<Animal> animals)
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

    
}
