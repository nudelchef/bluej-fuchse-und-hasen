import java.util.List;
import java.awt.*;
import java.util.Random;

public class Walker extends Animal
{
    public Walker(boolean zufaelligesAlter, Feld feld, Location location)
    {
        super(zufaelligesAlter,feld,location);
        
        setMaxAlter(0);
        setGebaerAlter(0);
        setGebaerWahrscheinlichkeit(0);
        setMaxWurfgroesse(0);
        
        setSättigung(0);
        
        setNaehrwert(0);
        
        setColor(new Color(127,25,255));
        
        
        addNahrung(Bär.class);
        addNahrung(Fuchs.class);
        addNahrung(Hase.class);
    }
    
    
    public void update(List<Animal> animals)
    {
        move(animals);
    }    
    
    public void gebaereNachwuchs(List<Animal> animals)
    {
        
    }  
    
    public void sterben()
    {
        
    }
}
