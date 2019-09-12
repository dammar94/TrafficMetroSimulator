/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import org.graphstream.graph.Node;

/** Questa classe è di vitale importanza per la simulazione; Gestiste
 * l'affluenza dei passeggeri nella rete secondo tre parametri: startNode, 
 * arrivalNode e frequency (ovvero, il nodo di partenza, quello d'arrivo e la
 * frequenza oraria con cui vengono generati i viaggiatori).
 *
 * @author damiano
 */
public class PassengerGenerator {
    
    /**
     * I nodi di partenza e arrivo dei passeggeri
     */
    Node startNode;
    Node arrivalNode;
    int frequency;
    
    public PassengerGenerator(Node startNode, Node arrivalNode, int frequency) {
        this.startNode = startNode;
        this.arrivalNode = arrivalNode;
        this.frequency = frequency;
    }
    
}
