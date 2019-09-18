/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Classe responsabile della coordinazione delle operazioni che avvengono
 * durante la simulazione.
 * @author damiano
 */
class SimulationEngine {

    final int TARGET_FPS = 24;
    private final int frame = 0;
    private int currentTime = 0;
    
    private final ArrayList<PassengerStencil> listaPassengerStencils;
    private final ArrayList<TransportStencil> listaTransportStencils;
    private final GraphHolder graphHolder;
    
    private final ArrayList<PassengerGenerator> listaPassengerGenerator;
    private final ArrayList<TransportGenerator> listaTransportGenerator;
    
    final ArrayList<Passenger> passengers;
    private static int STATION_SIZE = 50;
    
    private HashMap<Node, Float> nodesColor;

    public SimulationEngine(ArrayList<PassengerStencil> listaPassengerStencils, ArrayList<TransportStencil> listaTransportStencils, GraphHolder graphHolder) {
        this.listaPassengerStencils = listaPassengerStencils;
        this.listaTransportStencils = listaTransportStencils;
        this.graphHolder = graphHolder;
        this.listaPassengerGenerator = new ArrayList<>();
        this.listaTransportGenerator = new ArrayList<>();
        this.passengers = new ArrayList<>();
        this.nodesColor = new HashMap<Node, Float>();
    }
    
    synchronized public void register(Passenger p) {
        synchronized(this.passengers){
            this.passengers.add(p);
        }
    }
    

