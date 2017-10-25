import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Eine grafische Ansicht des Simulationsfeldes.
 * Die Ansicht zeigt f¸r jede Position ein gef‰rbtes Rechteck,
 * das den jeweiligen Inhalt repr‰sentiert, und hat eine
 * vorgebene Hintergrundfarbe.
 * Die Farben f¸r die verschiedenen Tierarten kˆnnen mit
 * der Methode setzeFarbe definiert werden.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class Simulationsansicht extends JFrame
{
    
    // Die Farbe f¸r leere Positionen
    private static final Color LEER_FARBE = Color.white;

    // Die Farbe f¸r Objekte ohne definierte Farbe
    private static final Color UNDEF_FARBE = Color.gray;

    private static final String SCHRITT_PREFIX = "Schritt: ";
    private static final String POPULATION_PREFIX = "Population: ";
    private static JLabel schrittLabel, population;
    private Canvas feldansicht;
    
    // Ein Statistik-Objekt zur Berechnung und Speicherung
    // von Simulationsdaten
    private static FeldStatistik stats = new FeldStatistik();
    
    private static Simulator sim;
    
    /**
     * Erzeuge eine Ansicht mit der gegebenen Breite und Hˆhe.
     * @param hoehe Die Hˆhe der Simulation.
     * @param breite Die Breite der Simulation.
     */
    public Simulationsansicht(int hoehe, int breite, Feld feld, Simulator sim)
    {
        this.sim = sim;

        setTitle("Simulation von F¸chsen und Hasen");
        addKeyListener(new AL());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        schrittLabel = new JLabel(SCHRITT_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        
        setLocation(100, 50);
        
        feldansicht = new Canvas(feld, hoehe, breite);

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
     * eigene grafische Komponente f¸r die Benutzungsschnittstelle
     * definiert. Diese Komponente zeigt das Feld an.
     * Dies ist fortgeschrittene GUI-Technik - Sie kˆnnen sie
     * f¸r Ihr Projekt ignorieren, wenn Sie wollen.
     */
    private static class Canvas extends JPanel
    {
        private static final long serialVersionUID = 20060330L;
        private final int DEHN_FAKTOR = 6;

        private int xFaktor, yFaktor;
        Dimension size;
        
        private Image offScreenImage = null;
        private Graphics offScreenGraphics = null;
        private Image offScreenImageDrawed = null;
        private Graphics offScreenGraphicsDrawed = null;              
        private Timer timer = new Timer();
        private int counter = 0;
        
        private Feld feld;
        
        @Override
        public void update(Graphics g) {                                
            paint(g);
            System.out.println("update called ----------->");
        }
        
        /**
         * Erzeuge eine neue Komponente zur Feldansicht.
         */
        public Canvas(Feld feld, int hoehe, int breite)
        {
            this.xFaktor = 5;
            this.yFaktor = 5;
            this.feld = feld;
            size = new Dimension(breite * xFaktor, hoehe * yFaktor);
            timer.schedule(new AutomataTask(), 0, 100);
            this.setPreferredSize(size);
            this.setBackground(Color.white);
        }

        /**
         * Der GUI-Verwaltung mitteilen, wie groﬂ wir sein wollen.
         * Der Name der Methode ist durch die GUI-Verwaltung festgelegt.
         */
        public Dimension getPreferredSize()
        {
            return size;
        }
        
        
        /**
         * Draw this generation.
         * @see java.awt.Component#paint(java.awt.Graphics)
         */
        @Override
        public void paint(final Graphics g) {

            final Dimension d = getSize();
            if (offScreenImageDrawed == null) {   
                // Double-buffer: clear the offscreen image.                
                offScreenImageDrawed = createImage(d.width, d.height);   
            }          
            offScreenGraphicsDrawed = offScreenImageDrawed.getGraphics();      
            offScreenGraphicsDrawed.setColor(Color.white);
            offScreenGraphicsDrawed.fillRect(0, 0, d.width, d.height) ;                           
            /////////////////////
            // Paint Offscreen //
            /////////////////////
            
            
            
            /////////////////////
            
            renderOffScreen(offScreenImageDrawed.getGraphics());
            g.drawImage(offScreenImageDrawed, 0, 0, null);
        }
        
        private class AutomataTask extends java.util.TimerTask {
            public void run() {
                // Run thread on event dispatching thread
                if (!EventQueue.isDispatchThread()) {
                    EventQueue.invokeLater(this);
                } else {
                    if (Canvas.this != null) {
                        Canvas.this.repaint();                        
                    }
                }
                
            } // End of Run
        } 
        
        public void renderOffScreen(final Graphics g)
        {
            

        schrittLabel.setText(SCHRITT_PREFIX + sim.getSchritt());
        stats.zuruecksetzen();
        
            for(int zeile = 0; zeile < feld.gibTiefe(); zeile++) {
                for(int spalte = 0; spalte < feld.gibBreite(); spalte++) {
                    Animal tier = feld.gibObjektAn(zeile, spalte);
                    if(tier != null) {
                        stats.erhoeheZaehler(tier.getClass());
                        zeichneMarkierung(g, spalte, zeile, tier.getColor());
                    }
                    else {
                        zeichneMarkierung(g, spalte, zeile, LEER_FARBE);
                    }
                }
            }
        
        
        stats.zaehlungBeendet();

        population.setText(POPULATION_PREFIX + stats.gibBewohnerInfo(feld));
        }
        
        
        /**
         * Zeichne an der gegebenen Position ein Rechteck mit
         * der gegebenen Farbe.
         */
        public void zeichneMarkierung(Graphics g, int x, int y, Color farbe)
        {
            g.setColor(farbe);
            g.fillRect(x * xFaktor, y * yFaktor, xFaktor, yFaktor);
        }
    }
}
