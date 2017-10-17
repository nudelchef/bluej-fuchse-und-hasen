import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.*;

abstract class Animal
{ 
    public static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    public int alter;
    public boolean lebendig;
    public Location location;
    public Feld feld;
    public int sättigung;
    
    public int futterLevel;
    
    private int MAX_ALTER;    
    private int GEBAER_ALTER;
    private double GEBAER_WAHRSCHEINLICHKEIT;
    private int MAX_WURFGROESSE;
    
    private int NAEHRWERT;
    private Color color;
    
    private List<Class> nahrung;
    
    public Animal(boolean zufaelligesAlter, Feld feld, Location location)
    {
        this.feld = feld;
        
        alter = 0;
        lebendig = true;
        setLocation(location);
        nahrung = new ArrayList<Class>();
        futterLevel = 10;
        sättigung = 20;
        color = new Color(0,0,0);
    }
    
    public abstract void update(List<Animal> animals);
    
    public abstract void gebaereNachwuchs(List<Animal> animals);
        
    public void sterben()
    {
        lebendig = false;
        if(location != null) {
            feld.raeumen(location);
            location = null;
            feld = null;
        }
    }
    
    public void alterErhoehen()
    {
        alter++;
        if(alter > MAX_ALTER) {
            sterben();
        }
    }
    
    public void move(List<Animal> animals)
    {
        if(lebendig && location!=null) {
            gebaereNachwuchs(animals);
            // In die Richtung bewegen, in der Futter gefunden wurde.
            
            Location neueLocation = null; 
            
            if (nahrung.size() > 0)
            {
                neueLocation = findeNahrung(location);                
            }
            
            if (neueLocation == null)
            {
                neueLocation = feld.freieNachbarlocation(location);
            }
            
            if(neueLocation != null) {
                setLocation(neueLocation);
            }
            else { // Überpopulation
                sterben();
            }
        }else{
            sterben();
        }
    }
    public Location findeNahrung(Location location)
    {
        //if (futterLevel <= sättigung)
        {
            List<Location> nachbarLocationen = 
                                   feld.nachbarlocationen(location);
            for (int ix = 0; ix < nachbarLocationen.size() ; ix++)
            {
                Location pos = nachbarLocationen.get(ix);
                Animal tier = feld.gibObjektAn(pos);
                
                if (tier !=null)
                {
                    for (int i = 0 ; i < nahrung.size();i++)
                    {
                        if (tier.getClass().equals(nahrung.get(i)))
                        {          
                            if(tier.istLebendig()) { 
                                tier.sterben();
                                futterLevel += tier.getNaehrwert();
                                return pos;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public int traechtig()
    {
        int wurfgroesse = 0;
        if(kannGebaeren() && rand.nextDouble() <= GEBAER_WAHRSCHEINLICHKEIT) {
            wurfgroesse = rand.nextInt(MAX_WURFGROESSE) + 1;
        }
        return wurfgroesse;
    }
    
    public void hungerVergroessern()
    {
        futterLevel--;
        if(futterLevel <= 0) {
            sterben();
        }
    }
    
    public void addNahrung(Class food)
    {
        nahrung.add(food);
    }
    
    
    public boolean kannGebaeren()
    {
        return alter >= GEBAER_ALTER;
    }    
    
    public boolean istLebendig()
    {
        return lebendig;
    }

    
    public Color setColor(Color color)
    {
        this.color = color;
        return color;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public void setLocation(Location newLocation)
    {
        if (newLocation !=null)
        {
            if(location != null) 
            {
                feld.raeumen(location);
            }
            
            this.location = newLocation;
            feld.platziere(this, newLocation);
        }
    }

    public Location getLocation()
    {
        return location;
    }

    public void setMaxAlter(int value)
    {
        this.MAX_ALTER = value;
    }

    public int getMaxAlter()
    {
        return MAX_ALTER;
    }

    public void setSättigung(int value)
    {
        this.sättigung = value;
    }

    public int getSättigung()
    {
        return sättigung;
    }
    
    public void setGebaerAlter(int value)
    {
        this.GEBAER_ALTER = value;
    }

    public int getGebaerAlter()
    {
        return this.GEBAER_ALTER;
    }
    
    public void setGebaerWahrscheinlichkeit(double value)
    {
        this.GEBAER_WAHRSCHEINLICHKEIT = value;
    }

    public double getGebaerWahrscheinlichkeit()
    {
        return this.GEBAER_WAHRSCHEINLICHKEIT;
    }
    
    public void setMaxWurfgroesse(int value)
    {
        this.MAX_WURFGROESSE = value;
    }

    public int getMaxWurfgroesse()
    {
        return this.MAX_WURFGROESSE;
    }
    
    public void setNaehrwert(int value)
    {
        this.NAEHRWERT = value;
    }

    public int getNaehrwert()
    {
        return this.NAEHRWERT;
    }
    
}
