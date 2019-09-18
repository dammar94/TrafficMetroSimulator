/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;

/**
 *
 * @author damiano
 */
public class Transport extends Thread {
    public int ID;
    public static int travellingTime = 10; // minuti necessari a percorrere i tratti tra stazione e stazione.
    public static int stationTime = 1; // minuti necessari a far scendere e salire i passeggeri.
    private ArrayList<Node> tragittoNodi;
    private int currentNodeIndex = 0;
    private ArrayList<Edge> tragittoEdge;
    private int currentEdgeIndex = -1;
    private boolean onEdge = false;
    private final int capacity;
    private int currentLoad = 0;
    private boolean running = false;
    // Sprite relativo al Transport
//    private Sprite sprite;
    private GraphHolder graphHolder;
    ArrayList<Passenger> passengers;
    private final SimulationEngine sm;
    private float currentLoadFraction = 0f;

    public Transport(SimulationEngine sm, GraphHolder graphHolder, ArrayList<Node> tragittoNodi, ArrayList<Edge> tragittoEdge, int capacity, String linea, String direzione) {
        this.ID = TransportGenerator.COUNTER;
        TransportGenerator.COUNTER = TransportGenerator.COUNTER + 1;
        this.tragittoNodi = tragittoNodi;
        this.tragittoEdge = tragittoEdge;
        this.capacity = capacity;
        this.graphHolder = graphHolder;
        this.passengers = new ArrayList<>();
        this.sm = sm;
//        this.sprite = graphHolder.getSprite(linea + "-" + direzione);
//        this.sprite.setAttribute("fill-color", "#777"); // NON FUNZIONA
//        this.sprite.setAttribute("ui.color", 777); // NON FUNZIONA
//        this.sprite.setAttribute("shape", "box"); // NON FUNZIONA
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Transport " + this.ID + " in partenza...");
        while(running) {
            if(!onEdge) { // Se sto in stazione.
                // Operazione carico scarico Passenger
                try {
                    // operazioni di statistiche della simulazione:
                    this.currentLoad = this.passengers.size();
                    if(this.currentLoad != 0){
                        this.renderEdge();
                    }
                    synchronized(this.tragittoNodi.get(this.currentNodeIndex)){ // Ogni Transport occupa il nodo per un tempo fissato, non proprio realistico.
                        System.out.println("Transport " + this.ID + " in stazione. node: " + tragittoNodi.get(currentNodeIndex).getId() + " - Passengers all'arrivo: " + this.passengers.size());
    //                    sprite.attachToNode(tragittoNodi.get(currentNodeIndex).getId());
    //                    sprite.setPosition(StyleConstants.Units.PX, 20, 180, 0);
                        Thread.sleep((Transport.stationTime / sm.TARGET_FPS) * 1000);
                        leavePassengers();
                        if(!(this.currentNodeIndex+1 == this.tragittoNodi.size())) {//Ci sono altre fermate
                            checkForPassengers(passengers);
                        }
                        System.out.println("Transport " + this.ID + " in stazione. node: " + tragittoNodi.get(currentNodeIndex).getId() + " - Passengers in partenza: " + this.passengers.size());
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
                }
                // TODO.
                // Aggiorna stato del Transoprt, in viaggio
                currentEdgeIndex = currentEdgeIndex + 1;
                if(currentEdgeIndex == tragittoEdge.size()) { // Tratta conclusa.
//                    this.graphHolder.removeSprite(this.sprite.getId());
//                    System.out.println("Sprite rimosso.");
                    running = false;
                }
                this.onEdge = true;
            }
            else { // Se sono in viaggio.
                // In sleep durante il viaggio
                try {
                    //System.out.println("In viaggio... edge = " + tragittoEdge.get(currentEdgeIndex).getId());
//                    sprite.attachToEdge(tragittoEdge.get(currentEdgeIndex).getId());
//                    sprite.setPosition(0.5);
                    Thread.sleep((Transport.travellingTime / sm.TARGET_FPS) * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
                }
                // TODO.
                // Aggiorna stato del Transport, arrivato in stazione
                this.currentNodeIndex = this.currentNodeIndex + 1;
                this.onEdge = false;
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        Graph g = new SingleGraph("grafo");
//        Node nodo1 = g.addNode("uno");
//        Node nodo2 = g.addNode("due");
//        Node nodo3 = g.addNode("tre");
//        Edge edge1 = g.addEdge("uno - due", nodo1, nodo2);
//        Edge edge2 = g.addEdge("due - tre", nodo2, nodo3);
//        ArrayList<Node> tragittoNodi = new ArrayList<>();
//        tragittoNodi.add(nodo1);
//        tragittoNodi.add(nodo2);
//        tragittoNodi.add(nodo3);
//        ArrayList<Edge> tragittoEdge = new ArrayList<>();
//        tragittoEdge.add(edge1);
//        tragittoEdge.add(edge2);
//        Transport t = new Transport(tragittoNodi, tragittoEdge, 50);
//        t.start();
    }

    private void checkForPassengers(ArrayList<Passenger> passengers) {
        // Interroga il SimulationEngine riguardo i passegeri da prendere.
        sm.checkForPassengers(tragittoNodi.get(this.currentNodeIndex), tragittoNodi.get(this.currentNodeIndex+1), passengers, this);
    }

    private void leavePassengers() {
        synchronized(sm.passengers){
            for(Passenger p : this.passengers) {
                p.currentNodeIndex = p.currentNodeIndex + 1;
                //System.out.println("currentNodeIndex: " + p.currentNodeIndex);
                p.travelling = false;
            }
            this.passengers = new ArrayList<>(); // svuota il treno.
        }
    }
    
    boolean isFull () {
        return this.passengers.size() == this.capacity;
    }

    private void renderEdge() {
        if(this.currentEdgeIndex != -1 && this.currentEdgeIndex < this.tragittoEdge.size()) {
            synchronized(this.tragittoEdge.get(this.currentEdgeIndex)){
                try {
                    this.currentLoadFraction = (float)(((float)this.currentLoad)/((float)this.capacity));
                    this.tragittoEdge.get(this.currentEdgeIndex).setAttribute("ui.color", (this.currentLoadFraction));
                } catch (Exception ex) {
                    // Eccezioni di GraphStream apparentemente innoque.
                }
            }
        }
    }
    
}
