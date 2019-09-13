/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.ArrayList;
import org.graphstream.graph.Node;

/**
 * Questa classe contiene e si occupa di gestire tutti i dettagli dell'ambiente
 * di lavoro corrente
 *
 * @author damiano
 */
public class WorkSpace {

    private final String nomeWorkSpace;
    /**
     * Elenco delle linee dei trasporti, rappresentate a loro volta da un elenco
     * di stringhe (L'indice x-0 è riservato al nome della linea).
     */
    private ArrayList<ArrayList<String>> fermate;
    /**
     * Lista dei PassengerGenerator disponibili per la simulazione.
     */
    private final ArrayList<PassengerGenerator> listaPassengerGenerators;
    /**
     * GraphHolder della WorkSpace.
     */
    private GraphHolder graphHolder;
    /**
     * Lista dei TransportStencil relativi alla WorkSpace.
     */
    private final ArrayList<TransportStencil> listaTransportStencils;
    
    /**
     * Imposta il nome della WorkSpace alla creazione.
     *
     * @param nomeWorkSpace
     */
    public WorkSpace(String nomeWorkSpace) {
        this.nomeWorkSpace = nomeWorkSpace;
        this.fermate = new ArrayList<>();
        this.listaPassengerGenerators = new ArrayList<>();
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
     * Crea e aggiunge alla propria lista un nuovo PassengerGenerator partendo dai
     * paramentri in input.
     * @param fermataPartenza
     * @param fermataArrivo
     * @param frequency 
     */
    void addNewPassengerGenerator(String fermataPartenza, String fermataArrivo, int frequency) {
        Node startNode = graphHolder.getNodeByName(fermataPartenza);
        Node arrivalNode = graphHolder.getNodeByName(fermataArrivo);
        PassengerGenerator passengerGenerator = new PassengerGenerator(startNode, arrivalNode, frequency);
        this.listaPassengerGenerators.add(passengerGenerator);
    }

    int getNumberOfPassengerGenerators() {
        return (this.listaPassengerGenerators.size() + 1);
    }

    void generateGraph() {
        GraphHolder graphHolder = new GraphHolder();
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

}
