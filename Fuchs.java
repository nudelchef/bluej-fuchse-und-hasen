import java.util.List;
import java.awt.*;

public class Fuchs extends Animal
{
    private static final int HASEN_NAEHRWERT = 7;
    
    public Fuchs(boolean zufaelligesAlter, Feld feld, Location location)
    {
        super(zufaelligesAlter,feld,location);
        
        setMaxAlter(150);
        setGebaerAlter(10);
        setGebaerWahrscheinlichkeit(0.35);
        setMaxWurfgroesse(5);
        
        setNaehrwert(40);
        
        setColor(Color.orange);
        
        addNahrung(Hase.class);
        
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
        for(int b = 0; b < geburten && frei.size() > 0; b++) {
            Location pos = frei.remove(0);
            Fuchs jung = new Fuchs(false, feld, pos);
            animals.add(jung);
        }
    }
        
    
    
}
