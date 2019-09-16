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
public class Transport implements Runnable {
    private ArrayList<Node> tragittoNodi;
    private ArrayList<Edge> tragittoEdge;
    private boolean onEdge = false;
    private final int capacity;
    private int currentLoad = 0;

    public Transport(ArrayList<Node> tragittoNodi, ArrayList<Edge> tragittoEdge, int capacity) {
        this.tragittoNodi = tragittoNodi;
        this.tragittoEdge = tragittoEdge;
        this.capacity = capacity;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
