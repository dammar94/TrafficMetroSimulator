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
import org.graphstream.graph.Node;

/**
 *
 * @author damiano
 */
class TransportGenerator extends Thread {
    private final TransportStencil transportStencil;
    private ArrayList<Node> tragittoNodi;
    private ArrayList<Edge> tragittoEdge;
    private int currentTime = 0;
    private int lastTransportDepartureTime = 0;
    private boolean running = true;

    public TransportGenerator(TransportStencil transportStencil, GraphHolder graphHolder) {
        this.transportStencil = transportStencil;
        initialise(graphHolder);
    }

    @Override
    public void run() {
        this.lastTransportDepartureTime = this.currentTime; // Allinea i tempi all'avvio.
        while(running){
            if(currentTime - lastTransportDepartureTime > transportStencil.cadency) {
                this.departTransport();
                lastTransportDepartureTime = currentTime;
            }
            try {
                wait();
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
        Transport t = new Transport(tragittoNodi, tragittoEdge, transportStencil.capacity); 
        t.start();
    }
    
}
