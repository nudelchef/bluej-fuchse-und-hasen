import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * Ein einfacher J�ger-Beute-Simulator, basierend auf einem
 * Feld mit F�chsen und Hasen.
 * 
 * @author David J. Barnes und Michael Kolling
 * @version 2008.03.30
 */
public class Simulator
{
    // Konstanten f�r Konfigurationsinformationen �ber die Simulation.
    // Die Standardbreite f�r ein Feld.
    private static final int STANDARD_WIDTH = 150;
    // Die Standardtiefe f�r ein Feld.
    private static final int STANDARD_HEIGHT = 150;
    // Die Wahrscheinlichkeit f�r die Geburt eines Fuchses an
    // einer beliebigen Location im Feld.
    private static final double FUCHSGEBURT_WAHRSCHEINLICH = 0.02;
    // Die Wahrscheinlichkeit f�r die Geburt eines Hasen an
    // einer beliebigen Location im Feld.
    private static final double HASENGEBURT_WAHRSCHEINLICH = 0.08;    
    // Die Wahrscheinlichkeit f�r die Geburt eines B�ren an
    // einer beliebigen Location im Feld.
    private static final double B�RENGEBURT_WAHRSCHEINLICH = 0.005;    

    // Listen der Tiere im Feld. Getrennte Listen vereinfachen das Iterieren.
    private List<Animal> animals;
    // Der aktuelle Zustand des Feldes
    private Feld feld;
    // Der aktuelle Schritt der Simulation
    private int schritt;
    // Eine grafische Ansicht der Simulation
    private Simulationsansicht ansicht;
    
    /**
     * Erzeuge ein Simulationsfeld mit einer Standardgr��e.
     */
    public Simulator()
    {
        this(STANDARD_WIDTH, STANDARD_HEIGHT);
        
       // simuliere(1000);
    }
      
    /**
     * Erzeuge ein Simulationsfeld mit der gegebenen Gr��e.
     * @param tiefe die Tiefe des Feldes (muss gr��er als Null sein).
     * @param breite die Breite des Feldes (muss gr��er als Null sein).
     */
    public Simulator(int width, int height)
    {
        if(width <= 0 || height <= 0) {
            System.out.println("Abmessungen m�ssen gr��er als Null sein.");
            System.out.println("Benutze Standardwerte.");
            width = STANDARD_WIDTH;
            height = STANDARD_HEIGHT;
        }
        animals = new ArrayList<Animal>();
        feld = new Feld(width, height);

        // Eine Ansicht der Zust�nde aller Locationen im Feld erzeugen.
        ansicht = new Simulationsansicht(width, height, feld, this);
        
        // Einen g�ltigen Startzustand einnehmen.
        zuruecksetzen();
    }
    
    /**
     * Starte die Simulation vom aktuellen Zustand aus f�r einen l�ngeren
     * Zeitraum, etwa 500 Schritte.
     */
    public void starteLangeSimulation()
    {
        simuliere(500);
    }
    
    /**
     * F�hre vom aktuellen Zustand aus die angegebene Anzahl an
     * Simulationsschritten durch.
     * Brich vorzeitig ab, wenn die Simulation nicht mehr aktiv ist.
     * @param schritte die Anzahl der auszuf�hrenden Schritte.
     */
    public void simuliere(int schritte)
    {
        for(int schritt = 1; schritt <= schritte && ansicht.istAktiv(feld); schritt++) {
            simuliereEinenSchritt();
        }
    }
    
    /**
     * F�hre einen einzelnen Simulationsschritt aus:
     * Durchlaufe alle Feldlocationen und aktualisiere den 
     * Zustand jedes Fuchses und Hasen.
     */
    public void simuliereEinenSchritt()
    {
        schritt++;
        
        List<Animal> newAnimals = new ArrayList<Animal>();
        
        for (int i = 0 ; i < animals.size(); i++)
        
        {
            
            Animal animal = animals.get(i);
            if (animal!=null) 
            {
                animal.update(newAnimals);
                if(!animal.istLebendig()) {
                    animals.remove(i);
                }
            }
            
        }
        
        animals.addAll(newAnimals);
    }
        
    /**
     * Setze die Simulation an den Anfang zur�ck.
     */
    public void zuruecksetzen()
    {
        schritt = 0;
        animals.clear();
        bevoelkere();
    }
    
    /**
     * Bev�lkere das Feld mit F�chsen und Hasen.
     */
    private void bevoelkere()
    {
        Random rand = Zufallssteuerung.gibZufallsgenerator();
        feld.raeumen();
        
        
        
        for(int zeile = 0; zeile < feld.gibTiefe(); zeile++) {
            for(int spalte = 0; spalte < feld.gibBreite(); spalte++) {
                if(rand.nextDouble() <= FUCHSGEBURT_WAHRSCHEINLICH) {
                    Location location = new Location(zeile, spalte); 
                    Fuchs fuchs = new Fuchs(true, feld, location);
                    animals.add(fuchs);
                }
                else if(rand.nextDouble() <= HASENGEBURT_WAHRSCHEINLICH) {
                    Location location = new Location(zeile, spalte); 
                    Hase hase = new Hase(true, feld, location);
                    animals.add(hase);
                }
                else if(rand.nextDouble() <= B�RENGEBURT_WAHRSCHEINLICH) {
                    Location location = new Location(zeile, spalte); 
                    B�r b�r = new B�r(true, feld, location);
                    animals.add(b�r);
                }
                /*
                else if(rand.nextDouble() <= B�RENGEBURT_WAHRSCHEINLICH) {
                    Location location = new Location(zeile, spalte); 
                    Walker walker = new Walker(true, feld, location);
                    animals.add(walker);
                }
                */
                // ansonsten die Location leer lassen
            }
        }
                
    }
    
    public int getSchritt()
    {
        return schritt;
    }
}