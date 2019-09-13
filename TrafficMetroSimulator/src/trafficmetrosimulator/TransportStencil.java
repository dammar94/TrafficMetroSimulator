/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

/** 
 * Questa classe funge da stampino per i Transport relativi a una WorkSpace
 * @author damiano
 */
public class TransportStencil {
    
    private String linea;
    private String direzione;
    private int cadency;
    private int capacity;

    public TransportStencil(String linea, String direzione, int cadency, int capacity) {
        this.linea = linea;
        this.direzione = direzione;
        this.cadency = cadency;
        this.capacity = capacity;
    }

    /**
     * Get the value of linea
     *
     * @return the value of linea
     */
    public String getLinea() {
        return linea;
    }

    /**
     * Get the value of direzione
     *
     * @return the value of direzione
     */
    public String getDirezione() {
        return direzione;
    }

}
