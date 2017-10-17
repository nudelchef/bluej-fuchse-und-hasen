import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * Ein einfacher Jäger-Beute-Simulator, basierend auf einem
 * Feld mit Füchsen und Hasen.
 * 
 * @author David J. Barnes und Michael Kolling
 * @version 2008.03.30
 */
public class Simulator
{
    // Konstanten für Konfigurationsinformationen über die Simulation.
    // Die Standardbreite für ein Feld.
    private static final int STANDARD_BREITE = 100;
    // Die Standardtiefe für ein Feld.
    private static final int STANDARD_TIEFE = 100;
    // Die Wahrscheinlichkeit für die Geburt eines Fuchses an
    // einer beliebigen Location im Feld.
    private static final double FUCHSGEBURT_WAHRSCHEINLICH = 0.02;
    // Die Wahrscheinlichkeit für die Geburt eines Hasen an
    // einer beliebigen Location im Feld.
    private static final double HASENGEBURT_WAHRSCHEINLICH = 0.08;    
    // Die Wahrscheinlichkeit für die Geburt eines Bären an
    // einer beliebigen Location im Feld.
    private static final double BÄRENGEBURT_WAHRSCHEINLICH = 0.005;    

    // Listen der Tiere im Feld. Getrennte Listen vereinfachen das Iterieren.
    private List<Animal> animals;
    // Der aktuelle Zustand des Feldes
    private Feld feld;
    // Der aktuelle Schritt der Simulation
    private int schritt;
    // Eine grafische Ansicht der Simulation
    private Simulationsansicht ansicht;
    
    /**
     * Erzeuge ein Simulationsfeld mit einer Standardgröße.
     */
    public Simulator()
    {
        this(STANDARD_TIEFE, STANDARD_BREITE);
        
       // simuliere(1000);
    }
      
    /**
     * Erzeuge ein Simulationsfeld mit der gegebenen Größe.
     * @param tiefe die Tiefe des Feldes (muss größer als Null sein).
     * @param breite die Breite des Feldes (muss größer als Null sein).
     */
    public Simulator(int tiefe, int breite)
    {
        if(breite <= 0 || tiefe <= 0) {
            System.out.println("Abmessungen müssen größer als Null sein.");
            System.out.println("Benutze Standardwerte.");
            tiefe = STANDARD_TIEFE;
            breite = STANDARD_BREITE;
        }
        animals = new ArrayList<Animal>();
        feld = new Feld(tiefe, breite);

        // Eine Ansicht der Zustände aller Locationen im Feld erzeugen.
        ansicht = new Simulationsansicht(tiefe, breite, this);
        
        // Einen gültigen Startzustand einnehmen.
        zuruecksetzen();
    }
    
    /**
     * Starte die Simulation vom aktuellen Zustand aus für einen längeren
     * Zeitraum, etwa 500 Schritte.
     */
    public void starteLangeSimulation()
    {
        simuliere(500);
    }
    
    /**
     * Führe vom aktuellen Zustand aus die angegebene Anzahl an
     * Simulationsschritten durch.
     * Brich vorzeitig ab, wenn die Simulation nicht mehr aktiv ist.
     * @param schritte die Anzahl der auszuführenden Schritte.
     */
    public void simuliere(int schritte)
    {
        for(int schritt = 1; schritt <= schritte && ansicht.istAktiv(feld); schritt++) {
            simuliereEinenSchritt();
        }
    }
    
    /**
     * Führe einen einzelnen Simulationsschritt aus:
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

        ansicht.zeigeStatus(schritt, feld);
    }
        
    /**
     * Setze die Simulation an den Anfang zurück.
     */
    public void zuruecksetzen()
    {
        schritt = 0;
        animals.clear();
        bevoelkere();
        
        // Zeige den Startzustand in der Ansicht.
        ansicht.zeigeStatus(schritt, feld);
    }
    
    /**
     * Bevölkere das Feld mit Füchsen und Hasen.
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
                else if(rand.nextDouble() <= BÄRENGEBURT_WAHRSCHEINLICH) {
                    Location location = new Location(zeile, spalte); 
                    Bär bär = new Bär(true, feld, location);
                    animals.add(bär);
                }
                /*
                else if(rand.nextDouble() <= BÄRENGEBURT_WAHRSCHEINLICH) {
                    Location location = new Location(zeile, spalte); 
                    Walker walker = new Walker(true, feld, location);
                    animals.add(walker);
                }
                */
                // ansonsten die Location leer lassen
            }
        }
                
    }
}