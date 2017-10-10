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
    private static final int STANDARD_BREITE = 100;
    // Die Standardtiefe f�r ein Feld.
    private static final int STANDARD_TIEFE = 100;
    // Die Wahrscheinlichkeit f�r die Geburt eines Fuchses an
    // einer beliebigen Location im Feld.
    private static final double FUCHSGEBURT_WAHRSCHEINLICH = 0.02;
    // Die Wahrscheinlichkeit f�r die Geburt eines Hasen an
    // einer beliebigen Location im Feld.
    private static final double HASENGEBURT_WAHRSCHEINLICH = 0.08;    
    // Die Wahrscheinlichkeit f�r die Geburt eines B�ren an
    // einer beliebigen Location im Feld.
    private static final double B�RENGEBURT_WAHRSCHEINLICH = 0.01;    

    // Listen der Tiere im Feld. Getrennte Listen vereinfachen das Iterieren.
    private List<Hase> hasen;
    private List<Fuchs> fuechse;
    private List<B�r> b�ren;
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
        this(STANDARD_TIEFE, STANDARD_BREITE);
    }
      
    /**
     * Erzeuge ein Simulationsfeld mit der gegebenen Gr��e.
     * @param tiefe die Tiefe des Feldes (muss gr��er als Null sein).
     * @param breite die Breite des Feldes (muss gr��er als Null sein).
     */
    public Simulator(int tiefe, int breite)
    {
        if(breite <= 0 || tiefe <= 0) {
            System.out.println("Abmessungen m�ssen gr��er als Null sein.");
            System.out.println("Benutze Standardwerte.");
            tiefe = STANDARD_TIEFE;
            breite = STANDARD_BREITE;
        }
        hasen = new ArrayList<Hase>();
        fuechse = new ArrayList<Fuchs>();
        b�ren = new ArrayList<B�r>();
        feld = new Feld(tiefe, breite);

        // Eine Ansicht der Zust�nde aller Locationen im Feld erzeugen.
        ansicht = new Simulationsansicht(tiefe, breite);
        ansicht.setzeFarbe(Fuchs.class, Color.blue);
        ansicht.setzeFarbe(Hase.class, Color.orange);
        ansicht.setzeFarbe(B�r.class, Color.DARK_GRAY);
        
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
        
        // Platz f�r neugeborene Hasen anlegen.
        List<Hase> neueHasen = new ArrayList<Hase>();
        // Alle Hasen agieren lassen.
        for(Iterator<Hase> iter = hasen.iterator(); iter.hasNext(); ) {
            Hase hase = iter.next();
            hase.update();
            hase.laufe(neueHasen);
            if(!hase.istLebendig()) {
                iter.remove();
            }
        }
        
       
        // Platz f�r neugeborene F�chse anlegen.
        List<B�r> neueB�ren = new ArrayList<B�r>();
        // Alle F�chse agieren lassen.
        for(Iterator<B�r> iter = b�ren.iterator(); iter.hasNext(); ) {
            B�r b�r = iter.next();
            b�r.update();
            // / TODO / b�r.jage(neueB�ren);
            if(!b�r.istLebendig()) {
                iter.remove();
            }
        }
        
        // Platz f�r neugeborene F�chse anlegen.
        List<Fuchs> neueFuechse = new ArrayList<Fuchs>();
        // Alle F�chse agieren lassen.
        for(Iterator<Fuchs> iter = fuechse.iterator(); iter.hasNext(); ) {
            Fuchs fuchs = iter.next();
            fuchs.update();
            fuchs.jage(neueFuechse);
            if(!fuchs.istLebendig()) {
                iter.remove();
            }
        }

        // Neu geborene F�chse und Hasen in die Hauptlisten einf�gen.
        hasen.addAll(neueHasen);
        fuechse.addAll(neueFuechse);

        ansicht.zeigeStatus(schritt, feld);
    }
        
    /**
     * Setze die Simulation an den Anfang zur�ck.
     */
    public void zuruecksetzen()
    {
        schritt = 0;
        hasen.clear();
        fuechse.clear();
        b�ren.clear();
        bevoelkere();
        
        // Zeige den Startzustand in der Ansicht.
        ansicht.zeigeStatus(schritt, feld);
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
                    fuechse.add(fuchs);
                }
                else if(rand.nextDouble() <= HASENGEBURT_WAHRSCHEINLICH) {
                    Location location = new Location(zeile, spalte); 
                    Hase hase = new Hase(true, feld, location);
                    hasen.add(hase);
                }
                else if(rand.nextDouble() <= B�RENGEBURT_WAHRSCHEINLICH) {
                    Location location = new Location(zeile, spalte); 
                    B�r b�r = new B�r(true, feld, location);
                    b�ren.add(b�r);
                }
                // ansonsten die Location leer lassen
            }
        }
    }
}