import java.util.List;
import java.awt.*;
import java.util.Iterator;
import java.util.Random;

public class B�r extends Animal
{
    public B�r(boolean zufaelligesAlter, Feld feld, Location location)
    {
        super(zufaelligesAlter,feld,location);
        
        setMaxAlter(200);
        setGebaerAlter(100);
        setGebaerWahrscheinlichkeit(0.2);
        setMaxWurfgroesse(1);
        
        setS�ttigung(50);
        
        setNaehrwert(100);
        
        setColor(new Color(127,25,0));
        
        addNahrung(Fuchs.class);
        
        
        if(zufaelligesAlter) {
            alter = rand.nextInt(getMaxAlter() / 2);
        }
    }
    
    
    public void update(List<Animal> animals)
    {
        alterErhoehen();     
        hungerVergroessern(); 
        move(animals);
    }    
    
    public void gebaereNachwuchs(List<Animal> animals)
    {
        // Neugeborene kommen in freie Nachbarlocationen.
        // Freie Nachbarlocationen abfragen.
        List<Location> frei = feld.freieNachbarlocationen(location);
        int geburten = traechtig();
        for(int b = 0; b < geburten && frei.size() > 0; b++) 
        {
            Location pos = frei.remove(0);
            B�r jung = new B�r(false, feld, pos);
            animals.add(jung);
        }
    }    
    
}
