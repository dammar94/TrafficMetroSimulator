/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;


/**
 *
 * @author Damiano
 */
public class TrafficMetroSimulator {
    
    final static int TARGET_FPS = 24;
    private static int frame = 0;
    private static int currentTime = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //inizializza grafo
        Graph graph = new SingleGraph("Grafo d'esempio");
        Node nodoA = graph.addNode("A");
        nodoA.addAttribute("ui.label", "A");
        Node nodoB = graph.addNode("B");
        nodoB.addAttribute("ui.label", "B");
        Node nodoC = graph.addNode("C");
        nodoC.addAttribute("ui.label", "C");
        Node nodoD = graph.addNode("D");
        nodoD.addAttribute("ui.label", "D");
        Node nodoE = graph.addNode("E");
        nodoE.addAttribute("ui.label", "E");
        Node nodoF = graph.addNode("F");
        nodoF.addAttribute("ui.label", "F");
        Node nodoG = graph.addNode("G");
        nodoG.addAttribute("ui.label", "G");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CD", "C", "D");
        graph.addEdge("CE", "C", "E");
        graph.addEdge("EF", "E", "F");
        graph.addEdge("CG", "C", "G");
        graph.display();
        
        //inizializza mezzi (4 mezzi per ora)
        ArrayList<Node> tragittoMetroA = new ArrayList<Node>();
        tragittoMetroA.add(nodoA);
        tragittoMetroA.add(nodoB);
        tragittoMetroA.add(nodoC);
        tragittoMetroA.add(nodoG);
        ArrayList<Node> tragittoMetroB = new ArrayList<Node>();
        tragittoMetroB.add(nodoF);
        tragittoMetroB.add(nodoE);
        tragittoMetroB.add(nodoC);
        tragittoMetroB.add(nodoD);
        int[] orariMetro1 = new int[]{400, 415, 425, 436};
        int[] orariMetro2 = new int[]{600, 615, 625, 636};
        int[] orariMetro3 = new int[]{1000, 1015, 1025, 1036};
        int[] orariMetro4 = new int[]{1200, 1215, 1225, 1236};
        // reference ai 4 mezzi
        Mezzo metro1 = new Mezzo(tragittoMetroA, orariMetro1);
        Mezzo metro2 = new Mezzo(tragittoMetroB, orariMetro2);
        Mezzo metro3 = new Mezzo(tragittoMetroA, orariMetro3);
        Mezzo metro4 = new Mezzo(tragittoMetroB, orariMetro4);
        // fai partire i thread dei mezzi
        Thread metro1Thread = new Thread(metro1);
        metro1Thread.start();
        Thread metro2Thread = new Thread(metro2);
        metro2Thread.start();
        Thread metro3Thread = new Thread(metro3);
        metro3Thread.start();
        Thread metro4Thread = new Thread(metro4);
        metro4Thread.start();
        
        // metti in piedi il motore
        boolean isRunning = true;
        long now;
        long updateTime;
        long wait;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        while (isRunning) {
            now = System.nanoTime();
            // update
            TrafficMetroSimulator.currentTime = (TrafficMetroSimulator.currentTime+1) % 2400;
            metro1.setCurrentTime(currentTime);
            metro2.setCurrentTime(currentTime);
            metro3.setCurrentTime(currentTime);
            metro4.setCurrentTime(currentTime);
            //update();
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

    private static void update() {
        TrafficMetroSimulator.currentTime = (TrafficMetroSimulator.currentTime+1) % 2400;
    }

    private static void render() {
        System.out.println("" + currentTime);
    }
    
}