    /**
     * @param args the command line arguments
     */
    public void start() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimulationEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean isRunning = true;
        long now;
        long updateTime;
        long wait;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        // Fai partire i TransportGenerator
        for (TransportGenerator tg : this.listaTransportGenerator) {
            tg.setCurrentTime(currentTime);
            //tg.start();
        }
        for (TransportGenerator tg : this.listaTransportGenerator) {
            //tg.setCurrentTime(currentTime);
            tg.start();
        }
        // Fai partire i PassengerGenerator
        for (PassengerGenerator pg : this.listaPassengerGenerator) {
            //tg.setCurrentTime(currentTime);
            pg.start();
        }
        while (isRunning) {
            now = System.nanoTime();
            update();
//            renderNodes();
            updateTime = System.nanoTime() - now;
            wait = (OPTIMAL_TIME - updateTime) / 1000000;
            try {
                if(wait >= 0) {
                    Thread.sleep(wait);
                }
 //               Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }

    private void update() {
        this.currentTime = (this.currentTime+1) % 2400;
        // Elimina i Passenger giunti a destinazione
        Iterator<Passenger> iter = this.passengers.iterator();
        while (iter.hasNext()) {
            Passenger pg = iter.next();
            if (pg.currentNodeIndex == pg.nodesPath.size())
                iter.remove();
        }
        // Aggiorna i TransportGenerator
        for (TransportGenerator tg : this.listaTransportGenerator) {
            tg.setCurrentTime(currentTime);
        }
        for (TransportGenerator tg : this.listaTransportGenerator) {
            if(tg.waiting){
                tg.notifyTransportGenerator();
            } 
        }
        // Aggiorna i PassengerGenerator
        for (PassengerGenerator pg : this.listaPassengerGenerator) {
            if(pg.waiting){
                pg.notifyPassengerGenerator();
            } 
        }
    }

    private void renderNodes() {
        //System.out.println("" + currentTime);
        //this.graphHolder.displayGraph();
        // Resetta tutti i colori dei nodi
        this.nodesColor = new HashMap<>();
        graphHolder.populate(this.nodesColor);
        // Ridefinisce i colori
        synchronized(this.passengers){
            for(Passenger pg : this.passengers) {
                Node node = pg.nodesPath.get(pg.currentNodeIndex);
                Float currentColor = this.nodesColor.get(node);
                currentColor = Math.min(1, (float)currentColor + (float)1/STATION_SIZE);
            }
        }
        graphHolder.renderNodesColor(this.nodesColor);
    }

    /**
     * Esegue tutte le operazioni preliminari all'avvio della simulazione.
     */
    void initialise() {
        create_TransportGenerators();
        create_PassengerGenerators();
        create_nodesColor();
    }

    private void create_TransportGenerators() {
        for (TransportStencil transportStencil : this.listaTransportStencils) {
            TransportGenerator transportGenerator = new TransportGenerator(this, transportStencil, graphHolder);
            this.listaTransportGenerator.add(transportGenerator);
        }
    }

    private void create_PassengerGenerators() {
        for (PassengerStencil passengerStencil : this.listaPassengerStencils) {
            PassengerGenerator passengerGenerator = new PassengerGenerator(this, passengerStencil, graphHolder);
            this.listaPassengerGenerator.add(passengerGenerator);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Graph g = new SingleGraph("grafo");
        ArrayList<ArrayList<String>> fermate = new ArrayList<>();
        // Metro A
        ArrayList<String> lineaA = new ArrayList<>();
        lineaA.add("Metro A");
        Node nodoA1 = g.addNode("Battistini");
        lineaA.add("Battistini");
        Node nodoA2 = g.addNode("Cornelia");
        lineaA.add("Cornelia");
        Edge edgeA1 = g.addEdge("Battistini-Cornelia", nodoA1, nodoA2);
        Node nodoA3 = g.addNode("Baldo degli Ubaldi");
        lineaA.add("Baldo degli Ubaldi");
        Edge edgeA2 = g.addEdge("Cornelia-Baldo degli Ubaldi", nodoA2, nodoA3);
        Node nodoA4 = g.addNode("Valle Aurelia");
        lineaA.add("Valle Aurelia");
        Edge edgeA3 = g.addEdge("Baldo degli Ubaldi-Valle Aurelia", nodoA3, nodoA4);
        Node nodoA5 = g.addNode("Cipro");
        lineaA.add("Cipro");
        Edge edgeA4 = g.addEdge("Valle Aurelia-Cipro", nodoA4, nodoA5);
        Node nodoA6 = g.addNode("Ottaviano");
        lineaA.add("Ottaviano");
        Edge edgeA5 = g.addEdge("Cipro-Ottaviano", nodoA5, nodoA6);        
        Node nodoA7 = g.addNode("Lepanto");
        lineaA.add("Lepanto");
        Edge edgeA6 = g.addEdge("Ottaviano-Lepanto", nodoA6, nodoA7);     
        Node nodoA8 = g.addNode("Flaminio");
        lineaA.add("Flaminio");
        Edge edgeA7 = g.addEdge("Lepanto-Flaminio", nodoA7, nodoA8);     
        Node nodoA9 = g.addNode("Spagna");
        lineaA.add("Spagna");
        Edge edgeA8 = g.addEdge("Flaminio-Spagna", nodoA8, nodoA9);     
        Node nodoA10 = g.addNode("Barberini");
        lineaA.add("Barberini");
        Edge edgeA9 = g.addEdge("Spagna-Barberini", nodoA9, nodoA10);    
        Node nodoA11 = g.addNode("Repubblica");
        lineaA.add("Repubblica");
        Edge edgeA10 = g.addEdge("Barberini-Repubblica", nodoA10, nodoA11); 
        Node nodoT = g.addNode("Termini");
        lineaA.add("Termini");
        Edge edgeA11 = g.addEdge("Repubblica-Termini", nodoA11, nodoT); 
        Node nodoA13 = g.addNode("Vittorio Emanuele");
        lineaA.add("Vittorio Emanuele");
        Edge edgeA12 = g.addEdge("Termini-Vittorio Emanuele", nodoT, nodoA13); 
        Node nodoA14 = g.addNode("Manzoni");
        lineaA.add("Manzoni");
        Edge edgeA13 = g.addEdge("Vittorio Emanuele-Manzoni", nodoA13, nodoA14); 
        Node nodoA15 = g.addNode("San Giovanni");
        lineaA.add("San Giovanni");
        Edge edgeA14 = g.addEdge("Manzoni-San Giovanni", nodoA14, nodoA15); 
        Node nodoA16 = g.addNode("Re di Roma");
        lineaA.add("Re di Roma");
        Edge edgeA15 = g.addEdge("San Giovanni-Re di Roma", nodoA15, nodoA16); 
        Node nodoA17 = g.addNode("Ponte Lungo");
        lineaA.add("Ponte Lungo");
        Edge edgeA16 = g.addEdge("Re di Roma-Ponte Lungo", nodoA16, nodoA17); 
        Node nodoA18 = g.addNode("Furio Camillo");
        lineaA.add("Furio Camillo");
        Edge edgeA17 = g.addEdge("Ponte Lungo-Furio Camillo", nodoA17, nodoA18); 
        Node nodoA19 = g.addNode("Colli Albani");
        lineaA.add("Colli Albani");
        Edge edgeA18 = g.addEdge("Furio Camillo-Colli Albani", nodoA18, nodoA19);
        Node nodoA20 = g.addNode("Arco di Travertino");
        lineaA.add("Arco di Travertino");
        Edge edgeA19 = g.addEdge("Colli Albani-Arco di Travertino", nodoA19, nodoA20);
        Node nodoA21 = g.addNode("Porta Furba");
        lineaA.add("Porta Furba");
        Edge edgeA20 = g.addEdge("Arco di Travertino-Porta Furba", nodoA20, nodoA21);
        Node nodoA22 = g.addNode("Numidio Quadrato");
        lineaA.add("Numidio Quadrato");
        Edge edgeA21 = g.addEdge("Porta Furba-Numidio Quadrato", nodoA21, nodoA22);
        Node nodoA23 = g.addNode("Lucio Sestio");
        lineaA.add("Lucio Sestio");
        Edge edgeA22 = g.addEdge("Numidio Quadrato-Lucio Sestio", nodoA22, nodoA23);
        Node nodoA24 = g.addNode("Giulio Agricola");
        lineaA.add("Giulio Agricola");
        Edge edgeA23 = g.addEdge("Lucio Sestio-Giulio Agricola", nodoA23, nodoA24);
        Node nodoA25 = g.addNode("Subaugusta");
        lineaA.add("Subaugusta");
        Edge edgeA24 = g.addEdge("Giulio Agricola-Subaugusta", nodoA24, nodoA25);
        Node nodoA26 = g.addNode("Cinecittà");
        lineaA.add("Cinecittà");
        Edge edgeA25 = g.addEdge("Subaugusta-Cinecittà", nodoA25, nodoA26);
        Node nodoA27 = g.addNode("Anagnina");
        lineaA.add("Anagnina");
        Edge edgeA26 = g.addEdge("Cinecittà-Anagnina", nodoA26, nodoA27);
        fermate.add(lineaA);
        // Metro B
        ArrayList<String> lineaB = new ArrayList<>();
        lineaB.add("Metro B");
        Node nodoB1 = g.addNode("Laurentina");
        lineaB.add("Laurentina");
        Node nodoB2 = g.addNode("Eur Fermi");
        lineaB.add("Eur Fermi");
        Edge edgeB1 = g.addEdge("Laurentina-Eur Fermi", nodoB1, nodoB2);
        Node nodoB3 = g.addNode("Eur Palasport");
        lineaB.add("Eur Palasport");
        Edge edgeB2 = g.addEdge("Eur Fermi-Eur Palasport", nodoB2, nodoB3);
        Node nodoB4 = g.addNode("Eur Magliana");
        lineaB.add("Eur Magliana");
        Edge edgeB3 = g.addEdge("Eur Palasport-Eur Magliana", nodoB3, nodoB4);
        Node nodoB5 = g.addNode("Marconi");
        lineaB.add("Marconi");
        Edge edgeB4 = g.addEdge("Eur Magliana-Marconi", nodoB4, nodoB5);
        Node nodoB6 = g.addNode("Basilica San Paolo");
        lineaB.add("Basilica San Paolo");
        Edge edgeB5 = g.addEdge("Marconi-Basilica San Paolo", nodoB5, nodoB6);        
        Node nodoB7 = g.addNode("Garbatella");
        lineaB.add("Garbatella");
        Edge edgeB6 = g.addEdge("Basilica San Paolo-Garbatella", nodoB6, nodoB7);     
        Node nodoB8 = g.addNode("Ostiense");
        lineaB.add("Ostiense");
        Edge edgeB7 = g.addEdge("Garbatella-Ostiense", nodoB7, nodoB8);     
        Node nodoB9 = g.addNode("Circo Massimo");
        lineaB.add("Circo Massimo");
        Edge edgeB8 = g.addEdge("Ostiense-Circo Massimo", nodoB8, nodoB9);     
        Node nodoB10 = g.addNode("Colosseo");
        lineaB.add("Colosseo");
        Edge edgeB9 = g.addEdge("Circo Massimo-Colosseo", nodoB9, nodoB10);    
        Node nodoB11 = g.addNode("Cavour");
        lineaB.add("Cavour");
        Edge edgeB10 = g.addEdge("Colosseo-Cavour", nodoB10, nodoB11); 
        //Node nodoB12 = g.addNode("Termini");
        lineaB.add("Termini");
        Edge edgeB11 = g.addEdge("Cavour-Termini", nodoB11, nodoT); 
        Node nodoB13 = g.addNode("Castro Pretorio");
        lineaB.add("Castro Pretorio");
        Edge edgeB12 = g.addEdge("Termini-Castro Pretorio", nodoT, nodoB13); 
        Node nodoB14 = g.addNode("Policlinico");
        lineaB.add("Policlinico");
        Edge edgeB13 = g.addEdge("Castro Pretorio-Policlinico", nodoB13, nodoB14); 
        Node nodoB15 = g.addNode("Bologna");
        lineaB.add("Bologna");
        Edge edgeB14 = g.addEdge("Policlinico-Bologna", nodoB14, nodoB15); 
        Node nodoB16 = g.addNode("Tiburtina");
        lineaB.add("Tiburtina");
        Edge edgeB15 = g.addEdge("Bologna-Tiburtina", nodoB15, nodoB16); 
        Node nodoB17 = g.addNode("Quintiliani");
        lineaB.add("Quintiliani");
        Edge edgeB16 = g.addEdge("Tiburtina-Quintiliani", nodoB16, nodoB17); 
        Node nodoB18 = g.addNode("Monti Tiburtini");
        lineaB.add("Monti Tiburtini");
        Edge edgeB17 = g.addEdge("Quintiliani-Monti Tiburtini", nodoB17, nodoB18); 
        Node nodoB19 = g.addNode("Pietralata");
        lineaB.add("Pietralata");
        Edge edgeB18 = g.addEdge("Monti Tiburtini-Pietralata", nodoB18, nodoB19);
        Node nodoB20 = g.addNode("Santa Maria del Soccorso");
        lineaB.add("Santa Maria del Soccorso");
        Edge edgeB19 = g.addEdge("Pietralata-Santa Maria del Soccorso", nodoB19, nodoB20);
        Node nodoB21 = g.addNode("Ponte Mammolo");
        lineaB.add("Ponte Mammolo");
        Edge edgeB20 = g.addEdge("Santa Maria del Soccorso-Ponte Mammolo", nodoB20, nodoB21);
        Node nodoB22 = g.addNode("Rebibbia");
        lineaB.add("Rebibbia");
        Edge edgeB21 = g.addEdge("Ponte Mammolo-Rebibbia", nodoB21, nodoB22);
        fermate.add(lineaB);
        
        GraphHolder graphHolder = new GraphHolder(fermate);
        graphHolder.generateGraphFromFermate(fermate);
        graphHolder.displayGraph();
        TransportStencil ts1 = new TransportStencil("Metro A", "Battistini", 5, 200); 
        TransportStencil ts2 = new TransportStencil("Metro A", "Anagnina", 5, 200); 
        TransportStencil ts3 = new TransportStencil("Metro B", "Laurentina", 15, 200); 
        TransportStencil ts4 = new TransportStencil("Metro B", "Rebibbia", 15, 200); 
        SimulationEngine.STATION_SIZE = 200;
        ArrayList<TransportStencil> listaTransportStencils = new ArrayList<>();
        listaTransportStencils.add(ts1);
        listaTransportStencils.add(ts2);
        listaTransportStencils.add(ts3);
        listaTransportStencils.add(ts4);
        PassengerStencil ps1 = new PassengerStencil("Circo Massimo", "Anagnina", 6); // la frequency media è al minuto.
        PassengerStencil ps2 = new PassengerStencil("Marconi", "Bologna", 8); // la frequency media è al minuto.
        PassengerStencil ps3 = new PassengerStencil("Battistini", "Tiburtina", 5); // la frequency media è al minuto.
        PassengerStencil ps4 = new PassengerStencil("Battistini", "Lucio Sestio", 3); // la frequency media è al minuto.
        ArrayList<PassengerStencil> listaPassengerStencils = new ArrayList<>();
        listaPassengerStencils.add(ps1);
        listaPassengerStencils.add(ps2);
        listaPassengerStencils.add(ps3);
        listaPassengerStencils.add(ps4);
        SimulationEngine simulationEngine = new SimulationEngine(listaPassengerStencils, listaTransportStencils, graphHolder);
        simulationEngine.initialise();
        simulationEngine.start();
    }

    void checkForPassengers(Node actualStation, Node nextStation, ArrayList<Passenger> passengers, Transport t) {
        synchronized(this.passengers){
            for(Passenger p : this.passengers) {
                if(p.getCurrentNode().equals(actualStation) && p.goingTo(nextStation) && !p.travelling) {
                    if(!t.isFull()){
                        p.travelling = true;
                        passengers.add(p);
                        // Manca check sulla capacity. TODO.
                    }
                }
            } 
        }
    }

    private void create_nodesColor() {
        graphHolder.populate(this.nodesColor);
    }
}
