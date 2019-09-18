/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import static java.lang.Math.round;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 *
 * @author damiano
 */
class PassengerGenerator extends Thread {
    private final PassengerStencil passengerStencil;
    private final List<Node> nodesPath;
    private int currentTime = 0;
    private boolean running = true;
    boolean waiting = false;
    private final SimulationEngine sm;

    public PassengerGenerator(SimulationEngine sm, PassengerStencil passengerStencil, GraphHolder graphHolder) {
        this.passengerStencil = passengerStencil;
        this.sm = sm;
        this.nodesPath = graphHolder.getNodePath(passengerStencil.startNode, passengerStencil.arrivalNode);
    }
    
    @Override
    public void run() {
        //this.lastTransportDepartureTime = this.currentTime; // Allinea i tempi all'avvio.
        while(running){
            // Genera il giusto numero di Passenger.
            generatePassengers(passengerStencil.frequency); // I Passenger sono generati al minuti adesso. Non più all'ora.
            //System.out.println("Generati Passengers.");
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
    
    synchronized void notifyPassengerGenerator() {
        notify();
    }

    private void generatePassengers(int number) {
        Random rd = new Random(); // creating Random object
        number = round(((float)number * 2) * (rd.nextFloat())); //un po' di randomness nella generazione dei passeggeri.
        for(int i=0; i<number; i++){
            Passenger p = new Passenger(nodesPath);
            sm.register(p);
        }
    }
    
}
