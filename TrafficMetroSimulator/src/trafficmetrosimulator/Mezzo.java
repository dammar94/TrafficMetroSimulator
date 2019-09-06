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
 *
 * @author Utente
 */
public class Mezzo implements Runnable {
    private ArrayList<Node> tragitto;
    private int[] orari;
    private int currentNode=-1;
    private int currentTime=0;
    private boolean going = true;
    private boolean alreadyMoved = false;
    
    public Mezzo(ArrayList<Node> tragitto, int[] orari) {
        this.tragitto = tragitto;
        this.orari = orari;
    } 

    @Override
    public void run() {
        while(going) {
            // controlla se il tempo corrente matcha uno degli orari
            // se ancora il mezzo non è stato mosso
            if(!alreadyMoved){
                for(int i=0; i<orari.length; i++){
                    // se c'è un match allora sposta il mezzo alla stazione successiva
                    if(currentTime == orari[i]) {
                        this.currentNode = this.currentNode + 1;
                        System.out.println("treno in stazione:" + tragitto.get(currentNode).getId());
                        this.alreadyMoved = true;
                        break;
                    }
                }
            }
            try {
                Thread.sleep(10);
//            try {
//                // metti quindi il thread in wait
//                wait();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Mezzo.class.getName()).log(Level.SEVERE, null, ex);
//            }
            } catch (InterruptedException ex) {
                Logger.getLogger(Mezzo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setCurrentTime(int time) {
        this.currentTime = time;
        alreadyMoved = false;
    }
    
    
    
}
