/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.List;
import org.graphstream.graph.Node;

/**
 *
 * @author damiano
 */
public class Passenger {
    final List<Node> nodesPath;
    int currentNodeIndex = 0;
    boolean travelling = false; // onStation oppure travelling

    Passenger(List<Node> nodesPath) {
        this.nodesPath = nodesPath;
    }

    boolean goingTo(Node nextStation) {
        if(currentNodeIndex < this.nodesPath.size() - 1){
            return nextStation.equals(this.nodesPath.get(currentNodeIndex + 1));
        }
        return false;
    }
    
    Node getCurrentNode() {
        return this.nodesPath.get(currentNodeIndex);
    }
    
}
