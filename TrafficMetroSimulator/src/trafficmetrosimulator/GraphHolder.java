/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

/**
 * Questa classe ha lo scopo di gestire e riassumere in maniera trasparente
 * tutte le operazioni possibili su un determinato grafo, campo della classe.
 *
 * @author damiano
 */
public class GraphHolder {
    // StyleSheet
    private static String STYLESHEET = 
            "node {\n" +
            "	size: 15px;\n" +
            "	fill-color: grey;\n" +
            "	z-index: 0;\n" +
            "}\n" +
            "\n" +
            "edge {\n" +
            "	shape: line;\n" +
            "	size: 2px;\n" +
            "	fill-mode: dyn-plain;\n" +
            "	fill-color: green, orange, red;\n" +
            "	arrow-size: 3px, 2px;\n" +
            "}";
    private ArrayList<ArrayList<String>> fermate;
    /**
     * Questo è il grafo contenuto nell'holder, oggetto di tutte le operazioni.
     */
    Graph graph;
    SpriteManager sman;
    
    public GraphHolder(ArrayList<ArrayList<String>> fermate) {
        this.fermate = fermate;
    }

    GraphHolder(Graph g) {
        this.graph = g;
        this.sman = new SpriteManager(graph);
        // L'array fermate è null in questo caso!
    }

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
                //Se il edge non esiste lo crea, altrimenti riprende quello già creato.
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
        this.sman = new SpriteManager(graph);
        this.graph.addAttribute("ui.stylesheet", GraphHolder.STYLESHEET);
        this.graph.addAttribute("ui.quality");
        this.graph.addAttribute("ui.antialias");
        for(Edge edge: graph.getEachEdge()) {
            edge.setAttribute("ui.color", 0);
        }
//        for(Node node: graph.getEachNode()) {
//            node.setAttribute("ui.color", 0);
//        }
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
        graph.display(true);
    }

    Node getNodeByName(String name) {
        return graph.getNode(name);
    }
    
    Edge getEdgeByName(String name) {
        return graph.getEdge(name);
    }

    ArrayList<Node> getLineaListaNodes(String linea, String direzione) {
        ArrayList<Node> listaNodes = new ArrayList<>();
        for(int i=0; i<fermate.size(); i++) {
            if(linea.equals(fermate.get(i).get(0))) { //La linea è quella giusta.
                ArrayList<String> l = fermate.get(i);
                //l.remove(0);
                if(direzione.equals(l.get(1))) { //La direzione è quella giusta.
                    for(int j=l.size()-1; j>0; j--){
                        Node nodo = this.getNodeByName(l.get(j));
                        listaNodes.add(nodo);
                    }
                } else{ //Altrimenti l'altra.
                    for(int j=1; j<l.size(); j++){
                        Node nodo = this.getNodeByName(l.get(j));
                        listaNodes.add(nodo);
                    }
                }
            }
        }
        return listaNodes;
    }

    ArrayList<Edge> getLineaListaEdges(String linea, String direzione) {
        ArrayList<Edge> listaEdges = new ArrayList<>();
        for(int i=0; i<fermate.size(); i++) {
            if(linea.equals(fermate.get(i).get(0))) { //La linea è quella giusta.
                ArrayList<String> l = fermate.get(i);
                //l.remove(0);
                if(direzione.equals(l.get(1))) { //La direzione è quella giusta.
                    for(int j=l.size()-1; j>1; j--){
                        Edge edge = this.getEdgeByNodeNames(l.get(j), l.get(j-1));
                        listaEdges.add(edge);
                    }
                } else{ //Altrimenti l'altra.
                    for(int j=1; j<l.size()-1; j++){
                        Edge edge = this.getEdgeByNodeNames(l.get(j), l.get(j+1));
                        listaEdges.add(edge);
                    }
                }
            }
        }
        return listaEdges;
    }

    private Edge getEdgeByNodeNames(String nodo1, String nodo2) {
        try{
            Edge edge = this.graph.getEdge(nodo1 + "-" + nodo2);
            if (edge != null) {
                return edge;
            }
            else {
                return this.graph.getEdge(nodo2 + "-" + nodo1);
            }
        } catch (IdAlreadyInUseException exc) {
            return this.graph.getEdge(nodo2 + "-" + nodo1);
        } catch (EdgeRejectedException exc) {
            return this.graph.getEdge(nodo2 + "-" + nodo1);
        } 
    }

    List<Node> getNodePath(String startNode, String arrivalNode) {
        AStar astar = new AStar(graph);
        astar.compute(startNode, arrivalNode); // with A and Z node identifiers in the graph.
        Path path = astar.getShortestPath();
        return path.getNodePath();
    }

    /**
     * Restituisce un reference allo Sprite creato.
     * @return 
     */
    Sprite getSprite(String string) {
        //SpriteManager sman = new SpriteManager(graph);
        return sman.addSprite(string);
    }

    synchronized void removeSprite(String id) {
        this.sman.removeSprite(id);
    }

    void populate(HashMap<Node, Float> nodesColor) {
        for(Node node : graph.getEachNode()) {
            Float f = 0f;
            nodesColor.put(node, f);
        }
    }

    void renderNodesColor(HashMap<Node, Float> nodesColor) {
        for(Node node : this.graph.getEachNode()) {
            Float color = nodesColor.get(node);
            node.setAttribute("ui.color", color);
        }
    }
    
}
