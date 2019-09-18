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

/**
 *
 * @author damiano
 */
class TransportGenerator extends Thread {
    public static int COUNTER = 0; // contatore dei Transport creati
    private final TransportStencil transportStencil;
    private ArrayList<Node> tragittoNodi;
    private ArrayList<Edge> tragittoEdge;
    private int currentTime = 0;
    private int lastTransportDepartureTime = 0;
    private boolean running = true;
    boolean waiting = false;
    private GraphHolder graphHolder;
    private final SimulationEngine sm;

    public TransportGenerator(SimulationEngine sm, TransportStencil transportStencil, GraphHolder graphHolder) {
        this.graphHolder = graphHolder;
        this.transportStencil = transportStencil;
        this.sm = sm;
        initialise(graphHolder);
    }

    @Override
    public void run() {
        this.lastTransportDepartureTime = this.currentTime; // Allinea i tempi all'avvio.
        while(running){
            if(currentTime - lastTransportDepartureTime >= transportStencil.cadency) {
                this.departTransport();
                lastTransportDepartureTime = currentTime;
            }
            try {
                synchronized(this) {
                    waiting = true;
                    wait();
                    waiting = false;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(TransportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initialise(GraphHolder graphHolder) {
        // Genera tragittoNodi e tragittoEdge.
        this.tragittoNodi = graphHolder.getLineaListaNodes(transportStencil.linea, transportStencil.direzione);
        this.tragittoEdge = graphHolder.getLineaListaEdges(transportStencil.linea, transportStencil.direzione);
    }
    
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * Fai partire un Transport.
     */
    private void departTransport() {
        synchronized(sm){
            Transport t = new Transport(sm, graphHolder, tragittoNodi, tragittoEdge, transportStencil.capacity, 
                    transportStencil.linea, transportStencil.direzione); 
            t.start();
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

    synchronized void notifyTransportGenerator() {
        notify();
    }

    void render() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
