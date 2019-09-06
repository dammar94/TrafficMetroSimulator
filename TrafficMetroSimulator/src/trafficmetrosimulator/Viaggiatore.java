/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

//rivedi riga sovrastante
/**
 *
 * @author elisa
 */

public class Viaggiatore {
    public static long contatore;
    //incremento nel blocco statico il contatore ad ogni istanza di un nuovo viaggiatore
    static { contatore ++; }
    private Instant oraPartenza, oraArrivo;
    private Node partenza, destinazione, nodoCorrente;
    private Node percorso[];
    private Duration tempoDiViaggio;
    private long nome;
    //manca tabella con nodi e orari?
    //costruttore
    //NOTE: nel costruttore aggiungere nodo arrivo tramite algoritmo di ricerca del percorso più breve fornito da graphstream
    public Viaggiatore (Instant oraPartenza, Node partenza){
        this.oraPartenza = oraPartenza;
        this.partenza = partenza;
        this.destinazione = destinazione;
        //chiamo un metodo qui che utilizza un algoritmo di ricerca del percorso più breve e setto percorso[]
        this.nodoCorrente = partenza;
        nome = contatore;
    }
    //costruttore di default
    public Viaggiatore(){
        //oraPartenza = Istant.parse("00.00.00", SECONDS);
        oraPartenza = Instant.now().plus(0, ChronoUnit.SECONDS);
    }
    //metodi
    public Node[] visualizzaPercorso (){
        return percorso;
    }
    public Node visualizzaNodoCorrente (){
        return nodoCorrente;
    }
    public Duration visualizzaTempoDiViaggio (){
        return tempoDiViaggio;
    }
    public Instant visualizzaOraPartenza (){
        return oraPartenza;
    }
    public Instant visualizzaOraArrivo(){
        return oraArrivo;
    }
    public void muoviViaggiatore(Node nodoCorrente){
        this.nodoCorrente = nodoCorrente;
    }
    //mancano metodi
        
}
    
