import java.util.List;
import java.awt.*;
import java.util.Iterator;
import java.util.Random;

public class Bär extends Animal
{
    public Bär(boolean zufaelligesAlter, Feld feld, Location location)
    {
        super(zufaelligesAlter,feld,location);
        
        setMaxAlter(1000);
        setGebaerAlter(900);
        setGebaerWahrscheinlichkeit(1.00);
        setMaxWurfgroesse(2);
        
        setNaehrwert(580);
        
        setColor(new Color(127,25,0));
        
        addNahrung(Fuchs.class);
        addNahrung(Hase.class);
        
        
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
            Bär jung = new Bär(false, feld, pos);
            animals.add(jung);
        }
    }    
    
}
