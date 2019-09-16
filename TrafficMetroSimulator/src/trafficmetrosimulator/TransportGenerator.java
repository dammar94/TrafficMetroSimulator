/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.ArrayList;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 *
 * @author damiano
 */
class TransportGenerator {
    private final TransportStencil transportStencil;
    private ArrayList<Node> tragittoNodi;
    private ArrayList<Edge> tragittoEdge;

    public TransportGenerator(TransportStencil transportStencil, GraphHolder graphHolder) {
        this.transportStencil = transportStencil;
        initialise(graphHolder);
    }
    
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initialise(GraphHolder graphHolder) {
        // Genera tragittoNodi e tragittoEdge.
        this.tragittoNodi = graphHolder.getLineaListaNodes(transportStencil.linea, transportStencil.direzione);
        this.tragittoEdge = graphHolder.getLineaListaEdges(transportStencil.linea, transportStencil.direzione);
    }
    
}
