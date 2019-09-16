/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import org.graphstream.graph.Node;

/**
 * Questa classe contiene e si occupa di gestire tutti i dettagli dell'ambiente
 * di lavoro corrente
 *
 * @author damiano
 */
public class WorkSpace implements Serializable {
    private final String nomeWorkSpace;
    /**
     * Elenco delle linee dei trasporti, rappresentate a loro volta da un elenco
     * di stringhe (L'indice x-0 è riservato al nome della linea).
     */
    private ArrayList<ArrayList<String>> fermate;
    /**
     * Lista dei PassengerStencil disponibili per la simulazione.
     */
    private final ArrayList<PassengerStencil> listaPassengerStencils;
    /**
     * GraphHolder della WorkSpace.
     */
    private transient GraphHolder graphHolder; // Non serializza il GraphHolder. TOFIX!
    /**
     * Lista dei TransportStencil relativi alla WorkSpace.
     */
    private final ArrayList<TransportStencil> listaTransportStencils;
    private transient SimulationEngine simulationEngine;
    
    /**
     * Imposta il nome della WorkSpace alla creazione.
     *
     * @param nomeWorkSpace
     */
    public WorkSpace(String nomeWorkSpace) {
        this.nomeWorkSpace = nomeWorkSpace;
        this.fermate = new ArrayList<>();
        this.listaPassengerStencils = new ArrayList<>();
        this.listaTransportStencils = new ArrayList<>();
    }
    
    public String getNomeWorkSpace() {
        return nomeWorkSpace;
    }
    
    public void setFermate(ArrayList<ArrayList<String>> fermate) {
        this.fermate = fermate;
    }
    
    public ArrayList<ArrayList<String>> getFermate() {
        return fermate;
    }

    /**
     * Crea e aggiunge alla propria lista un nuovo PassengerStencil partendo dai
     * paramentri in input.
     * @param fermataPartenza
     * @param fermataArrivo
     * @param frequency 
     */
    void addNewPassengerGenerator(String fermataPartenza, String fermataArrivo, int frequency) {
//        Node startNode = graphHolder.getNodeByName(fermataPartenza);
//        Node arrivalNode = graphHolder.getNodeByName(fermataArrivo);
        PassengerStencil passengerGenerator = new PassengerStencil(fermataPartenza, fermataArrivo, frequency);
        this.listaPassengerStencils.add(passengerGenerator);
    }

    int getNumberOfPassengerGenerators() {
        return (this.listaPassengerStencils.size() + 1);
    }

    void generateGraph() {
        GraphHolder graphHolder = new GraphHolder(this.fermate);
        graphHolder.generateGraphFromFermate(fermate);
        this.graphHolder = graphHolder;
    }

    void displayGraph() {
        graphHolder.displayGraph();
    }

    void addNewTransportStencil(String linea, String direzione, int cadency, int capacity) {
        TransportStencil ts = new TransportStencil(linea, direzione, cadency, capacity);
        this.listaTransportStencils.add(ts);
    }

    String getDataCreazione() {
        return "TODO";
    }

    String getDataUltimoSalvataggio() {
        return "TODO";
    }

    /**
     * Salva in memoria persistente la WorkSpace.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    void saveInHardDrive() throws FileNotFoundException, IOException {
        try {
            FileOutputStream f = new FileOutputStream(new File(this.nomeWorkSpace + ".wrsp"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(this);
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    void startSimulationEngine() {
        this.simulationEngine = new SimulationEngine(this.listaPassengerStencils, 
                this.listaTransportStencils, this.graphHolder);
        this.simulationEngine.initialise();
        this.simulationEngine.start();
    }
    
    
    
}
