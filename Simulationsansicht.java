import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Eine grafische Ansicht des Simulationsfeldes.
 * Die Ansicht zeigt für jede Position ein gefärbtes Rechteck,
 * das den jeweiligen Inhalt repräsentiert, und hat eine
 * vorgebene Hintergrundfarbe.
 * Die Farben für die verschiedenen Tierarten können mit
 * der Methode setzeFarbe definiert werden.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class Simulationsansicht extends JFrame
{
    
    // Die Farbe für leere Positionen
    private static final Color LEER_FARBE = Color.white;

    // Die Farbe für Objekte ohne definierte Farbe
    private static final Color UNDEF_FARBE = Color.gray;

    private final String SCHRITT_PREFIX = "Schritt: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel schrittLabel, population;
    private Feldansicht feldansicht;
    
    // Ein Statistik-Objekt zur Berechnung und Speicherung
    // von Simulationsdaten
    private FeldStatistik stats;
    
    private Simulator sim;
    
    /**
     * Erzeuge eine Ansicht mit der gegebenen Breite und Höhe.
     * @param hoehe Die Höhe der Simulation.
     * @param breite Die Breite der Simulation.
     */
    public Simulationsansicht(int hoehe, int breite, Simulator sim)
    {
        this.sim = sim;
        stats = new FeldStatistik();

        setTitle("Simulation von Füchsen und Hasen");
        addKeyListener(new AL());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        schrittLabel = new JLabel(SCHRITT_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        
        setLocation(100, 50);
        
        feldansicht = new Feldansicht(hoehe, breite);

        Container inhalt = getContentPane();
        inhalt.add(schrittLabel, BorderLayout.NORTH);
        inhalt.add(feldansicht, BorderLayout.CENTER);
        inhalt.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
     //Controls
    public class AL extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            int keyCode = event.getKeyCode();
            if (keyCode == event.VK_A)
            {
                sim.simuliereEinenSchritt();
            }
        }

        @Override
        public void keyReleased(KeyEvent event) {
            int keyCode = event.getKeyCode();
            if (keyCode == event.VK_A)
            {
                
            }
        }
    }
    

    /**
     * Zeige den aktuellen Zustand des Feldes.
     * @param schritt welcher Iterationsschritt ist dies.
     * @param feld das Feld, das angezeigt werden soll.
     */
    public void zeigeStatus(int schritt, Feld feld)
    {
        if(!isVisible())
            setVisible(true);

        schrittLabel.setText(SCHRITT_PREFIX + schritt);
        stats.zuruecksetzen();
        
        feldansicht.zeichnenVorbereiten();
            
        for(int zeile = 0; zeile < feld.gibTiefe(); zeile++) {
            for(int spalte = 0; spalte < feld.gibBreite(); spalte++) {
                Animal tier = feld.gibObjektAn(zeile, spalte);
                if(tier != null) {
                    stats.erhoeheZaehler(tier.getClass());
                    feldansicht.zeichneMarkierung(spalte, zeile, tier.getColor());
                }
                else {
                    feldansicht.zeichneMarkierung(spalte, zeile, LEER_FARBE);
                }
            }
        }
        stats.zaehlungBeendet();

        population.setText(POPULATION_PREFIX + stats.gibBewohnerInfo(feld));
        feldansicht.repaint();
    }

    /**
     * Entscheide, ob die Simulation weiterlaufen soll.
     * @return true wenn noch mehr als eine Spezies lebendig ist.
     */
    public boolean istAktiv(Feld feld)
    {
        return stats.istAktiv(feld);
    }
    
    /**
     * Liefere eine grafische Ansicht eines rechteckigen Feldes.
     * Dies ist eine geschachtelte Klasse (eine Klasse, die
     * innerhalb einer anderen Klasse definiert ist), die eine
     * eigene grafische Komponente für die Benutzungsschnittstelle
     * definiert. Diese Komponente zeigt das Feld an.
     * Dies ist fortgeschrittene GUI-Technik - Sie können sie
     * für Ihr Projekt ignorieren, wenn Sie wollen.
     */
    private class Feldansicht extends JPanel
    {
        private static final long serialVersionUID = 20060330L;
        private final int DEHN_FAKTOR = 6;

        private int feldBreite, feldHoehe;
        private int xFaktor, yFaktor;
        Dimension groesse;
        private Graphics g;
        private Image feldImage;

        /**
         * Erzeuge eine neue Komponente zur Feldansicht.
         */
        public Feldansicht(int hoehe, int breite)
        {
            feldHoehe = hoehe;
            feldBreite = breite;
            groesse = new Dimension(0, 0);
        }

        /**
         * Der GUI-Verwaltung mitteilen, wie groß wir sein wollen.
         * Der Name der Methode ist durch die GUI-Verwaltung festgelegt.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(feldBreite * DEHN_FAKTOR,
                                 feldHoehe * DEHN_FAKTOR);
        }
        
        /**
         * Bereite eine neue Zeichenrunde vor. Da die Komponente
         * in der Größe geändert werden kann, muss der Maßstab neu
         * berechnet werden.
         */
        public void zeichnenVorbereiten()
        {
            if(! groesse.equals(getSize())) {  // Größe wurde geändert...
                groesse = getSize();
                feldImage = feldansicht.createImage(groesse.width, groesse.height);
                g = feldImage.getGraphics();

                xFaktor = groesse.width / feldBreite;
                if(xFaktor < 1) {
                    xFaktor = DEHN_FAKTOR;
                }
                yFaktor = groesse.height / feldHoehe;
                if(yFaktor < 1) {
                    yFaktor = DEHN_FAKTOR;
                }
            }
        }
        
        /**
         * Zeichne an der gegebenen Position ein Rechteck mit
         * der gegebenen Farbe.
         */
        public void zeichneMarkierung(int x, int y, Color farbe)
        {
            g.setColor(farbe);
            g.fillRect(x * xFaktor, y * yFaktor, xFaktor-1, yFaktor-1);
        }

        /**
         * Die Komponente für die Feldansicht muss erneut angezeigt
         * werden. Kopiere das interne Image in die Anzeige.
         * Der Name der Methode ist durch die GUI-Verwaltung festgelegt.
         */
        public void paintComponent(Graphics g)
        {
            if(feldImage != null) {
                Dimension aktuelleGroesse = getSize();
                if(groesse.equals(aktuelleGroesse)) {
                    g.drawImage(feldImage, 0, 0, null);
                }
                else {
                    // Größe des aktuellen Images anpassen.
                    g.drawImage(feldImage, 0, 0, aktuelleGroesse.width,
                                aktuelleGroesse.height, null);
                }
            }
        }
    }
}
