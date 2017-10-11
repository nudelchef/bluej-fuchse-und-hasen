import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class Bär extends Animal
{
    public Bär(boolean zufaelligesAlter, Feld feld, Location location)
    {
        super(zufaelligesAlter,feld,location);
        
        setMaxAlter(300);
        setGebaerAlter(100);
        setGebaerWahrscheinlichkeit(0.05);
        setMaxWurfgroesse(2);
        
        setNaehrwert(90);
        
        addNahrung(new Fuchs(false,null,null));
        
        
        if(zufaelligesAlter) {
            alter = rand.nextInt(getMaxAlter());
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
