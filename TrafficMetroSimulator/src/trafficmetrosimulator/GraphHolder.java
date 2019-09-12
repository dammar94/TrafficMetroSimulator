/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.ArrayList;
import java.util.Random;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Questa classe ha lo scopo di gestire e riassumere in maniera trasparente
 * tutte le operazioni possibili su un determinato grafo, campo della classe.
 *
 * @author damiano
 */
public class GraphHolder {

    /**
     * Questo è il grafo contenuto nell'holder, oggetto di tutte le operazioni.
     */
    Graph graph;

    /**
     * Genera il grafo a partire dall'array delle fermate.
     *
     * @param fermate array delle fermate (il primo posto di ogni riga è
     * riservato al nome della linea).
     */
    void generateGraphFromFermate(ArrayList<ArrayList<String>> fermate) {
        Graph graph = new SingleGraph("Grafo Rete Trasporti");
        for (int i = 0; i < fermate.size(); i++) {
            Node nodoPrecedente = null;
            Node nodoCorrente = null;
            for (int j = 1; j < fermate.get(i).size(); j++) { //j=0 è il nome della linea
                //Se il nodo non esiste lo crea, altrimenti riprende quello già creato.
                try {
                    nodoCorrente = graph.addNode("" + fermate.get(i).get(j));
                    nodoCorrente.addAttribute("ui.label", fermate.get(i).get(j)); //Manipolazione sullo stile provvisoria. Verrà creato uno StyleSheet apposito.
                } catch (IdAlreadyInUseException exc) {
                    nodoCorrente = graph.getNode(fermate.get(i).get(j));
                } finally {
                    if (nodoPrecedente != null && nodoCorrente != null) {
                        //Crea l'edge se ancora non esiste, altrimenti va avanti.
                        try {
                            Edge edge = graph.addEdge(nodoPrecedente + "-" + nodoCorrente, nodoPrecedente, nodoCorrente);
//                            String colorCode = getExaColor();
//                            System.out.println(colorCode);
//                            edge.addAttribute("fill-color", colorCode); //Manipolazione sullo stile provvisoria. Verrà creato uno StyleSheet apposito.
                        } catch (IdAlreadyInUseException exc) {
                            //l'edge esiste già
                        } catch (EdgeRejectedException exc) {
                            //l'edge esiste già ma è stato creato nel verso opposto
                        } finally {

                        }
                    }
                    nodoPrecedente = nodoCorrente;
                }
            }
        }
        this.graph = graph;
        //graph.display();
    }

    /**
     * Restituisce un colore random in formato esadecimale.
     *
     * @return Stringa contenente il colore in esadecimale
     */
    private String getExaColor() {
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        // format it as hexadecimal string and print
        String colorCode = String.format("#%06x", rand_num);
        return colorCode;
    }

    void displayGraph() {
        this.graph.display();
    }

    Node getNodeByName(String stringa) {
        return graph.getNode(stringa);
    }
}
