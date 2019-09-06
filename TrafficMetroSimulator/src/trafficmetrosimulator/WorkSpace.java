/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.ArrayList;

/**
 * Questa classe contiene e si occupa di gestire tutti i dettagli dell'ambiente
 * di lavoro corrente
 *
 * @author damiano
 */
public class WorkSpace {

    private String nomeWorkSpace;
    /**
     * Elenco delle linee dei trasporti, rappresentate a loro volta da un elenco
     * di stringhe (L'indice x-0 è riservato al nome della linea).
     */
    private ArrayList<ArrayList<String>> fermate;
    /**
     * GraphHolder della WorkSpace.
     */
    private GraphHolder graphHolder;

    public void setGraphHolder(GraphHolder graphHolder) {
        this.graphHolder = graphHolder;
    }

    public void setFermate(ArrayList<ArrayList<String>> fermate) {
        this.fermate = fermate;
    }

    /**
     * Imposta il nome della WorkSpace alla creazione.
     *
     * @param NomeWorkSpace
     */
    public WorkSpace(String nomeWorkSpace) {
        this.nomeWorkSpace = nomeWorkSpace;
        this.fermate = new ArrayList<ArrayList<String>>();
    }

}
