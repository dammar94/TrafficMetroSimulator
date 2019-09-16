/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.graph.Node;

/**
 * Classe responsabile della coordinazione delle operazioni che avvengono
 * durante la simulazione.
 * @author damiano
 */
class SimulationEngine {
    final int TARGET_FPS = 12;
    private final int frame = 0;
    private int currentTime = 0;
    
    private final ArrayList<PassengerStencil> listaPassengerStencils;
    private final ArrayList<TransportStencil> listaTransportStencils;
    private final GraphHolder graphHolder;
    
    private ArrayList<PassengerGenerator> listaPassengerGenerator;
    private ArrayList<TransportGenerator> listaTransportGenerator;

    public SimulationEngine(ArrayList<PassengerStencil> listaPassengerStencils, ArrayList<TransportStencil> listaTransportStencils, GraphHolder graphHolder) {
        this.listaPassengerStencils = listaPassengerStencils;
        this.listaTransportStencils = listaTransportStencils;
        this.graphHolder = graphHolder;
    }
    

    /**
     * @param args the command line arguments
     */
    public void start() {
        boolean isRunning = true;
        long now;
        long updateTime;
        long wait;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        while (isRunning) {
            now = System.nanoTime();
            update();
            render();
            updateTime = System.nanoTime() - now;
            wait = (OPTIMAL_TIME - updateTime) / 1000000;
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        this.currentTime = (this.currentTime+1) % 2400;
        // Aggiorna i TransportGenerator
        // TODO
        // Aggiorna i PassengerGenerator
        // TODO
    }

    private void render() {
        System.out.println("" + currentTime);
    }

    /**
     * Esegue tutte le operazioni preliminari all'avvio della simulazione.
     */
    void initialise() {
        create_TransportGenerators();
        create_PassengerGenerators();
    }

    private void create_TransportGenerators() {
        for (TransportStencil transportStencil : this.listaTransportStencils) {
            TransportGenerator transportGenerator = new TransportGenerator(transportStencil, graphHolder);
            this.listaTransportGenerator.add(transportGenerator);
        }
    }

    private void create_PassengerGenerators() {
        for (PassengerStencil passengerStencil : this.listaPassengerStencils) {
            PassengerGenerator passengerGenerator = new PassengerGenerator(passengerStencil);
            this.listaPassengerGenerator.add(passengerGenerator);
        }
    }
    
}
